package angel.tfg.pymeris.Views.ManageProducts.ModifyProduct;

import angel.tfg.pymeris.Model.Entities.Producto;
import angel.tfg.pymeris.Model.Repository.Productos.ProductosRepository;
import angel.tfg.pymeris.Views.ManageProducts.MaPrContract;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ModifyProductPresenter — modificación de producto")
class ModifyProductPresenterTest {

    @Mock private MoPrContract.View      mockView;
    @Mock private ProductosRepository    mockRepository;
    @Mock private MaPrContract.Presenter mockManagePresenter;

    private ModifyProductPresenter presenter;

    @BeforeEach
    void setUp() {
        presenter = new ModifyProductPresenter(mockView, mockRepository, mockManagePresenter);
    }

    // ── Campos obligatorios ────────────────────────────────────────────

    @Test
    @DisplayName("Nombre vacío → showModifyStatus con false y repositorio NO llamado")
    void nombreVacio_muestraError() {
        configurarVistaValida();
        when(mockView.getNombre()).thenReturn("");

        presenter.onModifyProductClick();

        verify(mockView).showModifyStatus(anyString(), eq(false));
        verify(mockRepository, never()).actualizarProducto(any());
    }

    @Test
    @DisplayName("Categoría vacía → showModifyStatus con false")
    void categoriaVacia_muestraError() {
        configurarVistaValida();
        when(mockView.getCategoria()).thenReturn("");

        presenter.onModifyProductClick();

        verify(mockView).showModifyStatus(anyString(), eq(false));
    }

    @Test
    @DisplayName("Descripción vacía → showModifyStatus con false")
    void descripcionVacia_muestraError() {
        configurarVistaValida();
        when(mockView.getDescripcion()).thenReturn("");

        presenter.onModifyProductClick();

        verify(mockView).showModifyStatus(anyString(), eq(false));
    }

    @Test
    @DisplayName("Precio vacío → showModifyStatus con false")
    void precioVacio_muestraError() {
        configurarVistaValida();
        when(mockView.getPrecio()).thenReturn("");

        presenter.onModifyProductClick();

        verify(mockView).showModifyStatus(anyString(), eq(false));
    }

    @Test
    @DisplayName("IVA vacío → showModifyStatus con false")
    void ivaVacio_muestraError() {
        configurarVistaValida();
        when(mockView.getIVA()).thenReturn("");

        presenter.onModifyProductClick();

        verify(mockView).showModifyStatus(anyString(), eq(false));
    }

    @Test
    @DisplayName("Stock vacío → showModifyStatus con false")
    void stockVacio_muestraError() {
        configurarVistaValida();
        when(mockView.getStock()).thenReturn("");

        presenter.onModifyProductClick();

        verify(mockView).showModifyStatus(anyString(), eq(false));
    }

    // ── Validación precio ──────────────────────────────────────────────

    @ParameterizedTest(name = "precio = \"{0}\"")
    @ValueSource(strings = {"abc", "12.3.4", "precio", "€5"})
    @DisplayName("Precio no numérico → showModifyStatus con false")
    void precioNoNumerico_muestraError(String precio) {
        configurarVistaValida();
        when(mockView.getPrecio()).thenReturn(precio);

        presenter.onModifyProductClick();

        verify(mockView).showModifyStatus(anyString(), eq(false));
        verify(mockRepository, never()).actualizarProducto(any());
    }

    @Test
    @DisplayName("Precio con más de 2 decimales → error de decimales")
    void precioMasDe2Decimales_muestraError() {
        configurarVistaValida();
        when(mockView.getPrecio()).thenReturn("10.999");

        presenter.onModifyProductClick();

        verify(mockView).showModifyStatus(contains("decimales"), eq(false));
    }

    // ── Validación IVA ─────────────────────────────────────────────────

    @Test
    @DisplayName("IVA negativo → showModifyStatus con false")
    void ivaNegativo_muestraError() {
        configurarVistaValida();
        when(mockView.getIVA()).thenReturn("-1");

        presenter.onModifyProductClick();

        verify(mockView).showModifyStatus(contains("IVA"), eq(false));
    }

