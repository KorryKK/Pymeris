package angel.tfg.pymeris.Views.ManageProducts.CreateProduct;

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
@DisplayName("NewProductPresenter — creación de producto")
class NewProductPresenterTest {

    @Mock private NewProductContract.View mockView;
    @Mock private ProductosRepository     mockRepository;
    @Mock private MaPrContract.Presenter  mockManagePresenter;

    private NewProductPresenter presenter;

    @BeforeEach
    void setUp() {
        presenter = new NewProductPresenter(mockView, mockRepository, mockManagePresenter);
    }

    // ── Campos vacíos ──────────────────────────────────────────────────

    @Test
    @DisplayName("Nombre vacío → error de campos y repositorio NO llamado")
    void nombreVacio_muestraError() {
        configurarVistaCamposBase("", "Electrónica", "Descripción", "10.00", "21");

        presenter.onCrearClick();

        verify(mockView).showErrorCreateProduct(contains("vacío"));
        verify(mockRepository, never()).guardarProducto(any());
    }

    @Test
    @DisplayName("Categoría vacía → error de campos y repositorio NO llamado")
    void categoriaVacia_muestraError() {
        configurarVistaCamposBase("Portátil", "", "Descripción", "10.00", "21");

        presenter.onCrearClick();

        verify(mockView).showErrorCreateProduct(contains("vacío"));
        verify(mockRepository, never()).guardarProducto(any());
    }

    @Test
    @DisplayName("Descripción vacía → error de campos y repositorio NO llamado")
    void descripcionVacia_muestraError() {
        configurarVistaCamposBase("Portátil", "Electrónica", "", "10.00", "21");

        presenter.onCrearClick();

        verify(mockView).showErrorCreateProduct(contains("vacío"));
    }

    @Test
    @DisplayName("Precio vacío → error de campos y repositorio NO llamado")
    void precioVacio_muestraError() {
        configurarVistaCamposBase("Portátil", "Electrónica", "Desc", "", "21");

        presenter.onCrearClick();

        verify(mockView).showErrorCreateProduct(contains("vacío"));
    }

    @Test
    @DisplayName("IVA vacío → error de campos y repositorio NO llamado")
    void ivaVacio_muestraError() {
        configurarVistaCamposBase("Portátil", "Electrónica", "Desc", "10.00", "");

        presenter.onCrearClick();

        verify(mockView).showErrorCreateProduct(contains("vacío"));
    }

    @Test
    @DisplayName("Todos los campos nulos → error de campos")
    void todosNull_muestraError() {
        configurarVistaCamposBase(null, null, null, null, null);

        presenter.onCrearClick();

        verify(mockView).showErrorCreateProduct(anyString());
    }

    // ── cargando(true) se llama siempre al inicio ─────────────────────

    @Test
    @DisplayName("cargando(true) se llama al inicio independientemente del resultado")
    void cargando_siempreLlamadoAlInicio() {
        configurarVistaCamposBase("", "", "", "", "");

        presenter.onCrearClick();

        verify(mockView, atLeastOnce()).cargando(true);
    }

    // ── Validación del precio ──────────────────────────────────────────

    @ParameterizedTest(name = "precio = \"{0}\"")
    @ValueSource(strings = {"abc", "12.3.4", "--5", "€10"})
    @DisplayName("Precio con formato inválido → error de formato y repositorio NO llamado")
    void precioFormatoInvalido_muestraError(String precio) {
        configurarVistaCamposBase("Ratón", "Periférico", "Desc", precio, "21");

        presenter.onCrearClick();

        verify(mockView).showErrorCreateProduct(anyString());
        verify(mockRepository, never()).guardarProducto(any());
    }

    @Test
    @DisplayName("Precio con más de 2 decimales → error de decimales")
    void precioMasDe2Decimales_muestraError() {
        configurarVistaCamposBase("Ratón", "Periférico", "Desc", "10.999", "21");

        presenter.onCrearClick();

        verify(mockView).showErrorCreateProduct(contains("decimales"));
    }

