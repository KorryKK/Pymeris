package angel.tfg.pymeris.Model.Entities;

import java.math.BigDecimal;

public class DetallePedido {
    private int id_Pedido;
    private int id_Producto;
    private String nombre_Producto;
    private int cantidad;
    private BigDecimal precio_Unitario;
    private BigDecimal subtotal;
    private int IVA;
    private BigDecimal importe_Producto;

    // Constructor vacío
    public DetallePedido() {}

//    --- GETTERS ---
    public int getId_Pedido() {return id_Pedido;}

    public int getId_Producto() {return id_Producto;}

    public String getNombre_Producto() {return nombre_Producto;}

    public int getCantidad() {return cantidad;}

    public BigDecimal getPrecio_Unitario() {return precio_Unitario;}

    public BigDecimal getSubtotal() {return subtotal;}

    public int getIVA() {return IVA;}

    public BigDecimal getImporte_Producto() {return importe_Producto;}

//    --- SETTERS ---
    public void setId_Pedido(int id_Pedido) {this.id_Pedido = id_Pedido;}

    public void setId_Producto(int id_Producto) {this.id_Producto = id_Producto;}

    public void setNombre_Producto(String nombre_Producto) {this.nombre_Producto = nombre_Producto;}

    public void setCantidad(int cantidad) {this.cantidad = cantidad;}

    public void setPrecio_Unitario(BigDecimal precio_Unitario) {this.precio_Unitario = precio_Unitario;}

    public void setSubtotal(BigDecimal subtotal) {this.subtotal = subtotal;}

    public void setIVA(int IVA) {this.IVA = IVA;}

    public void setImporte_Producto(BigDecimal importe_Producto) {this.importe_Producto = importe_Producto;}
}
