package angel.tfg.pymeris.Views.ManageClients.CreateClient;

import angel.tfg.pymeris.Model.Entities.Cliente;
import angel.tfg.pymeris.Model.Repository.Clientes.ClientesRepository;
import angel.tfg.pymeris.Views.ManageClients.MaClContract;
import javafx.application.Platform;

public class NewClientPresenter implements NewClientContract.Presenter {

    private final NewClientContract.View controller;
    private final ClientesRepository repository;
    private final MaClContract.Presenter managepresenter;

    public NewClientPresenter(NewClientContract.View controller, ClientesRepository repository, MaClContract.Presenter managepresenter) {
        this.controller = controller;
        this.repository = repository;
        this.managepresenter = managepresenter;
    }

    @Override
    public void onCreateClick() {
        String dninif = (controller.getNifDni().isBlank()) ? null : controller.getNifDni();
        String nombre = (controller.getNombre().isBlank()) ? null : controller.getNombre();
        // Verificamos antes de seguir leyendo para ahorrarnos tiempos de ejecución si están mal
        if (dninif == null || nombre == null) {
            controller.showError("El DNI/NIF y el nombre son obligatorios.");
            return;
        }

        String apellido = (controller.getApellido().isBlank()) ? null : controller.getApellido();
        String correo = (controller.getCorreo() == null || controller.getCorreo().isBlank()) ? null : controller.getCorreo().trim();
        if (correo != null && !validarCorreo(correo)) {
            controller.showError("Formato de correo inválido.");
            return;
        }

        String telefono = (controller.getTelefono() == null || controller.getTelefono().isBlank()) ? null : controller.getTelefono().trim();
        if (telefono != null && !validarTelefono(telefono)) {
            controller.showError("Teléfono incorrecto.");
            return;
        }

        new Thread(() -> {
            // Si todo va bien
            System.out.println("Todo correcto. Se procede a crear el cliente...");
            Cliente c = new Cliente();
            c.setNIF_DNI(dninif);
            c.setNombre(nombre);
            c.setApellido(apellido);
            c.setCorreo(correo);
            c.setTelefono(telefono);

            // Llamamos al repository para guardarlo en la base de datos
            boolean success = repository.guardarCliente(c);

            if (success) {
                Platform.runLater(() -> {
                    System.out.println("Cliente guardado correctamente.");
                    controller.showSuccess("Cliente guardado correctamente.");
                    managepresenter.cargarClientes();
                });
            } else  {
                Platform.runLater(() -> controller.showError("Cliente no guardado."));
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
