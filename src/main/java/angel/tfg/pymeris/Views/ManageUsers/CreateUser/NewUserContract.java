package angel.tfg.pymeris.Views.ManageUsers.CreateUser;

public interface NewUserContract {
    interface View{
        String getNombre();
        String getApellido();
        String getTelefono();
        String getCorreo();
        void showSuccessCreate(String message);
        void showErrorCreate(String message);
        void setPresenter(NewUserContract.Presenter presenter);
    }
    interface Presenter{
        void onCreateClick();
    }
}