package angel.tfg.pymeris.Views.ChangePassword;

import angel.tfg.pymeris.Model.Repository.Authentication.AuthRepository;
import angel.tfg.pymeris.Views.Login.LoginContract;
import javafx.application.Platform;

public class ChangePasswordPresenter implements ChPwContract.Presenter{

    private boolean success= false;

    private final ChPwContract.View controller;
    private final AuthRepository repository;
    private final LoginContract.View loginController;

    /**
     * Constructor de este presenter.
     * @param controller La vista que implementa el contrato, utilizada para actualizar la interfaz.
     * @param repository El repositorio de autenticación para gestionar la lógica de datos.
     */
    public ChangePasswordPresenter(ChPwContract.View controller, AuthRepository repository, LoginContract.View loginController) {
        this.controller = controller;
        this.repository = repository;
        this.loginController = loginController;
    }

    @Override
    public void onChangeClicked() {
        String pass1, pass2;

        pass1 = controller.getPassword1();
        pass2 = controller.getPassword2();
        if(isAnyFieldEmpty(pass1, pass2)){
            return;
        }
        else if(!pass1.equals(pass2)){
            controller.istextUnmatch();
            return;
        }

        controller.setChanging();
        new Thread(() -> {
            // Almacenamos si se realiza el cambio en la base de datos (UPDATE)
            success = repository.changePass(controller.getUsername(), pass1);

            Platform.runLater(() -> {
                if(success){
                    controller.changeSuccess();
                    loginController.changePasswordOK();
                }
                else {
                    controller.changeError();
                }
            });
        }).start();

    }

    /**
     * Verificamos si hay algún campo de contraseña vacío
     * @param p1 Campo de contraseña 1
     * @param p2 Campo de contraseña 2
     * @return
     *      - true si alguno de los campos está vacío y actualiza el label con el controlador
     *      - false si están los dos campos escritos
     */
    private boolean isAnyFieldEmpty(String p1, String p2) {
        boolean p1Empty = (p1 == null || p1.isEmpty());
        boolean p2Empty = (p2 == null || p2.isEmpty());

        if (p1Empty && p2Empty) {
            controller.isbothtextEmpty();
            return true;
        }
        if (p1Empty) {
            controller.istext1Empty();
            return true;
        }
        if (p2Empty) {
            controller.istext2Empty();
            return true;
        }
        return false;
    }
}
