package angel.tfg.pymeris.Views.ManageProducts.ManageProductsInactivo;

import angel.tfg.pymeris.Model.Entities.Producto;
import angel.tfg.pymeris.Model.Repository.Productos.ProductosRepository;

import java.util.List;

public interface MaPrInContract {
    interface View{
        void mostrarProductos(List<Producto> lista);
        void mostrarCarga(boolean b);
        void setPresenter(Presenter presenter);
    }
    interface Presenter{
        void cargarProductos();
        void onUpClick(Producto p);
        ProductosRepository getRepository();
    }
}
