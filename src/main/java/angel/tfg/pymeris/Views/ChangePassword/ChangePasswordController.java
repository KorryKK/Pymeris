package angel.tfg.pymeris.Views.ChangePassword;

import angel.tfg.pymeris.Base.ViewController;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ChangePasswordController extends ViewController implements ChPwContract.View{

    private String username;
    private ChPwContract.Presenter presenter;

    @FXML
    private PasswordField pfPassword1, pfPassword2;

    @FXML
    private Label lblName, lblStatus;

    public void onChangePasswordClicked() {
        presenter.onChangeClicked();
    }

    @Override
    public String getPassword1() {
        return pfPassword1.getText();
    }

    @Override
    public String getPassword2() {
        return pfPassword2.getText();
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void changeSuccess() {
        System.out.println("La contraseña se cambio correctamente");
        lblStatus.setText("Contraseña cambiada exitosamente");
        // Creamos una "pausa" que se ejecuta en segundo plano
        PauseTransition delay = new PauseTransition(Duration.seconds(2));

        // Definimos qué hacer cuando termine la pausa
        // En este caso queremos cerrar la ventana de cambio de contraseña
        delay.setOnFinished(event -> {
            Stage stage = (Stage) pfPassword1.getScene().getWindow();
            stage.close();
        });

        // Arrancamos la pausa
        delay.play();
    }

    @Override
    public void changeError() {
        lblStatus.setText("Ha ocurrido un error");
    }

    @Override
    public void istext1Empty() {
        lblStatus.setText("La primera contraseña está vacía.");
    }

    @Override
    public void istext2Empty() {
        lblStatus.setText("La segunda contraseña está vacía.");
    }

    @Override
    public void isbothtextEmpty() {
        lblStatus.setText("Ambas contraseñas están vacías.");
    }

    @Override
    public void istextUnmatch() {
        lblStatus.setText("Las contraseñas no coinciden.");
    }

    @Override
    public void setPresenter(ChPwContract.Presenter presenter) {
        this.presenter = presenter;
    }

    public void setChanging() {
        lblStatus.setText("Cambiando...");
    }

    public void setUsername(String username) {
        this.username = username;
        lblName.setText(username);
    }
}
