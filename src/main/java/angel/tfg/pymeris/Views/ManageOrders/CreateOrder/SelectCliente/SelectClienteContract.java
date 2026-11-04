package angel.tfg.pymeris.Views.ManageOrders.CreateOrder.SelectCliente;

import angel.tfg.pymeris.Model.Entities.Cliente;

import java.util.List;

public interface SelectClienteContract {
    interface View {
        void mostrarClientes(List<Cliente> clientes);
        void setPresenter(Presenter presenter);
        void toClose();
    }
    interface Presenter {
        void cargarClientes();
        void onSeclectClick(Cliente c);
    }
}
