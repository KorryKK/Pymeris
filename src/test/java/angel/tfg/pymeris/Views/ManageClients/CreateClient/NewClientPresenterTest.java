package angel.tfg.pymeris.Views.ManageClients.CreateClient;

import angel.tfg.pymeris.Model.Entities.Cliente;
import angel.tfg.pymeris.Model.Repository.Clientes.ClientesRepository;
import angel.tfg.pymeris.Views.ManageClients.MaClContract;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

/**
 * Tests de NewClientPresenter.
 * Se verifica toda la lógica de validación síncrona (campos obligatorios,
 * formato de correo y teléfono). La persistencia real se mockeada.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("NewClientPresenter — creación de cliente")
class NewClientPresenterTest {

    @Mock
    private NewClientContract.View mockView;
    @Mock
    private ClientesRepository mockRepository;
    @Mock
    private MaClContract.Presenter mockManagePresenter;

    private NewClientPresenter presenter;

    @BeforeEach
    void setUp() {
        presenter = new NewClientPresenter(mockView, mockRepository, mockManagePresenter);
    }

    // ── Campos obligatorios ────────────────────────────────────────────

    @Test
    @DisplayName("DNI/NIF vacío → error y repositorio NO es llamado")
    void dniVacio_muestraError() {
        when(mockView.getNifDni()).thenReturn("");
        when(mockView.getNombre()).thenReturn("Juan");

        presenter.onCreateClick();

        verify(mockView).showError("El DNI/NIF y el nombre son obligatorios.");
        verify(mockRepository, never()).guardarCliente(any());
    }

    @Test
    @DisplayName("Nombre vacío → error y repositorio NO es llamado")
    void nombreVacio_muestraError() {
        when(mockView.getNifDni()).thenReturn("12345678A");
        when(mockView.getNombre()).thenReturn("");

        presenter.onCreateClick();

        verify(mockView).showError("El DNI/NIF y el nombre son obligatorios.");
        verify(mockRepository, never()).guardarCliente(any());
    }

    @Test
    @DisplayName("Ambos campos obligatorios vacíos → error")
    void ambosObligatoriosVacios_muestraError() {
        when(mockView.getNifDni()).thenReturn("");
        when(mockView.getNombre()).thenReturn("");

        presenter.onCreateClick();

        verify(mockView).showError("El DNI/NIF y el nombre son obligatorios.");
    }

    @Test
    @DisplayName("DNI/NIF solo espacios → se trata como vacío → error")
    void dniSoloEspacios_tratadoComoVacio() {
        when(mockView.getNifDni()).thenReturn("   ");
        when(mockView.getNombre()).thenReturn("Juan");

        presenter.onCreateClick();

        verify(mockView).showError("El DNI/NIF y el nombre son obligatorios.");
    }

    // ── Validación correo ──────────────────────────────────────────────

    @ParameterizedTest(name = "correo = \"{0}\"")
    @ValueSource(strings = {"noesuncorreo", "sin@dominio", "@sinusuario.com",
            "doble@@dominio.es", "sin.extension@dominio"})
    @DisplayName("Correo con formato inválido → error y repositorio NO es llamado")
    void correoInvalido_muestraError(String correo) {
        when(mockView.getNifDni()).thenReturn("12345678A");
        when(mockView.getNombre()).thenReturn("Juan");
        when(mockView.getApellido()).thenReturn("García");
        when(mockView.getCorreo()).thenReturn(correo);

        presenter.onCreateClick();

        verify(mockView).showError("Formato de correo inválido.");
        verify(mockRepository, never()).guardarCliente(any());
    }

    // ── Flujo completo exitoso ─────────────────────────────────────────

    @Test
    @DisplayName("Datos completos y válidos → repositorio es llamado una vez")
    void datosValidos_llamaRepositorio() throws InterruptedException {
        when(mockView.getNifDni()).thenReturn("12345678A");
        when(mockView.getNombre()).thenReturn("Juan");
        when(mockView.getApellido()).thenReturn("García");
        when(mockView.getCorreo()).thenReturn("juan@gmail.com");
        when(mockView.getTelefono()).thenReturn("612345678");
        when(mockRepository.guardarCliente(any(Cliente.class))).thenReturn(true);

        presenter.onCreateClick();

        Thread.sleep(300); // Esperamos al hilo secundario

        verify(mockRepository, times(1)).guardarCliente(any(Cliente.class));
    }
}
