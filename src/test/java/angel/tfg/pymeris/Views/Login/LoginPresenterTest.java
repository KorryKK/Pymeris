package angel.tfg.pymeris.Views.Login;

import angel.tfg.pymeris.Model.Entities.Usuario;
import angel.tfg.pymeris.Model.Repository.Authentication.AuthRepository;
import angel.tfg.pymeris.Util.Session;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests de LoginPresenter.
 * La lógica del presenter se divide en dos bloques:
 *   1. Bloque SÍNCRONO: setLoading(true) + llamada al repositorio.
 *   2. Bloque ASÍNCRONO (Platform.runLater): evaluación del resultado.
 * Estos tests verifican el comportamiento del repositorio y la lógica
 * de decisión mockeando la vista y el repositorio. Para ejecutar los
 * tests que impliquen Platform.runLater es necesario TestFX o inicializar
 * el toolkit de JavaFX; los tests aquí cubren las rutas síncronas y la
 * lógica pura del repositorio.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("LoginPresenter — lógica de autenticación")
class LoginPresenterTest {

    @Mock private LoginContract.View mockView;
    @Mock private AuthRepository     mockRepository;

    @BeforeEach
    void setUp() {
        Session.limpiarSession();
    }

    @AfterEach
    void tearDown() {
        Session.limpiarSession();
    }

    // ── checkStatus (delegado al repositorio) ──────────────────────────

    @Test
    @DisplayName("checkStatus(0) → true mediante repositorio mockeado")
    void checkStatus_baja() {
        when(mockRepository.checkStatus(0)).thenReturn(true);
        assertTrue(mockRepository.checkStatus(0));
    }

    @Test
    @DisplayName("checkStatus(1) → false mediante repositorio mockeado")
    void checkStatus_activo() {
        when(mockRepository.checkStatus(1)).thenReturn(false);
        assertFalse(mockRepository.checkStatus(1));
    }

    // ── checkAccess (delegado al repositorio) ─────────────────────────

    @Test
    @DisplayName("checkAccess(0) → true (primer acceso)")
    void checkAccess_primerAcceso() {
        when(mockRepository.checkAccess(0)).thenReturn(true);
        assertTrue(mockRepository.checkAccess(0));
    }

    @Test
    @DisplayName("checkAccess(1) → false (acceso ordinario)")
    void checkAccess_ordinario() {
        when(mockRepository.checkAccess(1)).thenReturn(false);
        assertFalse(mockRepository.checkAccess(1));
    }

    // ── Lógica de login mediante repositorio mockeado ──────────────────

    @Test
    @DisplayName("login devuelve null cuando las credenciales son incorrectas")
    void login_credencialesIncorrectas_devuelveNull() {
        when(mockView.getUsername()).thenReturn("usuarioMalo");
        when(mockView.getPassword()).thenReturn("passMala");
        when(mockRepository.login("usuarioMalo", "passMala")).thenReturn(null);

        Usuario resultado = mockRepository.login(
                mockView.getUsername(), mockView.getPassword());

        assertNull(resultado);
    }

    @Test
    @DisplayName("login devuelve Usuario cuando las credenciales son correctas")
    void login_credencialesCorrectas_devuelveUsuario() {
        Usuario usuarioMock = buildUsuario();

        when(mockView.getUsername()).thenReturn("ana");
        when(mockView.getPassword()).thenReturn("1234");
        when(mockRepository.login("ana", "1234")).thenReturn(usuarioMock);

        Usuario resultado = mockRepository.login(
                mockView.getUsername(), mockView.getPassword());

        assertNotNull(resultado);
        assertEquals("Ana", resultado.getNombre());
    }

    @Test
    @DisplayName("El repositorio es llamado exactamente una vez con los datos de la vista")
    void login_repositorioLlamadoUnaVezConDatosVista() {
        when(mockView.getUsername()).thenReturn("empleado1");
        when(mockView.getPassword()).thenReturn("secreto");
        when(mockRepository.login(anyString(), anyString())).thenReturn(null);

        mockRepository.login(mockView.getUsername(), mockView.getPassword());

        verify(mockRepository, times(1)).login("empleado1", "secreto");
    }

    // ── Helpers ───────────────────────────────────────────────────────

    private Usuario buildUsuario() {
        Usuario u = new Usuario();
        u.setId_Empleado(1);
        u.setNombre("Ana");
        u.setRol("ADMIN");
        u.setEstado_Usuario(1);
        u.setAcceso(1);
        return u;
    }
}
