package angel.tfg.pymeris.Views.ManageProducts.ModifyProduct;

import angel.tfg.pymeris.Model.Entities.Producto;

public interface MoPrContract {
    interface View{
        void showModifyStatus(String message, boolean b);
        void setPresenter(Presenter presenter);
        void getInfoProduct(Producto producto);
        String getID();
        String getNombre();
        String getCategoria();
        String getDescripcion();
        String getPrecio();
        String getIVA();
        String getStock();
    }
    interface Presenter{
        void onModifyProductClick();
    }
}
