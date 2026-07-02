package angel.tfg.pymeris.Views.ManageClients.ModifyClient;

import angel.tfg.pymeris.Model.Entities.Cliente;
import angel.tfg.pymeris.Model.Repository.Clientes.ClientesRepository;
import angel.tfg.pymeris.Views.ManageClients.MaClContract;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ModifyClientPresenter — modificación de cliente")
class ModifyClientPresenterTest {

    @Mock private MoClContract.View      mockView;
    @Mock private ClientesRepository     mockRepository;
    @Mock private MaClContract.Presenter mockManagePresenter;

    private ModifyClientPresenter presenter;

    @BeforeEach
    void setUp() {
        presenter = new ModifyClientPresenter(mockView, mockRepository, mockManagePresenter);
    }

    // ── Campos obligatorios ────────────────────────────────────────────

    @Test
    @DisplayName("DNI/NIF vacío → error y repositorio NO es llamado")
    void dniVacio_muestraError() {
        when(mockView.getDNI_NFI()).thenReturn("");
        when(mockView.getNombre()).thenReturn("Juan");

        presenter.onModifyClientClick("12345678A");

        verify(mockView).showError("El nombre y el dni o nif son OBLIGATORIOS.");
        verify(mockRepository, never()).actualizarCliente(any(), anyString());
    }

    @Test
    @DisplayName("DNI/NIF null → error y repositorio NO es llamado")
    void dniNull_muestraError() {
        when(mockView.getDNI_NFI()).thenReturn(null);
        when(mockView.getNombre()).thenReturn("Juan");

        presenter.onModifyClientClick("12345678A");

        verify(mockView).showError("El nombre y el dni o nif son OBLIGATORIOS.");
        verify(mockRepository, never()).actualizarCliente(any(), anyString());
    }

    @Test
    @DisplayName("Nombre vacío → error y repositorio NO es llamado")
    void nombreVacio_muestraError() {
        when(mockView.getDNI_NFI()).thenReturn("12345678A");
        when(mockView.getNombre()).thenReturn("");

        presenter.onModifyClientClick("12345678A");

        verify(mockView).showError("El nombre y el dni o nif son OBLIGATORIOS.");
        verify(mockRepository, never()).actualizarCliente(any(), anyString());
    }

    @Test
    @DisplayName("Nombre null → error y repositorio NO es llamado")
    void nombreNull_muestraError() {
        when(mockView.getDNI_NFI()).thenReturn("12345678A");
        when(mockView.getNombre()).thenReturn(null);

        presenter.onModifyClientClick("12345678A");

        verify(mockView).showError("El nombre y el dni o nif son OBLIGATORIOS.");
    }

    // ── btnOff se llama siempre al inicio ──────────────────────────────

    @Test
    @DisplayName("btnOff(true) se llama siempre al inicio de onModifyClientClick")
    void btnOff_siempreLlamadoAlInicio() {
        when(mockView.getDNI_NFI()).thenReturn("");
        when(mockView.getNombre()).thenReturn("Juan");

        presenter.onModifyClientClick("anterior");

        verify(mockView, times(1)).btnOff(true);
    }

    // ── Validación correo ──────────────────────────────────────────────

    @ParameterizedTest(name = "correo = \"{0}\"")
    @ValueSource(strings = {"sinArroba", "@sinUsuario.es", "doble@@dom.com", "sin.punto@dom"})
    @DisplayName("Correo inválido → error y repositorio NO es llamado")
    void correoInvalido_muestraError(String correo) {
        when(mockView.getDNI_NFI()).thenReturn("12345678A");
        when(mockView.getNombre()).thenReturn("Juan");
        when(mockView.getApellido()).thenReturn("García");
        when(mockView.getCorreo()).thenReturn(correo);

        presenter.onModifyClientClick("12345678A");

        verify(mockView).showError("El correo no tiene formato de correo.");
        verify(mockRepository, never()).actualizarCliente(any(), anyString());
    }

    // ── Validación teléfono ────────────────────────────────────────────

    @ParameterizedTest(name = "teléfono = \"{0}\"")
    @ValueSource(strings = {"912345678", "123456789", "6123456", "61234567890", "abc456789"})
    @DisplayName("Teléfono inválido → error y repositorio NO es llamado")
    void telefonoInvalido_muestraError(String telefono) {
        when(mockView.getDNI_NFI()).thenReturn("12345678A");
        when(mockView.getNombre()).thenReturn("Juan");
        when(mockView.getApellido()).thenReturn(null);
        when(mockView.getCorreo()).thenReturn(null);
        when(mockView.getTelefono()).thenReturn(telefono);

        presenter.onModifyClientClick("12345678A");

        verify(mockView).showError("El telefono no tiene formato de telefono.");
        verify(mockRepository, never()).actualizarCliente(any(), anyString());
    }

    // ── Flujo completo exitoso ─────────────────────────────────────────

    @Test
    @DisplayName("Datos completos y válidos → repositorio es llamado con dninifanterior correcto")
    void datosValidos_repositorioLlamadoConDniAnterior() throws InterruptedException {
        when(mockView.getDNI_NFI()).thenReturn("12345678B");
        when(mockView.getNombre()).thenReturn("María");
        when(mockView.getApellido()).thenReturn("Sánchez");
        when(mockView.getCorreo()).thenReturn("maria@empresa.es");
        when(mockView.getTelefono()).thenReturn("698000111");
        when(mockView.getActivo()).thenReturn(true);
        when(mockRepository.actualizarCliente(any(Cliente.class), eq("12345678A")))
                .thenReturn(true);

        presenter.onModifyClientClick("12345678A");

        Thread.sleep(300);

        verify(mockRepository, times(1))
                .actualizarCliente(any(Cliente.class), eq("12345678A"));
    }

    @Test
    @DisplayName("getActivo() false → activo=0 en el cliente construido")
    void getActivoFalse_clienteInactivo() throws InterruptedException {
        when(mockView.getDNI_NFI()).thenReturn("12345678A");
        when(mockView.getNombre()).thenReturn("Carlos");
        when(mockView.getApellido()).thenReturn(null);
        when(mockView.getCorreo()).thenReturn(null);
        when(mockView.getTelefono()).thenReturn(null);
        when(mockView.getActivo()).thenReturn(false);

        ArgumentCaptor<Cliente> captor = ArgumentCaptor.forClass(Cliente.class);
        when(mockRepository.actualizarCliente(captor.capture(), anyString()))
                .thenReturn(true);

        presenter.onModifyClientClick("12345678A");
        Thread.sleep(300);

        assertEquals(0, captor.getValue().getActivo(),
                "getActivo()=false debe traducirse a activo=0 en el Cliente");
    }
}

