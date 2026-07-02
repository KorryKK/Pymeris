package angel.tfg.pymeris.Views.ManageUsers.CreateUser;

import angel.tfg.pymeris.Model.Entities.Usuario;
import angel.tfg.pymeris.Model.Repository.Usuarios.UsuariosRepository;
import angel.tfg.pymeris.Views.ManageUsers.MaUsContract;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("NewUserPresenter — creación de usuario")
class NewUserPresenterTest {

    @Mock private NewUserContract.View   mockView;
    @Mock private UsuariosRepository     mockRepository;
    @Mock private MaUsContract.Presenter mockManagePresenter;

    private NewUserPresenter presenter;

    @BeforeAll
    static void initJFX() {
        // Inicializa el entorno de JavaFX para que Platform.runLater funcione
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // Si ya está inicializado, ignoramos la excepción
        }
    }

    @BeforeEach
    void setUp() {
        presenter = new NewUserPresenter(mockView, mockRepository, mockManagePresenter);
    }

    // ── Campos obligatorios ────────────────────────────────────────────

    @Test
    @DisplayName("Nombre vacío → error y repositorio NO llamado")
    void nombreVacio_muestraError() {
        when(mockView.getNombre()).thenReturn("");
        when(mockView.getApellido()).thenReturn("López");

        presenter.onCreateClick();

        verify(mockView).showErrorCreate("Nombre y apellido son obligatorios.");
        verify(mockRepository, never()).guardarUsuario(any());
    }

    @Test
    @DisplayName("Nombre null → error y repositorio NO llamado")
    void nombreNull_muestraError() {
        when(mockView.getNombre()).thenReturn(null);
        when(mockView.getApellido()).thenReturn("López");

        presenter.onCreateClick();

        verify(mockView).showErrorCreate("Nombre y apellido son obligatorios.");
        verify(mockRepository, never()).guardarUsuario(any());
    }

    @Test
    @DisplayName("Apellido vacío → error y repositorio NO llamado")
    void apellidoVacio_muestraError() {
        when(mockView.getNombre()).thenReturn("Ana");
        when(mockView.getApellido()).thenReturn("");

        presenter.onCreateClick();

        verify(mockView).showErrorCreate("Nombre y apellido son obligatorios.");
        verify(mockRepository, never()).guardarUsuario(any());
    }

    @Test
    @DisplayName("Apellido null → error y repositorio NO llamado")
    void apellidoNull_muestraError() {
        when(mockView.getNombre()).thenReturn("Ana");
        when(mockView.getApellido()).thenReturn(null);

        presenter.onCreateClick();

        verify(mockView).showErrorCreate("Nombre y apellido son obligatorios.");
    }

    @Test
    @DisplayName("Nombre solo espacios → tratado como vacío → error")
    void nombreSoloEspacios_tratadoComoVacio() {
        when(mockView.getNombre()).thenReturn("   ");
        when(mockView.getApellido()).thenReturn("López");

        presenter.onCreateClick();

        verify(mockView).showErrorCreate(anyString());
    }

    // ── Validación correo ──────────────────────────────────────────────

    @ParameterizedTest(name = "correo = \"{0}\"")
    @ValueSource(strings = {"sinArroba", "@sinUsuario.com", "sin.punto@dom", "doble@@dom.es"})
    @DisplayName("Correo con formato inválido → error y repositorio NO llamado")
    void correoInvalido_muestraError(String correo) {
        when(mockView.getNombre()).thenReturn("Ana");
        when(mockView.getApellido()).thenReturn("López");
        when(mockView.getCorreo()).thenReturn(correo);

        presenter.onCreateClick();

        verify(mockView).showErrorCreate("Formato de correo inválido.");
        verify(mockRepository, never()).guardarUsuario(any());
    }

    // ── Validación teléfono ────────────────────────────────────────────

    @ParameterizedTest(name = "teléfono = \"{0}\"")
    @ValueSource(strings = {"912345678", "123456789", "61234567", "61234567890", "abcdefghi"})
    @DisplayName("Teléfono con formato inválido → error y repositorio NO llamado")
    void telefonoInvalido_muestraError(String telefono) {
        when(mockView.getNombre()).thenReturn("Ana");
        when(mockView.getApellido()).thenReturn("López");
        when(mockView.getCorreo()).thenReturn(null);
        when(mockView.getTelefono()).thenReturn(telefono);

        presenter.onCreateClick();

        verify(mockView).showErrorCreate("Teléfono incorrecto.");
        verify(mockRepository, never()).guardarUsuario(any());
    }

    // ── Correo y teléfono null son opcionales ──────────────────────────

    @Test
    @DisplayName("Correo y teléfono null (opcionales) → repositorio es llamado")
    void camposOpcionalesNulos_repositorioLlamado() throws InterruptedException {
        when(mockView.getNombre()).thenReturn("Ana");
        when(mockView.getApellido()).thenReturn("López");
        when(mockView.getCorreo()).thenReturn(null);
        when(mockView.getTelefono()).thenReturn(null);
        when(mockRepository.guardarUsuario(any())).thenReturn(true);

        presenter.onCreateClick();
        Thread.sleep(300);

        verify(mockRepository, times(1)).guardarUsuario(any());
        verify(mockView, never()).showErrorCreate(anyString());
    }

    // ── Flujo exitoso ──────────────────────────────────────────────────

    @Test
    @DisplayName("Datos completos válidos → repositorio llamado con el usuario correcto")
    void datosValidos_repositorioLlamadoConUsuarioCorrecto() throws InterruptedException {
        when(mockView.getNombre()).thenReturn("Ana");
        when(mockView.getApellido()).thenReturn("López");
        when(mockView.getCorreo()).thenReturn("ana@empresa.es");
        when(mockView.getTelefono()).thenReturn("612345678");
        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        when(mockRepository.guardarUsuario(captor.capture())).thenReturn(true);

        presenter.onCreateClick();
        Thread.sleep(300);

        Usuario u = captor.getValue();
        assertAll(
            () -> assertEquals("Ana",           u.getNombre()),
            () -> assertEquals("López",         u.getApellido()),
            () -> assertEquals("ana@empresa.es",u.getCorreo()),
            () -> assertEquals("612345678",     u.getTelefono())
        );
    }
}
