package angel.tfg.pymeris.Views.ManageOrders.CreateOrder.AddProduct;

import angel.tfg.pymeris.Model.Entities.Producto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;

public class AddProductController implements AddProductContract.View {

    @FXML
    private Label lblStatus;
    @FXML
    private TextField tfCantidad;
    @FXML
    private TableView<Producto>  tvProducto;
    @FXML
    private TableColumn<Producto, String> colNombre, colDescripcion, colPrecio;
    @FXML
    private TableColumn<Producto, Integer> colStock;
    private final ObservableList<Producto> datosProductos = FXCollections.observableArrayList();

    private AddProductContract.Presenter presenter;

    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precioFormateado"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock_Actual"));

        tvProducto.setItems(datosProductos);
    }

    @Override
    public void mostrarProductos(List<Producto> productos) {
        datosProductos.clear();
        datosProductos.addAll(productos);
    }

    @Override
    public void setPresenter(AddProductContract.Presenter presenter) {
        this.presenter = presenter;
        this.presenter.cargarProductos();
    }

    @Override
    public int getCantidad() {
        return (tfCantidad.getText().isEmpty() || tfCantidad.getText().isBlank()) ? 0 : Integer.parseInt(tfCantidad.getText());
    }

    @Override
    public void showStatus(String message) {
        lblStatus.setText(message);
    }

    public void onAddProduct() {
        Producto p = tvProducto.getSelectionModel().getSelectedItem();

        if (p != null) {
            System.out.println("Se ha seleccionado: " + p.getNombre());
            presenter.onAddProductClick(p);
        } else {
            showStatus("Seleccione un producto.");
            System.out.println("Seleccione un producto.");
        }
    }

    public void onCancel() {
        Stage stage = (Stage) tvProducto.getScene().getWindow();
        stage.close();
    }
}
