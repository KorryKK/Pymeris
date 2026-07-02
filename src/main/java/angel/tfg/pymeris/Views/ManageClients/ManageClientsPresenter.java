package angel.tfg.pymeris.Views.ManageClients;

import angel.tfg.pymeris.Model.Entities.Cliente;
import angel.tfg.pymeris.Model.Repository.Clientes.ClientesRepository;
import angel.tfg.pymeris.Util.MostrarAlertas;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.List;
import java.util.Optional;

public class ManageClientsPresenter implements MaClContract.Presenter{

    private final MaClContract.View controller;
    private final ClientesRepository repository;

    public ManageClientsPresenter(MaClContract.View controller, ClientesRepository repository) {
        this.controller = controller;
        this.repository = repository;
    }

    @Override
    public void cargarClientes() {
        controller.mostrarCarga(true);

        new Thread(() -> {
            try {
                List<Cliente> lista = repository.obtenerTodosLosClientes();

                Platform.runLater(() -> {
                    controller.mostrarCarga(false);
                    controller.mostrarClientes(lista);
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    controller.mostrarCarga(false);
                    System.out.println(e.getMessage() + "\nError al cargar los clientes.");
                });
            }
        }).start();
    }

    @Override
    public void onDeleteClientClick(String dni) {
        controller.mostrarCarga(true);
        System.out.println("Pedimos al administrador confirmación de la eliminación.");
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, "¿Seguro que desea ELIMINAR este cliente?\n Esta opción no se puede deshacer.",  ButtonType.YES, ButtonType.NO);

        // Capturamos si el usuario confirma o cancela
        Optional<ButtonType> result = alerta.showAndWait();

        if(result.isEmpty() || result.get() != ButtonType.YES) {
            controller.mostrarCarga(false);
            System.out.println("No se ha eliminado el cliente.");
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "No se ha eliminado el cliente.");
            alert.setTitle("Acción cancelada");
            alert.setHeaderText("Acción cancelada");
            alert.showAndWait();
            return;
        }

        new Thread(()->{
            boolean success = repository.eliminarCliente(dni);

            if (success){
                Platform.runLater(()->{
                    controller.mostrarCarga(false);
                    System.out.println("Cliente eliminado correctamente.");
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Cliente eliminado correctamente.");
                    alert.setHeaderText("Cliente eliminado correctamente.");
                    alert.setContentText("Cliente eliminado correctamente.");
                    alert.show();
                    cargarClientes();
                });
            }else {
                Platform.runLater(()->{
                    controller.mostrarCarga(false);
                    MostrarAlertas.mostrarError("ELIMINAR CLIENTE", "Este cliente no se puede eliminar porque tiene pedidos creados.");
                });
            }
        }).start();
    }
}
