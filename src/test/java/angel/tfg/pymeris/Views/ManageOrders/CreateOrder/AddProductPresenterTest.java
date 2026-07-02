package angel.tfg.pymeris.Views.ManageOrders.CreateOrder;

import angel.tfg.pymeris.Model.Entities.Producto;
import angel.tfg.pymeris.Model.Repository.Pedidos.PedidosRepository;
import angel.tfg.pymeris.Views.ManageOrders.CreateOrder.AddProduct.AddProductContract;
import angel.tfg.pymeris.Views.ManageOrders.CreateOrder.AddProduct.AddProductPresenter;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.AdditionalMatchers.lt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AddProductPresenter — Añadir producto a pedido")
class AddProductPresenterTest {

    @Mock private AddProductContract.View    mockView;
    @Mock private PedidosRepository          mockRepository;
    @Mock private NewOrderContract.Presenter mockNewPresenter;

    private AddProductPresenter presenter;

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
        presenter = new AddProductPresenter(mockView, mockRepository, mockNewPresenter);
    }

    // ── cargarProductos ────────────────────────────────────────────────

    @Test
    @DisplayName("cargarProductos llama a listarProductos() del repositorio")
    void cargarProductos_llamaRepositorio() throws InterruptedException {
        when(mockRepository.listarProductos()).thenReturn(Collections.emptyList());

        presenter.cargarProductos();
        Thread.sleep(300);

        verify(mockRepository, times(1)).listarProductos();
    }

    @Test
    @DisplayName("cargarProductos pasa la lista al controlador mediante mostrarProductos")
    void cargarProductos_llamaMostrarProductos() throws InterruptedException {
        when(mockRepository.listarProductos()).thenReturn(Collections.emptyList());

        presenter.cargarProductos();
        Thread.sleep(300);

        verify(mockView).mostrarProductos(Collections.emptyList());
    }

    @Test
    @DisplayName("cargarProductos ante excepción del repositorio no propaga el error")
    void cargarProductos_excepcion_noPropaga() throws InterruptedException {
        when(mockRepository.listarProductos()).thenThrow(new RuntimeException("Error BD"));

        presenter.cargarProductos();
        Thread.sleep(300);

        verify(mockView, never()).mostrarProductos(any());
    }

    // ── onAddProductClick — validaciones síncronas ─────────────────────

    @Test
    @DisplayName("Cantidad 0 → showStatus con mensaje y repositorio NO llamado")
    void cantidad_cero_muestraMensaje() {
        Producto p = buildProducto(1, 10);
        when(mockView.getCantidad()).thenReturn(0);

        presenter.onAddProductClick(p);

        verify(mockView).showStatus("Ingresa un cantidad de productos.");
        verify(mockRepository, never()).stockPedidoAjuste(anyInt(), anyInt());
    }

    @Test
    @DisplayName("Cantidad negativa → showStatus con mensaje y repositorio NO llamado")
    void cantidad_negativa_muestraMensaje() {
        Producto p = buildProducto(2, 10);
        when(mockView.getCantidad()).thenReturn(-3);

        presenter.onAddProductClick(p);

        verify(mockView).showStatus("La cantidad debe ser mayor a cero.");
        verify(mockRepository, never()).stockPedidoAjuste(anyInt(), anyInt());
    }

    @Test
    @DisplayName("Cantidad mayor que el stock disponible → showStatus con mensaje y repositorio NO llamado")
    void cantidad_mayorQueStock_muestraMensaje() {
        Producto p = buildProducto(3, 5);   // stock = 5
        when(mockView.getCantidad()).thenReturn(6);  // pide 6

        presenter.onAddProductClick(p);

        verify(mockView).showStatus("La cantidad que quieres añadir no está disponible.");
        verify(mockRepository, never()).stockPedidoAjuste(anyInt(), anyInt());
    }

    @Test
    @DisplayName("Cantidad igual al stock disponible → pasa la validación y llama al repositorio")
    void cantidad_igualAlStock_llama_repositorio() throws InterruptedException {
        Producto p = buildProducto(4, 3);   // stock = 3
        when(mockView.getCantidad()).thenReturn(3);  // pide exactamente 3
        when(mockRepository.stockPedidoAjuste(4, -3)).thenReturn(true);

        presenter.onAddProductClick(p);
        Thread.sleep(300);

        verify(mockRepository, times(1)).stockPedidoAjuste(4, -3);
    }

    // ── onAddProductClick — lógica asíncrona ──────────────────────────

    @Test
    @DisplayName("Operación exitosa → anadirProducto es llamado en el newPresenter")
    void operacionExitosa_llamaAnadirProducto() throws InterruptedException {
        Producto p = buildProducto(5, 20);
        when(mockView.getCantidad()).thenReturn(4);
        when(mockRepository.stockPedidoAjuste(5, -4)).thenReturn(true);

        presenter.onAddProductClick(p);
        Thread.sleep(300);

        verify(mockNewPresenter, times(1)).anadirProducto(p, 4);
    }

    @Test
    @DisplayName("Operación exitosa → cargarProductos() se llama de nuevo (refresco lista)")
    void operacionExitosa_refrescaProductos() throws InterruptedException {
        Producto p = buildProducto(6, 10);
        when(mockView.getCantidad()).thenReturn(2);
        when(mockRepository.stockPedidoAjuste(6, -2)).thenReturn(true);
        when(mockRepository.listarProductos()).thenReturn(Collections.emptyList());

        presenter.onAddProductClick(p);
        Thread.sleep(500);

        // listarProductos debe llamarse al menos una vez como parte del refresco
        verify(mockRepository, atLeastOnce()).listarProductos();
    }

    @Test
    @DisplayName("Operación fallida → anadirProducto NO es llamado")
    void operacionFallida_noLlamaAnadirProducto() throws InterruptedException {
        Producto p = buildProducto(7, 15);
        when(mockView.getCantidad()).thenReturn(5);
        when(mockRepository.stockPedidoAjuste(7, -5)).thenReturn(false);

        presenter.onAddProductClick(p);
        Thread.sleep(300);

        verify(mockNewPresenter, never()).anadirProducto(any(), anyInt());
    }

    @Test
    @DisplayName("El ajuste de stock es siempre negativo (se resta del stock)")
    void ajusteStock_siempreNegativo() throws InterruptedException {
        Producto p = buildProducto(8, 100);
        when(mockView.getCantidad()).thenReturn(7);
        when(mockRepository.stockPedidoAjuste(8, -7)).thenReturn(true);

        presenter.onAddProductClick(p);
        Thread.sleep(300);

        // Verifica que NUNCA se llama con valor positivo
        verify(mockRepository, timeout(500)).stockPedidoAjuste(anyInt(), lt(0));
        verify(mockRepository).stockPedidoAjuste(8, -7);
    }

    @Test
    @DisplayName("Cantidad 0 activa mostrarCargaView(true) y luego (false)")
    void cantidad_cero_activaYDesactivaCarga() {
        Producto p = buildProducto(9, 5);
        when(mockView.getCantidad()).thenReturn(0);

        presenter.onAddProductClick(p);

        verify(mockNewPresenter).mostrarCargaView(true);
        verify(mockNewPresenter).mostrarCargaView(false);
    }

    // ── Helper ────────────────────────────────────────────────────────

    private Producto buildProducto(int id, int stock) {
        Producto p = new Producto();
        p.setId_Producto(id);
        p.setNombre("Producto-" + id);
        p.setPrecioUnitario(new BigDecimal("10.00"));
        p.setStock_Actual(stock);
        return p;
    }
}
