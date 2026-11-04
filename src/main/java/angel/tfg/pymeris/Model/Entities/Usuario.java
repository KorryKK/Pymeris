package angel.tfg.pymeris.Model.Entities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Usuario {
    private int id_Empleado;
    private String nombre;
    private String apellido;
    private String correo;
    private String telefono;
    private String user;
    private String contrasena; // Se usa "n" en vez de "ñ" por compatibilidades
    private int estado_Usuario;
    private String rol;
    private LocalDateTime fecha_registro;
    private String fecha_registro_formatted;
    private LocalDateTime ultima_modificacion;
    private String ultima_modificacion_formatted;
    private int acceso;

//    --- CONSTRUCTORES ---
//    Se irán creando en función de necesidad

//    --- GETTERS ---

    public int getId_Empleado() {return id_Empleado;}

    public String getNombre() {return nombre;}

    public String getApellido() {return apellido;}

    public String getCorreo() {return correo;}

    public String getTelefono() {return telefono;}

    public String getUser() {return user;}

    public String getContrasena() {return contrasena;}

    public int getEstado_Usuario() {return estado_Usuario;}

    public String getRol() {return rol;}

    public LocalDateTime getFecha_registro() {return fecha_registro;}

    public String getFecha_registro_formatted() {return fecha_registro_formatted;}

    public String getUltima_modificacion_formatted() {return ultima_modificacion_formatted;}

    public LocalDateTime getUltima_modificacion() {return ultima_modificacion;}

    public int getAcceso() {return acceso;}

//    --- SETTERS ---

    public void setId_Empleado(int id_Empleado) {this.id_Empleado = id_Empleado;}

    public void setNombre(String nombre) {this.nombre = nombre;}

    public void setApellido(String apellido) {this.apellido = apellido;}

    public void setCorreo(String correo) {this.correo = correo;}

    public void setTelefono(String telefono) {this.telefono = telefono;}

    public void setUser(String user) {this.user = user;}

    public void setContrasena(String contrasena) {this.contrasena = contrasena;}

    public void setEstado_Usuario(int estado_Usuario) {this.estado_Usuario = estado_Usuario;}

    public void setRol(String rol) {this.rol = rol;}

    public void setFecha_registro(LocalDateTime fecha_registro) {
        this.fecha_registro = fecha_registro;
        this.fecha_registro_formatted = fecha_registro.format(DateTimeFormatter.ofPattern("dd/MM/yyyy                HH:mm"));
    }

    public void setUltima_modificacion(LocalDateTime ultima_modificacion) {
        this.ultima_modificacion = ultima_modificacion;
        this.ultima_modificacion_formatted = ultima_modificacion.format(DateTimeFormatter.ofPattern("dd/MM/yyyy                HH:mm"));
    }

    public void setAcceso(int acceso) {this.acceso = acceso;}
}