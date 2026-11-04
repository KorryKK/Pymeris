package angel.tfg.pymeris.Views.ManageProducts.ManageProductsInactivo;

import angel.tfg.pymeris.Model.Entities.Producto;
import angel.tfg.pymeris.Model.Repository.Productos.ProductosRepository;
import angel.tfg.pymeris.Util.MostrarAlertas;
import javafx.application.Platform;

import java.util.List;

public class ManageProductInactivoPresenter implements MaPrInContract.Presenter{

    MaPrInContract.View controller;
    ProductosRepository repository;

    public ManageProductInactivoPresenter(MaPrInContract.View controller, ProductosRepository repository){
        this.controller = controller;
        this.repository = repository;
    }

    @Override
    public void cargarProductos() {
        controller.mostrarCarga(true);

        new Thread(() -> {
            try {
                List<Producto> productos = repository.obtenerTodosLosInactivos();

                Platform.runLater(() -> {
                    controller.mostrarCarga(false);
                    controller.mostrarProductos(productos);
                });
            } catch (Exception ex) {
                Platform.runLater(() -> {
                    controller.mostrarCarga(false);
                    System.out.println(ex.getMessage() +"\nError al cargar los productos inactivos.");
                });
            }
        }).start();
    }

    @Override
    public void onUpClick(Producto p) {
        controller.mostrarCarga(true);
        new Thread(()->{
            boolean success = repository.uploadProducto(p.getId_Producto());

            if(success){
                Platform.runLater(()->{
                    controller.mostrarCarga(false);
                    MostrarAlertas.mostrarInformacion("Alta producto","El producto se ha dado de alta.");

                    cargarProductos();
                });
            } else {
                Platform.runLater(()-> MostrarAlertas.mostrarInformacion("Alta producto","El producto no se ha podido dar de alta."));
            }
        }).start();
    }

    @Override
    public ProductosRepository getRepository() {
        return repository;
    }
}