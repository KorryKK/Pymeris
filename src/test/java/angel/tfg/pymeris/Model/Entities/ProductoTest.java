package angel.tfg.pymeris.Model.Entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Producto — entidad")
class ProductoTest {

    private Producto producto;

    @BeforeEach
    void setUp() {
        producto = new Producto();
    }

    // ── Getters / Setters ──────────────────────────────────────────────

    @Test
    @DisplayName("Todos los campos se almacenan y recuperan correctamente")
    void todosLosCampos_setGet() {
        producto.setId_Producto(10);
        producto.setCategoria("Electrónica");
        producto.setNombre("Portátil");
        producto.setDescripcion("Portátil de alta gama");
        producto.setIVA(21);
        producto.setPrecioUnitario(new BigDecimal("999.99"));
        producto.setStock_Actual(5);
        producto.setActivo(1);

        assertAll(
                () -> assertEquals(10,                      producto.getId_Producto()),
                () -> assertEquals("Electrónica",           producto.getCategoria()),
                () -> assertEquals("Portátil",              producto.getNombre()),
                () -> assertEquals("Portátil de alta gama", producto.getDescripcion()),
                () -> assertEquals(21,                      producto.getIVA()),
                () -> assertEquals(new BigDecimal("999.99"),producto.getPrecioUnitario()),
                () -> assertEquals(5,                       producto.getStock_Actual()),
                () -> assertEquals(1,                       producto.getActivo())
        );
    }

    // ── getPrecioFormateado ────────────────────────────────────────────

    @Test
    @DisplayName("getPrecioFormateado con precio null devuelve '0,00 €'")
    void precioFormateado_null_devuelve_cero() {
        producto.setPrecioUnitario(null);
        assertEquals("0,00 €", producto.getPrecioFormateado());
    }

    @Test
    @DisplayName("getPrecioFormateado con precio 0 devuelve '0,00 €'")
    void precioFormateado_cero() {
        producto.setPrecioUnitario(BigDecimal.ZERO);
        assertEquals("0,00 €", producto.getPrecioFormateado());
    }

    @Test
    @DisplayName("getPrecioFormateado formatea dos decimales correctamente")
    void precioFormateado_dosDecimales() {
        producto.setPrecioUnitario(new BigDecimal("19.99"));
        assertEquals("19,99 €", producto.getPrecioFormateado());
    }

    @Test
    @DisplayName("getPrecioFormateado con precio entero muestra ,00")
    void precioFormateado_entero() {
        producto.setPrecioUnitario(new BigDecimal("50.00"));
        assertEquals("50,00 €", producto.getPrecioFormateado());
    }

    @Test
    @DisplayName("getPrecioFormateado con precio grande formatea bien")
    void precioFormateado_grande() {
        producto.setPrecioUnitario(new BigDecimal("1500.50"));
        assertEquals("1500,50 €", producto.getPrecioFormateado());
    }

    // ── IVA ───────────────────────────────────────────────────────────

    @ParameterizedTest(name = "IVA = {0}%")
    @ValueSource(ints = {4, 10, 21})
    @DisplayName("Los tipos de IVA legales se almacenan correctamente")
    void iva_tiposLegales(int tipoIva) {
        producto.setIVA(tipoIva);
        assertEquals(tipoIva, producto.getIVA());
    }

    // ── Stock ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("Stock puede ser 0 (sin existencias)")
    void stock_cero() {
        producto.setStock_Actual(0);
        assertEquals(0, producto.getStock_Actual());
    }
}

