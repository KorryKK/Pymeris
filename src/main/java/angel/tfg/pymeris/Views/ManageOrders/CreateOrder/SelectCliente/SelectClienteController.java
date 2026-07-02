package angel.tfg.pymeris.Views.ManageOrders.CreateOrder.SelectCliente;

import angel.tfg.pymeris.Model.Entities.Cliente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;

public class SelectClienteController implements SelectClienteContract.View {

    @FXML
    private TableView<Cliente> tvCliente;
    @FXML
    private TableColumn<Cliente, String> colNombre, colApellido, colDniNif;
    private final ObservableList<Cliente> datosClientes = FXCollections.observableArrayList();

    private SelectClienteContract.Presenter presenter;

    @FXML
    public void initialize() {
        colDniNif.setCellValueFactory(new PropertyValueFactory<>("NIF_DNI"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));

        tvCliente.setItems(datosClientes);
    }

    @Override
    public void mostrarClientes(List<Cliente> clientes) {
        datosClientes.setAll(clientes);
    }

    @Override
    public void setPresenter(SelectClienteContract.Presenter presenter) {
        this.presenter = presenter;
        this.presenter.cargarClientes();
    }

    public void onSelect() {
        Cliente cliente = tvCliente.getSelectionModel().getSelectedItem();

        if (cliente != null) {
            System.out.println("Le pasamos cliente al presenter.");
            presenter.onSeclectClick(cliente);
        } else {
            System.out.println("Seleccione un cliente.");
        }
    }

    @Override
    public void toClose() {
        Stage stage = (Stage) tvCliente.getScene().getWindow();
        stage.close();
    }

    public void onCancel() {
        Stage stage = (Stage) tvCliente.getScene().getWindow();
        stage.close();
    }
}
