package angel.tfg.pymeris.Views.ManageUsers.ModifyUser;

import angel.tfg.pymeris.Model.Entities.Usuario;
import angel.tfg.pymeris.Model.Repository.Usuarios.UsuariosRepository;
import angel.tfg.pymeris.Views.ManageUsers.MaUsContract;
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
@DisplayName("ModifyUserPresenter — modificación de usuario")
class ModifyUserPresenterTest {

    @Mock private MoUsContract.View      mockView;
    @Mock private UsuariosRepository     mockRepository;
    @Mock private MaUsContract.Presenter mockManagePresenter;

    private ModifyUserPresenter presenter;

    @BeforeEach
    void setUp() {
        presenter = new ModifyUserPresenter(mockView, mockRepository, mockManagePresenter);
    }

    // ── btnOf(true) siempre al inicio ──────────────────────────────────

    @Test
    @DisplayName("btnOf(true) se llama siempre al inicio de onModifyUserClick")
    void btnOf_siempreLlamadoAlInicio() {
        when(mockView.getNombre()).thenReturn("");
        when(mockView.getApellido()).thenReturn("X");
        when(mockView.getUsuario()).thenReturn("x");

        presenter.onModifyUserClick();

        verify(mockView, times(1)).btnOf(true);
    }

    // ── Campos obligatorios ────────────────────────────────────────────

    @Test
    @DisplayName("Nombre vacío → showError y repositorio NO llamado")
    void nombreVacio_muestraError() {
        when(mockView.getNombre()).thenReturn("");
        when(mockView.getApellido()).thenReturn("López");
        when(mockView.getUsuario()).thenReturn("alopez");

        presenter.onModifyUserClick();

        verify(mockView).showError("Nombre, apellido y usuario son obligatorios.");
        verify(mockRepository, never()).actualizarUsuario(any());
    }

    @Test
    @DisplayName("Nombre null → showError y repositorio NO llamado")
    void nombreNull_muestraError() {
        when(mockView.getNombre()).thenReturn(null);
        when(mockView.getApellido()).thenReturn("López");
        when(mockView.getUsuario()).thenReturn("alopez");

        presenter.onModifyUserClick();

        verify(mockView).showError("Nombre, apellido y usuario son obligatorios.");
        verify(mockRepository, never()).actualizarUsuario(any());
    }

    @Test
    @DisplayName("Apellido vacío → showError y repositorio NO llamado")
    void apellidoVacio_muestraError() {
        when(mockView.getNombre()).thenReturn("Ana");
        when(mockView.getApellido()).thenReturn("");
        when(mockView.getUsuario()).thenReturn("alopez");

        presenter.onModifyUserClick();

        verify(mockView).showError("Nombre, apellido y usuario son obligatorios.");
        verify(mockRepository, never()).actualizarUsuario(any());
    }

    @Test
    @DisplayName("Usuario (login) vacío → showError y repositorio NO llamado")
    void usuarioVacio_muestraError() {
        when(mockView.getNombre()).thenReturn("Ana");
        when(mockView.getApellido()).thenReturn("López");
        when(mockView.getUsuario()).thenReturn("");

        presenter.onModifyUserClick();

        verify(mockView).showError("Nombre, apellido y usuario son obligatorios.");
        verify(mockRepository, never()).actualizarUsuario(any());
    }

    @Test
    @DisplayName("Los tres obligatorios null → showError y no se llama al repositorio")
    void todosObligatoriosNull_muestraError() {
        when(mockView.getNombre()).thenReturn(null);
        when(mockView.getApellido()).thenReturn(null);
        when(mockView.getUsuario()).thenReturn(null);

        presenter.onModifyUserClick();

        verify(mockView).showError(anyString());
        verify(mockRepository, never()).actualizarUsuario(any());
    }

    // ── Validación correo ──────────────────────────────────────────────

    @ParameterizedTest(name = "correo = \"{0}\"")
    @ValueSource(strings = {"sinArroba", "@sinUsuario.es", "doble@@dom.com", "sin.punto@dom"})
    @DisplayName("Correo con formato inválido → showError y repositorio NO llamado")
    void correoInvalido_muestraError(String correo) {
        configurarVistaObligatoriosValidos();
        when(mockView.getCorreo()).thenReturn(correo);

        presenter.onModifyUserClick();

        verify(mockView).showError("El correo no tiene un formato válido.");
        verify(mockRepository, never()).actualizarUsuario(any());
    }

