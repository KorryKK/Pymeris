package angel.tfg.pymeris.Views.ManageClients;

import angel.tfg.pymeris.Base.ViewController;
import angel.tfg.pymeris.Model.Entities.Cliente;
import angel.tfg.pymeris.Model.Repository.Clientes.ClientesRepositoryImpl;
import angel.tfg.pymeris.Util.Enums.Scenes;
import angel.tfg.pymeris.Views.ManageClients.CreateClient.NewClientController;
import angel.tfg.pymeris.Views.ManageClients.CreateClient.NewClientPresenter;
import angel.tfg.pymeris.Views.ManageClients.ModifyClient.ModifyClientController;
import angel.tfg.pymeris.Views.ManageClients.ModifyClient.ModifyClientPresenter;
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

public class ManageClientesController extends ViewController implements MaClContract.View{

    private MaClContract.Presenter presenter;
    private final ClientesRepositoryImpl repository = new ClientesRepositoryImpl();

    @FXML
    private TextField tfSearch;

    @FXML
    private ProgressIndicator piCarga;

    @FXML
    private TableView<Cliente> tvClientes;

    @FXML
    private TableColumn<Cliente, Integer> colDNI, colActivo;
    @FXML
    private TableColumn<Cliente, String> colNombre, colApellido, colCorreo, colTelefono;

    private final ObservableList<Cliente> masterData = FXCollections.observableArrayList();
    private FilteredList<Cliente> filteredData;

    @FXML
    public void initialize() {
        // Inicializamos la FilteredList con la masterData
        // El predicado "p -> true" significa que al principio se ven todos
        filteredData = new FilteredList<>(masterData, p -> true);

        // Configuramos el Listener
        tfSearch.textProperty().addListener((obs, oldValue, newValue) -> {
            System.out.println("Buscando: " + newValue);
            filteredData.setPredicate(cliente -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lower = newValue.toLowerCase();

                // IMPORTANTE: Comprobar que los campos no sean nul antes de hacer toLowerCase()
                String nombre = (cliente.getNombre() != null) ? cliente.getNombre().toLowerCase() : "";
                String apellido = (cliente.getApellido() != null) ? cliente.getApellido().toLowerCase() : "";

                return nombre.contains(lower) || apellido.contains(lower);
            });
        });

        // Cargamos la configuración de la tabla
        configurarTabla();
    }

    @Override
    public void mostrarClientes(List<Cliente> lista) {
        // Añadimos los datos
        masterData.setAll(lista);
    }

    public void onCreateClient() {
        System.out.println("Creando Cliente...");
        NewClientController controller = navegador.goToModal(Scenes.NewCliente, "NEW CLIENTE");
        assert controller != null;
        controller.setPresenter(new NewClientPresenter(controller, repository, presenter));
    }

    public void onModifyClient() {
        Cliente c = tvClientes.getSelectionModel().getSelectedItem();
        if (c != null) {
            System.out.println("Modificando cliente: " + c.getNombre());

            ModifyClientController controller = navegador.goToModal(Scenes.ModifyClient, "MODIFICANDO CLIENTE");
            assert controller != null;
            controller.getInfoClient(c);
            controller.setPresenter(new ModifyClientPresenter(controller, repository, presenter));
        } else  {
            System.out.println("No hay ningún cliente seleccionado.\nPor favor, seleccione uno.");
        }
    }

    public void onDeleteClient() {
        Cliente c = tvClientes.getSelectionModel().getSelectedItem();
        if (c != null) {
            System.out.println("Eliminando cliente: " + c);
            presenter.onDeleteClientClick(c.getNIF_DNI());
        } else  {
            System.out.println("No hay ningún cliente seleccionado.\nPor favor, seleccione uno.");
        }
    }

    public void goBack() {
        navegador.goTo(Scenes.SelectOptionMain);
    }

    @Override
    public void setPresenter(MaClContract.Presenter presenter){
        this.presenter = presenter;
        this.presenter.cargarClientes();
    }

    private void configurarTabla() {
        // 1. Vinculamos las columnas con las propiedades de mi objeto Cliente
        // El nombre debe coincidir EXACTAMENTE con el atributo de la clase Cliente
        colDNI.setCellValueFactory(new PropertyValueFactory<>("NIF_DNI"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colActivo.setCellValueFactory(new PropertyValueFactory<>("activo"));

        // 2. Conectamos la lista con la tabla
        tvClientes.setItems(filteredData);
    }

    @Override
    public void mostrarCarga(boolean b) {
        piCarga.setVisible(b);
    }

}
