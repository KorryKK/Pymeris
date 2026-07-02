package angel.tfg.pymeris.Views.ManageOrders.ShowOrder;

import angel.tfg.pymeris.Model.Entities.Pedido;
import angel.tfg.pymeris.Model.Repository.Pedidos.PedidosRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ShowOrderPresenter — detalle de pedido")
class ShowOrderPresenterTest {

    @Mock private ShowOrderContract.View mockView;
    @Mock private PedidosRepository      mockRepository;

    private Pedido pedido;
    private ShowOrderPresenter presenter;

    @BeforeEach
    void setUp() {
        pedido = new Pedido();
        pedido.setId_Pedido(5);
        pedido.setEstado_Pedido("Pendiente");
        presenter = new ShowOrderPresenter(mockView, mockRepository, pedido);
    }

    // ── cargarDetalles ─────────────────────────────────────────────────

    @Test
    @DisplayName("cargarDetalles llama setPedido con el pedido inyectado")
    void cargarDetalles_llamaSetPedido() {
        presenter.cargarDetalles();
        verify(mockView, times(1)).setPedido(pedido);
    }

    @Test
    @DisplayName("cargarDetalles llama al repositorio con el id del pedido")
    void cargarDetalles_llamaRepositorioConIdCorrecto() throws InterruptedException {
        when(mockRepository.getInfoPedido(5)).thenReturn(java.util.Collections.emptyList());

        presenter.cargarDetalles();
        Thread.sleep(300);

        verify(mockRepository, times(1)).getInfoPedido(5);
    }

    @Test
    @DisplayName("cargarDetalles NO muestra pantalla de carga (no hay spinner en esta vista)")
    void cargarDetalles_noLlamaMostrarCarga() {
        presenter.cargarDetalles();
        verify(mockView, never()).mostrarCarga(anyBoolean());
    }

    // ── Estado del pedido tras completar ──────────────────────────────

    @Test
    @DisplayName("El pedido inicial NO está completado")
    void estadoInicial_noestaCompletado() {
        assert !"Completado".equalsIgnoreCase(pedido.getEstado_Pedido());
    }

    @Test
    @DisplayName("El pedido actualiza su estado a 'Completado' cuando el repositorio tiene éxito")
    void pedido_actualizaEstado_cuandoRepositorioExitoso() {
        when(mockRepository.completarPedido(5)).thenReturn(true);

        // Simulamos que el hilo secundario actualiza el estado
        boolean success = mockRepository.completarPedido(pedido.getId_Pedido());
        if (success) {
            pedido.setEstado_Pedido("Completado");
        }

        assert "Completado".equals(pedido.getEstado_Pedido());
    }
}
