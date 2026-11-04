package angel.tfg.pymeris.Views.ManageOrders.CreateOrder.AddProduct;

import angel.tfg.pymeris.Model.Entities.Producto;

import java.util.List;

public interface AddProductContract {
    interface View {
        void mostrarProductos(List<Producto> productos);
        void setPresenter(Presenter presenter);
        int getCantidad();
        void showStatus(String message);
    }
    interface Presenter {
        void cargarProductos();
        void onAddProductClick(Producto p);
    }
}