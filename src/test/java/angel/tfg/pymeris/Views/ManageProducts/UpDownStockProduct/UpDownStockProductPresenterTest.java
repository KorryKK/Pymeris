package angel.tfg.pymeris.Views.ManageProducts.UpDownStockProduct;

import angel.tfg.pymeris.Model.Repository.Productos.ProductosRepository;
import angel.tfg.pymeris.Views.ManageProducts.MaPrContract;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UpDownStockProductPresenter — ajuste de stock")
class UpDownStockProductPresenterTest {

    @Mock private UpDoStPrContract.View  mockView;
    @Mock private ProductosRepository    mockRepository;
    @Mock private MaPrContract.Presenter mockManagePresenter;

    private UpDownStockProductPresenter presenter;

    @BeforeEach
    void setUp() {
        presenter = new UpDownStockProductPresenter(mockView, mockRepository, mockManagePresenter);
    }

    // ── Cantidad no numérica ───────────────────────────────────────────

    @ParameterizedTest(name = "cantidad = \"{0}\"")
    @ValueSource(strings = {"abc", "", "doce", "1.5", "1,5", "3 unidades"})
    @DisplayName("Cantidad no numérica → showStatus con false y repositorio NO llamado")
    void cantidadNoNumerica_muestraError(String cantidad) {
        when(mockView.getCantidad()).thenReturn(cantidad);

        presenter.onModifyStockClick();

        verify(mockView).showStatus(anyString(), eq(false));
        verify(mockRepository, never()).modificarStock(anyInt(), anyInt());
    }

    // ── Entrada de stock ───────────────────────────────────────────────

    @Test
    @DisplayName("Entrada de 5 unidades → modificarStock llamado con +5")
    void entrada_llamaRepositorioConPositivo() throws InterruptedException {
        when(mockView.getCantidad()).thenReturn("5");
        when(mockView.getSeleccionado()).thenReturn("Entrada");
        when(mockView.getIdProducto()).thenReturn(10);
        when(mockRepository.modificarStock(10, 5)).thenReturn(true);

        presenter.onModifyStockClick();
        Thread.sleep(300);

        verify(mockRepository, times(1)).modificarStock(10, 5);
    }

    @Test
    @DisplayName("Entrada de 1 unidad → modificarStock con ajuste +1")
    void entrada_unaUnidad_ajustePositivo() throws InterruptedException {
        when(mockView.getCantidad()).thenReturn("1");
        when(mockView.getSeleccionado()).thenReturn("Entrada");
        when(mockView.getIdProducto()).thenReturn(3);
        when(mockRepository.modificarStock(3, 1)).thenReturn(true);

        presenter.onModifyStockClick();
        Thread.sleep(300);

        verify(mockRepository).modificarStock(3, 1);
    }

    // ── Salida de stock ────────────────────────────────────────────────

    @Test
    @DisplayName("Salida de 3 unidades → modificarStock llamado con -3")
    void salida_llamaRepositorioConNegativo() throws InterruptedException {
        when(mockView.getCantidad()).thenReturn("3");
        when(mockView.getSeleccionado()).thenReturn("Salida");
        when(mockView.getIdProducto()).thenReturn(7);
        when(mockRepository.modificarStock(7, -3)).thenReturn(true);

        presenter.onModifyStockClick();
        Thread.sleep(300);

        verify(mockRepository, times(1)).modificarStock(7, -3);
    }

    @Test
    @DisplayName("Salida de 10 unidades → modificarStock con ajuste -10")
    void salida_diezUnidades_ajusteNegativo() throws InterruptedException {
        when(mockView.getCantidad()).thenReturn("10");
        when(mockView.getSeleccionado()).thenReturn("Salida");
        when(mockView.getIdProducto()).thenReturn(2);
        when(mockRepository.modificarStock(2, -10)).thenReturn(true);

        presenter.onModifyStockClick();
        Thread.sleep(300);

        verify(mockRepository).modificarStock(2, -10);
    }

    // ── Signo del ajuste ───────────────────────────────────────────────

    @Test
    @DisplayName("Selección distinta de 'Entrada' se trata como Salida (valor negativo)")
    void seleccionNoEntrada_tratadaComoSalida() throws InterruptedException {
        when(mockView.getCantidad()).thenReturn("4");
        when(mockView.getSeleccionado()).thenReturn("Salida");
        when(mockView.getIdProducto()).thenReturn(1);
        when(mockRepository.modificarStock(1, -4)).thenReturn(true);

        presenter.onModifyStockClick();
        Thread.sleep(300);

        verify(mockRepository).modificarStock(1, -4);
    }

    @Test
    @DisplayName("Selección 'Entrada' (case sensitive) produce ajuste positivo")
    void seleccionEntradaExacta_ajustePositivo() throws InterruptedException {
        when(mockView.getCantidad()).thenReturn("6");
        when(mockView.getSeleccionado()).thenReturn("Entrada");
        when(mockView.getIdProducto()).thenReturn(9);
        when(mockRepository.modificarStock(9, 6)).thenReturn(true);

        presenter.onModifyStockClick();
        Thread.sleep(300);

        verify(mockRepository).modificarStock(9, 6);
    }

    // ── Resultado de la operación ──────────────────────────────────────

    @Test
    @DisplayName("modificarStock fallido → managepresenter.cargarProductos() NO es llamado")
    void modificarStockFallido_noLlamaCargarProductos() throws InterruptedException {
        when(mockView.getCantidad()).thenReturn("3");
        when(mockView.getSeleccionado()).thenReturn("Entrada");
        when(mockView.getIdProducto()).thenReturn(4);
        when(mockRepository.modificarStock(4, 3)).thenReturn(false);

        presenter.onModifyStockClick();
        Thread.sleep(300);

        verify(mockManagePresenter, never()).cargarProductos();
    }
}
