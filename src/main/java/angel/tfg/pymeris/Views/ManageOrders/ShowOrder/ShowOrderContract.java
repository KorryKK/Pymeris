package angel.tfg.pymeris.Views.ManageOrders.ShowOrder;

import angel.tfg.pymeris.Model.Entities.DetallePedido;
import angel.tfg.pymeris.Model.Entities.Pedido;

import java.util.List;

public interface ShowOrderContract {
    interface View {
        void mostrarDetalles(List<DetallePedido> lista);
        void mostrarCarga(boolean b);
        void setPedido(Pedido p);
        void setPresenter(Presenter presenter);
    }
    interface Presenter {
        void cargarDetalles();
        void onCompletarClick();
    }
}
