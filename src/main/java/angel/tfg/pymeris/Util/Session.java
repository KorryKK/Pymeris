package angel.tfg.pymeris.Util;

public class Session {

    private static int idEmpleado;
    private static String nombre;
    private static String rol;

    // Al hacer login existoso, llamamos a esta función para "guardar" quien ha iniciado sesión
    public static void configurarSession(int id, String nombre, String rol) {
        Session.idEmpleado = id;
        Session.nombre = nombre;
        Session.rol = rol;
    }

    // Cuando hacemos logout, limpiamos los datos de quien ha iniciado sesión
    public static void limpiarSession() {
        Session.idEmpleado = 0;
        Session.nombre = "";
        Session.rol = "";
    }

    // Getters para recuperar los datos y plasmarlos
    public static int getIdEmpleado() {
        return Session.idEmpleado;
    }

    public static String getNombre() {
        return Session.nombre;
    }

    public static String getRol() {
        return Session.rol;
    }
}
