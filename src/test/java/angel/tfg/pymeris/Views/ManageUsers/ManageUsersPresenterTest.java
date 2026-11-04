package angel.tfg.pymeris.Views.ManageUsers;

import angel.tfg.pymeris.Model.Repository.Usuarios.UsuariosRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ManageUsersPresenter — Gestión de usuarios")
class ManageUsersPresenterTest {

    @Mock private MaUsContract.View      mockView;
    @Mock private UsuariosRepository     mockRepository;

    private ManageUsersPresenter presenter;

    @BeforeEach
    void setUp() {
        presenter = new ManageUsersPresenter(mockView, mockRepository);
    }

    // ── getRepository ──────────────────────────────────────────────────

    @Test
    @DisplayName("getRepository devuelve la instancia inyectada")
    void getRepository_devuelveInstanciaInyectada() {
        assertSame(mockRepository, presenter.getRepository());
    }

    // ── cargarUsuarios ─────────────────────────────────────────────────

    @Test
    @DisplayName("cargarUsuarios llama a mostrarCarga(true) inmediatamente")
    void cargarUsuarios_llamaMostrarCargaTrue() {
        presenter.cargarUsuarios();
        verify(mockView, times(1)).mostrarCarga(true);
    }

    @Test
    @DisplayName("cargarUsuarios llama al repositorio en segundo plano")
    void cargarUsuarios_llamaRepositorio() throws InterruptedException {
        when(mockRepository.obtenerTodosLosUsuarios()).thenReturn(java.util.Collections.emptyList());

        presenter.cargarUsuarios();
        Thread.sleep(300);

        verify(mockRepository, times(1)).obtenerTodosLosUsuarios();
    }

    // ── El repositorio correcto se inyecta ────────────────────────────

    @Test
    @DisplayName("getRepository devuelve exactamente el repositorio inyectado y no otro")
    void getRepository_noDevuelveRepositorioDistinto() {
        UsuariosRepository otroRepo = mock(UsuariosRepository.class);
        assertNotSame(otroRepo, presenter.getRepository());
    }

}
