package angel.tfg.pymeris;

import angel.tfg.pymeris.Model.DAO.Usuarios.UsuarioDAOImpl;
import angel.tfg.pymeris.Util.Database;
import angel.tfg.pymeris.Util.Enums.Imagenes;
import angel.tfg.pymeris.Util.Navegation.Navegador;
import angel.tfg.pymeris.Util.Enums.Scenes;
import angel.tfg.pymeris.Util.Enums.Titulos;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class PymerisApp extends Application {

    Navegador navegador;

    @Override
    public void start(Stage stage) {

        // Conectamos a la base de datos y creamos las tablas si no existen
        Database.inicializarBaseDeDatos();

        // Creamos el administrador predeterminado
        UsuarioDAOImpl.crearAdminPredeterminado();

        navegador = new Navegador(stage);

        navegador.goTo(Scenes.Main);
        stage.setTitle(Titulos.Pymeris.getTitle());
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream(Imagenes.Logo128.getImg()))));
        stage.setMaximized(true);
        stage.show();
    }
}
