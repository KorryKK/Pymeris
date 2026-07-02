package angel.tfg.pymeris.Views.ManageUsers.CreateUser;

import angel.tfg.pymeris.Model.Entities.Usuario;
import angel.tfg.pymeris.Model.Repository.Usuarios.UsuariosRepository;
import angel.tfg.pymeris.Views.ManageUsers.MaUsContract;
import javafx.application.Platform;

public class NewUserPresenter implements NewUserContract.Presenter{

    private final NewUserContract.View controller;
    private final UsuariosRepository repository;
    private final MaUsContract.Presenter managepresenter;

    public NewUserPresenter(NewUserContract.View controller, UsuariosRepository repository, MaUsContract.Presenter managepresenter) {
        this.controller = controller;
        this.repository = repository;
        this.managepresenter = managepresenter;
    }

    @Override
    public void onCreateClick(){
        String nombre = (controller.getNombre() == null || controller.getNombre().isBlank()) ? null : controller.getNombre();
        String apellido = (controller.getApellido() == null || controller.getApellido().isBlank()) ? null : controller.getApellido();
        // Comprobamos los campos que están vacíos
        if (nombre == null || apellido == null) {
            controller.showErrorCreate("Nombre y apellido son obligatorios.");
            return;
        }

        String correo = (controller.getCorreo() == null || controller.getCorreo().isBlank()) ? null : controller.getCorreo();
        if (correo != null && !validarCorreo(correo)) {
            controller.showErrorCreate("Formato de correo inválido.");
            return;
        }

        String telefono = (controller.getTelefono() == null || controller.getTelefono().isBlank()) ? null : controller.getTelefono();
        if (telefono != null && !validarTelefono(telefono)) {
            controller.showErrorCreate("Teléfono incorrecto.");
            return;
        }

        new Thread(() -> {
            // Si todos los campos están bien
            System.out.println("Todo correcto. Se procede a crear el usuario...");
            Usuario u = new Usuario();
            u.setNombre(nombre);
            u.setApellido(apellido);
            u.setTelefono(telefono);
            u.setCorreo(correo);

            // Llamamos al repository para guardarlo en la base de datos
            boolean success = repository.guardarUsuario(u);

            if (success) {
                Platform.runLater(() -> {
                    System.out.println("Usuario guardado correctamente.");
                    controller.showSuccessCreate("Usuario guardado correctamente.");
                    managepresenter.cargarUsuarios();
                });
            } else  {
                Platform.runLater(() -> controller.showErrorCreate("Usuario no guardado."));
            }
        }).start();
    }

    /**
     * Validar si un número de teléfono (en formato String) es correcto.
     * Debe empezar por 6 o 7 y tener exactamente 9 dígitos en total.
     */
    private boolean validarTelefono(String telefono) {
        if (telefono == null || telefono.isEmpty()) {
            return false;
        }
        // Definimos el "molde" (Expresión Regular o Regex)
        // ^: Indica el inicio de la cadena
        // [67]: El primer carácter debe ser un 6 o un 7
        // [0-9]: Los siguientes caracteres deben ser números del 0 al 9
        // {8}: Debe haber exactamente 8 repeticiones del grupo anterior
        // $: Indica el final de la cadena para qye no haya letras después
        String regex = "^[67][0-9]{8}$";
        return telefono.matches(regex);
    }

    /**
     * Validar si un String tiene un formato de correo electrónico válido.
     * Ejemplo: usuario@dominio.com
     */
    private boolean validarCorreo(String correo) {
        if (correo == null || correo.isEmpty()) {
            return false;
        }

        // El "molde" para el correo:
        // ^[\\w.-]+: Admite letras, números, puntos y guiones antes del @.
        // @: Obliga a que haya una arroba.
        // [a-zA-Z\\d.-]+: Admite el nombre del dominio (ej.: Gmail, hotmail).
        // \\. : Obliga a que haya un punto después del dominio.
        // [a-zA-Z]{2,}: Obliga a que la extensión (com, es, net) tenga al menos 2 letras.
        String regex = "^[\\w.-]+@[a-zA-Z\\d.-]+\\.[a-zA-Z]{2,}$";

        return correo.matches(regex);
    }

}
