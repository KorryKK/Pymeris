package angel.tfg.pymeris.Views.ManageUsers.CreateUser;

import angel.tfg.pymeris.Base.ViewController;
import angel.tfg.pymeris.Util.MostrarAlertas;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

public class NewUserController extends ViewController implements NewUserContract.View {

    @FXML
    private TextField tfnombre, tfapellido, tftelefono, tfcorreo;

    @FXML
    private Label lblStatus;

    private NewUserContract.Presenter presenter;

    public void onCreate(){
        presenter.onCreateClick();
    }

    @Override
    public void setPresenter(NewUserContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showSuccessCreate(String message){
        MostrarAlertas.mostrarInformacion("Usuario Creado","La contraseña del usuario es el 'nombre + 1234'\nEjemplo: Juan1234.");

        PauseTransition delay = new PauseTransition(Duration.seconds(1.25));

        // 3. Definimos qué hacer cuando termine la pausa
        delay.setOnFinished(event -> {
            Stage stage = (Stage) lblStatus.getScene().getWindow();
            stage.close();
        });

        delay.play();
    }

    public void onCancel(){
        Stage stage = (Stage) lblStatus.getScene().getWindow();
        stage.close();
    }

    @Override
    public void showErrorCreate(String message) {
        lblStatus.setText(message);
    }

    @Override
    public String getNombre() { return tfnombre.getText();}
    @Override
    public String getApellido() { return tfapellido.getText(); }
    @Override
    public String getTelefono() { return tftelefono.getText(); }
    @Override
    public String getCorreo() { return tfcorreo.getText(); }
}
