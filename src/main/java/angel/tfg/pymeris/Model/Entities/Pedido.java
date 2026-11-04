package angel.tfg.pymeris.Model.Entities;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

public class Pedido {
    private int id_Pedido;
    private int id_Empleado;
    private String NIF_DNI_Cliente;
    private String cliente;
    private LocalDateTime fecha_Pedido;
    private String fecha_Pedido_formatted;
    private BigDecimal importe_Total;
    private String estado_Pedido;

//    --- GETTERS
    public int getId_Pedido() {return id_Pedido;}

    public int getId_Empleado() {return id_Empleado;}

    public String getNIF_DNI_Cliente() {return NIF_DNI_Cliente;}

    public String getCliente() {return cliente;}

    public LocalDateTime getFecha_Pedido() {return fecha_Pedido;}

    public String getFecha_Pedido_formatted() {return fecha_Pedido_formatted;}

    public BigDecimal getImporte_Total() {return importe_Total;}

    public String getEstado_Pedido() {return estado_Pedido;}

//    --- SETTERS
    public void setId_Pedido(int id_Pedido) {this.id_Pedido = id_Pedido;}

    public void setId_Empleado(int id_Empleado) {this.id_Empleado = id_Empleado;}

    public void setNIF_DNI_Cliente(String NIF_DNI_Cliente) {this.NIF_DNI_Cliente = NIF_DNI_Cliente;}

    public void setCliente(String cliente) {this.cliente = cliente;}

    public void setFecha_Pedido(LocalDateTime fecha_Pedido) {
        this.fecha_Pedido = fecha_Pedido;
        this.fecha_Pedido_formatted = fecha_Pedido.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public void setImporte_Total(BigDecimal importe_Total) {this.importe_Total = importe_Total;}

    public void setEstado_Pedido(String estado_Pedido) {this.estado_Pedido = estado_Pedido;}
}