    // ── Validación teléfono ────────────────────────────────────────────

    @ParameterizedTest(name = "teléfono = \"{0}\"")
    @ValueSource(strings = {"912345678", "123456789", "6123456", "61234567890", "abcde1234", "78348", "7123456891"})
    @DisplayName("Teléfono con formato inválido → showError y repositorio NO llamado")
    void telefonoInvalido_muestraError(String telefono) {
        configurarVistaObligatoriosValidos();
        when(mockView.getCorreo()).thenReturn(null);
        when(mockView.getTelefono()).thenReturn(telefono);

        presenter.onModifyUserClick();

        verify(mockView).showError("El teléfono no es válido (9 dígitos, empieza por 6 o 7).");
        verify(mockRepository, never()).actualizarUsuario(any());
    }

    // ── Mapeo de rol ───────────────────────────────────────────────────

    @Test
    @DisplayName("getAdmin()=true → rol 'Administrador' en el Usuario construido")
    void adminTrue_rolAdministrador() throws InterruptedException {
        configurarVistaCompleta(true, true);
        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        when(mockRepository.actualizarUsuario(captor.capture())).thenReturn(true);

        presenter.onModifyUserClick();
        Thread.sleep(300);

        assertEquals("Administrador", captor.getValue().getRol());
    }

    @Test
    @DisplayName("getAdmin()=false → rol 'Empleado' en el Usuario construido")
    void adminFalse_rolEmpleado() throws InterruptedException {
        configurarVistaCompleta(false, true);
        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        when(mockRepository.actualizarUsuario(captor.capture())).thenReturn(true);

        presenter.onModifyUserClick();
        Thread.sleep(300);

        assertEquals("Empleado", captor.getValue().getRol());
    }

    // ── Mapeo de estado ────────────────────────────────────────────────

    @Test
    @DisplayName("getEstado()=true → estado_Usuario=1 en el Usuario construido")
    void estadoTrue_usuarioActivo() throws InterruptedException {
        configurarVistaCompleta(false, true);
        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        when(mockRepository.actualizarUsuario(captor.capture())).thenReturn(true);

        presenter.onModifyUserClick();
        Thread.sleep(300);

        assertEquals(1, captor.getValue().getEstado_Usuario());
    }

    @Test
    @DisplayName("getEstado()=false → estado_Usuario=0 en el Usuario construido")
    void estadoFalse_usuarioBaja() throws InterruptedException {
        configurarVistaCompleta(false, false);
        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        when(mockRepository.actualizarUsuario(captor.capture())).thenReturn(true);

        presenter.onModifyUserClick();
        Thread.sleep(300);

        assertEquals(0, captor.getValue().getEstado_Usuario());
    }

    // ── Flujo exitoso ──────────────────────────────────────────────────

    @Test
    @DisplayName("Actualización fallida → managePresenter.cargarUsuarios() NO es llamado")
    void actualizacionFallida_noLlamaCargarUsuarios() throws InterruptedException {
        configurarVistaCompleta(false, true);
        when(mockRepository.actualizarUsuario(any())).thenReturn(false);

        presenter.onModifyUserClick();
        Thread.sleep(300);

        verify(mockManagePresenter, never()).cargarUsuarios();
    }

    // ── Helpers ───────────────────────────────────────────────────────

    /** Configura solo los tres campos obligatorios (nombre, apellido, usuario). */
    private void configurarVistaObligatoriosValidos() {
        when(mockView.getNombre()).thenReturn("Ana");
        when(mockView.getApellido()).thenReturn("López");
        when(mockView.getUsuario()).thenReturn("alopez");
    }

    /** Configura todos los campos para un flujo completo válido. */
    private void configurarVistaCompleta(boolean isAdmin, boolean isActivo) {
        when(mockView.getNombre()).thenReturn("Ana");
        when(mockView.getApellido()).thenReturn("López");
        when(mockView.getUsuario()).thenReturn("alopez");
        when(mockView.getCorreo()).thenReturn(null);
        when(mockView.getTelefono()).thenReturn(null);
        when(mockView.getAdmin()).thenReturn(isAdmin);
        when(mockView.getEstado()).thenReturn(isActivo);
        when(mockView.getID()).thenReturn("1");
    }
}
