package angel.tfg.pymeris.Model.Entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Usuario — entidad")
class UsuarioTest {

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
    }

    @Test
    @DisplayName("Todos los campos se almacenan y recuperan correctamente")
    void todosLosCampos_setGet() {
        LocalDateTime ahora = LocalDateTime.now();

        usuario.setId_Empleado(1);
        usuario.setNombre("Ana");
        usuario.setApellido("López");
        usuario.setCorreo("ana@empresa.com");
        usuario.setTelefono("698765432");
        usuario.setUser("alopez");
        usuario.setContrasena("$2a$15$hashed");
        usuario.setEstado_Usuario(1);
        usuario.setRol("ADMIN");
        usuario.setFecha_registro(ahora);
        usuario.setUltima_modificacion(ahora);
        usuario.setAcceso(1);

        assertAll(
                () -> assertEquals(1,          usuario.getId_Empleado()),
                () -> assertEquals("Ana",      usuario.getNombre()),
                () -> assertEquals("López",    usuario.getApellido()),
                () -> assertEquals("ana@empresa.com", usuario.getCorreo()),
                () -> assertEquals("698765432",usuario.getTelefono()),
                () -> assertEquals("alopez",   usuario.getUser()),
                () -> assertEquals("$2a$15$hashed", usuario.getContrasena()),
                () -> assertEquals(1,          usuario.getEstado_Usuario()),
                () -> assertEquals("ADMIN",    usuario.getRol()),
                () -> assertEquals(ahora,      usuario.getFecha_registro()),
                () -> assertEquals(ahora,      usuario.getUltima_modificacion()),
                () -> assertEquals(1,          usuario.getAcceso())
        );
    }

    // ── estado_Usuario ─────────────────────────────────────────────────

    @Test
    @DisplayName("estado_Usuario = 0 indica usuario dado de baja")
    void estado_baja() {
        usuario.setEstado_Usuario(0);
        assertEquals(0, usuario.getEstado_Usuario());
    }

    @Test
    @DisplayName("estado_Usuario = 1 indica usuario activo")
    void estado_activo() {
        usuario.setEstado_Usuario(1);
        assertEquals(1, usuario.getEstado_Usuario());
    }

    // ── acceso ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("acceso = 0 indica primer acceso (debe cambiar contraseña)")
    void acceso_primerAcceso() {
        usuario.setAcceso(0);
        assertEquals(0, usuario.getAcceso());
    }

    @Test
    @DisplayName("acceso = 1 indica acceso ordinario")
    void acceso_ordinario() {
        usuario.setAcceso(1);
        assertEquals(1, usuario.getAcceso());
    }

    // ── roles ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("El rol ADMIN se almacena correctamente")
    void rol_admin() {
        usuario.setRol("ADMIN");
        assertEquals("ADMIN", usuario.getRol());
    }

    @Test
    @DisplayName("El rol EMPLEADO se almacena correctamente")
    void rol_empleado() {
        usuario.setRol("EMPLEADO");
        assertEquals("EMPLEADO", usuario.getRol());
    }
}

