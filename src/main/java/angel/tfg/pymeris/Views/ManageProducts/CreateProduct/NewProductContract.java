package angel.tfg.pymeris.Views.ManageProducts.CreateProduct;

public interface NewProductContract {
    interface View {
        void showSuccessCreateProduct(String message);
        void showErrorCreateProduct(String error);
        void cargando(boolean b);
        String getNombre();
        String getCategoria();
        String getDescripcion();
        String getPrecioUnitario();
        String getIVA();
        void setPresenter(Presenter presenter);
    }
    interface Presenter {
        void onCrearClick();
    }
}
