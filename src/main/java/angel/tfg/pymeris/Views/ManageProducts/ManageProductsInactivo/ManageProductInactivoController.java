package angel.tfg.pymeris.Views.ManageProducts.ManageProductsInactivo;

import angel.tfg.pymeris.Base.ViewController;
import angel.tfg.pymeris.Model.Entities.Producto;
import angel.tfg.pymeris.Util.Enums.Scenes;
import angel.tfg.pymeris.Util.Enums.Titulos;
import angel.tfg.pymeris.Views.ManageProducts.ManageProductsController;
import angel.tfg.pymeris.Views.ManageProducts.ManageProductsPresenter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class ManageProductInactivoController extends ViewController implements MaPrInContract.View {

    @FXML
    private TextField tfSearch;
    @FXML
    private ProgressIndicator piCarga;
    @FXML
    private TableView<Producto> tvProductos;
    @FXML
    private TableColumn<Producto, Integer> colIdProducto, colIva, colStock;
    @FXML
    private TableColumn<Producto, String> colNombre, colDescripcion, colCategoria, colPrecio;

    private final ObservableList<Producto> masterData = FXCollections.observableArrayList();
    private FilteredList<Producto> filteredData;

    private MaPrInContract.Presenter presenter;

    @FXML
    private void initialize(){
        // Iniciamos la FilteredList con la masterData
        // El predicado "p -> true" significa que al principio se ven todos
        filteredData = new FilteredList<>(masterData, p -> true);

        // Configuramos el Listener
        tfSearch.textProperty().addListener((obs, oldValue, newValue) -> {
            System.out.println("Buscando: " + newValue);
            filteredData.setPredicate(producto -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lower = newValue.toLowerCase();

                // IMPORTANTE: Comprobar que los campos no sean nulos antes de hacer toLowerCase()
                String nombre = (producto.getNombre() != null) ? producto.getNombre().toLowerCase() : "";

                return nombre.contains(lower);
            });
        });

        // Cargamos la configuracion de la tabla
        configurarTabla();
    }

    public void onUpProduct() {
        Producto p = tvProductos.getSelectionModel().getSelectedItem();

        if  (p != null) {
            System.out.println("Modificando producto..." + p.getNombre());

            presenter.onUpClick(p);
        } else {
            System.out.println("No hay ningun producto seleccionado.\nSelecciona un producto...");
        }
    }

    public void goBack() {
        System.out.println("Volvemos atrás");

        ManageProductsController controller = navegador.goTo(Scenes.Productos);
        assert controller != null;
        controller.setTitle(Titulos.Productos);
        controller.setPresenter(new ManageProductsPresenter(controller, presenter.getRepository()));
    }

    @Override
    public void mostrarProductos(List<Producto> lista) {
        // Añadimos los datos
        masterData.setAll(lista);
    }

    @Override
    public void mostrarCarga(boolean b) {
        piCarga.setVisible(b);
        tvProductos.setOpacity(b ? 0.5 : 1.0);
    }

    @Override
    public void setPresenter(MaPrInContract.Presenter presenter) {
        this.presenter = presenter;
        this.presenter.cargarProductos();
    }

    private void configurarTabla() {
        // 1. Vinculamos las columnas con las propiedades de mi objeto Producto
        colIdProducto.setCellValueFactory(new PropertyValueFactory<>("id_Producto"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colIva.setCellValueFactory(new PropertyValueFactory<>("IVA"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precioFormateado"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock_Actual"));

        // 2. Conectamos la lista con la tabla
        tvProductos.setItems(filteredData);
    }
}