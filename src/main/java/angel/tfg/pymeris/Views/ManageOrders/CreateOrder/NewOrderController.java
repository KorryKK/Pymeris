package angel.tfg.pymeris.Views.ManageOrders.CreateOrder;

import angel.tfg.pymeris.Base.ViewController;
import angel.tfg.pymeris.Model.Entities.DetallePedido;
import angel.tfg.pymeris.Util.Enums.Scenes;
import angel.tfg.pymeris.Util.Enums.Titulos;
import angel.tfg.pymeris.Util.Session;
import angel.tfg.pymeris.Views.ManageOrders.CreateOrder.AddProduct.AddProductController;
import angel.tfg.pymeris.Views.ManageOrders.CreateOrder.AddProduct.AddProductPresenter;
import angel.tfg.pymeris.Views.ManageOrders.CreateOrder.SelectCliente.SelectClienteController;
import angel.tfg.pymeris.Views.ManageOrders.CreateOrder.SelectCliente.SelectClientePresenter;
import angel.tfg.pymeris.Views.ManageOrders.ManageOrdersController;
import angel.tfg.pymeris.Views.ManageOrders.ManageOrdersPresenter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.util.List;

public class NewOrderController extends ViewController implements NewOrderContract.View {

    @FXML
    private Button btnAnadir, btnEliminar, btnConfirmar;
    @FXML
    private ProgressIndicator piCarga;
    @FXML
    private Label lblInfoCl, lblInfoEm, lblCantidad, lblSubtotal, lblTotal;
    @FXML
    private TableView<DetallePedido> tvProductos;
    @FXML
    private TableColumn<DetallePedido, Integer> colIdProducto, colCantidad, colIVA;
    @FXML
    private TableColumn<DetallePedido, String> colNombre, colSubtotal, colTotal;
    @FXML
    private TableColumn<DetallePedido, BigDecimal> colPrecio;

    private final ObservableList<DetallePedido> datosTabla = FXCollections.observableArrayList();
    private NewOrderContract.Presenter presenter;

    @FXML
    private void initialize() {
        configurarTabla();

        // Ponemos la información del usuario
        setLblInfoEm(Session.getIdEmpleado() + " - " + Session.getNombre());

        // Creamos un listener para asegurar la recuperación de stock si se cierra la aplicación queriendo o sin querer.
        javafx.application.Platform.runLater(() -> {
            // Obtenemos el Stage y le asignamos el evento de la X
            getStage().setOnCloseRequest(event -> ejecutarAlCerrar());
        });
    }

    @Override
    public void mostrarDetalles(List<DetallePedido> lista) {
        // La vista recibe la lista del presenter y actualiza la tabla
        datosTabla.setAll(lista);
        lblCantidad.setText(presenter.getCantidadProductos());
        lblSubtotal.setText(presenter.getCantidadSubtotal());
        lblTotal.setText(presenter.getCantidadImporteFINAL().toString());
    }

    @Override
    public void setLblInfoCl(String txt) {
        lblInfoCl.setText(txt);
    }

    public void setLblInfoEm(String txt) {
        lblInfoEm.setText(txt);
    }

    @Override
    public void setPresenter(NewOrderContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void mostrarCarga(boolean b) {
        piCarga.setVisible(b);
    }

    @Override
    public void goBack() {
        ManageOrdersController controller = navegador.goTo(Scenes.Pedidos);
        assert controller != null;
        controller.setTitle(Titulos.Pedidos);
        controller.setPresenter(new ManageOrdersPresenter(controller, presenter.getRepository()));
    }

    @Override
    public void showAnadir(boolean b) {
        btnAnadir.setDisable(!b);
    }

    @Override
    public void showEliminar(boolean b) {
        btnEliminar.setDisable(!b);
    }

    @Override
    public void showConfirmar(boolean b) {
        btnConfirmar.setDisable(!b);
    }

    public void onSelectCliente() {
        SelectClienteController controller = navegador.goToModal(Scenes.SelectCliente, "Selección Cliente");
        assert controller != null;
        controller.setPresenter(new SelectClientePresenter(controller, presenter.getRepository(), presenter));
    }

    public void onAddProduct() {
        AddProductController controller = navegador.goToModal(Scenes.AddProducto, "Añadir producto");
        assert controller != null;
        controller.setPresenter(new AddProductPresenter(controller, presenter.getRepository(), presenter));
    }

    public void onDeleteProduct() {
        DetallePedido dp = tvProductos.getSelectionModel().getSelectedItem();
        if (dp != null) {
            System.out.println("Se va a eliminar el producto " + dp.getNombre_Producto() + "del pedido");
            presenter.borrarProducto(dp);
        } else {
            System.out.println("No se ha selecionado ningún producto");
        }
    }

    public void onCreateOrder() {
        presenter.onCreateOrderClick();
    }

    public void onCancel() {
        if(presenter.onCancelOrderClick()) {
            System.out.println("Cancelamos. Volvemos a pedidos.");
            goBack();
        } else System.out.println("No se han devuelto los productos al stock.");
    }

    private void configurarTabla() {
        // Vinculación directa con los atributos de DetallePedido
        colIdProducto.setCellValueFactory(new PropertyValueFactory<>("id_Producto"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre_Producto"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio_Unitario"));
        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
        colIVA.setCellValueFactory(new PropertyValueFactory<>("IVA"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("importe_Producto"));

        // Vinculamos la lista a la tabla
        tvProductos.setItems(datosTabla);
    }

    // Método que se ejecuta al cerrar la ventana para guardar el stock de nuevo
    private void ejecutarAlCerrar() {
        System.out.println("Limpiando datos y cerrando...");
        presenter.onCancelOrderClick();
    }

    // Método para coger el stage de la ventana
    private javafx.stage.Stage getStage() {
        // Método auxiliar para obtener la ventana desde el controlador
        return (javafx.stage.Stage) lblCantidad.getScene().getWindow();
    }
}
