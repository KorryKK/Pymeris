package angel.tfg.pymeris.Model.Repository.Pedidos;

import angel.tfg.pymeris.Model.Entities.Cliente;
import angel.tfg.pymeris.Model.Entities.DetallePedido;
import angel.tfg.pymeris.Model.Entities.Pedido;
import angel.tfg.pymeris.Model.Entities.Producto;

import java.util.List;

public interface PedidosRepository {
    List<Pedido> getPedidos();
    List<DetallePedido> getInfoPedido(int idPedido);
    boolean insertarPedido(Pedido p, List<DetallePedido> detalles);
    List<Cliente> listarClientes();
    List<Producto> listarProductos();
    boolean stockPedidoAjuste(int id, int cantidad);
    boolean completarPedido(int  idPedido);
    boolean eliminarPedido(int idPedido);
}
