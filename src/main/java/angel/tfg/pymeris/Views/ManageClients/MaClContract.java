package angel.tfg.pymeris.Views.ManageClients;

import angel.tfg.pymeris.Model.Entities.Cliente;

import java.util.List;

public interface MaClContract {
    interface View{
        void mostrarClientes(List<Cliente> lista);
        void setPresenter(MaClContract.Presenter presenter);
        void mostrarCarga(boolean b);
    }
    interface Presenter {
        void cargarClientes();
        void onDeleteClientClick(String dni);
    }
}
