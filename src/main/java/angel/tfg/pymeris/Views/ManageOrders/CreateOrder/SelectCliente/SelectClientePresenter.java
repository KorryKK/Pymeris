package angel.tfg.pymeris.Views.ManageOrders.CreateOrder.SelectCliente;

import angel.tfg.pymeris.Model.Entities.Cliente;
import angel.tfg.pymeris.Model.Repository.Pedidos.PedidosRepository;
import angel.tfg.pymeris.Views.ManageOrders.CreateOrder.NewOrderContract;
import javafx.application.Platform;

import java.util.List;

public class SelectClientePresenter implements SelectClienteContract.Presenter {

    private final SelectClienteContract.View controller;
    private final PedidosRepository repository;
    private final NewOrderContract.Presenter newPresenter;

    public SelectClientePresenter(SelectClienteContract.View controller, PedidosRepository repository, NewOrderContract.Presenter newPresenter) {
        this.controller = controller;
        this.repository = repository;
        this.newPresenter = newPresenter;
    }

    @Override
    public void cargarClientes() {
        new Thread(() -> {
            try {
                List<Cliente> clientes = repository.listarClientes();

                Platform.runLater(() -> controller.mostrarClientes(clientes));
            } catch (Exception e) {
                System.out.println("Error al cargar los clientes: " + e.getMessage());
            }
        }).start();
    }

    @Override
    public void onSeclectClick(Cliente c) {
        newPresenter.anadirCliente(c);
        controller.toClose();
    }
}
