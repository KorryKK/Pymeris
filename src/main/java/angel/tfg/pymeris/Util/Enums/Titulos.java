package angel.tfg.pymeris.Util.Enums;

public enum Titulos {
    Pymeris("PYMERIS"),
    Login("PYMERIS - LOGIN"),
    SelectOptionMain("PYMERIS - MAIN"),
    Clientes("PYMERIS - Clientes"),
    Pedidos("PYMERIS - Pedidos"),
    NewOrder("PYMERIS - Creando pedido nuevo"),
    Productos("PYMERIS - Productos"),
    ProductosInactivos("PYMERIS - Productos inactivos"),
    Usuarios("PYMERIS - Usuarios");

    private final String title;

    Titulos(String title){
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
