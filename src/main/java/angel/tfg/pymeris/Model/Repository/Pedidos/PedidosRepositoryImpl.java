package angel.tfg.pymeris.Model.Repository.Pedidos;

import angel.tfg.pymeris.Model.DAO.Pedidos.PedidosDaoImpl;
import angel.tfg.pymeris.Model.Entities.Cliente;
import angel.tfg.pymeris.Model.Entities.DetallePedido;
import angel.tfg.pymeris.Model.Entities.Pedido;
import angel.tfg.pymeris.Model.Entities.Producto;

import java.util.List;

public class PedidosRepositoryImpl implements PedidosRepository {
    @Override
    public List<Pedido> getPedidos() {
        return PedidosDaoImpl.obtenerTodos();
    }

    @Override
    public List<DetallePedido> getInfoPedido(int idPedido) {
        return PedidosDaoImpl.obtenerDetallePedido(idPedido);
    }

    @Override
    public boolean insertarPedido(Pedido p, List<DetallePedido> detalles) {
        return PedidosDaoImpl.insertarPedido(p, detalles);
    }

    @Override
    public List<Cliente> listarClientes() {
        return PedidosDaoImpl.listadoClientes();
    }

    @Override
    public List<Producto> listarProductos() {
        return PedidosDaoImpl.obtenerProductosActivos();
    }

    @Override
    public boolean stockPedidoAjuste(int id, int cantidad) {
        return PedidosDaoImpl.reservarRecuperarStock(id, cantidad);
    }

    @Override
    public boolean completarPedido(int idPedido) {
        return PedidosDaoImpl.completarPedido(idPedido);
    }

    @Override
    public boolean eliminarPedido(int idPedido) {
        return PedidosDaoImpl.eliminarPedido(idPedido);
    }
}
