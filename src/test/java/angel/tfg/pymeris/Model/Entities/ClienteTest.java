package angel.tfg.pymeris.Model.Entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Cliente — entidad")
class ClienteTest {

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
    }

    // ── Getters / Setters ──────────────────────────────────────────────

    @Test
    @DisplayName("setNIF_DNI y getNIF_DNI preservan el valor")
    void nifDni_setGet() {
        cliente.setNIF_DNI("12345678A");
        assertEquals("12345678A", cliente.getNIF_DNI());
    }

    @Test
    @DisplayName("setNombre y getNombre preservan el valor")
    void nombre_setGet() {
        cliente.setNombre("Juan");
        assertEquals("Juan", cliente.getNombre());
    }

    @Test
    @DisplayName("setApellido y getApellido preservan el valor")
    void apellido_setGet() {
        cliente.setApellido("García");
        assertEquals("García", cliente.getApellido());
    }

    @Test
    @DisplayName("setCorreo y getCorreo preservan el valor")
    void correo_setGet() {
        cliente.setCorreo("juan@empresa.es");
        assertEquals("juan@empresa.es", cliente.getCorreo());
    }

    @Test
    @DisplayName("setTelefono y getTelefono preservan el valor")
    void telefono_setGet() {
        cliente.setTelefono("612345678");
        assertEquals("612345678", cliente.getTelefono());
    }

    // ── Campo activo ───────────────────────────────────────────────────

    @Test
    @DisplayName("activo = 1 indica cliente dado de alta")
    void activo_alta() {
        cliente.setActivo(1);
        assertEquals(1, cliente.getActivo());
    }

    @Test
    @DisplayName("activo = 0 indica cliente dado de baja")
    void activo_baja() {
        cliente.setActivo(0);
        assertEquals(0, cliente.getActivo());
    }

    // ── Campos opcionales ──────────────────────────────────────────────

    @Test
    @DisplayName("Los campos opcionales admiten null")
    void camposOpcionales_admiten_null() {
        cliente.setApellido(null);
        cliente.setCorreo(null);
        cliente.setTelefono(null);

        assertNull(cliente.getApellido());
        assertNull(cliente.getCorreo());
        assertNull(cliente.getTelefono());
    }

    @Test
    @DisplayName("NIF formato empresa (letra inicial) se almacena correctamente")
    void nif_formatoEmpresa() {
        cliente.setNIF_DNI("B98765432");
        assertEquals("B98765432", cliente.getNIF_DNI());
    }
}
