package angel.tfg.pymeris.Views.SelectOptionMain;

import angel.tfg.pymeris.Base.ViewController;
import angel.tfg.pymeris.Model.Repository.Clientes.ClientesRepositoryImpl;
import angel.tfg.pymeris.Model.Repository.Pedidos.PedidosRepositoryImpl;
import angel.tfg.pymeris.Model.Repository.Productos.ProductosRepositoryImpl;
import angel.tfg.pymeris.Model.Repository.Usuarios.UsuariosRepositoryImpl;
import angel.tfg.pymeris.Util.Enums.Scenes;
import angel.tfg.pymeris.Util.Enums.Titulos;
import angel.tfg.pymeris.Util.Session;
import angel.tfg.pymeris.Views.Inicio.InicioController;
import angel.tfg.pymeris.Views.ManageClients.ManageClientesController;
import angel.tfg.pymeris.Views.ManageClients.ManageClientsPresenter;
import angel.tfg.pymeris.Views.ManageOrders.ManageOrdersController;
import angel.tfg.pymeris.Views.ManageOrders.ManageOrdersPresenter;
import angel.tfg.pymeris.Views.ManageProducts.ManageProductsController;
import angel.tfg.pymeris.Views.ManageProducts.ManageProductsPresenter;
import angel.tfg.pymeris.Views.ManageUsers.ManageUsersController;
import angel.tfg.pymeris.Views.ManageUsers.ManageUsersPresenter;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class SelectOptionMainController extends ViewController{


    public HBox principal;
    @FXML
    private Label lblNameSession;

    @FXML
    private Button btnUsuarios;

    @FXML
    public void initialize() {
        String rol = Session.getRol();

        // Comprobamos el rol del usuario que ha iniciado sesión y mostramos el botón usuario si es administrador
        switch (rol){
            case "Administrador":
                btnUsuarios.setVisible(true);
                break;
            case "Empleado":
                btnUsuarios.setVisible(false);
                break;
        }

        //Mostramos quien ha iniciado sesión
        lblNameSession.setText(Session.getNombre());
        System.out.println("MainMenu");

    }

    public void onClientes() {
        System.out.println("Vamos a clientes");
        ManageClientesController controller = navegador.goTo(Scenes.Clientes);
        assert controller != null;
        controller.setTitle(Titulos.Clientes);
        controller.setPresenter(new ManageClientsPresenter(controller, new ClientesRepositoryImpl()));
    }

    public void onPedidos() {
        System.out.println("Vamos a pedidos");
        ManageOrdersController controller = navegador.goTo(Scenes.Pedidos);
        assert controller != null;
        controller.setTitle(Titulos.Pedidos);
        controller.setPresenter(new ManageOrdersPresenter(controller, new PedidosRepositoryImpl()));
    }

    public void onProductos() {
        System.out.println("Vamos a productos");
        ManageProductsController controller = navegador.goTo(Scenes.Productos);
        assert controller != null;
        controller.setTitle(Titulos.Productos);
        controller.setPresenter(new ManageProductsPresenter(controller, new ProductosRepositoryImpl()));
    }

    public void onUsuarios() {
        System.out.println("Vamos a usuarios");
        ManageUsersController controller = navegador.goTo(Scenes.Usuarios);
        assert controller != null;
        controller.setTitle(Titulos.Usuarios);
        controller.setPresenter(new ManageUsersPresenter(controller, new UsuariosRepositoryImpl()));
    }

    public void onLogout() {
        System.out.println("Vamos a logout");
        Session.limpiarSession();
        InicioController controller = navegador.goTo(Scenes.Main);
        assert controller != null;
        controller.setTitle(Titulos.Pymeris);
    }
}
