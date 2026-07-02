package angel.tfg.pymeris.Util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests de Session.
 * Session es una clase con estado estático compartido, por lo que
 * cada test limpia dicho estado en @BeforeEach y @AfterEach para
 * garantizar el aislamiento entre tests.
 */
@DisplayName("Session — gestión de sesión estática")
class SessionTest {

    @BeforeEach
    @AfterEach
    void limpiar() {
        Session.limpiarSession();
    }

    // ── Estado por defecto ─────────────────────────────────────────────

    @Test
    @DisplayName("Sin configurar, idEmpleado es 0")
    void sinConfigurar_idEmpleadoEsCero() {
        assertEquals(0, Session.getIdEmpleado());
    }

    @Test
    @DisplayName("Sin configurar, nombre es cadena vacía")
    void sinConfigurar_nombreEsVacio() {
        assertEquals("", Session.getNombre());
    }

    @Test
    @DisplayName("Sin configurar, rol es cadena vacía")
    void sinConfigurar_rolEsVacio() {
        assertEquals("", Session.getRol());
    }

    // ── configurarSession ──────────────────────────────────────────────

    @Test
    @DisplayName("configurarSession guarda todos los datos correctamente")
    void configurarSession_guardaDatos() {
        Session.configurarSession(42, "María", "ADMIN");

        assertAll(
                () -> assertEquals(42,      Session.getIdEmpleado()),
                () -> assertEquals("María", Session.getNombre()),
                () -> assertEquals("ADMIN", Session.getRol())
        );
    }

    @Test
    @DisplayName("configurarSession con rol EMPLEADO se almacena correctamente")
    void configurarSession_rolEmpleado() {
        Session.configurarSession(7, "Pedro", "EMPLEADO");
        assertEquals("EMPLEADO", Session.getRol());
    }

    @Test
    @DisplayName("configurarSession sobreescribe una sesión previa")
    void configurarSession_sobreescribeAnterior() {
        Session.configurarSession(1, "Primero", "EMPLEADO");
        Session.configurarSession(99, "Segundo", "ADMIN");

        assertAll(
                () -> assertEquals(99,        Session.getIdEmpleado()),
                () -> assertEquals("Segundo", Session.getNombre()),
                () -> assertEquals("ADMIN",   Session.getRol())
        );
    }

    // ── limpiarSession ─────────────────────────────────────────────────

    @Test
    @DisplayName("limpiarSession restablece idEmpleado a 0")
    void limpiar_idEmpleadoCero() {
        Session.configurarSession(5, "Ana", "ADMIN");
        Session.limpiarSession();
        assertEquals(0, Session.getIdEmpleado());
    }

    @Test
    @DisplayName("limpiarSession restablece nombre a cadena vacía")
    void limpiar_nombreVacio() {
        Session.configurarSession(5, "Ana", "ADMIN");
        Session.limpiarSession();
        assertEquals("", Session.getNombre());
    }

    @Test
    @DisplayName("limpiarSession restablece rol a cadena vacía")
    void limpiar_rolVacio() {
        Session.configurarSession(5, "Ana", "ADMIN");
        Session.limpiarSession();
        assertEquals("", Session.getRol());
    }

    @Test
    @DisplayName("limpiarSession sobre sesión vacía no lanza excepción")
    void limpiar_sobreVacioNoLanzaExcepcion() {
        assertDoesNotThrow(Session::limpiarSession);
    }
}

