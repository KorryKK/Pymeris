package angel.tfg.pymeris.Views.ManageOrders.CreateOrder;

import angel.tfg.pymeris.Model.Entities.Cliente;
import angel.tfg.pymeris.Model.Entities.DetallePedido;
import angel.tfg.pymeris.Model.Entities.Pedido;
import angel.tfg.pymeris.Model.Entities.Producto;
import angel.tfg.pymeris.Model.Repository.Pedidos.PedidosRepository;
import angel.tfg.pymeris.Util.MostrarAlertas;
import angel.tfg.pymeris.Util.Session;
import angel.tfg.pymeris.Views.ManageOrders.MaOrContract;
import javafx.application.Platform;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class NewOrderPresenter implements NewOrderContract.Presenter {

    Cliente cliente;

    private final NewOrderContract.View controller;
    private final PedidosRepository repository;
    private final MaOrContract.Presenter managepresenter;

    private final List<DetallePedido> detallePedido = new ArrayList<>();

    public NewOrderPresenter(NewOrderContract.View controller, PedidosRepository repository, MaOrContract.Presenter managepresenter) {
        this.controller = controller;
        this.repository = repository;
        this.managepresenter = managepresenter;
    }

    @Override
    public void anadirCliente(Cliente c) {
        this.cliente = c;
        controller.setLblInfoCl(c.getNIF_DNI() + " - " + c.getNombre());
        controller.showAnadir(true);
    }

    @Override
    public void anadirProducto(Producto p, int cantidad) {

        if (modificarObjetoenLista(p.getId_Producto(), cantidad)) {

            controller.showEliminar(true);
            controller.showConfirmar(true);
            return;
        }

        DetallePedido detalle = new DetallePedido();
        detalle.setId_Producto(p.getId_Producto());
        detalle.setNombre_Producto(p.getNombre());
        detalle.setCantidad(cantidad);
        detalle.setPrecio_Unitario(p.getPrecioUnitario());
        detalle.setIVA(p.getIVA());

        detalle.setSubtotal(p.getPrecioUnitario().multiply(BigDecimal.valueOf(cantidad)));

        detalle.setImporte_Producto(getTotalProducto(p.getPrecioUnitario(), p.getIVA(), cantidad));

        detallePedido.add(detalle);
        controller.showEliminar(true);
        controller.showConfirmar(true);
        controller.mostrarDetalles(detallePedido);
    }

    @Override
    public void borrarProducto(DetallePedido dp) {
        boolean success = MostrarAlertas.solictarConfirmacion("BORRAR PRODUCTO", "¿Quieres borrar el productod el pedido?");

        if  (success) {
            boolean ajuste = repository.stockPedidoAjuste(dp.getId_Producto(), dp.getCantidad());

            if (ajuste) {
                detallePedido.remove(dp);
                controller.mostrarDetalles(detallePedido);
                if (detallePedido.isEmpty()) {
                    controller.showEliminar(false);
                    controller.showConfirmar(false);
                }
            } else {
                System.out.println("No se pudo devolver el producto al almacen.");
            }
        } else {
            MostrarAlertas.mostrarInformacion("BORRAR PRODUCTO", "No se ha borrado el producto.");
        }
    }

    @Override
    public void onCreateOrderClick() {
        controller.mostrarCarga(true);

        new Thread(() -> {
            // Creamos el objeto Pedido
            Pedido p = new Pedido();
            p.setId_Empleado(Session.getIdEmpleado());
            p.setNIF_DNI_Cliente(cliente.getNIF_DNI());
            p.setCliente(cliente.getNombre());
            p.setImporte_Total(getCantidadImporteFINAL());

            boolean success = repository.insertarPedido(p, detallePedido);

            Platform.runLater(() -> {
                controller.mostrarCarga(false);
                if (success) {
                    System.out.println("Pedido insertado correctamente.");
                    MostrarAlertas.mostrarInformacion("INSERTAR PEDIDO", "Pedido insertado correctamente.");
                    managepresenter.cargarPedidos();
                    controller.goBack();
                }  else {
                    System.out.println("No se ha podido insertar correctamente el pedido.");
                    MostrarAlertas.mostrarWarning("INSERTAR PEDIDO", "No se pudo procesar el pedido.");
                }
            });
        }).start();
    }

    @Override
    public boolean onCancelOrderClick() {
        controller.mostrarCarga(true);
        try {
            for (DetallePedido dp : detallePedido) {
                // Se ejecuta uno tras otro. Es rápido porque es SQLite local.
                repository.stockPedidoAjuste(dp.getId_Producto(), dp.getCantidad());
            }
            System.out.println("Se ha devuelto el stock.");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void mostrarCargaView(boolean b) {
        controller.mostrarCarga(b);
    }

    @Override
    public PedidosRepository getRepository() {
        return repository;
    }

    @Override
    public String getCantidadProductos() {
        int cantidad = 0;
        for (DetallePedido dp : detallePedido) {
            cantidad += dp.getCantidad();

        }
        return cantidad + " PRODUCTOS";
    }

    @Override
    public String getCantidadSubtotal() {
        BigDecimal subtotal = BigDecimal.ZERO;
        for (DetallePedido dp : detallePedido) {
            subtotal = subtotal.add(dp.getSubtotal());
        }
        return subtotal + " €";
    }

    @Override
    public BigDecimal getCantidadImporteFINAL() {
        BigDecimal importefinal = BigDecimal.ZERO;
        for (DetallePedido dp : detallePedido) {
            importefinal = importefinal.add(dp.getImporte_Producto());
        }
        return importefinal;
    }

    /**
     * Método auxiliar para comprobar si existe algún objeto que se añade ya a la lista
     *
     * @param id Id del producto a añadir la cantidad
     * @param cantidad Cantidad a añadir al producto existente
     * @return boolean
     *      - true si había producto y se ha añadido la cantidad
     *      - false si no había producto
     */
    private boolean modificarObjetoenLista(int id, int cantidad) {
        for (DetallePedido detalle : detallePedido) {
            if (detalle.getId_Producto() == id) {
                detalle.setCantidad(detalle.getCantidad() + cantidad);

                // Ajustamos el nuevo subtotal y total producto
                detalle.setSubtotal(detalle.getPrecio_Unitario().multiply(BigDecimal.valueOf(detalle.getCantidad())));
                detalle.setImporte_Producto(getTotalProducto(detalle.getPrecio_Unitario(), detalle.getIVA(), detalle.getCantidad()));

                // Actualizamos tabla
                controller.mostrarDetalles(detallePedido);
                return true;
            }
        }
        return false;
    }

    /**
     * Método centralizado para obtener el precio total del producto con la cantidad e iva
     * @param precio Precio del producto
     * @param ivaint IVA que se le aplica
     * @param cantidad Cantidad de produtos que hay
     * @return BigDecimal con el importe final de dicho producto
     */
    private BigDecimal getTotalProducto(BigDecimal precio, int ivaint, int cantidad) {

        BigDecimal iva = BigDecimal.valueOf(ivaint).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_EVEN);
        BigDecimal cant = BigDecimal.valueOf(cantidad);

        BigDecimal totalproducto = precio.multiply(cant).add((precio.multiply(cant).multiply(iva)));
        return totalproducto.setScale(2, RoundingMode.HALF_EVEN);
    }

}
