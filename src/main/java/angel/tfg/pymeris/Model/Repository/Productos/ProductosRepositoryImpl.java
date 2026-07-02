package angel.tfg.pymeris.Model.Repository.Productos;

import angel.tfg.pymeris.Model.DAO.Productos.ProductosDaoImpl;
import angel.tfg.pymeris.Model.Entities.Producto;

import java.util.List;

/**
 * The type Productos repository.
 */
public class ProductosRepositoryImpl implements ProductosRepository {

    @Override
    public List<Producto> obtenerTodosLosProductos() {
        // El repositorio pide al dao la lista cruda
        return ProductosDaoImpl.obtenerTodos();
    }

    @Override
    public List<Producto> obtenerTodosLosInactivos() {
        return ProductosDaoImpl.obtenerTodosInactivos();
    }

    @Override
    public boolean guardarProducto(Producto p) {
        System.out.println("Llamamos al dao para guardar el producto");
        return ProductosDaoImpl.insertarProducto(p);
    }

    @Override
    public boolean actualizarProducto(Producto p) {
        return ProductosDaoImpl.actualizarProducto(p);
    }

    @Override
    public boolean downloadProducto(Integer id) {
        return ProductosDaoImpl.bajaProducto(id);
    }

    @Override
    public boolean uploadProducto(Integer id) {
        return ProductosDaoImpl.altaProducto(id);
    }

    @Override
    public boolean modificarStock(int idProducto, int cantidad) {
        return ProductosDaoImpl.actualizarStock(idProducto, cantidad);
    }
}
