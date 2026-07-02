package angel.tfg.pymeris.Views.ManageProducts.UpDownStockProduct;

import angel.tfg.pymeris.Base.ViewController;
import angel.tfg.pymeris.Model.Entities.Producto;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javafx.util.Duration;

public class UpDownStockProductController extends ViewController implements UpDoStPrContract.View {

    @FXML
    private Label lblNombre, lblStatus;
    @FXML
    private ToggleGroup Pepe;
    @FXML
    private TextField tfCantidad;

    private Producto p;
    private UpDoStPrContract.Presenter presenter;

    public void onModifyStock() {
        presenter.onModifyStockClick();
    }

    @Override
    public void getProducto(Producto p) {
        this.p = p;
        lblNombre.setText(p.getNombre());
    }

    @Override
    public String getSeleccionado() {
        RadioButton rb = (RadioButton) Pepe.getSelectedToggle();
        return  rb.getText();
    }

    @Override
    public int getIdProducto() {
        return p.getId_Producto();
    }

    @Override
    public String getCantidad() {
        return tfCantidad.getText();
    }

    @Override
    public void showStatus(String message, boolean b) {
        lblStatus.setText(message);
        if (b) {    // Se ha compleatdo la modificación, se cierra la ventana en 1.25 segundos
            PauseTransition delay = new PauseTransition(Duration.seconds(1.25));

            delay.setOnFinished(event -> {

                Stage stage = (Stage) lblStatus.getScene().getWindow();
                stage.close();
            });

            delay.play();
        }
    }

    @Override
    public void setPresenter(UpDoStPrContract.Presenter presenter) {
        this.presenter = presenter;
    }

    public void onCancel() {
        Stage  stage = (Stage) lblStatus.getScene().getWindow();
        stage.close();
    }
}
