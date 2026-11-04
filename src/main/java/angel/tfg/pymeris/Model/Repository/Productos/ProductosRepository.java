package angel.tfg.pymeris.Model.Repository.Productos;

import angel.tfg.pymeris.Model.Entities.Producto;

import java.util.List;

public interface ProductosRepository {
    List<Producto> obtenerTodosLosProductos();
    List<Producto> obtenerTodosLosInactivos();
    boolean guardarProducto(Producto p);
    boolean actualizarProducto(Producto p);
    boolean downloadProducto(Integer id);
    boolean uploadProducto(Integer id);
    boolean modificarStock(int idProducto, int cantidad);
}
