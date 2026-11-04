package angel.tfg.pymeris.Views.ChangePassword;

import angel.tfg.pymeris.Model.Repository.Authentication.AuthRepository;
import angel.tfg.pymeris.Views.Login.LoginContract;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.mockito.Mockito.*;

/**
 * Tests de ChangePasswordPresenter.
 * La validación síncrona (campos vacíos, contraseñas no coinciden) se
 * puede testar directamente. La parte asíncrona (changePass + Platform)
 * requiere inicializar el toolkit de JavaFX y se cubre con mocks del
 * repositorio.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ChangePasswordPresenter — cambio de contraseña")
class ChangePasswordPresenterTest {

    @Mock private ChPwContract.View    mockView;
    @Mock private AuthRepository       mockRepository;
    @Mock private LoginContract.View   mockLoginView;

    private ChangePasswordPresenter presenter;

    @BeforeEach
    void setUp() {
        presenter = new ChangePasswordPresenter(mockView, mockRepository, mockLoginView);
    }

    // ── Campos vacíos ──────────────────────────────────────────────────

    @Test
    @DisplayName("Ambos campos vacíos → isbothtextEmpty() y no llama al repositorio")
    void ambosCamposVacios_llamaBothEmpty() {
        when(mockView.getPassword1()).thenReturn("");
        when(mockView.getPassword2()).thenReturn("");

        presenter.onChangeClicked();

        verify(mockView).isbothtextEmpty();
        verify(mockRepository, never()).changePass(anyString(), anyString());
    }

    @Test
    @DisplayName("Solo contraseña 1 vacía → istext1Empty() y no llama al repositorio")
    void soloPass1Vacio_llamaText1Empty() {
        when(mockView.getPassword1()).thenReturn("");
        when(mockView.getPassword2()).thenReturn("algo");

        presenter.onChangeClicked();

        verify(mockView).istext1Empty();
        verify(mockRepository, never()).changePass(anyString(), anyString());
    }

    @Test
    @DisplayName("Solo contraseña 2 vacía → istext2Empty() y no llama al repositorio")
    void soloPass2Vacio_llamaText2Empty() {
        when(mockView.getPassword1()).thenReturn("algo");
        when(mockView.getPassword2()).thenReturn("");

        presenter.onChangeClicked();

        verify(mockView).istext2Empty();
        verify(mockRepository, never()).changePass(anyString(), anyString());
    }

    @Test
    @DisplayName("Password1 null se trata como vacío → isbothtextEmpty()")
    void pass1Null_tratadoComoVacio() {
        when(mockView.getPassword1()).thenReturn(null);
        when(mockView.getPassword2()).thenReturn(null);

        presenter.onChangeClicked();

        verify(mockView).isbothtextEmpty();
    }

    // ── Contraseñas no coinciden ───────────────────────────────────────

    @Test
    @DisplayName("Contraseñas distintas → istextUnmatch() y no llama al repositorio")
    void contrasenhasDistintas_llamaUnmatch() {
        when(mockView.getPassword1()).thenReturn("pass1");
        when(mockView.getPassword2()).thenReturn("pass2");

        presenter.onChangeClicked();

        verify(mockView).istextUnmatch();
        verify(mockRepository, never()).changePass(anyString(), anyString());
    }

    @Test
    @DisplayName("Contraseñas distintas → NO llama setChanging()")
    void contrasenhasDistintas_noLlamaSetChanging() {
        when(mockView.getPassword1()).thenReturn("aaa");
        when(mockView.getPassword2()).thenReturn("bbb");

        presenter.onChangeClicked();

        verify(mockView, never()).setChanging();
    }

    // ── Contraseñas correctas ──────────────────────────────────────────

    @Test
    @DisplayName("Contraseñas correctas → repositorio es llamado con usuario y contraseña")
    void contrasenhasCorrectas_llamaRepositorio() throws InterruptedException {
        when(mockView.getPassword1()).thenReturn("Pass123!");
        when(mockView.getPassword2()).thenReturn("Pass123!");
        when(mockView.getUsername()).thenReturn("alopez");
        when(mockRepository.changePass("alopez", "Pass123!")).thenReturn(true);

        presenter.onChangeClicked();

        // Esperamos al hilo secundario
        Thread.sleep(300);

        verify(mockRepository, times(1)).changePass("alopez", "Pass123!");
    }
}
