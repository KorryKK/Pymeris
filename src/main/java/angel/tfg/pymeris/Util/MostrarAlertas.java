package angel.tfg.pymeris.Util;

import angel.tfg.pymeris.Util.Enums.Imagenes;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.Optional;

public class MostrarAlertas {

    /**
     * Función genérica para mostrar una información
     *
     * @param titulo Título que tendrá la alerta
     * @param mensaje Mensaje que contrendrá la alerta
     */
    public static void mostrarInformacion(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Objects.requireNonNull(MostrarAlertas.class.getResourceAsStream(Imagenes.Logo128.getImg()))));
        alert.showAndWait();
    }

    /**
     * Función genérica para mostrar un warning
     *
     * @param titulo Título que tendrá la alerta
     * @param mensaje Mensaje que contrendrá la alerta
     */
    public static void mostrarWarning(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Objects.requireNonNull(MostrarAlertas.class.getResourceAsStream(Imagenes.Logo128.getImg()))));
        alert.showAndWait();
    }

    /**
     * Función genérica para mostrar un error
     *
     * @param titulo Título que tendrá la alerta
     * @param mensaje Mensaje que contrendrá la alerta
     */
    public static void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Objects.requireNonNull(MostrarAlertas.class.getResourceAsStream(Imagenes.Logo128.getImg()))));
        alert.showAndWait();
    }

    /**
     * Función genérica para solicitar una confirmación
     *
     * @param titulo Título que tendrá la alerta
     * @param mensaje Mensaje que contrendrá la alerta
     */
    public static boolean solictarConfirmacion(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, mensaje, ButtonType.YES, ButtonType.NO);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Objects.requireNonNull(MostrarAlertas.class.getResourceAsStream(Imagenes.Logo128.getImg()))));
        Optional<ButtonType> result = alert.showAndWait();

        return result.isPresent() && result.get() == ButtonType.YES;
    }
}
