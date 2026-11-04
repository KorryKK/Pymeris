package angel.tfg.pymeris.Views.ManageOrders.CreateOrder;

import angel.tfg.pymeris.Model.Entities.Cliente;
import angel.tfg.pymeris.Model.Entities.Producto;
import angel.tfg.pymeris.Model.Repository.Pedidos.PedidosRepository;
import angel.tfg.pymeris.Views.ManageOrders.MaOrContract;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests de NewOrderPresenter.
 * Se cubren los métodos con lógica pura (sin hilo ni Platform.runLater):
 *   - anadirCliente
 *   - anadirProducto (añadir nuevo / acumular existente)
 *   - getCantidadProductos
 *   - getCantidadSubtotal
 *   - getCantidadImporteFINAL
 *   - onCancelOrderClick (devuelve true sin excepción)
 *   - getRepository
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("NewOrderPresenter — lógica de pedidos")
class NewOrderPresenterTest {

    @Mock private NewOrderContract.View  mockView;
    @Mock private PedidosRepository      mockRepository;
    @Mock private MaOrContract.Presenter mockManagePresenter;

    private NewOrderPresenter presenter;

    @BeforeEach
    void setUp() {
        presenter = new NewOrderPresenter(mockView, mockRepository, mockManagePresenter);
    }

    // ── anadirCliente ──────────────────────────────────────────────────

    @Test
    @DisplayName("anadirCliente actualiza la etiqueta de la vista con DNI y nombre")
    void anadirCliente_actualizaLabelVista() {
        Cliente c = buildCliente();

        presenter.anadirCliente(c);

        verify(mockView).setLblInfoCl("12345678A - Juan");
    }

    // ── Lista vacía ────────────────────────────────────────────────────

    @Test
    @DisplayName("getCantidadProductos con lista vacía → '0 PRODUCTOS'")
    void cantidadProductos_listaVacia() {
        assertEquals("0 PRODUCTOS", presenter.getCantidadProductos());
    }

    @Test
    @DisplayName("getCantidadSubtotal con lista vacía → '0 €'")
    void cantidadSubtotal_listaVacia() {
        assertEquals("0 €", presenter.getCantidadSubtotal());
    }

    @Test
    @DisplayName("getCantidadImporteFINAL con lista vacía → BigDecimal.ZERO")
    void importeFinal_listaVacia() {
        assertEquals(BigDecimal.ZERO, presenter.getCantidadImporteFINAL());
    }

    // ── anadirProducto ─────────────────────────────────────────────────

    @Test
    @DisplayName("anadirProducto nuevo llama mostrarDetalles con la lista actualizada")
    void anadirProducto_nuevo_llamaMostrarDetalles() {
        Producto p = buildProducto(1, "Ratón", new BigDecimal("25.00"), 21);

        presenter.anadirProducto(p, 2);

        verify(mockView, times(1)).mostrarDetalles(any());
    }

    @Test
    @DisplayName("anadirProducto suma la cantidad si el producto ya existe en la lista")
    void anadirProducto_existente_acumulaCantidad() {
        Producto p = buildProducto(1, "Ratón", new BigDecimal("25.00"), 21);

        presenter.anadirProducto(p, 2);
        presenter.anadirProducto(p, 3);

        // getCantidadProductos debe reflejar 5 unidades (2+3)
        assertEquals("5 PRODUCTOS", presenter.getCantidadProductos());
    }

    @Test
    @DisplayName("anadirProducto con dos productos distintos suma cantidades por separado")
    void anadirProducto_dosDistintos_cuentaAmbos() {
        Producto p1 = buildProducto(1, "Ratón",    new BigDecimal("25.00"), 21);
        Producto p2 = buildProducto(2, "Teclado",  new BigDecimal("50.00"), 21);

        presenter.anadirProducto(p1, 1);
        presenter.anadirProducto(p2, 2);

        assertEquals("3 PRODUCTOS", presenter.getCantidadProductos());
    }

    // ── getCantidadSubtotal e getCantidadImporteFINAL ──────────────────

    @Test
    @DisplayName("getCantidadSubtotal refleja precio × cantidad sin IVA")
    void cantidadSubtotal_conProducto() {
        // 10 € × 2 = 20 €
        Producto p = buildProducto(1, "Bolígrafo", new BigDecimal("10.00"), 21);
        presenter.anadirProducto(p, 2);

        assertEquals("20.00 €", presenter.getCantidadSubtotal());
    }

    @Test
    @DisplayName("getCantidadImporteFINAL refleja precio × cantidad CON IVA")
    void importeFinal_conProductoConIva() {
        // 10 € × 2 = 20 €; IVA 21 % → 20 × 1,21 = 24,20 €
        Producto p = buildProducto(1, "Bolígrafo", new BigDecimal("10.00"), 21);
        presenter.anadirProducto(p, 2);

        assertEquals(new BigDecimal("24.20"), presenter.getCantidadImporteFINAL());
    }

    @Test
    @DisplayName("getCantidadImporteFINAL con IVA 0 % coincide con el subtotal")
    void importeFinal_iva0_igualAlSubtotal() {
        Producto p = buildProducto(1, "Exento", new BigDecimal("15.00"), 0);
        presenter.anadirProducto(p, 3);

        // 15 × 3 = 45 €; IVA 0 % → 45 €
        assertEquals(new BigDecimal("45.00"), presenter.getCantidadImporteFINAL());
    }

    // ── onCancelOrderClick ─────────────────────────────────────────────

    @Test
    @DisplayName("onCancelOrderClick devuelve true")
    void cancelOrder_devuelveTrue() {
        assertTrue(presenter.onCancelOrderClick());
    }

    // ── getRepository ──────────────────────────────────────────────────

    @Test
    @DisplayName("getRepository devuelve la instancia inyectada")
    void getRepository_devuelveInstanciaInyectada() {
        assertSame(mockRepository, presenter.getRepository());
    }

    // ── Helpers ───────────────────────────────────────────────────────

    private Cliente buildCliente() {
        Cliente c = new Cliente();
        c.setNIF_DNI("12345678A");
        c.setNombre("Juan");
        return c;
    }

    private Producto buildProducto(int id, String nombre, BigDecimal precio, int iva) {
        Producto p = new Producto();
        p.setId_Producto(id);
        p.setNombre(nombre);
        p.setPrecioUnitario(precio);
        p.setIVA(iva);
        return p;
    }
}
