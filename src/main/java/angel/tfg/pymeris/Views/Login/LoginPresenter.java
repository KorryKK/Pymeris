package angel.tfg.pymeris.Views.Login;

import angel.tfg.pymeris.Util.Session;
import angel.tfg.pymeris.Model.Entities.Usuario;
import angel.tfg.pymeris.Model.Repository.Authentication.AuthRepository;
import javafx.application.Platform;

public class LoginPresenter implements LoginContract.Presenter {

    private final LoginContract.View controller;
    private final AuthRepository repository;

    public LoginPresenter(LoginContract.View controller, AuthRepository repository) {
        this.controller = controller;
        this.repository = repository;
    }

    @Override
    public void onLoginClicked() {
        controller.setLoading(true);

        new Thread(() -> {
            Usuario user = repository.login(
                    controller.getUsername(),
                    controller.getPassword()
            );

            Platform.runLater(() -> {
                controller.setLoading(false);

                // Comprobamos que existe un usuario
                if (user == null) {
                    controller.loginError();
                    return;
                }

                // Comprobamos que el usuario está dado de alta para poder continuar
                if (repository.checkStatus(user.getEstado_Usuario())){
                    controller.loginDown();
                    return;
                }

                // Comprobamos si es el primer acceso, solo entra si en la base de datos pone "0"
                if (repository.checkAccess(user.getAcceso())){
                    System.out.println("Primer acceso, debe cambiar la contraseña");
                    controller.mostrarCambioPassword();
                }
                // Es un empleado normal el que accede al sistema
                else {
                    Session.configurarSession(user.getId_Empleado(), user.getNombre(), user.getRol());
                    System.out.println(Session.getRol() + " acceso ordinario");
                    controller.loginOK();
                }
            });
        }).start();

    }
}