    @Test
    @DisplayName("IVA mayor de 100 → showModifyStatus con false")
    void ivaMayorQue100_muestraError() {
        configurarVistaValida();
        when(mockView.getIVA()).thenReturn("101");

        presenter.onModifyProductClick();

        verify(mockView).showModifyStatus(contains("IVA"), eq(false));
    }

    @Test
    @DisplayName("IVA no numérico → error de formato")
    void ivaNoNumerico_muestraError() {
        configurarVistaValida();
        when(mockView.getIVA()).thenReturn("veintiuno");

        presenter.onModifyProductClick();

        verify(mockView).showModifyStatus(anyString(), eq(false));
    }

    // ── Validación stock ───────────────────────────────────────────────

    @Test
    @DisplayName("Stock no numérico → error de formato")
    void stockNoNumerico_muestraError() {
        configurarVistaValida();
        when(mockView.getStock()).thenReturn("muchos");

        presenter.onModifyProductClick();

        verify(mockView).showModifyStatus(anyString(), eq(false));
        verify(mockRepository, never()).actualizarProducto(any());
    }

    // ── Precio con coma ────────────────────────────────────────────────

    @Test
    @DisplayName("Precio con coma se normaliza a BigDecimal con punto")
    void precioConComa_normalizadoCorrectamente() throws InterruptedException {
        configurarVistaValida();
        when(mockView.getPrecio()).thenReturn("49,99");
        ArgumentCaptor<Producto> captor = ArgumentCaptor.forClass(Producto.class);
        when(mockRepository.actualizarProducto(captor.capture())).thenReturn(true);

        presenter.onModifyProductClick();
        Thread.sleep(300);

        assertEquals(new BigDecimal("49.99"), captor.getValue().getPrecioUnitario());
    }

    // ── Flujo exitoso ──────────────────────────────────────────────────

    @Test
    @DisplayName("Datos válidos → repositorio llamado con el producto correcto")
    void datosValidos_repositorioLlamadoConProductoCorrecto() throws InterruptedException {
        configurarVistaValida();
        ArgumentCaptor<Producto> captor = ArgumentCaptor.forClass(Producto.class);
        when(mockRepository.actualizarProducto(captor.capture())).thenReturn(true);

        presenter.onModifyProductClick();
        Thread.sleep(300);

        Producto p = captor.getValue();
        assertAll(
            () -> assertEquals(1,                       p.getId_Producto()),
            () -> assertEquals("Monitor",               p.getNombre()),
            () -> assertEquals("Pantallas",             p.getCategoria()),
            () -> assertEquals("Monitor Full HD",       p.getDescripcion()),
            () -> assertEquals(new BigDecimal("199.99"),p.getPrecioUnitario()),
            () -> assertEquals(21,                      p.getIVA()),
            () -> assertEquals(10,                      p.getStock_Actual())
        );
    }

    // ── IVA límites válidos ────────────────────────────────────────────

    @ParameterizedTest(name = "IVA = {0}")
    @ValueSource(strings = {"0", "4", "10", "21", "100"})
    @DisplayName("IVA en rango [0,100] → repositorio es llamado")
    void ivaEnRango_repositorioLlamado(String iva) throws InterruptedException {
        configurarVistaValida();
        when(mockView.getIVA()).thenReturn(iva);
        when(mockRepository.actualizarProducto(any())).thenReturn(true);

        presenter.onModifyProductClick();
        Thread.sleep(300);

        verify(mockRepository, times(1)).actualizarProducto(any());
    }

    // ── Helper ────────────────────────────────────────────────────────

    private void configurarVistaValida() {
        when(mockView.getID()).thenReturn("1");
        when(mockView.getNombre()).thenReturn("Monitor");
        when(mockView.getCategoria()).thenReturn("Pantallas");
        when(mockView.getDescripcion()).thenReturn("Monitor Full HD");
        when(mockView.getPrecio()).thenReturn("199.99");
        when(mockView.getIVA()).thenReturn("21");
        when(mockView.getStock()).thenReturn("10");
    }
}
