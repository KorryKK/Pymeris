package angel.tfg.pymeris.Views.Inicio;

import angel.tfg.pymeris.Model.Repository.Authentication.AuthRepositoryImpl;
import angel.tfg.pymeris.Util.Enums.Titulos;
import angel.tfg.pymeris.Util.Enums.Scenes;
import angel.tfg.pymeris.Base.ViewController;
import angel.tfg.pymeris.Views.Login.LoginController;
import angel.tfg.pymeris.Views.Login.LoginPresenter;
import javafx.fxml.FXML;

public class InicioController extends ViewController {

    @FXML
    protected void goToLogin() {

        LoginController controller = navegador.goTo(Scenes.Login);
        assert controller != null;
        controller.setTitle(Titulos.Login);
        controller.setPresenter(new LoginPresenter(controller, new AuthRepositoryImpl()));

    }
}