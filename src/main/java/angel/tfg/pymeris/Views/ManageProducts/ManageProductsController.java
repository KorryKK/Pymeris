package angel.tfg.pymeris.Views.ManageProducts;

import angel.tfg.pymeris.Base.ViewController;
import angel.tfg.pymeris.Model.Entities.Producto;
import angel.tfg.pymeris.Util.Enums.Scenes;
import angel.tfg.pymeris.Util.Enums.Titulos;
import angel.tfg.pymeris.Views.ManageProducts.CreateProduct.NewProductController;
import angel.tfg.pymeris.Views.ManageProducts.CreateProduct.NewProductPresenter;
import angel.tfg.pymeris.Views.ManageProducts.ManageProductsInactivo.ManageProductInactivoController;
import angel.tfg.pymeris.Views.ManageProducts.ManageProductsInactivo.ManageProductInactivoPresenter;
import angel.tfg.pymeris.Views.ManageProducts.ModifyProduct.ModifyProductController;
import angel.tfg.pymeris.Views.ManageProducts.ModifyProduct.ModifyProductPresenter;
import angel.tfg.pymeris.Views.ManageProducts.UpDownStockProduct.UpDownStockProductController;
import angel.tfg.pymeris.Views.ManageProducts.UpDownStockProduct.UpDownStockProductPresenter;
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

public class ManageProductsController extends ViewController implements MaPrContract.View{

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

    private MaPrContract.Presenter presenter;

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

    @Override
    public void mostrarProductos(List<Producto> lista) {
        // Añadimos los datos
        masterData.setAll(lista);
    }

    public void onCreateProduct() {
        System.out.println("Creando producto...");
        NewProductController controller = navegador.goToModal(Scenes.NewProducto, "NUEVO PRODUCTO");
        assert controller != null;
        controller.setPresenter(new NewProductPresenter(controller, presenter.getRepository(), presenter));
    }

    public void onModifyProduct() {
        Producto p = tvProductos.getSelectionModel().getSelectedItem();

        if  (p != null) {
            System.out.println("Modificando producto..." + p.getNombre() + ", Activo: " + p.getActivo());

            ModifyProductController controller = navegador.goToModal(Scenes.ModifyProducto, "MODIFICAR PRODUCTO");
            assert controller != null;
            controller.getInfoProduct(p);
            controller.setPresenter(new ModifyProductPresenter(controller, presenter.getRepository(), presenter));
        } else {
            System.out.println("No hay ningun producto seleccionado.\nSelecciona un producto...");
        }
    }

    public void onModifyStock() {
        Producto p = tvProductos.getSelectionModel().getSelectedItem();

        if  (p != null) {
            System.out.println("Modificando stock producto..." + p.getNombre());

            UpDownStockProductController controller = navegador.goToModal(Scenes.ModifyStockProductos, "MODIFICAR STOCK PRODUCTO");
            assert controller != null;
            controller.getProducto(p);
            controller.setPresenter(new UpDownStockProductPresenter(controller, presenter.getRepository(), presenter));
        } else {
            System.out.println("No hay ningun producto seleccionado.\nSelecciona un producto...");
        }
    }

    public void onDownProduct() {
        Producto p = tvProductos.getSelectionModel().getSelectedItem();

        if  (p != null) {
            System.out.println("Dando de baja producto:  " + p.getNombre());

            presenter.onDownClick(p);
        } else {
            System.out.println("No hay ningun producto seleccionado.\nSelecciona un producto...");
        }
    }

    public void onShowInactivo() {
        System.out.println("Vamos a ver los productos inactivos...");

        ManageProductInactivoController controller = navegador.goTo(Scenes.ProductosInactivos);
        assert controller != null;
        controller.setTitle(Titulos.ProductosInactivos);
        controller.setPresenter(new ManageProductInactivoPresenter(controller, presenter.getRepository()));
    }

    public void goBack() {
        navegador.goTo(Scenes.SelectOptionMain);
    }

    @Override
    public void setPresenter(MaPrContract.Presenter presenter) {
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

    @Override
    public void mostrarCarga(boolean b) {
        piCarga.setVisible(b);
        tvProductos.setOpacity(b ? 0.5 : 1.0);
    }
}
