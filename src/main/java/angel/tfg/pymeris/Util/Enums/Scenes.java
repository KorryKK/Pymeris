package angel.tfg.pymeris.Util.Enums;

public enum Scenes {
    Main("/Vistas/Login/VistaInicio.fxml"),
    Login("/Vistas/Login/Login.fxml"),
    EditPassword("/Vistas/Login/ChangePassword.fxml"),
    SelectOptionMain("/Vistas/SelectOptionMain.fxml"),
    Usuarios("/Vistas/Usuario/Usuarios.fxml"),
    NewUsuario("/Vistas/Usuario/NewUsuario.fxml"),
    ModifyUsuario("/Vistas/Usuario/ModifyUsuario.fxml"),
    Clientes("/Vistas/Clientes/Clientes.fxml"),
    NewCliente("/Vistas/Clientes/NewCliente.fxml"),
    ModifyClient("/Vistas/Clientes/ModifyCliente.fxml"),
    Productos("/Vistas/Productos/Productos.fxml"),
    ProductosInactivos("/Vistas/Productos/ProductosInactivos.fxml"),
    NewProducto("/Vistas/Productos/NewProducto.fxml"),
    ModifyProducto("/Vistas/Productos/ModifyProducto.fxml"),
    ModifyStockProductos("/Vistas/Productos/UpDownStockProductos.fxml"),
    Pedidos("/Vistas/Pedidos/Pedidos.fxml"),
    NewPedido("/Vistas/Pedidos/NewPedido.fxml"),
    SelectCliente("/Vistas/Pedidos/SelectCliente.fxml"),
    AddProducto("/Vistas/Pedidos/AddProductoPedido.fxml"),
    ShowPedido("/Vistas/Pedidos/ShowDetallePedido.fxml");

    private final String fxml;

    Scenes(String path) {
        fxml=path;
    }

    public String getFxml() {
        return fxml;
    }
}
