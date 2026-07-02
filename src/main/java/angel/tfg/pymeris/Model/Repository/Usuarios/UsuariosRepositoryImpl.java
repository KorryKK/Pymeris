package angel.tfg.pymeris.Model.Repository.Usuarios;

import angel.tfg.pymeris.Model.DAO.Usuarios.UsuarioDAOImpl;
import angel.tfg.pymeris.Model.Entities.Usuario;

import java.util.List;

public class UsuariosRepositoryImpl implements UsuariosRepository {

    @Override
    public List<Usuario> obtenerTodosLosUsuarios() {
        // El repositorio pide al DAO la lista cruda
        return UsuarioDAOImpl.obtenerTodos();
    }

    @Override
    public boolean guardarUsuario(Usuario usuario){
        System.out.println("Llamamos al dao");
        return UsuarioDAOImpl.insertar(usuario);
    }

    @Override
    public boolean actualizarUsuario(Usuario u){
        System.out.println("Se va a actualizar a " + u.getNombre());
        return UsuarioDAOImpl.actualizarUsuario(u);
    }

    @Override
    public boolean restartPassUsuario(Usuario u) {
        System.out.println("Llamamos al dao para resetear la contraseña");
        return UsuarioDAOImpl.restartPassword(u);
    }
}