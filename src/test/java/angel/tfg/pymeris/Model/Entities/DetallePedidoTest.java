package angel.tfg.pymeris.Model.Entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DetallePedido — entidad")
class DetallePedidoTest {

    private DetallePedido detalle;

    @BeforeEach
    void setUp() {
        detalle = new DetallePedido();
    }

    @Test
    @DisplayName("El constructor vacío crea un objeto no nulo")
    void constructorVacio_noNull() {
        assertNotNull(detalle);
    }

    @Test
    @DisplayName("Todos los campos se almacenan y recuperan correctamente")
    void todosLosCampos_setGet() {
        detalle.setId_Pedido(1);
        detalle.setId_Producto(10);
        detalle.setNombre_Producto("Teclado mecánico");
        detalle.setCantidad(3);
        detalle.setPrecio_Unitario(new BigDecimal("59.99"));
        detalle.setSubtotal(new BigDecimal("179.97"));
        detalle.setIVA(21);
        detalle.setImporte_Producto(new BigDecimal("217.76"));

        assertAll(
                () -> assertEquals(1,                         detalle.getId_Pedido()),
                () -> assertEquals(10,                        detalle.getId_Producto()),
                () -> assertEquals("Teclado mecánico",        detalle.getNombre_Producto()),
                () -> assertEquals(3,                         detalle.getCantidad()),
                () -> assertEquals(new BigDecimal("59.99"),   detalle.getPrecio_Unitario()),
                () -> assertEquals(new BigDecimal("179.97"),  detalle.getSubtotal()),
                () -> assertEquals(21,                        detalle.getIVA()),
                () -> assertEquals(new BigDecimal("217.76"),  detalle.getImporte_Producto())
        );
    }

    @Test
    @DisplayName("La cantidad puede ser 1 (unidad mínima)")
    void cantidad_unitaria() {
        detalle.setCantidad(1);
        assertEquals(1, detalle.getCantidad());
    }

    @Test
    @DisplayName("La cantidad puede actualizarse a un valor mayor")
    void cantidad_seActualiza() {
        detalle.setCantidad(2);
        detalle.setCantidad(detalle.getCantidad() + 3);
        assertEquals(5, detalle.getCantidad());
    }
}
