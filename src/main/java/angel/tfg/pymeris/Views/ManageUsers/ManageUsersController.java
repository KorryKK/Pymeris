package angel.tfg.pymeris.Views.ManageUsers;

import angel.tfg.pymeris.Base.ViewController;
import angel.tfg.pymeris.Model.Entities.Usuario;
import angel.tfg.pymeris.Util.Enums.Scenes;
import angel.tfg.pymeris.Util.MostrarAlertas;
import angel.tfg.pymeris.Util.Session;
import angel.tfg.pymeris.Views.ManageUsers.CreateUser.NewUserController;
import angel.tfg.pymeris.Views.ManageUsers.CreateUser.NewUserPresenter;
import angel.tfg.pymeris.Views.ManageUsers.ModifyUser.ModifyUserController;
import angel.tfg.pymeris.Views.ManageUsers.ModifyUser.ModifyUserPresenter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class ManageUsersController extends ViewController implements MaUsContract.View {

    private final ObservableList<Usuario> masterData = FXCollections.observableArrayList();
    @FXML
    private TextField tfSearch;
    @FXML
    private ProgressIndicator piCarga;
    @FXML
    private TableView<Usuario> tvUsuarios;
    @FXML
    private TableColumn<Usuario, Integer> colId;
    @FXML
    private TableColumn<Usuario, String> colNombre, colApellido, colCorreo, colTelefono, colUser, colEstadoUsuario, colRol, colAcceso;
    @FXML
    private TableColumn<Usuario, String> colFechaRegistro, colFechaModificacion;
    private FilteredList<Usuario> filteredData;
    private MaUsContract.Presenter presenter;

    @FXML
    private void initialize() {
        // Inicializamos la FilteredList con la masterData
        // El predicado "p -> true" significa que al principio se ven todos
        filteredData = new FilteredList<>(masterData, p -> true);

        // Configuramos el Listener
        tfSearch.textProperty().addListener((obs, oldValue, newValue) -> {
            System.out.println("Buscando: " + newValue);
            filteredData.setPredicate(user -> {
                // Si no hay texto, mostramos todos
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lower = newValue.toLowerCase();

                // IMPORTANTE: Comprobar que los campos no sean null antes de hacer toLowerCase()
                String nombre = (user.getNombre() != null) ? user.getNombre().toLowerCase() : "";
                String apellido = (user.getApellido() != null) ? user.getApellido().toLowerCase() : "";

                // Filtramos por Nombre, Apellido o cualquier otro campo que desees
                return nombre.contains(lower) || apellido.contains(lower);
            });
        });

        // Cargamos la configuración de la tabla
        configurarTabla();
    }

    @Override
    public void mostrarUsuarios(List<Usuario> lista) {
        // Añadimos los datos
        masterData.setAll(lista);
    }

    public void onCreateUser() {
        System.out.println("Creando usuario...");
        NewUserController controller = navegador.goToModal(Scenes.NewUsuario, "NUEVO USUARIO");
        assert controller != null;
        controller.setPresenter(new NewUserPresenter(controller, presenter.getRepository(), presenter));
    }

    public void onModifyUser() {
        Usuario u = tvUsuarios.getSelectionModel().getSelectedItem();

        // Si hay alguno seleccionado, lo pasamos a la nueva ventana
        if (u != null) {

            // --- INICIO DE LAS RESTRICCIONES ---

            // REGLA A: El usuaio predeterminado "admin" es intocable
            // comparamos el atributo 'user' (ignorando mayúsculas por si acaso)
            if (u.getUser().equalsIgnoreCase("admin")) {
                // Mostramos una alerta que bloquea el paso al usuario
                MostrarAlertas.mostrarError(null,"""
                        Este usuario es intocable.
                        No se puede modificar este usuario.
                        """);
                mostrarAlertaError("""
                        Este usuario es intocable.
                        No se puede modificar este usuario.
                        """);
                return;
            }

            // REGLA B: El administrador no puede modificarse a sí mismo.
            // Comparamos el ID del usuario seleccionado con el ID del usuario que ha iniciado sesión
            if (u.getId_Empleado() == Session.getIdEmpleado()) {
                MostrarAlertas.mostrarError(null,"""
                        Un administrador no puede modificarse a sí mismo.
                        Pida ayuda a otro administrador.
                        """);
                return;
            }

            // --- FIN RESTRICCIONES ---

            // Si llegamos aquí quiere decir que todo va bien
            System.out.println("Modificando: " + u.getNombre());

            ModifyUserController controller = navegador.goToModal(Scenes.ModifyUsuario, "MODIFICAR USUARIO");
            assert controller != null;
            controller.getInfoUser(u);
            controller.setPresenter(new ModifyUserPresenter(controller, presenter.getRepository(), presenter));
        } else {
            System.out.println("No hay ningún usuario seleccionado.\nNor favor, seleccione uno.");
        }
    }

    public void onRestartPassword() {
        Usuario u = tvUsuarios.getSelectionModel().getSelectedItem();
        if (u != null) {

            // --- INICIO DE LAS RESTRICCIONES ---

            // REGLA A: El usuaio predeterminado "admin" es intocable
            // comparamos el atributo 'user' (ignorando mayúsculas por si acaso)
            if (u.getUser().equalsIgnoreCase("admin")) {
                // Mostramos una alerta que bloquea el paso al usuario
                mostrarAlertaError("""
                        Estás intentando modificar la contraseña del administrador predeterminado.
                        Este usuario es intocable.
                        No se restaurar esta contraseña.
                        """);
                return;
            }

            // REGLA B: El administrador no puede modificarse a sí mismo.
            // Comparamos el ID del usuario seleccionado con el ID del usuario que ha iniciado sesión
            if (u.getId_Empleado() == Session.getIdEmpleado()) {
                mostrarAlertaError("""
                        Estás intentando restaurar tu propia contraseña.
                        Eso no se puede hacer.
                        Caca, suelta eso.
                        """);
                return;
            }

            // --- FIN RESTRICCIONES ---

            // Si llegamos aquí quiere decir que todo va bien
            System.out.println("Restaurando la contraseña de : " + u.getNombre());
            presenter.onRestartPasswordClick(u);
        } else {
            System.out.println("No hay ningún usuario seleccionado.\nPor favor, seleccione uno.");
        }
    }

    public void goBackUser() {
        navegador.goTo(Scenes.SelectOptionMain);
    }

    @Override
    public void setPresenter(MaUsContract.Presenter presenter) {
        this.presenter = presenter;
        this.presenter.cargarUsuarios();
    }

    private void configurarTabla() {
        // 1. Vinculamos las columnas con las propiedades de mi tabla Usuarios<
        colId.setCellValueFactory(new PropertyValueFactory<>("id_Empleado"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colUser.setCellValueFactory(new PropertyValueFactory<>("user"));
        colEstadoUsuario.setCellValueFactory(new PropertyValueFactory<>("estado_Usuario"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));
        colFechaRegistro.setCellValueFactory(new PropertyValueFactory<>("fecha_registro_formatted"));
        colFechaModificacion.setCellValueFactory(new PropertyValueFactory<>("ultima_modificacion_formatted"));
        colAcceso.setCellValueFactory(new PropertyValueFactory<>("acceso"));

        // 2. Conectamos la lista con la tabla
        tvUsuarios.setItems(filteredData);
    }

    @Override
    public void mostrarCarga(boolean b) {
        piCarga.setVisible(b);
        tvUsuarios.setOpacity(b ? 0.5 : 1.0);
    }

    /**
     * Crea y muestra un cuadro de diálogo de error con el título y mensaje indicados.
     * Esto evita repetir el código de creación de Alertas múltiples veces.
     * @param mensaje Es el mensaje que se va a utilizar en la ventana que se muestra al usuario
     */
    private void mostrarAlertaError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("OPERACIÓN DENEGADA");
        alert.setHeaderText(null); // Lo dejamos en null para que el diseño sea más limpio
        alert.setContentText(mensaje);
        alert.showAndWait(); // Pausa la aplicación hasta que el usuario cierre la alerta
    }
}