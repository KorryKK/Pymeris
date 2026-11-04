package angel.tfg.pymeris.Views.ManageClients.ModifyClient;

import angel.tfg.pymeris.Model.Entities.Cliente;
import angel.tfg.pymeris.Model.Repository.Clientes.ClientesRepository;
import angel.tfg.pymeris.Views.ManageClients.MaClContract;
import javafx.application.Platform;

public class ModifyClientPresenter implements MoClContract.Presenter {

    private final ClientesRepository repository;
    private final MoClContract.View controller;
    private final MaClContract.Presenter managepresenter;

    public ModifyClientPresenter(MoClContract.View controller, ClientesRepository repository, MaClContract.Presenter managepresenter) {
        this.controller = controller;
        this.repository = repository;
        this.managepresenter = managepresenter;
    }

    @Override
    public void onModifyClientClick(String dninifanterior) {
        controller.btnOff(true);

        // 1. Declaramos y asignamos usando ternario, si está "" o si está null, se deja en null
        String dninif = (controller.getDNI_NFI() == null || controller.getDNI_NFI().isBlank()) ? null : controller.getDNI_NFI();
        String nombre = (controller.getNombre() == null || controller.getNombre().isBlank()) ? null : controller.getNombre();
        // 2. Validaciones de campos obligatorios
        if (dninif == null || nombre == null) {
            System.out.println("DNI/NIF o nombre inválidos");
            controller.showError("El nombre y el dni o nif son OBLIGATORIOS.");
            return;
        }

        String apellido = (controller.getApellido() == null || controller.getApellido().isBlank()) ?  null : controller.getApellido();

        // 3. Validaciones de formato (solo si el usuario escribió algo, es decir, no es null)
        String correo = (controller.getCorreo() == null || controller.getCorreo().isBlank()) ?  null : controller.getCorreo();
        if (correo != null && !validarCorreo(correo)) {
            controller.showError("El correo no tiene formato de correo.");
            return;
        }

        String telefono = (controller.getTelefono() == null || controller.getTelefono().isBlank()) ?  null : controller.getTelefono();
        if  (telefono != null && !validarTelefono(telefono)) {
            controller.showError("El telefono no tiene formato de telefono.");
            return;
        }

        int activo = (controller.getActivo() ? 1 : 0);

        new Thread(() -> {
            // Si todos los campos están bien
            System.out.println("Todo correcto. Se procede a modificar el usuario...");
            Cliente c= new Cliente();
            c.setNIF_DNI(dninif);
            c.setNombre(nombre);
            c.setApellido(apellido);
            c.setCorreo(correo);
            c.setTelefono(telefono);
            c.setActivo(activo);

            boolean sucess = repository.actualizarCliente(c, dninifanterior);
            if (sucess) {
                Platform.runLater(() -> {
                    System.out.println("Cliente modificado correctamente.");
                    controller.showModifyStatus("Cliente modificado correctamente.");
                    managepresenter.cargarClientes();
                });
            } else {
                Platform.runLater(() -> {
                    controller.showError("No se ha podido modificar el cliente.");
                    System.out.println("No se ha podido modificar el cliente.");
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
