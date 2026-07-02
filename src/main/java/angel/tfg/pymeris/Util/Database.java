package angel.tfg.pymeris.Util;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private static class Esquemas{
        // TABLA Usuarios
        //language=SQLite
        public static final String tUsuarios = "CREATE TABLE IF NOT EXISTS Usuarios("
                + "ID_Empleado INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"                                     // Id único que crece solo con cada registro
                + "Nombre VARCHAR NOT NULL,"                                                                    // Columna para el nombre, obligatorio
                + "Apellido VARCHAR NOT NULL,"                                                                  // Columna para el apellido, obligatorio
                + "Correo VARCHAR,"                                                                             // Correo electrónico con el que se va a acceder
                + "Telefono VARCHAR,"                                                                           // Número de teléfono, el cual puede o no haber
                + "Usuario VARCHAR NOT NULL UNIQUE,"                                                            // Usuario con el que accede el usuario
                + "Contrasena VARCHAR NOT NULL,"                                                                // Contraseña con la que accede el usuario
                + "Estado INTEGER NOT NULL DEFAULT 1,"                                                          // Si es 1, el usuario puede iniciar sesión
                + "Rol VARCHAR NOT NULL DEFAULT 'Empleado',"                                                    // Rol que puede tener un empleado
                + "Fecha_Registro DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,"                                 // Fecha y hora en la que se crea el usuario, se establece solo
                + "Ultima_Modificacion DATETIME NULL,"                                                          // Fecha con la última vez que se ha modificado un usuario
                + "Primer_Acceso INTEGER NOT NULL DEFAULT 0,"                                                   // Si es 0, el usuario tendrá que cambiar la contraseña al iniciar por primera vez
                + "CHECK (Correo IS NULL OR Correo LIKE '%_@_%._%' AND Correo NOT LIKE '%@%@%'),"               // Norma que regula el formato del correo
                + "CHECK (Telefono IS NULL OR Telefono GLOB '[67][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]'),"   // Norma que regula el formato del teléfono
                + "CHECK (Estado IN(0, 1)),"                                                                    // Norma que regula los únicos valores permitidos para el Estado
                + "CHECK (Rol IN('Empleado','Administrador')),"
                + "CHECK (Primer_Acceso IN(0,1))"
                + ");";

        // TABLA Clientes
        //language=SQLite
        public static final String tClientes = "CREATE TABLE IF NOT EXISTS Clientes("
                + "NIF_DNI VARCHAR NOT NULL UNIQUE PRIMARY KEY,"
                + "Nombre VARCHAR NOT NULL,"
                + "Apellido VARCHAR,"
                + "Correo VARCHAR,"
                + "Telefono VARCHAR,"
                + "Activo INTEGER NOT NULL DEFAULT 1,"
                + "CHECK (Correo IS NULL OR Correo LIKE '%_@_%._%' AND Correo NOT LIKE '%@%@%'),"
                + "CHECK (Telefono IS NULL OR Telefono GLOB '[67][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]'),"
                + "CHECK (Activo IN(0,1))"
                + ");";

        // TABLA Productos
        //language=SQLite
        public static final String tProductos = "CREATE TABLE IF NOT EXISTS Productos("
                + "ID_Producto INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + "Categoria VARCHAR NOT NULL,"
                + "Nombre VARCHAR NOT NULL,"
                + "Descripcion VARCHAR NOT NULL,"
                + "Precio REAL NOT NULL,"
                + "IVA INTEGER NOT NULL,"
                + "Stock_Actual INTEGER DEFAULT 0 NOT NULL,"
                + "Activo INTEGER NOT NULL DEFAULT 1,"
                + "CHECK (IVA BETWEEN 0 AND 100),"
                + "CHECK (Precio >=0),"
                + "CHECK (Stock_Actual >= 0),"
                + "CHECK (Activo IN(0,1))"
                + ");";

        // TABLA Pedidos
        //language=SQLite
        public static final String tPedidos = "CREATE TABLE IF NOT EXISTS Pedidos("
                + "ID_Pedido INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + "ID_Empleado INTEGER NOT NULL,"
                + "NIF_DNI_Cliente VARCHAR NOT NULL,"
                + "Cliente VARCHAR NOT NULL,"
                + "Fecha DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,"
                + "Total_Importe REAL NOT NULL,"
                + "Estado_Pedido VARCHAR NOT NULL DEFAULT 'Creado',"
                + "CONSTRAINT 'fk_usuario' FOREIGN KEY ('ID_Empleado') REFERENCES Usuarios('ID_Empleado') ON DELETE RESTRICT,"
                + "CONSTRAINT 'fk_cliente' FOREIGN KEY ('NIF_DNI_Cliente') REFERENCES Clientes('NIF_DNI') ON DELETE RESTRICT,"
                + "CHECK (Estado_Pedido IN ('Creado', 'Completado'))"
                + ");";

        // TABLA Detalles Pedidos
        //language=SQLite
        public static final String tDetallesPedidos =  "CREATE TABLE IF NOT EXISTS DetallePedidos("
                + "ID_Pedido INTEGER NOT NULL,"
                + "ID_Producto INTEGER NOT NULL,"
                + "Nombre_Producto VARCHAR NOT NULL,"
                + "Cantidad INTEGER NOT NULL,"
                + "Precio_Unitario REAL NOT NULL,"
                + "IVA INTEGER NOT NULL,"
                + "Subtotal REAL GENERATED ALWAYS AS (Cantidad * Precio_Unitario ) STORED,"
                + "Precio_Final REAL GENERATED ALWAYS AS (( Cantidad * Precio_Unitario ) * (1 + (CAST(IVA AS REAL)/100))) STORED,"
                + "PRIMARY KEY (ID_Pedido, ID_Producto),"
                + "CONSTRAINT 'fk_pedido' FOREIGN KEY ('ID_Pedido') REFERENCES Pedidos('ID_Pedido') ON DELETE CASCADE,"
                + "CONSTRAINT 'fk_producto' FOREIGN KEY ('ID_Producto') REFERENCES Productos('ID_Producto'),"
                + "CHECK (Cantidad >0)"
                + ");";


        // TRIGGER que establece la fecha modificación en usuarios siempre que se cambie algún dato de algún usuario
        //language=SQLite
        public static final String trigger_mod_usuario = "CREATE TRIGGER IF NOT EXISTS actualizar_fecha "
                + "AFTER UPDATE OF Nombre, Apellido, Correo, Telefono, Contrasena, Estado ON Usuarios " //Solo en estas columnas
                + "FOR EACH ROW "
                + "BEGIN "
                + "UPDATE Usuarios "
                + "SET Ultima_Modificacion = CURRENT_TIMESTAMP "
                + "WHERE ID_Empleado = OLD.ID_Empleado;"
                + "END;";
    }

    private final static String urlDB = "jdbc:sqlite:" +
            System.getProperty("user.home") + File.separator +
            "Pymeris" +   File.separator +
            "database_protecto.db";

    public static Connection conectar() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(urlDB);
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON;");
            }
        } catch (SQLException e) {
            System.out.println("Error al conectar a SQLite DB: " + e.getMessage());
        }
        return conn;
    }

    public static void inicializarBaseDeDatos() {
        java.io.File carpeta = new java.io.File(
                System.getProperty("user.home") + File.separator + "Pymeris"
        );
        if (!carpeta.exists()) {
            System.out.println("Vamos a crear la carpeta de la base de datos");
            boolean success = carpeta.mkdir();
            if  (!success) {
                System.out.println("No se pudo crear la carpeta de la base de datos");
            }
        }

        try (Connection conn = conectar();
             Statement stmt = conn.createStatement()) {

            // Ejecutar la creación de las tablas
            System.out.println("Tabla Usuarios.");
            stmt.execute(Esquemas.tUsuarios);
            System.out.println("Tabla Clientes.");
            stmt.execute(Esquemas.tClientes);
            System.out.println("Tabla Productos.");
            stmt.execute(Esquemas.tProductos);
            System.out.println("Tabla Pedidos.");
            stmt.execute(Esquemas.tPedidos);
            System.out.println("Tabla Detalles.");
            stmt.execute(Esquemas.tDetallesPedidos);

            //Ejecutar la creación del trigger
            stmt.execute(Esquemas.trigger_mod_usuario);

            System.out.println("✅ Base de datos lista en: " + urlDB);
        } catch (SQLException e) {
            System.out.println("Error al crear la tabla: " + e.getMessage());
        }
    }
}