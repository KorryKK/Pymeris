package angel.tfg.pymeris.Model.Repository.Usuarios;

import angel.tfg.pymeris.Model.Entities.Usuario;

import java.util.List;

public interface UsuariosRepository {
    List<Usuario> obtenerTodosLosUsuarios();
    boolean guardarUsuario(Usuario usuario);
    boolean actualizarUsuario(Usuario usuario);
    boolean restartPassUsuario(Usuario usuario);
}