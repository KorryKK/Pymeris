package angel.tfg.pymeris.Views.ManageOrders.CreateOrder;

import angel.tfg.pymeris.Model.Entities.Cliente;
import angel.tfg.pymeris.Model.Repository.Pedidos.PedidosRepository;
import angel.tfg.pymeris.Views.ManageOrders.CreateOrder.SelectCliente.SelectClienteContract;
import angel.tfg.pymeris.Views.ManageOrders.CreateOrder.SelectCliente.SelectClientePresenter;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SelectClientePresenter — selección de cliente en pedido")
class SelectClientePresenterTest {

    @Mock private SelectClienteContract.View mockController;
    @Mock private PedidosRepository         mockRepository;
    @Mock private NewOrderContract.Presenter mockNewOrderPresenter;

    private SelectClientePresenter presenter;

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
        presenter = new SelectClientePresenter(mockController, mockRepository, mockNewOrderPresenter);
    }

    // ── cargarClientes ─────────────────────────────────────────────────

    @Test
    @DisplayName("cargarClientes llama a listarClientes() del repositorio")
    void cargarClientes_llamaRepositorio() throws InterruptedException {
        when(mockRepository.listarClientes()).thenReturn(Collections.emptyList());

        presenter.cargarClientes();
        Thread.sleep(300);

        verify(mockRepository, times(1)).listarClientes();
    }

    @Test
    @DisplayName("cargarClientes pasa la lista al controlador mediante mostrarClientes")
    void cargarClientes_llamaMostrarClientes() throws InterruptedException {
        Cliente c1 = buildCliente("11111111A", "Ana");
        Cliente c2 = buildCliente("22222222B", "Luis");
        when(mockRepository.listarClientes()).thenReturn(Arrays.asList(c1, c2));

        presenter.cargarClientes();
        Thread.sleep(300);

        verify(mockController).mostrarClientes(Arrays.asList(c1, c2));
    }

    @Test
    @DisplayName("cargarClientes con lista vacía pasa lista vacía al controlador")
    void cargarClientes_listaVacia_pasaListaVacia() throws InterruptedException {
        when(mockRepository.listarClientes()).thenReturn(Collections.emptyList());

        presenter.cargarClientes();
        Thread.sleep(300);

        verify(mockController).mostrarClientes(Collections.emptyList());
    }

    @Test
    @DisplayName("cargarClientes ante excepción del repositorio no propaga el error")
    void cargarClientes_excepcion_noPropaga() throws InterruptedException {
        when(mockRepository.listarClientes()).thenThrow(new RuntimeException("Error BD"));

        presenter.cargarClientes();
        Thread.sleep(300);

        // Si no lanza excepción no controlada el test pasa; el controlador NO debe recibir nada
        verify(mockController, never()).mostrarClientes(any());
    }

    // ── onSeclectClick ────────────────────────────────────────────────

    @Test
    @DisplayName("onSeclectClick delega el cliente en el newOrderPresenter")
    void onSelectClick_delegaEnNewOrderPresenter() {
        Cliente c = buildCliente("33333333C", "Carlos");

        presenter.onSeclectClick(c);

        verify(mockNewOrderPresenter, times(1)).anadirCliente(c);
    }

    @Test
    @DisplayName("onSeclectClick cierra la ventana del controlador")
    void onSelectClick_cierraVentana() {
        Cliente c = buildCliente("44444444D", "Marta");

        presenter.onSeclectClick(c);

        verify(mockController, times(1)).toClose();
    }

    @Test
    @DisplayName("onSeclectClick llama primero anadirCliente y luego toClose")
    void onSelectClick_ordenEjecucion() {
        Cliente c = buildCliente("55555555E", "Pedro");
        var inOrder = inOrder(mockNewOrderPresenter, mockController);

        presenter.onSeclectClick(c);

        inOrder.verify(mockNewOrderPresenter).anadirCliente(c);
        inOrder.verify(mockController).toClose();
    }

    @Test
    @DisplayName("onSeclectClick con distintos clientes siempre pasa el cliente correcto")
    void onSelectClick_pasaClienteCorrecto() {
        Cliente c = buildCliente("66666666F", "Laura");

        presenter.onSeclectClick(c);

        verify(mockNewOrderPresenter).anadirCliente(c);
        verify(mockNewOrderPresenter, never()).anadirCliente(argThat(x -> !x.getNIF_DNI().equals("66666666F")));
    }

    // ── Helper ────────────────────────────────────────────────────────

    private Cliente buildCliente(String dni, String nombre) {
        Cliente c = new Cliente();
        c.setNIF_DNI(dni);
        c.setNombre(nombre);
        return c;
    }
}
