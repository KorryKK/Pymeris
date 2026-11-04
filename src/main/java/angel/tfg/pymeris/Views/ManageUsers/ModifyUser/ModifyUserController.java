package angel.tfg.pymeris.Views.ManageUsers.ModifyUser;

import angel.tfg.pymeris.Base.ViewController;
import angel.tfg.pymeris.Model.Entities.Usuario;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.ToggleSwitch;

public class ModifyUserController extends ViewController implements MoUsContract.View{

    @FXML
    private Label lblNombre, lblStatus, lblID;
    @FXML
    private TextField tfNombre, tfApellido, tfCorreo, tfTelefono, tfUsuario;
    @FXML
    private Button btnModify;
    @FXML
    private ToggleSwitch tsAdmin, tsEstado;

    private Usuario u;
    private MoUsContract.Presenter presenter;

    public void onModifyUser() {
        presenter.onModifyUserClick();
    }

    @Override
    public void showModifyStatus(String message) {
        lblStatus.setText(message);
        btnOf(false);
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
        btnModify.setDisable(false);
    }

    public void onCancel() {
        Stage stage = (Stage) lblStatus.getScene().getWindow();
        stage.close();
    }

    @Override
    public void setPresenter(MoUsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void btnOf(boolean b) {
        btnModify.setDisable(b);
    }

    @Override
    public void getInfoUser(Usuario u) {
        this.u = u;

        lblID.setText(String.valueOf(u.getId_Empleado()));
        lblNombre.setText(u.getNombre());
        tfNombre.setText(u.getNombre());
        tfApellido.setText(u.getApellido());
        tfCorreo.setText(u.getCorreo());
        tfTelefono.setText(u.getTelefono());
        tfUsuario.setText(u.getUser());
        tsAdmin.setSelected(u.getRol().equals("Administrador"));
        tsEstado.setSelected(u.getEstado_Usuario() == 1);
    }

    @Override
    public String getID() {
        return String.valueOf(u.getId_Empleado());
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
    public String getUsuario() {
        return tfUsuario.getText();
    }
    @Override
    public boolean getAdmin() {
        return tsAdmin.isSelected();
    }
    @Override
    public boolean getEstado() {
        return tsEstado.isSelected();
    }
}