package angel.tfg.pymeris.Views.ManageUsers.ModifyUser;

import angel.tfg.pymeris.Model.Entities.Usuario;
import angel.tfg.pymeris.Model.Repository.Usuarios.UsuariosRepository;
import angel.tfg.pymeris.Views.ManageUsers.MaUsContract;
import javafx.application.Platform;

public class ModifyUserPresenter implements MoUsContract.Presenter {


    private final MoUsContract.View controller;
    private final UsuariosRepository repository;
    private final MaUsContract.Presenter managePresenter;

    public ModifyUserPresenter(MoUsContract.View controller, UsuariosRepository repository, MaUsContract.Presenter managePresenter) {
        this.controller = controller;
        this.repository = repository;
        this.managePresenter = managePresenter;
    }

    @Override
    public void onModifyUserClick() {
        controller.btnOf(true);

        // 1. Declaramos y asignamos usando el ternario DESDE EL PRINCIPIO.
        // Al hacerlo así, las variables no cambian después y el Thread las acepta.
        String nombre = (controller.getNombre() == null || controller.getNombre().isBlank()) ? null : controller.getNombre().trim();
        String apellido = (controller.getApellido() == null || controller.getApellido().isBlank()) ? null : controller.getApellido().trim();
        String usuario = (controller.getUsuario() == null || controller.getUsuario().isBlank()) ? null : controller.getUsuario().trim();
        // 2. Validaciones de campos obligatorios
        if (nombre == null || apellido == null || usuario == null) {
            controller.showError("Nombre, apellido y usuario son obligatorios.");
            return;
        }

        String correo = (controller.getCorreo() == null || controller.getCorreo().isBlank()) ? null : controller.getCorreo().trim();
        // 3. Validaciones de formato (solo si el usuario escribió algo, es decir, no es null)
        if (correo != null && !validarCorreo(correo)) {
            controller.showError("El correo no tiene un formato válido.");
            return;
        }

        String telefono = (controller.getTelefono() == null || controller.getTelefono().isBlank()) ? null : controller.getTelefono().trim();
        if (telefono != null && !validarTelefono(telefono)) {
            controller.showError("El teléfono no es válido (9 dígitos, empieza por 6 o 7).");
            return;
        }

        // 4. Capturamos los valores de los botones de selección
        String rol = (controller.getAdmin() ? "Administrador" : "Empleado");
        int estado = (controller.getEstado() ? 1 : 0);

        // 5. Recogemos el mismo ID que tiene para encontrarlo en la base de datos
        int id = Integer.parseInt(controller.getID());

        new Thread(() -> {
            // Si todos los campos están bien
            System.out.println("Todo correcto. Se procede a modificar el usuario...");
            Usuario u = new Usuario();
            u.setId_Empleado(id);
            u.setNombre(nombre);
            u.setApellido(apellido);
            u.setCorreo(correo);
            u.setTelefono(telefono);
            u.setUser(usuario);
            u.setRol(rol);
            u.setEstado_Usuario(estado);

            boolean success = repository.actualizarUsuario(u);

            if (success) {
                Platform.runLater(() -> {
                    System.out.println("Usuario modificado correctamente.");
                    controller.showModifyStatus("Usuario modificado correctamente.");
                    managePresenter.cargarUsuarios();
                });
            } else {
                Platform.runLater(() -> {
                    controller.showError("No se ha modificado el usuario.");
                    System.out.println("No se ha modificado el usuario.");
                });
            }
        }).start();
    }

    /**
     * Validar si un String tiene un formato de correo electrónico válido.
     * Ejemplo: usuario@dominio.com
     */
    private boolean validarCorreo(String correo) {
        // El "molde" para el correo:
        // ^[\\w.-]+: Admite letras, números, puntos y guiones antes del @.
        // @: Obliga a que haya una arroba.
        // [a-zA-Z\\d.-]+: Admite el nombre del dominio (ej.: Gmail, hotmail).
        // \\. : Obliga a que haya un punto después del dominio.
        // [a-zA-Z]{2,}: Obliga a que la extensión (com, es, net) tenga al menos 2 letras.
        String regex = "^[\\w.-]+@[a-zA-Z\\d.-]+\\.[a-zA-Z]{2,}$";

        return correo.matches(regex);
    }

    /**
     * Validar si un número de teléfono (en formato String) es correcto.
     * Debe empezar por 6 o 7 y tener exactamente 9 dígitos en total.
     */
    private boolean validarTelefono(String telefono) {
        if (telefono.isEmpty()) {
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
}