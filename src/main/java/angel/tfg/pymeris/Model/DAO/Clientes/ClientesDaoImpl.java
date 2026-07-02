package angel.tfg.pymeris.Model.DAO.Clientes;

import angel.tfg.pymeris.Model.Entities.Cliente;
import angel.tfg.pymeris.Util.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientesDaoImpl {

    public static List<Cliente> obtenerTodos(){
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM Clientes";

        try (Connection conn = Database.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                clientes.add(mapearCliente(rs));
            }
        }catch (SQLException e){
            System.out.println("Error al obtener todos los clientes.");
        }
        return clientes;
    }

    /**
     * FUNCIÓN AUXILIAR: Transforma una fila de la BD en un Objeto Usuario
     */
    private static Cliente mapearCliente(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setNIF_DNI(rs.getString("NIF_DNI"));
        c.setNombre(rs.getString("Nombre"));
        c.setApellido(rs.getString("Apellido"));
        c.setCorreo(rs.getString("Correo"));
        c.setTelefono(rs.getString("Telefono"));
        c.setActivo(rs.getInt("Activo"));

        return c;
    }

    public static boolean guardarCliente(Cliente c){
        // language=SQLite
        String sql = "INSERT INTO Clientes(NIF_DNI,Nombre,Apellido,Correo,Telefono) VALUES (?,?,?,?,?)";

        try (Connection conn = Database.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, c.getNIF_DNI());
            pstmt.setString(2, c.getNombre());
            pstmt.setString(3, c.getApellido());
            pstmt.setString(4, c.getCorreo());
            pstmt.setString(5, c.getTelefono());

            return pstmt.executeUpdate() == 1;
        }catch (SQLException e) {
            System.out.println("Error al insertar cliente : " + e.getMessage());
            return false;
        }
    }

    public static boolean actualizarCliente(Cliente c, String dninifanterior){
        // language=SQLite
        String sql = "UPDATE Clientes SET NIF_DNI = ?, Nombre =?, Apellido = ?, Correo = ?, Telefono = ?, Activo = ? WHERE NIF_DNI = ?";

        try(Connection conn = Database.conectar();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, c.getNIF_DNI());
            pstmt.setString(2, c.getNombre());
            pstmt.setString(3, c.getApellido());
            pstmt.setString(4, c.getCorreo());
            pstmt.setString(5, c.getTelefono());
            pstmt.setInt(6, c.getActivo());
            pstmt.setString(7, dninifanterior);

            return pstmt.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println("Error al actualizar cliente : " + e.getMessage());
            return false;
        }
    }

    public static boolean eliminarCliente(String Dni)  {
        String sql = "DELETE FROM Clientes WHERE NIF_DNI = ?";

        try(Connection conn = Database.conectar();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, Dni);

            return pstmt.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println("Error al eliminar cliente : " + e.getMessage());
            return false;
        }
    }
}
