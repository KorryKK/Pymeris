package angel.tfg.pymeris.Model.Entities;

import java.math.BigDecimal;

public class Producto {
    private int id_Producto;
    private String categoria;
    private String nombre;
    private String descripcion;
    private int IVA;
    private BigDecimal precioUnitario;
    private int stock_Actual;
    private int activo;

    // --- GETTERS ---

    public int getId_Producto() {return id_Producto;}

    public String getCategoria() {return categoria;}

    public String getNombre() {return nombre;}

    public String getDescripcion() {return descripcion;}

    public int getIVA() {return IVA;}

    public BigDecimal getPrecioUnitario() {return precioUnitario;}

    public String getPrecioFormateado() {
        if (this.precioUnitario == null) return "0,00 €";
        return String.format("%.2f €", this.precioUnitario.doubleValue());
    }

    public int getStock_Actual() {return stock_Actual;}

    public int getActivo() {return activo;}

    //    --- SETTERS ---

    public void setId_Producto(int id_Producto) {this.id_Producto = id_Producto;}

    public void setCategoria(String categoria) {this.categoria = categoria;}

    public void setNombre(String nombre) {this.nombre = nombre;}

    public void setDescripcion(String descripcion) {this.descripcion = descripcion;}

    public void setIVA(int IVA) {this.IVA = IVA;}

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public void setStock_Actual(int stock_Actual) {this.stock_Actual = stock_Actual;}

    public void setActivo(int activo) {this.activo = activo;}
}
