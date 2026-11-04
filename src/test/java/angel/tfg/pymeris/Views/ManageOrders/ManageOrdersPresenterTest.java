package angel.tfg.pymeris.Views.ManageOrders;

import angel.tfg.pymeris.Model.Entities.Pedido;
import angel.tfg.pymeris.Model.Repository.Pedidos.PedidosRepository;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ManageOrdersPresenter — gestión de pedidos")
class ManageOrdersPresenterTest {

    @Mock private MaOrContract.View    mockView;
    @Mock private PedidosRepository    mockRepository;

    private ManageOrdersPresenter presenter;

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
        presenter = new ManageOrdersPresenter(mockView, mockRepository);
    }

    // ── getRepository ──────────────────────────────────────────────────

    @Test
    @DisplayName("getRepository devuelve la instancia inyectada")
    void getRepository_devuelveInstanciaInyectada() {
        assertSame(mockRepository, presenter.getRepository());
    }

    // ── cargarPedidos ──────────────────────────────────────────────────

    @Test
    @DisplayName("cargarPedidos llama a mostrarCarga(true) de forma inmediata")
    void cargarPedidos_llamaMostrarCargaTrue() {
        presenter.cargarPedidos();
        verify(mockView, times(1)).mostrarCarga(true);
    }

    @Test
    @DisplayName("onCompletarClick con pedido 'COMPLETADO' (mayúsculas) también lo detecta")
    void onCompletarClick_pedidoCompletadoMayusculas_noLlamaRepositorio() {
        Pedido pedido = buildPedido(5, "COMPLETADO");

        presenter.onCompletarClick(pedido);

        verify(mockRepository, never()).completarPedido(anyInt());
    }

    @Test
    @DisplayName("onCompletarClick exitoso llama al repositorio con el id correcto del pedido")
    void onCompletarClick_exitoso_llamaRepositorioConIdCorrecto() throws InterruptedException {
        Pedido pedido = buildPedido(42, "En proceso");
        when(mockRepository.completarPedido(42)).thenReturn(true);

        presenter.onCompletarClick(pedido);
        Thread.sleep(300);

        verify(mockRepository).completarPedido(42);
    }

    // ── onDeleteClick ──────────────────────────────────────────────────

    @Test
    @DisplayName("onDeleteClick no lanza excepción (implementación vacía)")
    void onDeleteClick_noLanzaExcepcion() {
        Pedido pedido = buildPedido(1, "Pendiente");
        assertDoesNotThrow(() -> presenter.onDeleteClick(pedido));
    }

    // ── Helpers ───────────────────────────────────────────────────────

    private Pedido buildPedido(int id, String estado) {
        Pedido p = new Pedido();
        p.setId_Pedido(id);
        p.setEstado_Pedido(estado);
        return p;
    }
}
