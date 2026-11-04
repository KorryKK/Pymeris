package angel.tfg.pymeris.Views.ManageUsers;

import angel.tfg.pymeris.Model.Entities.Usuario;
import angel.tfg.pymeris.Model.Repository.Usuarios.UsuariosRepository;

import java.util.List;

public interface MaUsContract {
    interface View{
        void mostrarUsuarios(List<Usuario> lista);
        void setPresenter(Presenter presenter);
        void mostrarCarga(boolean b);
    }
    interface Presenter{
        void cargarUsuarios();
        void onRestartPasswordClick(Usuario u);
        UsuariosRepository getRepository();
    }
}
