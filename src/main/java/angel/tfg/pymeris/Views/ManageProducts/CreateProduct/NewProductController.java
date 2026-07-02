package angel.tfg.pymeris.Views.ManageProducts.CreateProduct;

import angel.tfg.pymeris.Base.ViewController;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

public class NewProductController extends ViewController implements NewProductContract.View{

    @FXML
    private TextField tfNombre, tfCategoria, tfPrecio, tfIVA;
    @FXML
    private TextArea taDescripcion;
    @FXML
    private Label lblStatus;
    @FXML
    private ProgressBar pbIndicator;

    private NewProductContract.Presenter presenter;

    public void onCrear() {
        presenter.onCrearClick();
    }

    @Override
    public void showSuccessCreateProduct(String message) {
        lblStatus.setText(message);
        PauseTransition delay = new PauseTransition(Duration.seconds(2));

        delay.setOnFinished(event -> {
            Stage stage = (Stage) lblStatus.getScene().getWindow();
            stage.close();
        });

        delay.play();
    }

    @Override
    public void showErrorCreateProduct(String error) {
        lblStatus.setText(error);
    }

    @Override
    public void cargando(boolean b) {
        pbIndicator.setVisible(b);
    }

    public void onCancelar() {
        lblStatus.setText("No se crea ningun producto.");
        PauseTransition delay = new PauseTransition(Duration.seconds(1.2));

        delay.setOnFinished(event -> {
            Stage stage = (Stage) lblStatus.getScene().getWindow();
            stage.close();
        });

        delay.play();
    }

    @Override
    public String getNombre() {
        return tfNombre.getText();
    }

    @Override
    public String getCategoria() {
        return tfCategoria.getText();
    }

    @Override
    public String getDescripcion() {
        return taDescripcion.getText();
    }

    @Override
    public String getPrecioUnitario() {
        return tfPrecio.getText();
    }

    @Override
    public String getIVA() {
        return tfIVA.getText();
    }

    @Override
    public void setPresenter(NewProductContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
