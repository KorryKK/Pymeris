package angel.tfg.pymeris.Views.ManageOrders.ShowOrder;

import angel.tfg.pymeris.Base.ViewController;
import angel.tfg.pymeris.Model.Entities.DetallePedido;
import angel.tfg.pymeris.Model.Entities.Pedido;
import angel.tfg.pymeris.Model.Repository.Pedidos.PedidosRepositoryImpl;
import angel.tfg.pymeris.Util.Enums.Scenes;
import angel.tfg.pymeris.Util.Enums.Titulos;
import angel.tfg.pymeris.Views.ManageOrders.ManageOrdersController;
import angel.tfg.pymeris.Views.ManageOrders.ManageOrdersPresenter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.util.List;

public class ShowOrderController extends ViewController implements ShowOrderContract.View {

    @FXML
    private ProgressIndicator piCarga;
    @FXML
    private Label lblNoPedido, lblDniNif, lblCliente, lblFecha, lblImporte, lblNoEmpleado, lblEstado;
    @FXML
    private TableView<DetallePedido> tvProductos;
    @FXML
    private TableColumn<DetallePedido, Integer> colIdProducto, colCantidad, colIVA;
    @FXML
    private TableColumn<DetallePedido, String> colNombre, colSubtotal, colTotal;
    @FXML
    private TableColumn<DetallePedido, BigDecimal> colPrecio;

    private final ObservableList<DetallePedido> datosTabla = FXCollections.observableArrayList();
    private ShowOrderContract.Presenter presenter;

    @FXML
    public void initialize() {
        configurarTabla();
    }

    @Override
    public void mostrarDetalles(List<DetallePedido> lista) {
        datosTabla.setAll(lista);
    }

    @Override
    public void setPresenter(ShowOrderContract.Presenter presenter) {
        this.presenter = presenter;
        this.presenter.cargarDetalles();
    }

    @Override
    public void mostrarCarga(boolean b) {
        piCarga.setVisible(b);
    }

    @Override
    public void setPedido(Pedido p) {
        lblNoPedido.setText(String.valueOf(p.getId_Pedido()));
        lblDniNif.setText(p.getNIF_DNI_Cliente());
        lblCliente.setText(p.getCliente());
        lblFecha.setText(p.getFecha_Pedido().toString());
        lblImporte.setText(p.getImporte_Total() + " €");
        lblNoEmpleado.setText(String.valueOf(p.getId_Empleado()));
        lblEstado.setText(p.getEstado_Pedido());
    }

    public void onCompletar() {
        presenter.onCompletarClick();
        presenter.cargarDetalles();
    }

    public void onCancel() {
        System.out.println("Volvemos a pedidos");
        ManageOrdersController controller = navegador.goTo(Scenes.Pedidos);
        assert controller != null;
        controller.setTitle(Titulos.Pedidos);
        controller.setPresenter(new ManageOrdersPresenter(controller, new PedidosRepositoryImpl()));
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
}
