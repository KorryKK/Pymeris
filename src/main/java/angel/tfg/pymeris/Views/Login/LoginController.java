package angel.tfg.pymeris.Views.Login;

import angel.tfg.pymeris.Base.Titleable;
import angel.tfg.pymeris.Model.Repository.Authentication.AuthRepositoryImpl;
import angel.tfg.pymeris.Util.Enums.Scenes;
import angel.tfg.pymeris.Util.Enums.Titulos;
import angel.tfg.pymeris.Util.Session;
import angel.tfg.pymeris.Base.ViewController;
import angel.tfg.pymeris.Views.ChangePassword.ChangePasswordController;
import angel.tfg.pymeris.Views.ChangePassword.ChangePasswordPresenter;
import angel.tfg.pymeris.Views.SelectOptionMain.SelectOptionMainController;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LoginController extends ViewController implements LoginContract.View {

    @FXML
    private TextField tfUsername;
    @FXML
    private PasswordField pfPassword;
    @FXML
    private ProgressIndicator piLoginIndicator;
    @FXML
    private Button btnLogin;
    @FXML
    private Label lblStatus;

    private LoginContract.Presenter presenter;

    public void onLogin(){
        lblStatus.setText("Comprobando...");
        presenter.onLoginClicked();
    }

    @Override
    public String getUsername() {
        return tfUsername.getText();
    }

    @Override
    public String getPassword() {
        return pfPassword.getText();
    }

    @FXML
    public void goBack(){

        Titleable destino = navegador.goTo(Scenes.Main);
        assert destino != null;
        destino.setTitle(Titulos.Pymeris);
    }

    // Función que activa la barra de progreso y desactiva el botón
    @Override
    public void setLoading(boolean b){
        piLoginIndicator.setVisible(b);
        btnLogin.setDisable(b);
    }

    @Override
    public void loginOK() {
        lblStatus.setText("Empleado OK. " + Session.getRol() + ".");
        SelectOptionMainController controller = navegador.goTo(Scenes.SelectOptionMain);
        assert controller != null;
        controller.setTitle(Titulos.SelectOptionMain);
    }

    @Override
    public void loginDown() {
        lblStatus.setText("Su usuario ha sido dado de baja.\nHable con un administrador.");
    }

    @Override
    public void loginError() {
        lblStatus.setText("El usuario o la contraseña introducidos son incorrectos.");
    }

    @Override
    public void mostrarCambioPassword(){
        lblStatus.setText("Bienvenido. Debe cambiar su contraseña.");
        ChangePasswordController controller = navegador.goToModal(Scenes.EditPassword, "Seguridad: cambio de contraseña");
        assert controller != null;
        controller.setUsername(tfUsername.getText());
        controller.setPresenter(new ChangePasswordPresenter(controller, new AuthRepositoryImpl(), this));
    }

    @Override
    public void changePasswordOK() {
        lblStatus.setText("Su contraseña se ha cambiado correctamente.\nVuelva a iniciar sesión.");
        tfUsername.clear();
        pfPassword.clear();
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
