module angel.tfg.pymeris {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;
    requires jbcrypt;
    requires java.desktop;

    // Exports
    exports angel.tfg.pymeris;
    exports angel.tfg.pymeris.Base;
    exports angel.tfg.pymeris.Model.Entities;
    exports angel.tfg.pymeris.Model.Repository.Authentication;
    exports angel.tfg.pymeris.Model.Repository.Usuarios;
    exports angel.tfg.pymeris.Model.Repository.Clientes;
    exports angel.tfg.pymeris.Model.Repository.Productos;
    exports angel.tfg.pymeris.Model.Repository.Pedidos;
    exports angel.tfg.pymeris.Util;
    exports angel.tfg.pymeris.Util.Navegation;
    exports angel.tfg.pymeris.Util.Enums;
    exports angel.tfg.pymeris.Views.Inicio;
    exports angel.tfg.pymeris.Views.Login;
    exports angel.tfg.pymeris.Views.ChangePassword;
    exports angel.tfg.pymeris.Views.ManageUsers;
    exports angel.tfg.pymeris.Views.ManageClients;
    exports angel.tfg.pymeris.Views.ManageProducts;
    exports angel.tfg.pymeris.Views.ManageProducts.UpDownStockProduct;
    exports angel.tfg.pymeris.Views.ManageOrders;
    exports angel.tfg.pymeris.Views.ManageOrders.CreateOrder;
    exports angel.tfg.pymeris.Views.ManageOrders.CreateOrder.SelectCliente;
    exports angel.tfg.pymeris.Views.ManageOrders.CreateOrder.AddProduct;
    exports angel.tfg.pymeris.Views.ManageOrders.ShowOrder;

    // Opens
    opens angel.tfg.pymeris to javafx.fxml;
    opens angel.tfg.pymeris.Model.DAO.Login to javafx.fxml;
    opens angel.tfg.pymeris.Views.Login to javafx.fxml;
    opens angel.tfg.pymeris.Views.ChangePassword to javafx.fxml;
    opens angel.tfg.pymeris.Views.Inicio to javafx.fxml;
    opens angel.tfg.pymeris.Views.SelectOptionMain to javafx.fxml;
    opens angel.tfg.pymeris.Views.ManageUsers to javafx.fxml;
    opens angel.tfg.pymeris.Views.ManageUsers.CreateUser to javafx.fxml;
    opens angel.tfg.pymeris.Views.ManageUsers.ModifyUser to javafx.fxml;
    opens angel.tfg.pymeris.Views.ManageClients to javafx.fxml;
    opens angel.tfg.pymeris.Views.ManageClients.CreateClient to javafx.fxml;
    opens angel.tfg.pymeris.Views.ManageClients.ModifyClient to javafx.fxml;
    opens angel.tfg.pymeris.Views.ManageProducts to javafx.fxml;
    opens angel.tfg.pymeris.Views.ManageProducts.CreateProduct to javafx.fxml;
    opens angel.tfg.pymeris.Views.ManageProducts.ModifyProduct to javafx.fxml;
    opens angel.tfg.pymeris.Views.ManageProducts.ManageProductsInactivo to javafx.fxml;
    opens angel.tfg.pymeris.Views.ManageProducts.UpDownStockProduct to javafx.fxml;
    opens angel.tfg.pymeris.Views.ManageOrders to javafx.fxml;
    opens angel.tfg.pymeris.Views.ManageOrders.CreateOrder to javafx.fxml;
    opens angel.tfg.pymeris.Views.ManageOrders.CreateOrder.SelectCliente to javafx.fxml;
    opens angel.tfg.pymeris.Views.ManageOrders.CreateOrder.AddProduct to javafx.fxml;
    opens angel.tfg.pymeris.Views.ManageOrders.ShowOrder to javafx.fxml;
}