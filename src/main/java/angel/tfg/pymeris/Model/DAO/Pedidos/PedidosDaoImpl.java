package angel.tfg.pymeris.Model.DAO.Pedidos;

import angel.tfg.pymeris.Model.Entities.Cliente;
import angel.tfg.pymeris.Model.Entities.DetallePedido;
import angel.tfg.pymeris.Model.Entities.Pedido;
import angel.tfg.pymeris.Model.Entities.Producto;
import angel.tfg.pymeris.Util.Database;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PedidosDaoImpl {

    // Formato por defecto de SQLite para las fechas (CURRENT_TIMESTAMP)
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Consulta a la base de datos y recorre las filas del resultado
     *
     * @return List<Pedido> Devuelve la lista cruda de pedidos
     */
    public static List<Pedido> obtenerTodos() {
        List<Pedido> pedidos = new ArrayList<>();
        // language=SQLite
        String sql = "SELECT * FROM Pedidos";

        try (Connection conn = Database.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Recorremos cada fila devuelta de la consulta y la convertimos en objeto
            while (rs.next()) {
                pedidos.add(mapearPedido(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener todos los pedidos: " + e.getMessage());
        }
        return pedidos;
    }

    /**
     * FUNCIÓN AUXILIAR: Transforma una fila de la BD en un Objeto Pedido.
     *
     * @param rs Es la fila del resultado generado por la consulta
     * @return Devuelve el objeto pedido con los datos de la fila
     */
    private static Pedido mapearPedido(ResultSet rs) throws SQLException {
        Pedido p = new Pedido();

        p.setId_Pedido(rs.getInt("ID_Pedido"));
        p.setId_Empleado(rs.getInt("ID_Empleado"));
        p.setNIF_DNI_Cliente(rs.getString("NIF_DNI_Cliente"));
        p.setCliente(rs.getString("Cliente"));
        p.setImporte_Total(rs.getBigDecimal("Total_Importe"));
        p.setEstado_Pedido(rs.getString("Estado_Pedido"));

        // Conversión de Fecha (SQLite devuelve un String para el tipo DATETIME)
        String fechaStr = rs.getString("Fecha");
        if (fechaStr != null) {
            p.setFecha_Pedido(LocalDateTime.parse(fechaStr, formatter));
        }

        return p;
    }

    public static List<DetallePedido> obtenerDetallePedido(int idPedido) {
        List<DetallePedido> detallePedido = new ArrayList<>();
        //language=SQLite
        String sql = "SELECT * FROM DetallePedidos WHERE ID_Pedido = ?";

        try (Connection conn = Database.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPedido);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                detallePedido.add(mapearDetalle(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener detalle pedido: " + e.getMessage());
        }
        return detallePedido;
    }

    /**
     * FUNCIÓN AUXILIAR: Transforma una fila de la BD en un Objeto Pedido.
     *
     * @param rs Es la fila del resultado generado por la consulta
     * @return Devuelve el objeto detallepedido con los datos de la fila
     */
    private static DetallePedido mapearDetalle(ResultSet rs) throws SQLException {
        DetallePedido d = new DetallePedido();
        d.setId_Pedido(rs.getInt("ID_Pedido"));
        d.setId_Producto(rs.getInt("ID_Producto"));
        d.setNombre_Producto(rs.getString("Nombre_Producto"));
        d.setCantidad(rs.getInt("Cantidad"));
        d.setPrecio_Unitario(rs.getBigDecimal("Precio_Unitario"));
        d.setSubtotal(rs.getBigDecimal("Subtotal"));
        d.setIVA(rs.getInt("IVA"));
        d.setImporte_Producto(rs.getBigDecimal("Precio_Final"));

        return d;
    }

    /**
     * Insertar un Pedido Completo (Pedido + DetallePedido)
     *
     * @param p Objeto Pedido con los datos generales del pedido
     * @param detalles Lista de objetos DetallePedidos con los productos del pedido
     * @return boolean
     *      - true si se guardó todo correctamente
     *      - false si hubo algún error
     */
    public static boolean insertarPedido(Pedido p, List<DetallePedido> detalles) {
        //language=SQLite
        String sqlPedido = "INSERT INTO Pedidos (ID_Empleado, NIF_DNI_Cliente, Cliente, Total_Importe) VALUES (?,?,?,?) RETURNING ID_Pedido;";
        //language=SQLite
        String sqlDetalle = "INSERT INTO DetallePedidos (ID_Pedido, ID_Producto,Nombre_Producto, Cantidad, Precio_Unitario, IVA) VALUES (?,?,?,?,?,?)";

        Connection conn = null;
        try {
            conn = Database.conectar();
            // DESACTIVAMOS el AutoCommit.
            // Queremos controlar nosotros cuándo se guardan los cambios permanentemente.
            conn.setAutoCommit(false);

            int idPedidoGenerado = -1;
            // INSERTAMOS LA CABECERA DEL PEDIDO
            try(PreparedStatement pstmtP = conn.prepareStatement(sqlPedido)) {
                pstmtP.setInt(1, p.getId_Empleado());
                pstmtP.setString(2, p.getNIF_DNI_Cliente());
                pstmtP.setString(3, p.getCliente());
                pstmtP.setBigDecimal(4, p.getImporte_Total());

                try (ResultSet rs = pstmtP.executeQuery()) {
                    if (rs.next()) {
                        idPedidoGenerado = rs.getInt("ID_Pedido");
                    }
                }
            }

            // Si no obtuvimos el ID, algo salió mal en la inserción
            // Deshacemos lo que se haya podido generar y salimos
            if (idPedidoGenerado == -1) {
                conn.rollback();
                return false;
            }

            // INSERTAR LAS LÍNEAS DE DETALLE USANDO EL ID OBTENIDO
            try (PreparedStatement pstmtD = conn.prepareStatement(sqlDetalle)) {
                for (DetallePedido linea : detalles) {
                    // Vinculamos cada línea al ID del pedido
                    pstmtD.setInt(1, idPedidoGenerado);
                    pstmtD.setInt(2, linea.getId_Producto());
                    pstmtD.setString(3, linea.getNombre_Producto());
                    pstmtD.setInt(4, linea.getCantidad());
                    pstmtD.setBigDecimal(5, linea.getPrecio_Unitario());
                    pstmtD.setInt(6, linea.getIVA());

                    // Añadimos al lote para hacer la ejecución masiva del pedido
                    pstmtD.addBatch();
                }
                // Ejecutamos todos los detalles de una sola vez (la ejecución masiva nombrada antes)
                pstmtD.executeBatch();
            }

            // FINALIZAMOS LA TRANSACCIÓN
            conn.commit();
            System.out.println("✅ Pedido " + idPedidoGenerado + " guardado con éxito.");
            return true;

        } catch (SQLException e) {
            // Si algo falla, hacemos ROLLBACK
            // Deshace cualquier cambio hecho en esta conexión para no dejar datos
            try {
                System.out.println("Error en la transacción.\nDeshaciendo cambios...");
                conn.rollback();
            } catch (SQLException ex) {
                System.out.println("Error al hacer rollback: " + ex.getMessage());
            }
            System.out.println("Error al insertar el pedido completo: " + e.getMessage());
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                }catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
    }

    /**
     * Función para obtener todos los clientes activos de la base de datos
     *
     * @return List<Cliente> Listado de clientes activos
     */
    public static List<Cliente> listadoClientes() {
        List<Cliente> clientes = new ArrayList<>();
        //language=SQLite
        String sqlCliente = "SELECT NIF_DNI, Nombre, Apellido FROM Clientes WHERE Activo = 1;";

        try (Connection conn = Database.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlCliente)) {
            // Recorremos cada fila devuelta por SQLite y la convertimos en objeto
            while (rs.next()) {
                System.out.println("Cargando cliente");
                clientes.add(mapearCliente(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener listado de clientes: " + e.getMessage());
        }

        return clientes;
    }

    /**
     * Función auxiliar para mapear el resultado de la consulta
     *
     * @param rs Línea de la consulta
     * @return Devuelve un objeto cliente con los datos de la fila de la consulta
     */
    private static Cliente mapearCliente(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setNIF_DNI(rs.getString("NIF_DNI"));
        c.setNombre(rs.getString("Nombre"));
        c.setApellido(rs.getString("Apellido"));
        return c;
    }

    /**
     * Esta funcion devuelve los productos que están activos
     */
    public static List<Producto> obtenerProductosActivos() {
        List<Producto> productos = new ArrayList<>();
        // language=SQLite
        String sql = "SELECT * FROM Productos WHERE Stock_Actual > 0 AND Activo = 1";

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

    /**
     * Actualizar el estado a 'Completado'.
     * LA RESTRICCIÓN: Solo se actualiza si el estado actual es 'Creado'.
     * Es imposible hacer el proceso inverso porque la consulta fallará silenciosamente si ya está completado.
     *
     * @param idPedido the id pedido
     * @return the boolean
     *      - true si se modifió correctamente
     *      - false si ya estaba completado
     */
    public static boolean completarPedido(int idPedido) {
        // language=SQLite
        String sql = "UPDATE Pedidos SET Estado_Pedido = 'Completado' " +
                "WHERE ID_Pedido = ? AND Estado_Pedido = 'Creado'";

        try (Connection conn = Database.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idPedido);

            // Devuelve true solo si cambió el estado exitosamente
            return pstmt.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println("Error al completar el pedido: " + e.getMessage());
            return false;
        }
    }

    /**
     * Eliminar un pedido 'Creado'.
     * LA RESTRUICCIÓN: Solo se borra si el estado actual es 'Creado'.
     * Es imposible hacer el proceso inverso porque la consulta fallará.
     *
     * @param idPedido El ID del pedido que se va a eliminar
     * @return the boolean
     *      - true si se eliminó correctamente
     *      - false si estaba completado o no existe
     */
    public static boolean eliminarPedido(int idPedido) {
        //language=SQLite
        String sqlPedido = " DELETE FROM Pedidos WHERE ID_Pedido = ? AND Estado_Pedido = 'Creado'";
        //language=SQLite
        String sqlDetalle = "DELETE FROM DetallePedidos WHERE ID_Pedido = ?" ;

        // Utilizamos una transacción como al crear el pedido
        Connection conn = null;

        try {
            conn = Database.conectar();
            conn.setAutoCommit(false);

            // Borramos primero los detalles (hijos) por si quedaran huerfanos borrando la cabezera primero
            try (PreparedStatement pstmtD = conn.prepareStatement(sqlDetalle)) {
                pstmtD.setInt(1, idPedido);
                pstmtD.executeUpdate();
            }

            // Borramos la cabecera (padre) con la condición del estado
            // Si un pedido está completado no se puede borrar
            try (PreparedStatement pstmtP = conn.prepareStatement(sqlPedido)) {
                pstmtP.setInt(1,idPedido);
                int fillasBorradas = pstmtP.executeUpdate();

                // Si el número de filas borradas es 0, significa que el pedido no estaba en 'Creado'
                // o no existía. En este caso deshacemos el borrado de los detalles
                if (fillasBorradas == 0) {
                    conn.rollback();
                    System.out.println("No se pudo borrar;\nEl pedido no existe o está completado");
                    return false;
                }
            }

            conn.commit(); // Si todo va bien, guardamos
            return true;

        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
            System.out.println("Error al eliminar el pedido: " + e.getMessage());
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                }catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
    }

    /**
     * Método para restar el stock cuando se añade un producto al pedido, o sumarlo cuando se elimnina o cancela
     *
     * @param idProducto Id del producto a ajustar stock
     * @param cantidad Cantidad a justar el stock
     * @return boolean
     *      -true si ejecuta bien
     *      -false si no ejecuta bien
     */
    public static boolean reservarRecuperarStock(int idProducto, int cantidad) {
        //language=SQLite
        String sqlStock = "UPDATE Productos SET Stock_Actual = Stock_Actual + ? WHERE ID_Producto = ?";

        try (Connection conn = Database.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sqlStock)) {

            pstmt.setInt(1, cantidad);
            pstmt.setInt(2, idProducto);

            return pstmt.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println("Error al actualizar de producto " + e.getMessage());
        }
        return false;
    }
}
