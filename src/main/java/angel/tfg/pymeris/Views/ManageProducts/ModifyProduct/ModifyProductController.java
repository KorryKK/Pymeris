package angel.tfg.pymeris.Views.ManageProducts.ModifyProduct;

import angel.tfg.pymeris.Base.ViewController;
import angel.tfg.pymeris.Model.Entities.Producto;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ModifyProductController extends ViewController implements MoPrContract.View{

    @FXML
    private Label lblID, lblStatus;
    @FXML
    private TextField tfNombre, tfCategoria, tfPrecio, tfIVA, tfStock;
    @FXML
    private TextArea taDescripcion;
    @FXML
    private Button btnModify;

    private Producto producto;
    private MoPrContract.Presenter presenter;

    public void onModifyProduct(){
        presenter.onModifyProductClick();
    }

    @Override
    public void showModifyStatus(String message, boolean b) {
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
    public void setPresenter(MoPrContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void getInfoProduct(Producto producto) {
        this.producto = producto;

        if (producto != null) {
            lblID.setText(String.valueOf(producto.getId_Producto()));
            tfNombre.setText(producto.getNombre());
            tfCategoria.setText(producto.getCategoria());
            taDescripcion.setText(producto.getDescripcion());
            // toPlainString para enviar el número limpio
            tfPrecio.setText(producto.getPrecioUnitario().toPlainString());

            tfIVA.setText(String.valueOf(producto.getIVA()));
            tfStock.setText(String.valueOf(producto.getStock_Actual()));
        } else {
            System.out.println("Error al cargar los datos en la ventana");
        }
    }

    @Override
    public String getID() {return String.valueOf(producto.getId_Producto());}
    @Override
    public String getNombre() {return tfNombre.getText();}
    @Override
    public String getCategoria() {return tfCategoria.getText();}
    @Override
    public String getDescripcion() {return taDescripcion.getText();}
    @Override
    public String getPrecio() {return tfPrecio.getText();}
    @Override
    public String getIVA() {return tfIVA.getText();}
    @Override
    public String getStock() {return tfStock.getText();}

    public void onCancel() {
        Stage stage = (Stage) btnModify.getScene().getWindow();
        stage.close();
    }
}