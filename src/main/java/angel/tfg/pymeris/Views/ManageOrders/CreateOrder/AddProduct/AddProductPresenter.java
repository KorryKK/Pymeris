package angel.tfg.pymeris.Views.ManageOrders.CreateOrder.AddProduct;

import angel.tfg.pymeris.Model.Entities.Producto;
import angel.tfg.pymeris.Model.Repository.Pedidos.PedidosRepository;
import angel.tfg.pymeris.Views.ManageOrders.CreateOrder.NewOrderContract;
import javafx.application.Platform;

import java.util.List;

public class AddProductPresenter implements AddProductContract.Presenter {

    private final AddProductContract.View controller;
    private final PedidosRepository repository;
    private final NewOrderContract.Presenter newPresenter;

    public AddProductPresenter(AddProductContract.View controller, PedidosRepository repository, NewOrderContract.Presenter newPresenter) {
        this.controller = controller;
        this.repository = repository;
        this.newPresenter = newPresenter;
    }

    @Override
    public void cargarProductos() {
        new Thread(() -> {
            try {
                List<Producto> productos = repository.listarProductos();

                Platform.runLater(() -> controller.mostrarProductos(productos));
            } catch (Exception e) {
                System.out.println("Error al cargar los clientes: " + e.getMessage());
            }
        }).start();
    }

    @Override
    public void onAddProductClick(Producto p) {
        newPresenter.mostrarCargaView(true);
        int cantidad = controller.getCantidad();
        if (cantidad == 0) {
            controller.showStatus("Ingresa un cantidad de productos.");
            newPresenter.mostrarCargaView(false);
            return;
        } else if (cantidad < 0) {
            controller.showStatus("La cantidad debe ser mayor a cero.");
            newPresenter.mostrarCargaView(false);
            return;
        } else if (cantidad > p.getStock_Actual()) {
            controller.showStatus("La cantidad que quieres añadir no está disponible.");
            newPresenter.mostrarCargaView(false);
            return;
        }

        new  Thread(() -> {
            boolean succes = repository.stockPedidoAjuste(p.getId_Producto(), (cantidad * -1));

            Platform.runLater(() -> {
                newPresenter.mostrarCargaView(false);
                if (succes) {
                    System.out.println("Se ha podido restar la cantidad al stock.");
                    controller.showStatus((cantidad > 1 ? "Productos añadidos" : "Producto añadido") + " correctamente.") ;
                    newPresenter.anadirProducto(p, cantidad);
                    cargarProductos();
                } else  {
                    controller.showStatus("Error al anadir producto al pedido");
                }
            });
        }).start();
    }
}
