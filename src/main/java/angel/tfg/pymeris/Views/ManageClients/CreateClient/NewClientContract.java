package angel.tfg.pymeris.Views.ManageClients.CreateClient;

public interface NewClientContract {
    interface View{
        String getNifDni();
        String getNombre();
        String getApellido();
        String getCorreo();
        String getTelefono();
        void showSuccess(String message);
        void showError(String error);
        void setPresenter(NewClientContract.Presenter presenter);
    }
    interface Presenter{
        void onCreateClick();
    }
}