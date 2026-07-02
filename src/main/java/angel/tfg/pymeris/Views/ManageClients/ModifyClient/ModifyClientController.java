package angel.tfg.pymeris.Views.ManageClients.ModifyClient;

import angel.tfg.pymeris.Base.ViewController;
import angel.tfg.pymeris.Model.Entities.Cliente;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.ToggleSwitch;

public class ModifyClientController extends ViewController implements MoClContract.View {

    private String dninifanterior;

    @FXML
    private Label lblStatus, lblNombre;
    @FXML
    private TextField tfDniNif, tfNombre, tfApellido, tfCorreo, tfTelefono;
    @FXML
    private Button btnModify;
    @FXML
    private ToggleSwitch tsActivo;

    private MoClContract.Presenter presenter;

    public void onModifyClient() {
        presenter.onModifyClientClick(dninifanterior);
    }

    @Override
    public void showModifyStatus(String message) {
        lblStatus.setText(message);
        btnOff(false);
        PauseTransition delay = new PauseTransition(Duration.seconds(1.25));

        delay.setOnFinished(event -> {
            Stage stage = (Stage) lblStatus.getScene().getWindow();
            stage.close();
        });

        delay.play();
    }

    @Override
    public void showError(String message) {
        lblStatus.setText(message);
        btnOff(false);
    }

    public void onCancel() {
        Stage stage = (Stage) lblStatus.getScene().getWindow();
        stage.close();
    }

    @Override
    public String getDNI_NFI() {
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
    public boolean getActivo() {
        return tsActivo.isSelected();
    }

    @Override
    public void setPresenter(MoClContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void getInfoClient(Cliente c) {
        dninifanterior = c.getNIF_DNI();

        lblNombre.setText(c.getNombre());
        tfDniNif.setText(String.valueOf(c.getNIF_DNI()));
        tfNombre.setText(c.getNombre());
        tfApellido.setText(c.getApellido());
        tfCorreo.setText(c.getCorreo());
        tfTelefono.setText(c.getTelefono());
        tsActivo.setSelected(c.getActivo() == 1);
    }

    @Override
    public void btnOff(boolean b) {
        btnModify.setDisable(b);
    }
}
