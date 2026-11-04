package angel.tfg.pymeris.Model.Entities;

public class Cliente {
    private String NIF_DNI;
    private String nombre;
    private String apellido;
    private String correo;
    private String telefono;
    private int activo;

//    --- GETTERS ---
    public String getNIF_DNI() {return NIF_DNI;}

    public String getNombre() {return nombre;}

    public String getApellido() {return apellido;}

    public String getCorreo() {return correo;}

    public String getTelefono() {return telefono;}

    public int getActivo() {return activo;}

//    --- SETTERS ---
    public void setNIF_DNI(String NIF_DNI) {this.NIF_DNI = NIF_DNI;}

    public void setNombre(String nombre) {this.nombre = nombre;}

    public void setApellido(String apellido) {this.apellido = apellido;}

    public void setCorreo(String correo) {this.correo = correo;}

    public void setTelefono(String telefono) {this.telefono = telefono;}

    public void setActivo(int activo) {this.activo = activo;}
}
