package angel.tfg.pymeris.Views.ManageProducts.ModifyProduct;

import angel.tfg.pymeris.Model.Entities.Producto;
import angel.tfg.pymeris.Model.Repository.Productos.ProductosRepository;
import angel.tfg.pymeris.Views.ManageProducts.MaPrContract;
import javafx.application.Platform;

import java.math.BigDecimal;

public class ModifyProductPresenter implements MoPrContract.Presenter{

    MoPrContract.View controller;
    ProductosRepository repository;
    MaPrContract.Presenter managepresenter;

    public ModifyProductPresenter(MoPrContract.View controller,ProductosRepository repository, MaPrContract.Presenter managepresenter) {
        this.controller = controller;
        this.repository = repository;
        this.managepresenter = managepresenter;
    }

    @Override
    public void onModifyProductClick() {
        // 1. Recogemos los textos
        String idStr = controller.getID();
        String nombre = controller.getNombre();
        String categoria = controller.getCategoria();
        String descripcion = controller.getDescripcion();
        String precioStr = controller.getPrecio();
        String ivaStr = controller.getIVA();
        String stockStr = controller.getStock();

        // 2. Validaciones básicas
        if (esNuloOVacio(nombre) || esNuloOVacio(categoria) || esNuloOVacio(descripcion) || esNuloOVacio(precioStr) || esNuloOVacio(ivaStr) || esNuloOVacio(stockStr)) {
            controller.showModifyStatus("Faltan campos obligatorios por rellenar.", false);
            return;
        }

        int id = Integer.parseInt(idStr);
        BigDecimal precioDecimal;
        int ivaInt;
        int stockInt;

        // 3. Validar y procesar Precio e IVA
        try {
            precioDecimal = procesarPrecio(precioStr);
            ivaInt = Integer.parseInt(ivaStr);

            if (ivaInt < 0 || ivaInt > 100) {
                controller.showModifyStatus("El IVA debe estar entre 0 y 100.", false);
                return;
            }

            stockInt = Integer.parseInt(stockStr);
        } catch (NumberFormatException e) {
            if (e.getMessage() != null && e.getMessage().equals("Demasiados decimales")) {
                controller.showModifyStatus("El precio no puede tener más de 2 decimales.", false);
            } else {
                controller.showModifyStatus("Formato de preciom, IVA o stock incorrecto.", false);
            }
            return;
        }

        // 4. Hilo secundario para base de datos
        new Thread(() -> {
            Producto p = new Producto();
            p.setId_Producto(id); // Importante para el UPDATE en SQL
            p.setNombre(nombre);
            p.setCategoria(categoria);
            p.setDescripcion(descripcion);
            p.setPrecioUnitario(precioDecimal); // BigDecimal directo
            p.setIVA(ivaInt);
            p.setStock_Actual(stockInt);

            boolean success = repository.actualizarProducto(p);

            if (success) {
                Platform.runLater(() -> {
                    controller.showModifyStatus("Producto actualizado correctamente.", true);
                    managepresenter.cargarProductos(); // Refrescamos la tabla
                });
            } else {
                Platform.runLater(() -> controller.showModifyStatus("Error al actualizar el producto.", false) );
            }
        }).start();
    }

    /**
     * Misma función exacta de conversión exacta que usamos al crear el producto
     */
    private BigDecimal procesarPrecio(String precioString) throws NumberFormatException {
        String p = precioString.trim().replace(",", ".");
        BigDecimal bd = new BigDecimal(p);

        if (bd.scale() > 2) {
            throw new NumberFormatException("Demasiados decimales");
        }
        return bd.setScale(2, java.math.RoundingMode.HALF_UP);
    }

    // Función auxiliar para limpiar el código de comprobaciones repetitivas
    private boolean esNuloOVacio(String texto) {
        return texto == null || texto.isBlank();
    }
}
