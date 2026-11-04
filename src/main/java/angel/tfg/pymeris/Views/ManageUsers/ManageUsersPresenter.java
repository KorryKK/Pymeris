package angel.tfg.pymeris.Views.ManageUsers;

import angel.tfg.pymeris.Model.Entities.Usuario;
import angel.tfg.pymeris.Model.Repository.Usuarios.UsuariosRepository;
import angel.tfg.pymeris.Util.MostrarAlertas;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.util.List;

public class ManageUsersPresenter implements MaUsContract.Presenter{

    String messageSuccess = """
            Se ha actualizado la contraseña del usuario.
            La próxima vez que inicie sesión tendrá que asignar una nueva.
            La contraseña temporal es 'Hola1234'""";

    private final MaUsContract.View controller;
    private final UsuariosRepository repository;


    public ManageUsersPresenter(MaUsContract.View controller, UsuariosRepository repository) {
        this.controller = controller;
        this.repository = repository;
    }

    @Override
    public void onRestartPasswordClick(Usuario u) {
        controller.mostrarCarga(true);
        System.out.println("Pedimos al administrador confirmación.");

        boolean confirm = MostrarAlertas.solictarConfirmacion("CONFIRMACIÓN", "¿Seguro que desea restablecer la contraseña del usuario?");

        if(!confirm) {
            controller.mostrarCarga(false);
            System.out.println("No se ha restaurado la contraseña.");
            return;
        }

        new Thread(()->{
            boolean success = repository.restartPassUsuario(u);

            if (success){
                Platform.runLater(()->{
                    controller.mostrarCarga(false);
                    System.out.println("Contraseña restablecida.");
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Usuario actualizado");
                    alert.setHeaderText("Usuario actualizado");
                    alert.setContentText(messageSuccess);
                    alert.show();
                });
            }else {
                Platform.runLater(()->{
                    controller.mostrarCarga(false);
                    System.out.println("La contraseña no se ha restablecido.");
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error");
                    alert.setContentText("No ha sido posible restaurar la contraseña.");
                });
            }
        }).start();
    }

    @Override
    public void cargarUsuarios() {
        controller.mostrarCarga(true);

        new Thread(() -> {
            try {
                List<Usuario> lista = repository.obtenerTodosLosUsuarios();

                Platform.runLater(() -> {
                    controller.mostrarCarga(false);
                    controller.mostrarUsuarios(lista);
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    controller.mostrarCarga(false);
                    System.out.println(e.getMessage() + "\nError al cargar los usuarios");
                });
            }
        }).start();
    }

    @Override
    public UsuariosRepository getRepository() {
        return repository;
    }
}