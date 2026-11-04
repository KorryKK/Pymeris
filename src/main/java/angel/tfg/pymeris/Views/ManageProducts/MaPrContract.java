package angel.tfg.pymeris.Views.ManageProducts;

import angel.tfg.pymeris.Model.Entities.Producto;
import angel.tfg.pymeris.Model.Repository.Productos.ProductosRepository;

import java.util.List;

public interface MaPrContract {
    interface View{
        void mostrarProductos(List<Producto> lista);
        void setPresenter(MaPrContract.Presenter presenter);
        void mostrarCarga(boolean b);
    }
    interface Presenter{
        void cargarProductos();
        void onDownClick(Producto p);
        ProductosRepository getRepository();
    }
}