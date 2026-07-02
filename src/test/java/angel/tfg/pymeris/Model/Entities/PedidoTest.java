package angel.tfg.pymeris.Model.Entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pedido — entidad")
class PedidoTest {

    private Pedido pedido;

    @BeforeEach
    void setUp() {
        pedido = new Pedido();
    }

    @Test
    @DisplayName("Todos los campos se almacenan y recuperan correctamente")
    void todosLosCampos_setGet() {
        LocalDateTime ahora = LocalDateTime.now();

        pedido.setId_Pedido(100);
        pedido.setId_Empleado(5);
        pedido.setNIF_DNI_Cliente("12345678A");
        pedido.setCliente("Juan García");
        pedido.setFecha_Pedido(ahora);
        pedido.setImporte_Total(new BigDecimal("250.75"));
        pedido.setEstado_Pedido("PENDIENTE");

        assertAll(
                () -> assertEquals(100,                       pedido.getId_Pedido()),
                () -> assertEquals(5,                         pedido.getId_Empleado()),
                () -> assertEquals("12345678A",               pedido.getNIF_DNI_Cliente()),
                () -> assertEquals("Juan García",             pedido.getCliente()),
                () -> assertEquals(ahora,                     pedido.getFecha_Pedido()),
                () -> assertEquals(new BigDecimal("250.75"),  pedido.getImporte_Total()),
                () -> assertEquals("PENDIENTE",               pedido.getEstado_Pedido())
        );
    }

    @Test
    @DisplayName("El estado del pedido puede actualizarse")
    void estado_seActualiza() {
        pedido.setEstado_Pedido("PENDIENTE");
        assertEquals("PENDIENTE", pedido.getEstado_Pedido());

        pedido.setEstado_Pedido("COMPLETADO");
        assertEquals("COMPLETADO", pedido.getEstado_Pedido());
    }

    @Test
    @DisplayName("Importe total admite null")
    void importeTotal_admiteNull() {
        pedido.setImporte_Total(null);
        assertNull(pedido.getImporte_Total());
    }
}
