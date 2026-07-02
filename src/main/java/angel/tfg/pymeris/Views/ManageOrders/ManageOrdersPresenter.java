package angel.tfg.pymeris.Views.ManageOrders;

import angel.tfg.pymeris.Model.Entities.Pedido;
import angel.tfg.pymeris.Model.Repository.Pedidos.PedidosRepository;
import angel.tfg.pymeris.Util.MostrarAlertas;
import javafx.application.Platform;

import java.util.List;

public class ManageOrdersPresenter implements MaOrContract.Presenter{

    private final MaOrContract.View controller;
    private final PedidosRepository repository;

    public ManageOrdersPresenter(MaOrContract.View controller, PedidosRepository repository) {
        this.repository = repository;
        this.controller = controller;
    }

    @Override
    public void cargarPedidos() {
        controller.mostrarCarga(true);

        new Thread(() -> {
            try {
                List<Pedido> pedidos = repository.getPedidos();

                Platform.runLater(() -> {
                    controller.mostrarCarga(false);
                    controller.mostrarPedidos(pedidos);
                });
            } catch (Exception ex) {
                Platform.runLater(() -> {
                    controller.mostrarCarga(false);
                    System.out.println(ex.getMessage() + "\nError al cargar los pedidos.");
                });
            }
        }).start();
    }

    @Override
    public void onCompletarClick(Pedido p) {
        if (p.getEstado_Pedido().equalsIgnoreCase("Completado")) {
            Platform.runLater(() -> MostrarAlertas.mostrarInformacion("Acción cancelada","Este pedido ya está completado"));
            return;
        }

        controller.mostrarCarga(true);

        new Thread(() -> {
            boolean success = repository.completarPedido(p.getId_Pedido());

            Platform.runLater(() -> {

                controller.mostrarCarga(false);
                if (success) {
                    System.out.println("Pedido completado correctamente.");
                    cargarPedidos();
                    MostrarAlertas.mostrarInformacion("Pedido Completado", "Pedido completado");
                } else  {
                    System.out.println("El pedido no se pudo completar.");
                    MostrarAlertas.mostrarWarning(null, "Pedido completado");
                }

            });
        }).start();
    }

    @Override
    public void onDeleteClick(Pedido p) {
        if (p.getEstado_Pedido().equalsIgnoreCase("Completado")) {
            MostrarAlertas.mostrarError("PEDIDO COMPLETADO", "ESTE PEDIDO NO SE PUEDE ELIMINAR PORQUE YA ESTÁ COMPLETADO");
            return;
        }
        controller.mostrarCarga(true);

        new Thread(() -> {
            boolean success = repository.eliminarPedido(p.getId_Pedido());

            Platform.runLater(() -> {
                controller.mostrarCarga(false);
                if (success) {
                    MostrarAlertas.mostrarInformacion("PEDIDO ELIMINADO", "Pedido eliminado correctamente.");
                    cargarPedidos();
                } else {
                    MostrarAlertas.mostrarWarning("ELIMINAR PEDIDO", "El pedido no se pudo eliminar.");
                }
            });
        }).start();
    }

    @Override
    public PedidosRepository getRepository() {
        return repository;
    }
}
