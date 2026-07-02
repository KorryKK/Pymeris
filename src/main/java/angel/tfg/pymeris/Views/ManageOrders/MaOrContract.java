package angel.tfg.pymeris.Views.ManageOrders;

import angel.tfg.pymeris.Model.Entities.Pedido;
import angel.tfg.pymeris.Model.Repository.Pedidos.PedidosRepository;

import java.util.List;

public interface MaOrContract {
    interface View {
        void mostrarPedidos(List<Pedido> pedidos);
        void setPresenter(Presenter presenter);
        void mostrarCarga(boolean b);
    }
    interface Presenter {
        void cargarPedidos();
        void onCompletarClick(Pedido p);
        void onDeleteClick(Pedido p);
        PedidosRepository getRepository();
    }
}
