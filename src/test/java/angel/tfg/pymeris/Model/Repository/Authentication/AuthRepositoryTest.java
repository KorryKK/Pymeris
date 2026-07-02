package angel.tfg.pymeris.Model.Repository.Authentication;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests de AuthRepositoryImpl.
 * Los métodos checkStatus y checkAccess contienen lógica pura (sin BD),
 * por lo que se pueden testar directamente sin mocks.
 * login() y changePass() delegan en CheckLogin (acceso a BD) y se testean
 * de forma indirecta en LoginPresenterTest mediante mocks del repositorio.
 */
@DisplayName("AuthRepository — lógica de estado y acceso")
class AuthRepositoryTest {

    private AuthRepository repository;

    @BeforeEach
    void setUp() {
        repository = new AuthRepositoryImpl();
    }

    // ── checkStatus ────────────────────────────────────────────────────
    // Semántica: devuelve true si el usuario está DADO DE BAJA (status == 0)

    @Test
    @DisplayName("checkStatus(0) → true (usuario dado de baja)")
    void checkStatus_cero_esBaja() {
        assertTrue(repository.checkStatus(0));
    }

    @Test
    @DisplayName("checkStatus(1) → false (usuario activo)")
    void checkStatus_uno_esActivo() {
        assertFalse(repository.checkStatus(1));
    }

    // ── checkAccess ────────────────────────────────────────────────────
    // Semántica: devuelve true si hay que cambiar la contraseña (acceso == 0)

    @Test
    @DisplayName("checkAccess(0) → true (primer acceso, debe cambiar contraseña)")
    void checkAccess_cero_esPrimerAcceso() {
        assertTrue(repository.checkAccess(0));
    }

    @Test
    @DisplayName("checkAccess(1) → false (acceso ordinario)")
    void checkAccess_uno_esAccesoOrdinario() {
        assertFalse(repository.checkAccess(1));
    }
}

