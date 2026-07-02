package angel.tfg.pymeris.Views.ManageProducts.UpDownStockProduct;

import angel.tfg.pymeris.Model.Repository.Productos.ProductosRepository;
import angel.tfg.pymeris.Views.ManageProducts.MaPrContract;
import javafx.application.Platform;

public class UpDownStockProductPresenter implements UpDoStPrContract.Presenter {

    UpDoStPrContract.View controller;
    ProductosRepository repository;
    MaPrContract.Presenter managepresenter;

    public UpDownStockProductPresenter(UpDoStPrContract.View controller, ProductosRepository repository, MaPrContract.Presenter managepresenter) {
        this.controller = controller;
        this.repository = repository;
        this.managepresenter = managepresenter;
    }

    public void onModifyStockClick() {
        String cantidadStr = controller.getCantidad();
        int cantidad;
        boolean esEntrada;
        try {
            cantidad = Integer.parseInt(cantidadStr);

            esEntrada = controller.getSeleccionado().equals("Entrada");
        } catch (NumberFormatException e) {
            System.out.println("Error: introduce un numero válido.");
            controller.showStatus("Error: introduce un número válido.", false);
            return;
        }

        int id = controller.getIdProducto();
        int ajustefinal = esEntrada ? cantidad : (cantidad * -1);

        new Thread(() -> {
            boolean success = repository.modificarStock(id, ajustefinal);

            Platform.runLater(() -> {
                if(success) {
                    System.out.println("Stock modificado correctamente.");
                    controller.showStatus("Stock modificado correctamente.", true);
                    managepresenter.cargarProductos();
                } else  {
                    controller.showStatus("No se ha modificado el stock.", false);
                }
            });
        }).start();
    }
}
