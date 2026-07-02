package angel.tfg.pymeris.Util.Navegation;

import angel.tfg.pymeris.Util.Enums.Imagenes;
import angel.tfg.pymeris.Util.Enums.Scenes;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public record Navegador(Stage stage) {

    /**
     * Constructor del navegador
     */
    public Navegador {
    }

    /**
     * Método para cambiar de pantalla.
     *
     * @param fxmlArchivo enumerado de destino (lleva asociada la ruta al FXML)
     * @return - Controlador cargado
     * - null si hubo algún error
     */
    public <T> T goTo(Scenes fxmlArchivo) {

        // Obtenemos el recurso para comprobar que existe
        var recurso = Navegador.class.getResource(fxmlArchivo.getFxml());

        // Si es null, es que el nombre está mal escrito o el archivo no está ahí
        if (recurso == null) {
            System.err.println("❌ ERROR: No se encuentra el archivo: " + fxmlArchivo);
            System.err.println("Revisa si las mayúsculas coinciden exactamente.");
            return null; // Salimos para evitar el error de "Location is required"
        }

        return getController(recurso);
    }

    /**
     * Método para cargar la vista pasada por parámetro
     *
     * @param recurso URL del recurso a cargar
     * @return - Controlador cargado
     * - null si hubo algún error
     */
    private <T> T getController(URL recurso) {
        try {
            // 1. Cargamos el nuevo diseño (el archivo .fxml)
            FXMLLoader loader = new FXMLLoader(recurso);
            setControllerFactory(loader);

            Parent root = loader.load();

            // 2. Obtenemos la escena actual de la ventana
            Scene escenaActual = stage.getScene();

            if (escenaActual == null) {
                // Si por alguna razón no hubiera escena (primera vez), la creamos
                escenaActual = new Scene(root);
                stage.setScene(escenaActual);
            } else {
                // En lugar de una nueva Scene, cambiamos solo el "Root" (la raíz)
                // Esto mantiene el estado de la ventana (maximizado) intacto.
                escenaActual.setRoot(root);
            }
            return loader.getController();

        } catch (IOException e) {
            System.err.println("❌ Error al cargar la vista " + recurso + ": " + e.getMessage());
            return null;
        }
    }

    /**
     * Método para mostrar una vista modal
     *
     * @param fxmlArchivo enumerado de destino (lleva asociada la ruta al FXML)
     * @param title       Título de la ventana modal
     * @return - Controlador cargado
     * - null si hubo algún error
     */
    public <T> T goToModal(Scenes fxmlArchivo, String title) {
        URL recurso = Navegador.class.getResource(fxmlArchivo.getFxml());
        if (recurso == null) {
            System.err.println("❌ ERROR: No se encuentra el archivo: " + fxmlArchivo.getFxml());
            System.err.println("Revisa si las mayúsculas coinciden exactamente.");
        } else {
            return getModalController(recurso, title);
        }

        return null;
    }

    /**
     * Método para cargar la vista pasada por parámetro - como modal
     *
     * @param resource URL del recurso a cargar
     * @param title    Título de la ventana modal
     * @return - Controlador cargado
     * - null si hubo algún error
     */
    private <T> T getModalController(URL resource, String title) {
        try {

            FXMLLoader loader = new FXMLLoader(resource);
            setControllerFactory(loader);

            Parent root = loader.load();

            Stage modal = new Stage();
            modal.initOwner(stage); // El stage principal
            modal.initModality(Modality.APPLICATION_MODAL);

            modal.setTitle(title);
            modal.setScene(new Scene(root));
            modal.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream(Imagenes.Logo128.getImg()))));
            modal.show();

            return loader.getController();
        } catch (IOException e) {
            System.err.println("❌ Error al cargar la vista " + resource.getPath() + ": " + e.getMessage());
            return null;
        }
    }

    /**
     * Método para pasar el navegador al nuevo controlador
     *
     * @param loader Es la ruta de la nueva vista con el controlador
     */
    private void setControllerFactory(FXMLLoader loader) {
        // Definir la manera en la que se van a generar las instancias de un controlador
        loader.setControllerFactory(type -> {
            try {

                // Instancia el typo con su constructor por defecto (vacío)
                Object controller = type.getDeclaredConstructor().newInstance();

                // Si esa instancia implementa la interfaz Navegable
                if (controller instanceof Navegable n) {
                    n.setNavegador(this);
                }
                return controller;
            } catch (Exception e) {
                throw new RuntimeException("Error al fabricar el controlador", e);
            }
        });
    }
}
