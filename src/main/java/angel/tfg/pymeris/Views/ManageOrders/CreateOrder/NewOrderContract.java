package angel.tfg.pymeris.Views.ManageOrders.CreateOrder;

import angel.tfg.pymeris.Model.Entities.Cliente;
import angel.tfg.pymeris.Model.Entities.DetallePedido;
import angel.tfg.pymeris.Model.Entities.Producto;
import angel.tfg.pymeris.Model.Repository.Pedidos.PedidosRepository;

import java.math.BigDecimal;
import java.util.List;

public interface NewOrderContract {
    interface View {
        void mostrarDetalles(List<DetallePedido> lista);
        void setLblInfoCl(String txt);
        void setPresenter(Presenter presenter);
        void mostrarCarga(boolean b);
        void goBack();
        void showAnadir(boolean b);
        void showEliminar(boolean b);
        void showConfirmar(boolean b);
    }
    interface Presenter {
        void anadirCliente(Cliente c);
        void anadirProducto(Producto p, int cantidad);
        void borrarProducto(DetallePedido p);
        void onCreateOrderClick();
        boolean onCancelOrderClick();
        void mostrarCargaView(boolean b);
        PedidosRepository getRepository();
        String getCantidadProductos();
        String getCantidadSubtotal();
        BigDecimal getCantidadImporteFINAL();
    }
}