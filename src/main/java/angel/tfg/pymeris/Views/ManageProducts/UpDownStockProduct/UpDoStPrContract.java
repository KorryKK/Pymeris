package angel.tfg.pymeris.Views.ManageProducts.UpDownStockProduct;

import angel.tfg.pymeris.Model.Entities.Producto;

public interface UpDoStPrContract {
    interface View{
        void getProducto(Producto p);
        String getSeleccionado();
        int getIdProducto();
        String getCantidad();
        void showStatus(String message, boolean b);
        void setPresenter(Presenter presenter);
    }
    interface Presenter{
        void onModifyStockClick();
    }
}
