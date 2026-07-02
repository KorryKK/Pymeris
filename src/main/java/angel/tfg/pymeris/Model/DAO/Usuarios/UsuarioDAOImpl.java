package angel.tfg.pymeris.Model.DAO.Usuarios;

import angel.tfg.pymeris.Model.Entities.Usuario;
import angel.tfg.pymeris.Util.Database;
import angel.tfg.pymeris.Util.PasswordHasher;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAOImpl{

    // Formato por defecto de SQLite para CURRENT_TIMESTAMP
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static List<Usuario> obtenerTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        // language=SQLite
        String sql = "SELECT * FROM Usuarios";

        try (Connection conn = Database.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                usuarios.add(mapearUsuario(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener todos los usuarios" + e.getMessage());
        }
        return usuarios;
    }

    /**
     * FUNCIÓN AUXILIAR: Transforma una fila de la BD en un Objeto Usuario
     */
    private static Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setId_Empleado(rs.getInt("ID_Empleado"));
        u.setNombre(rs.getString("Nombre"));
        u.setApellido(rs.getString("Apellido"));
        u.setCorreo(rs.getString("Correo"));
        u.setTelefono(rs.getString("Telefono"));
        u.setUser(rs.getString("Usuario"));
        u.setEstado_Usuario(rs.getInt("Estado"));
        u.setRol(rs.getString("Rol"));
        u.setAcceso(rs.getInt("Primer_Acceso"));

        // Conversión de Fechas (SQLite devuelve String para DATETIME)
        String fRegistro = rs.getString("Fecha_Registro");
        if (fRegistro != null) {
            u.setFecha_registro(LocalDateTime.parse(fRegistro, formatter));
        }

        String fMod = rs.getString("Ultima_Modificacion");
        if (fMod != null) {
            u.setUltima_modificacion(LocalDateTime.parse(fMod, formatter));
        }

        return u;
    }

    public static boolean insertar(Usuario u){
        // language=SQLite
        String sql = "INSERT INTO Usuarios (Nombre, Apellido, Correo, Telefono, Usuario, Contrasena) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, u.getNombre());
            pstmt.setString(2, u.getApellido());

            // GESTIÓN DE CAMPOS OPCIONALES (NULLABLES)
            pstmt.setString(3, (u.getCorreo() != null && !u.getCorreo().trim().isEmpty()) ? u.getCorreo() : null);
            pstmt.setString(4, (u.getTelefono() != null && !u.getTelefono().isEmpty()) ? u.getTelefono() : null);

            // Generamos el usuario
            pstmt.setString(5, generarUserUnique(u.getNombre(), u.getApellido()));

            // HASHEAMOS la contraseña por defecto (ej.: "Nombre1234")
            String passPlana = u.getNombre() + "1234";
            String passHasheada = PasswordHasher.hashPassword(passPlana);
            pstmt.setString(6, passHasheada);

            return pstmt.executeUpdate() == 1;
        }catch (SQLException e){
            System.out.println("Error al insertar el usuario: " + e.getMessage());
            return false;
        }
    }

    /**
     * FUNCIÓN AUXILIAR: Genera un nombre de usuario de acceso único con el que acceder.
     * Utiliza las dos primeras letras del nombre y del apellido y 4 numeros generados al azar.
     * Si el nombre generado existe en la BD, usamos recursividad para generar uno nuevo.
     */
    private static String generarUserUnique(String nombre, String apellido) {
        // Obtenemos las 2 primeras letras, si alguno es más corto, deja el que se escribió
        String pNombre = (nombre.length() >= 2) ? nombre.trim().substring(0, 2) : nombre;
        String pApellido = (apellido.length() >= 2) ? apellido.trim().substring(0, 2) : apellido;

        // Generamos 4 números aleatorios (entre 1000 y 9999)
        int numeroAleatorio = (int) (Math.random() * 9000) + 1000;

        // Formamos la cadena (ejemplo: anpe1234)
        String usernameGenerado = (pNombre + pApellido + numeroAleatorio).toLowerCase();

        if (existeUsuario(usernameGenerado)) {
            // Si existe, usamos recursividad para generar otro distinto
            return generarUserUnique(nombre, apellido);
        }
        return usernameGenerado;
    }

    private static boolean existeUsuario(String nombre) {
        //laguage=SQLite
        String sql = "SELECT COUNT(*) FROM Usuarios WHERE Usuario = ?";

        try(Connection con = Database.conectar();
            PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, nombre);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;    // Si es > 0, es que ya existe un usuario
            }
        }catch (SQLException e) {
            System.out.println("Ya existe un usuario.\nSe genera otro nuevo.");
        }
        return false;
    }

    public static boolean actualizarUsuario(Usuario u) {
        // Usamos el ID_Empleado para identificar qué fila tocar
        // language=SQLite
        String sql = "UPDATE Usuarios SET Nombre = ?, Apellido = ?, Correo = ?, " +
                "Telefono = ?, Usuario = ?, Estado = ?, Rol = ? WHERE ID_Empleado = ?";

        System.out.println("Intentando actualizar ID: " + u.getId_Empleado());
        try (Connection conn = Database.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Pasamos los valores del objeto 'u' a los interrogantes
            pstmt.setString(1, u.getNombre());
            pstmt.setString(2, u.getApellido());
            pstmt.setString(3, u.getCorreo());
            pstmt.setString(4, u.getTelefono());
            pstmt.setString(5, u.getUser());
            pstmt.setInt(6, u.getEstado_Usuario());
            pstmt.setString(7, u.getRol());

            // El WHERE: le decimos qué ID actualizar
            pstmt.setInt(8, u.getId_Empleado());

            // Ejecutamos y comprobamos si se afectó alguna fila
            return pstmt.executeUpdate() == 1;

        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }

    public static boolean restartPassword(Usuario u){
        // Usamos el ID_Empleado para identificar qué fila tocar
        // language=SQLite
        String sql = "UPDATE Usuarios SET Contrasena = ?, Primer_Acceso = 0 WHERE ID_Empleado = ?";
        String newPass = "Hola1234";
        System.out.println("Intentando restaurar contraseña de  ID: " + u.getId_Empleado());

        try (Connection conn = Database.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, PasswordHasher.hashPassword(newPass));
            pstmt.setInt(2, u.getId_Empleado());
            return pstmt.executeUpdate() == 1;

        }catch (SQLException e){
            System.out.println("Error al restablecer la contraseña del usuario: " + e.getMessage());
            return false;
        }
    }

    public static void crearAdminPredeterminado() {
        // language=SQLite
        String checkSql = "SELECT COUNT(*) FROM Usuarios WHERE Rol = 'Administrador'";
        // language=SQLite
        String insertSql = "INSERT INTO Usuarios (Nombre, Apellido, Correo, Usuario, Contrasena, Rol, Estado, Primer_Acceso) "
                + "VALUES ('Admin', 'Sistema', 'admin@pymeris.com', 'admin', ?, 'Administrador', 1, 1)";

        try (Connection conn = Database.conectar();
             Statement stmt = conn.createStatement();
             PreparedStatement pstmt = conn.prepareStatement(insertSql);
             ResultSet rs = stmt.executeQuery(checkSql)) {

            if (rs.next() && rs.getInt(1) == 0) {
                // La tabla está vacía, insertamos el admin inicial
                String pass = "admin123";
                pstmt.setString(1, PasswordHasher.hashPassword(pass));
                pstmt.executeUpdate();
                System.out.println("Base de datos vacía. Se ha creado el usuario 'admin' con contraseña 'admin123'.");
            } else {
                System.out.println("Ya existe un usuario administrador.\nNo hace falta generar ninguno nuevo.");
            }
        } catch (SQLException e) {
            System.out.println("Error al crear el administrador predeterminado: " + e.getMessage());
        }
    }
}
