package angel.tfg.pymeris.Views.ManageOrders.ShowOrder;

import angel.tfg.pymeris.Model.Entities.DetallePedido;
import angel.tfg.pymeris.Model.Entities.Pedido;
import angel.tfg.pymeris.Model.Repository.Pedidos.PedidosRepository;
import angel.tfg.pymeris.Util.MostrarAlertas;
import javafx.application.Platform;

import java.util.List;

public class ShowOrderPresenter implements ShowOrderContract.Presenter {

    private final ShowOrderContract.View controller;
    private final PedidosRepository repository;
    private final Pedido p;

    public ShowOrderPresenter(ShowOrderContract.View controller, PedidosRepository repository, Pedido p) {
        this.controller = controller;
        this.repository = repository;
        this.p = p;
    }

    @Override
    public void cargarDetalles() {
        controller.setPedido(p);

        new Thread(() -> {
            try {
                List<DetallePedido> lista = repository.getInfoPedido(p.getId_Pedido());

                Platform.runLater(() -> controller.mostrarDetalles(lista));
            } catch (Exception e) {
                Platform.runLater(() -> System.out.println("Error al cargar los detalles del pedido."));
            }
        }).start();
    }

    @Override
    public void onCompletarClick() {
        controller.mostrarCarga(true);

        boolean confirm = MostrarAlertas.solictarConfirmacion("COMPLETANDO PEDIDO", "¿QUIERES COMPLETAR ESTE PEDIDO?");

        if (confirm) {
            new Thread(() -> {
                boolean success = repository.completarPedido(p.getId_Pedido());

                if (success) {
                    p.setEstado_Pedido("Completado");
                }

                Platform.runLater(() -> {
                    controller.mostrarCarga(false);
                    if (success) {
                        System.out.println("Pedido completado");
                        controller.setPedido(p);
                    }else  {
                        MostrarAlertas.mostrarInformacion("PEDIDO COMPLETADO", "Este pedido ya está completado.");
                    }
                });
            }).start();
        } else {
            controller.mostrarCarga(false);
        }
    }
}
