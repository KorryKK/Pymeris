package angel.tfg.pymeris.Views.ManageProducts.ManageProductsInactivo;

import angel.tfg.pymeris.Model.Entities.Producto;
import angel.tfg.pymeris.Model.Repository.Productos.ProductosRepository;
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
@DisplayName("ManageProductInactivoPresenter — gestión de productos inactivos")
class ManageProductInactivoPresenterTest {

    @Mock private MaPrInContract.View    mockView;
    @Mock private ProductosRepository    mockRepository;

    private ManageProductInactivoPresenter presenter;

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
        presenter = new ManageProductInactivoPresenter(mockView, mockRepository);
    }

    // ── getRepository ──────────────────────────────────────────────────

    @Test
    @DisplayName("getRepository devuelve la instancia inyectada")
    void getRepository_devuelveInstanciaInyectada() {
        assertSame(mockRepository, presenter.getRepository());
    }

    // ── cargarProductos ────────────────────────────────────────────────

    @Test
    @DisplayName("cargarProductos llama a mostrarCarga(true) inmediatamente")
    void cargarProductos_llamaMostrarCargaTrue() {
        presenter.cargarProductos();
        verify(mockView, times(1)).mostrarCarga(true);
    }

    @Test
    @DisplayName("cargarProductos llama al repositorio de inactivos (no al de activos)")
    void cargarProductos_llamaMetodoInactivos() throws InterruptedException {
        when(mockRepository.obtenerTodosLosInactivos()).thenReturn(java.util.Collections.emptyList());

        presenter.cargarProductos();
        Thread.sleep(300);

        verify(mockRepository, times(1)).obtenerTodosLosInactivos();
        verify(mockRepository, never()).obtenerTodosLosProductos();
    }

    // ── onUpClick ──────────────────────────────────────────────────────

    @Test
    @DisplayName("onUpClick llama a mostrarCarga(true) inmediatamente")
    void onUpClick_llamaMostrarCargaTrue() {
        Producto p = buildProducto(5);

        presenter.onUpClick(p);

        verify(mockView, times(1)).mostrarCarga(true);
    }

    @Test
    @DisplayName("onUpClick llama al repositorio con el id correcto para dar de alta")
    void onUpClick_llamaRepositorioConIdCorrecto() throws InterruptedException {
        Producto p = buildProducto(8);
        when(mockRepository.uploadProducto(8)).thenReturn(true);

        presenter.onUpClick(p);
        Thread.sleep(300);

        verify(mockRepository, times(1)).uploadProducto(8);
    }

    @Test
    @DisplayName("onUpClick exitoso → repositorio llamado y no uploadProducto de otro id")
    void onUpClick_exitoso_noLlamaOtroId() throws InterruptedException {
        Producto p = buildProducto(12);
        when(mockRepository.uploadProducto(12)).thenReturn(true);

        presenter.onUpClick(p);
        Thread.sleep(300);

        verify(mockRepository, never()).uploadProducto(99);
    }

    @Test
    @DisplayName("onUpClick fallido → repositorio llamado igual")
    void onUpClick_fallido_repositorioLlamado() throws InterruptedException {
        Producto p = buildProducto(2);
        when(mockRepository.uploadProducto(2)).thenReturn(false);

        presenter.onUpClick(p);
        Thread.sleep(300);

        verify(mockRepository, times(1)).uploadProducto(2);
    }

    @Test
    @DisplayName("onUpClick NO llama a downloadProducto (operación contraria)")
    void onUpClick_noLlamaDownload() throws InterruptedException {
        Producto p = buildProducto(3);
        when(mockRepository.uploadProducto(anyInt())).thenReturn(true);

        presenter.onUpClick(p);
        Thread.sleep(300);

        verify(mockRepository, never()).downloadProducto(anyInt());
    }

    // ── Helper ────────────────────────────────────────────────────────

    private Producto buildProducto(int id) {
        Producto p = new Producto();
        p.setId_Producto(id);
        p.setNombre("Inactivo-" + id);
        return p;
    }
}
