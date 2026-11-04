package angel.tfg.pymeris.Views.ManageClients.CreateClient;

import angel.tfg.pymeris.Base.ViewController;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

public class NewClientController extends ViewController implements NewClientContract.View {

    @FXML
    private TextField tfDniNif, tfNombre, tfApellido, tfCorreo, tfTelefono;
    @FXML
    private Label lblStatus;

    private NewClientContract.Presenter presenter;

    public void onCreate() {
        presenter.onCreateClick();
    }

    @Override
    public String getNifDni() {
        return tfDniNif.getText();
    }
    @Override
    public String getNombre() {
        return tfNombre.getText();
    }
    @Override
    public String getApellido() {
        return tfApellido.getText();
    }
    @Override
    public String getCorreo() {
        return tfCorreo.getText();
    }
    @Override
    public String getTelefono() {
        return tfTelefono.getText();
    }

    @Override
    public void showSuccess(String message) {
        lblStatus.setText(message);

        // Hacemos una pausa para cerrar la ventana
        PauseTransition delay = new PauseTransition(Duration.seconds(1.25));

        delay.setOnFinished(event -> {
            Stage stage = (Stage) lblStatus.getScene().getWindow();
            stage.close();
        });

        delay.play();
    }

    @Override
    public void showError(String error) {
        lblStatus.setText(error);
    }

    public void onCancel() {
        Stage stage = (Stage) lblStatus.getScene().getWindow();
        stage.close();
    }

    @Override
    public void setPresenter(NewClientContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
