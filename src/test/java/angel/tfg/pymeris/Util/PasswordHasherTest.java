package angel.tfg.pymeris.Util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests de PasswordHasher.
 * NOTA: hashPassword usa un work-factor de 15, lo que hace cada hash
 * lento a propósito (seguridad). Si los tests tardan, se puede reemplazar
 * BCrypt.gensalt(15) por gensalt(4) en el entorno de pruebas.
 */
@DisplayName("PasswordHasher — utilidad de cifrado BCrypt")
class PasswordHasherTest {

    // ── hashPassword ───────────────────────────────────────────────────

    @Test
    @DisplayName("hashPassword devuelve un hash no nulo")
    void hash_noEsNull() {
        assertNotNull(PasswordHasher.hashPassword("miPassword123"));
    }

    @Test
    @DisplayName("hashPassword devuelve un hash no vacío")
    void hash_noEsVacio() {
        assertFalse(PasswordHasher.hashPassword("miPassword123").isEmpty());
    }

    @Test
    @DisplayName("hashPassword genera hashes distintos para la misma contraseña (salt aleatorio)")
    void hash_saltAleatorio_generaHashesDistintos() {
        String hash1 = PasswordHasher.hashPassword("igualContraseña");
        String hash2 = PasswordHasher.hashPassword("igualContraseña");
        assertNotEquals(hash1, hash2,
                "BCrypt debe generar un salt distinto en cada llamada");
    }

    @Test
    @DisplayName("hashPassword no devuelve la contraseña en texto plano")
    void hash_noContienePasswordOriginal() {
        String password = "secreto123";
        String hash = PasswordHasher.hashPassword(password);
        assertNotEquals(password, hash);
    }

    // ── checkPassword ──────────────────────────────────────────────────

    @Test
    @DisplayName("checkPassword devuelve true con la contraseña correcta")
    void check_contrasenhaCorrecta_devuelveTrue() {
        String password = "contraseñaSegura99";
        String hash = PasswordHasher.hashPassword(password);
        assertTrue(PasswordHasher.checkPassword(password, hash));
    }

    @Test
    @DisplayName("checkPassword devuelve false con contraseña incorrecta")
    void check_contrasenhaIncorrecta_devuelveFalse() {
        String hash = PasswordHasher.hashPassword("correcta");
        assertFalse(PasswordHasher.checkPassword("incorrecta", hash));
    }

    @Test
    @DisplayName("checkPassword devuelve false con hash inválido (no lanza excepción)")
    void check_hashInvalido_devuelveFalse() {
        assertFalse(PasswordHasher.checkPassword("cualquier", "esto_no_es_un_hash_bcrypt"));
    }

    @Test
    @DisplayName("checkPassword devuelve false con hash null (no lanza excepción)")
    void check_hashNull_devuelveFalse() {
        assertFalse(PasswordHasher.checkPassword("cualquier", null));
    }

    @Test
    @DisplayName("checkPassword devuelve false con hash vacío (no lanza excepción)")
    void check_hashVacio_devuelveFalse() {
        assertFalse(PasswordHasher.checkPassword("cualquier", ""));
    }

    @Test
    @DisplayName("checkPassword diferencia mayúsculas y minúsculas")
    void check_caseSensitive() {
        String hash = PasswordHasher.hashPassword("Password");
        assertFalse(PasswordHasher.checkPassword("password", hash),
                "BCrypt debe diferenciar 'Password' de 'password'");
    }

    // ── Compatibilidad hash ↔ check ────────────────────────────────────

    @ParameterizedTest(name = "password = \"{0}\"")
    @ValueSource(strings = {"abc123", "P@ssw0rd!", "hola mundo", "12345678", "Ab1Cd2Ef3", "PyMeRiStEsT"})
    @DisplayName("hashPassword + checkPassword son siempre compatibles")
    void hash_y_check_sonCompatibles(String password) {
        String hash = PasswordHasher.hashPassword(password);
        assertTrue(PasswordHasher.checkPassword(password, hash),
                "checkPassword debería devolver true para: " + password);
    }
}
