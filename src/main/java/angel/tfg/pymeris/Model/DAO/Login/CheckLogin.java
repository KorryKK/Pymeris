package angel.tfg.pymeris.Model.DAO.Login;

import angel.tfg.pymeris.Model.Entities.Usuario;
import angel.tfg.pymeris.Util.Database;
import angel.tfg.pymeris.Util.PasswordHasher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CheckLogin {

    public static Usuario checkUserPass(String username, String password){
        // Consulta SQL
        String sql = "SELECT * FROM Usuarios WHERE Usuario = ?";

        try(Connection con = Database.conectar();
            PreparedStatement pstmt = con.prepareStatement(sql)){

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Obtenemos el hash almacenado
                String storedHash = rs.getString("Contrasena");

                if (PasswordHasher.checkPassword(password, storedHash)) {

                    // Si existe, creamos el objeto con sus datos
                    Usuario u = new Usuario();
                    u.setId_Empleado(rs.getInt("id_Empleado"));
                    u.setNombre(rs.getString("Nombre"));
                    u.setUser(rs.getString("Usuario"));
                    u.setEstado_Usuario(rs.getInt("Estado"));
                    u.setRol(rs.getString("Rol"));
                    u.setAcceso(rs.getInt("Primer_Acceso"));
                    return u;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en getUsuarioLogin: " + e.getMessage());
        }
        return null;    // Si no existe el usuario o la contraseña es incorrecta
    }

    public static boolean changePassword(String username, String newPassword){
        String sql = "UPDATE Usuarios SET Contrasena = ?, Primer_Acceso = 1 WHERE Usuario = ?";

        try(Connection con = Database.conectar();
            PreparedStatement pstmt = con.prepareStatement(sql)){

            // Hasheamos la nueva contraseña elegida por el usuario
            String hashedPass = PasswordHasher.hashPassword(newPassword);

            pstmt.setString(1, hashedPass);
            pstmt.setString(2, username);

            return pstmt.executeUpdate() == 1;
        }catch (SQLException e) {
            System.err.println("Error en changePassword: " + e.getMessage());
        }
        return false;
    }
}