package angel.tfg.pymeris.Views.ManageProducts;

import angel.tfg.pymeris.Model.Entities.Producto;
import angel.tfg.pymeris.Model.Repository.Productos.ProductosRepository;
import angel.tfg.pymeris.Util.MostrarAlertas;
import javafx.application.Platform;

import java.util.List;

public class ManageProductsPresenter implements MaPrContract.Presenter{

    private final MaPrContract.View controller;
    private final ProductosRepository repository;

    public ManageProductsPresenter(MaPrContract.View controller, ProductosRepository repository) {
        this.controller = controller;
        this.repository = repository;
    }

    @Override
    public void cargarProductos() {
        controller.mostrarCarga(true);

        new Thread(() -> {
            try {
                List<Producto> productos = repository.obtenerTodosLosProductos();

                Platform.runLater(() -> {
                    controller.mostrarCarga(false);
                    controller.mostrarProductos(productos);
                });
            } catch (Exception ex) {
                Platform.runLater(() -> {
                    controller.mostrarCarga(false);
                    System.out.println(ex.getMessage() +"\nError al cargar los productos");
                });
            }
        }).start();
    }

    @Override
    public void onDownClick(Producto p) {
        controller.mostrarCarga(true);
        new Thread(()->{
            boolean success = repository.downloadProducto(p.getId_Producto());

            if(success){
                Platform.runLater(()->{
                    controller.mostrarCarga(false);
                    MostrarAlertas.mostrarInformacion("Alta producto","El producto se ha dado de baja.");

                    cargarProductos();
                });
            } else {
                Platform.runLater(()->{
                    controller.mostrarCarga(false);
                    MostrarAlertas.mostrarInformacion("Alta producto","El producto no se ha podido dar de baja.");
                });
            }
        }).start();
    }

    @Override
    public ProductosRepository getRepository() {
        return repository;
    }
}