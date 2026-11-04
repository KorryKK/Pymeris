package angel.tfg.pymeris.Views.ManageProducts;

import angel.tfg.pymeris.Model.Entities.Producto;
import angel.tfg.pymeris.Model.Repository.Productos.ProductosRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ManageProductsPresenter — listado de productos activos")
class ManageProductsPresenterTest {

    @Mock private MaPrContract.View      mockView;
    @Mock private ProductosRepository    mockRepository;

    private ManageProductsPresenter presenter;

    @BeforeEach
    void setUp() {
        presenter = new ManageProductsPresenter(mockView, mockRepository);
    }

    // ── getRepository ──────────────────────────────────────────────────

    @Test
    @DisplayName("getRepository devuelve la instancia inyectada")
    void getRepository_devuelveInstanciaInyectada() {
        assertSame(mockRepository, presenter.getRepository());
    }

    // ── cargarProductos ────────────────────────────────────────────────

    @Test
    @DisplayName("cargarProductos llama a mostrarCarga(true) de forma inmediata")
    void cargarProductos_llamaMostrarCargaTrue() {
        presenter.cargarProductos();
        verify(mockView, times(1)).mostrarCarga(true);
    }

    // ── onDownClick ────────────────────────────────────────────────────

    @Test
    @DisplayName("onDownClick llama mostrarCarga(true) inmediatamente")
    void onDownClick_llamaMostrarCargaTrue() {
        Producto p = buildProducto(10);

        presenter.onDownClick(p);

        verify(mockView, times(1)).mostrarCarga(true);
    }

    @Test
    @DisplayName("onDownClick llama al repositorio con el id correcto del producto")
    void onDownClick_llamaRepositorioConIdCorrecto() throws InterruptedException {
        Producto p = buildProducto(7);
        when(mockRepository.downloadProducto(7)).thenReturn(true);

        presenter.onDownClick(p);
        Thread.sleep(300);

        verify(mockRepository, times(1)).downloadProducto(7);
    }

    @Test
    @DisplayName("onDownClick fallido → repositorio llamado igual")
    void onDownClick_fallido_repositorioLlamado() throws InterruptedException {
        Producto p = buildProducto(3);
        when(mockRepository.downloadProducto(3)).thenReturn(false);

        presenter.onDownClick(p);
        Thread.sleep(300);

        verify(mockRepository, times(1)).downloadProducto(3);
    }

    // ── Helper ────────────────────────────────────────────────────────

    private Producto buildProducto(int id) {
        Producto p = new Producto();
        p.setId_Producto(id);
        p.setNombre("Producto-" + id);
        return p;
    }
}
