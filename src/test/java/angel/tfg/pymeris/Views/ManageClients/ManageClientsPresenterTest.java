package angel.tfg.pymeris.Views.ManageClients;

import angel.tfg.pymeris.Model.Repository.Clientes.ClientesRepository;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ManageClientsPresenter — gestión de clientes")
class ManageClientsPresenterTest {

    @Mock private MaClContract.View   mockView;
    @Mock private ClientesRepository  mockRepository;

    private ManageClientsPresenter presenter;

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
        presenter = new ManageClientsPresenter(mockView, mockRepository);
    }

    // ── cargarClientes ─────────────────────────────────────────────────

    @Test
    @DisplayName("cargarClientes llama a mostrarCarga(true) inmediatamente")
    void cargarClientes_llamaMostrarCargaTrue() {
        presenter.cargarClientes();
        verify(mockView, times(1)).mostrarCarga(true);
    }

    @Test
    @DisplayName("cargarClientes llama al repositorio en segundo plano")
    void cargarClientes_llamaRepositorio() throws InterruptedException {
        when(mockRepository.obtenerTodosLosClientes()).thenReturn(Collections.emptyList());

        presenter.cargarClientes();
        Thread.sleep(300);

        verify(mockRepository, times(1)).obtenerTodosLosClientes();
    }

    @Test
    @DisplayName("cargarClientes llama mostrarClientes con la lista devuelta por el repositorio")
    void cargarClientes_llamaMostrarClientes() throws InterruptedException {
        when(mockRepository.obtenerTodosLosClientes()).thenReturn(Collections.emptyList());

        presenter.cargarClientes();
        Thread.sleep(300);

        verify(mockView).mostrarClientes(Collections.emptyList());
    }

    @Test
    @DisplayName("cargarClientes oculta la carga (false) tras obtener la lista")
    void cargarClientes_ocultaCargaTrasObtenerLista() throws InterruptedException {
        when(mockRepository.obtenerTodosLosClientes()).thenReturn(Collections.emptyList());

        presenter.cargarClientes();
        Thread.sleep(300);

        verify(mockView).mostrarCarga(false);
    }

    @Test
    @DisplayName("cargarClientes sigue ocultando carga incluso si el repositorio lanza excepción")
    void cargarClientes_excepcionRepositorio_ocultaCarga() throws InterruptedException {
        when(mockRepository.obtenerTodosLosClientes()).thenThrow(new RuntimeException("BD caída"));

        presenter.cargarClientes();
        Thread.sleep(300);

        // Debe haber llamado mostrarCarga(false) en el bloque catch
        verify(mockView, atLeastOnce()).mostrarCarga(false);
    }
}