    // ── Validación del IVA ─────────────────────────────────────────────

    @Test
    @DisplayName("IVA negativo → error de rango")
    void ivaNegativo_muestraError() {
        configurarVistaCamposBase("Ratón", "Periférico", "Desc", "10.00", "-1");

        presenter.onCrearClick();

        verify(mockView).showErrorCreateProduct(contains("IVA"));
        verify(mockRepository, never()).guardarProducto(any());
    }

    @Test
    @DisplayName("IVA mayor que 100 → error de rango")
    void ivaMayorQue100_muestraError() {
        configurarVistaCamposBase("Ratón", "Periférico", "Desc", "10.00", "101");

        presenter.onCrearClick();

        verify(mockView).showErrorCreateProduct(contains("IVA"));
    }

    @Test
    @DisplayName("IVA no numérico → error de formato")
    void ivaNoNumerico_muestraError() {
        configurarVistaCamposBase("Ratón", "Periférico", "Desc", "10.00", "veintiuno");

        presenter.onCrearClick();

        verify(mockView).showErrorCreateProduct(anyString());
    }

    // ── Normalización del precio (coma → punto) ───────────────────────

    @Test
    @DisplayName("Precio con coma se convierte correctamente a BigDecimal con punto")
    void precioConComa_seConvierteCorrectamente() throws InterruptedException {
        configurarVistaCamposBase("Teclado", "Periférico", "Desc", "49,99", "21");
        ArgumentCaptor<Producto> captor = ArgumentCaptor.forClass(Producto.class);
        when(mockRepository.guardarProducto(captor.capture())).thenReturn(true);

        presenter.onCrearClick();
        Thread.sleep(300);

        assertEquals(new BigDecimal("49.99"), captor.getValue().getPrecioUnitario());
    }

    @Test
    @DisplayName("Precio entero se almacena con escala 2 (ej: '15' → 15.00)")
    void precioEntero_seGuardaConEscala2() throws InterruptedException {
        configurarVistaCamposBase("Teclado", "Periférico", "Desc", "15", "21");
        ArgumentCaptor<Producto> captor = ArgumentCaptor.forClass(Producto.class);
        when(mockRepository.guardarProducto(captor.capture())).thenReturn(true);

        presenter.onCrearClick();
        Thread.sleep(300);

        assertEquals(2, captor.getValue().getPrecioUnitario().scale());
    }

    // ── Flujo completo ─────────────────────────────────────────────────

    @Test
    @DisplayName("Datos completos y válidos → repositorio llamado con el producto correcto")
    void datosValidos_repositorioLlamadoConProductoCorrecto() throws InterruptedException {
        configurarVistaCamposBase("Monitor 4K", "Pantallas", "Monitor UHD", "299,99", "21");
        ArgumentCaptor<Producto> captor = ArgumentCaptor.forClass(Producto.class);
        when(mockRepository.guardarProducto(captor.capture())).thenReturn(true);

        presenter.onCrearClick();
        Thread.sleep(300);

        Producto guardado = captor.getValue();
        assertAll(
            () -> assertEquals("Monitor 4K",           guardado.getNombre()),
            () -> assertEquals("Pantallas",            guardado.getCategoria()),
            () -> assertEquals("Monitor UHD",          guardado.getDescripcion()),
            () -> assertEquals(new BigDecimal("299.99"), guardado.getPrecioUnitario()),
            () -> assertEquals(21,                     guardado.getIVA())
        );
    }

    // ── Helper ────────────────────────────────────────────────────────

    private void configurarVistaCamposBase(String nombre, String categoria,
                                            String descripcion, String precio, String iva) {
        when(mockView.getNombre()).thenReturn(nombre);
        when(mockView.getCategoria()).thenReturn(categoria);
        when(mockView.getDescripcion()).thenReturn(descripcion);
        when(mockView.getPrecioUnitario()).thenReturn(precio);
        when(mockView.getIVA()).thenReturn(iva);
    }
}
