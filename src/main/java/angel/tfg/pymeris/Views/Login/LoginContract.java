package angel.tfg.pymeris.Views.Login;

public interface LoginContract {
    interface View{
        String getUsername();
        String getPassword();
        void setLoading(boolean loading);
        void loginOK();
        void loginDown();
        void loginError();
        void mostrarCambioPassword();
        void changePasswordOK();
        void setPresenter(LoginContract.Presenter presenter);
    }
    interface Presenter{
        void onLoginClicked();
    }
}
