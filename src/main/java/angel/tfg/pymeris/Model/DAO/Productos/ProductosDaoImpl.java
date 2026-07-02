package angel.tfg.pymeris.Model.DAO.Productos;

import angel.tfg.pymeris.Model.Entities.Producto;
import angel.tfg.pymeris.Util.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductosDaoImpl {

    /**
     * Esta funcion devuelve los productos que están activos
     */
    public static List<Producto> obtenerTodos() {
        List<Producto> productos = new ArrayList<>();
        // language=SQLite
        String sql = "SELECT * FROM Productos WHERE Activo = 1";

        try (Connection conn = Database.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                productos.add(mapearProducto(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener todos los productos" + e.getMessage());
        }
        return productos;
    }

    /**
     * Esta funcion devuelve los productos que no están activos
     */
    public static List<Producto> obtenerTodosInactivos() {
        List<Producto> productos = new ArrayList<>();
        // language=SQLite
        String sql = "SELECT * FROM Productos WHERE Activo = 0";

        try (Connection conn = Database.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                productos.add(mapearProducto(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener todos los productos" + e.getMessage());
        }
        return productos;
    }

    /**
     * FUNCIÓN AUXILIAR: Transforma una fila de la BD en un Objeto Producto
     */
    private static Producto mapearProducto(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setId_Producto(rs.getInt("ID_Producto"));
        p.setNombre(rs.getString("Nombre"));
        p.setDescripcion(rs.getString("Descripcion"));
        p.setCategoria(rs.getString("Categoria"));
        p.setIVA(rs.getInt("IVA"));
        p.setPrecioUnitario(rs.getBigDecimal("Precio"));
        p.setStock_Actual(rs.getInt("Stock_Actual"));
        p.setActivo(rs.getInt("Activo"));
        return p;
    }

    public static boolean insertarProducto(Producto p) {
        //language=SQLite
        String sql = "INSERT INTO Productos(Categoria, Nombre, Descripcion, Precio, IVA) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Database.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, p.getCategoria());
            pstmt.setString(2, p.getNombre());
            pstmt.setString(3, p.getDescripcion());
            pstmt.setBigDecimal(4, p.getPrecioUnitario());
            pstmt.setInt(5, p.getIVA());

            System.out.println("Se va a guardar desde el dao...");
            return pstmt.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println("Error al insertar producto" + e.getMessage());
            return false;
        }
    }

    public static boolean actualizarProducto(Producto p) {
        // Usamos el ID_Producto para identificar qué fila hay que tocar
        //language=SQLite
        String sql = "UPDATE Productos SET Nombre = ?, Categoria = ?, Descripcion = ?," +
                "Precio = ?, IVA = ?, Stock_Actual = ? WHERE ID_Producto = ?";

        System.out.println("Se va a actualizar desde el dao...");
        try (Connection conn = Database.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Pasamos los valores del objeto "p" a los interrogantes
            pstmt.setString(1, p.getNombre());
            pstmt.setString(2, p.getCategoria());
            pstmt.setString(3, p.getDescripcion());
            pstmt.setBigDecimal(4, p.getPrecioUnitario());
            pstmt.setInt(5, p.getIVA());
            pstmt.setInt(6, p.getStock_Actual());

            // En el WHERE le decimos que ID tiene que actualizar
            pstmt.setInt(7, p.getId_Producto());

            // Ejecutamos y comprobamos si se ha actualizado 1 fila
            return pstmt.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println("Error al actualizar producto" + e.getMessage());
            return false;
        }
    }

    public static boolean bajaProducto(Integer id) {
        String sql = "UPDATE Productos SET Activo = 0 WHERE ID_Producto = ?";

        try (Connection conn = Database.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            System.out.println("Se va a dar de baja desde el dao...");
            return pstmt.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println("Error al dar de baja el producto" + e.getMessage());
            return false;
        }
    }

    public static boolean altaProducto(Integer id) {
        //language=SQLite
        String sql = "UPDATE Productos SET Activo = 1 WHERE ID_Producto = ?";

        try (Connection conn = Database.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            System.out.println("Se va a dar de alta desde el dao...");
            return pstmt.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println("Error al dar de baja el producto" + e.getMessage());
            return false;
        }
    }

    public static boolean actualizarStock(int idProducto, int cantidad) {
        // Si 'cantidad' es 5, suma 5. Si es -5, resta 5.
        // language=SQLite
        String sql = "UPDATE Productos SET Stock_Actual = Stock_Actual + ? WHERE ID_Producto = ?";

        try (Connection conn = Database.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // El primer '?' es la cantidad (ej: 10 o -10)
            pstmt.setInt(1, cantidad);
            // El segundo '?', identifica el producto
            pstmt.setInt(2, idProducto);

            // Retorna true si se ha actualizado correctamente una fila
            return pstmt.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println("Error al modificar el stock: " + e.getMessage());
            return false;
        }
    }
}