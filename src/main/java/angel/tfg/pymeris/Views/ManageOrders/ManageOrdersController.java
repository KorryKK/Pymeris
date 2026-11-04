package angel.tfg.pymeris.Views.ManageOrders;

import angel.tfg.pymeris.Base.ViewController;
import angel.tfg.pymeris.Model.Entities.Pedido;
import angel.tfg.pymeris.Util.Enums.Scenes;
import angel.tfg.pymeris.Util.Enums.Titulos;
import angel.tfg.pymeris.Util.MostrarAlertas;
import angel.tfg.pymeris.Views.ManageOrders.CreateOrder.NewOrderController;
import angel.tfg.pymeris.Views.ManageOrders.CreateOrder.NewOrderPresenter;
import angel.tfg.pymeris.Views.ManageOrders.ShowOrder.ShowOrderController;
import angel.tfg.pymeris.Views.ManageOrders.ShowOrder.ShowOrderPresenter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.List;

public class ManageOrdersController extends ViewController implements MaOrContract.View {

    @FXML
    private ProgressIndicator piCarga;
    @FXML
    private TextField tfSearch;
    @FXML
    private TableView<Pedido> tvPedidos;
    @FXML
    private TableColumn<Pedido, Integer> colIdPedido, colIdEmpleado;
    @FXML
    private TableColumn<Pedido, String> colNifDniCliente, colCliente, colImporteTotal, colEstadoPedido;
    @FXML
    private TableColumn<Pedido, String> colFechaPedido;
    @FXML
    private DatePicker dpInicio, dpFin;
    private final ObservableList<Pedido> masterData = FXCollections.observableArrayList();
    private FilteredList<Pedido> filteredData;

    private MaOrContract.Presenter presenter;

    @FXML
    private void initialize() {
        // Iniciamos la FilteredList con la masterData
        // El predicado "p -> true" significa que al principio se ven todos
        filteredData = new FilteredList<>(masterData, p -> true);

        tfSearch.textProperty().addListener((obs, old, newValue) -> actualizarFiltro());
        dpInicio.valueProperty().addListener((obs, old, newValue) -> actualizarFiltro());
        dpFin.valueProperty().addListener((obs, old, newValue) -> actualizarFiltro());

        // Cargamos la configuracion de la tabla
        configurarTabla();
    }

    @Override
    public void mostrarPedidos(List<Pedido> pedidos) {
        masterData.setAll(pedidos);
    }

    @Override
    public void setPresenter(MaOrContract.Presenter presenter) {
        this.presenter = presenter;
        this.presenter.cargarPedidos();
    }

    @Override
    public void mostrarCarga(boolean b) {
        piCarga.setVisible(b);
        tvPedidos.setOpacity(b ? 0.5 : 1.0);
    }

    public void onCreatePedido() {
        System.out.println("Vamos a crear un nuevo pedido.");
        NewOrderController controller = navegador.goTo(Scenes.NewPedido);
        assert controller != null;
        controller.setTitle(Titulos.NewOrder);
        controller.setPresenter(new NewOrderPresenter(controller,presenter.getRepository(), presenter));
    }

    public void onCompletarPedido() {
        Pedido p = tvPedidos.getSelectionModel().getSelectedItem();

        if (p != null) {
            boolean confirm = MostrarAlertas.solictarConfirmacion("COMPLETANDO PEDIDO", "¿QUIERES COMPLETAR ESTE PEDIDO?");
            if (confirm) {
                System.out.println("Completando pedido ... " + p.getId_Pedido());

                presenter.onCompletarClick(p);
            } else {
                System.out.println("No hay ningun pedido seleccionado.\nSelecciona un pedido...");
            }
        }
    }

    public void onShowPedido() {
        Pedido p = tvPedidos.getSelectionModel().getSelectedItem();

        if (p != null) {
            ShowOrderController controller = navegador.goTo(Scenes.ShowPedido);
            assert controller != null;
            controller.setPresenter(new ShowOrderPresenter(controller, presenter.getRepository(),p));

        } else {
            System.out.println("Selecciona un pedido...");
        }
    }

    public void onDeletePedido(){
        Pedido p = tvPedidos.getSelectionModel().getSelectedItem();
        if (p != null) {
            presenter.onDeleteClick(p);
        } else {
            System.out.println("Selecciona un pedido...");
        }
    }

    public void goBack() {
        navegador.goTo(Scenes.SelectOptionMain);
    }

    /**
     * Método opcional para limpiar todos los filtros de golpe
     */
    public void onLimpiarFiltros() {
        tfSearch.clear();
        dpInicio.setValue(null);
        dpFin.setValue(null);
    }

    private void configurarTabla() {
        // 1. Vinculamos las columnas con las propiedades de mi objeto Producto
        colIdPedido.setCellValueFactory(new PropertyValueFactory<>("id_Pedido"));
        colIdEmpleado.setCellValueFactory(new PropertyValueFactory<>("id_Empleado"));
        colNifDniCliente.setCellValueFactory(new PropertyValueFactory<>("NIF_DNI_Cliente"));
        colCliente.setCellValueFactory(new PropertyValueFactory<>("Cliente"));
        colFechaPedido.setCellValueFactory(new PropertyValueFactory<>("fecha_Pedido_formatted"));
        colImporteTotal.setCellValueFactory(new PropertyValueFactory<>("importe_Total"));
        colEstadoPedido.setCellValueFactory(new PropertyValueFactory<>("estado_Pedido"));

        // 2. Conectamos la lista con la tabla
        tvPedidos.setItems(filteredData);
    }

    /**
     * Lógica centralizada de filtrado.
     * Combina el buscador de texto con el rango de fechas de los DatePickers.
     */
    private void actualizarFiltro() {
        filteredData.setPredicate(pedido -> {
            // Devuelve true si coincide el texto y la fecha
            return isCoincideTexto(pedido) && isCoincideFecha(pedido);
        });
    }

    private boolean isCoincideTexto(Pedido pedido) {
        String texto = tfSearch.getText();
        if (texto == null || texto.isEmpty()) {
            return true; // Si no hay texto, todos coinciden
        }

        String lower = texto.toLowerCase();
        String nombre = (pedido.getCliente() != null) ? pedido.getCliente().toLowerCase() : "";
        String nifdni = (pedido.getNIF_DNI_Cliente() != null) ? pedido.getNIF_DNI_Cliente().toLowerCase() : "";

        return nombre.contains(lower) || nifdni.contains(lower);
    }

    private boolean isCoincideFecha(Pedido pedido) {
        if (pedido.getFecha_Pedido() == null) return true;

        LocalDate fechaPed = pedido.getFecha_Pedido().toLocalDate();
        LocalDate inicio = dpInicio.getValue();
        LocalDate fin = dpFin.getValue();

        if (inicio != null && fechaPed.isBefore(inicio)) return false;
        return fin == null || !fechaPed.isAfter(fin);
    }
}