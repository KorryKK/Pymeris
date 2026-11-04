package angel.tfg.pymeris.Model.Repository.Authentication;

import angel.tfg.pymeris.Model.DAO.Login.CheckLogin;
import angel.tfg.pymeris.Model.Entities.Usuario;

public class AuthRepositoryImpl implements AuthRepository {
    @Override
    public Usuario login(String user, String pass) {
        return CheckLogin.checkUserPass(user, pass);
    }

    @Override
    public boolean checkStatus(int status) {
        return status == 0;
    }   // Devuelve true si el usuario está dado de baja

    @Override
    public boolean checkAccess(int acceso) {
        return acceso == 0;
    }   // Devuelve true si hay que cambiar la contraseña

    @Override
    public boolean changePass(String username, String newPassword) {
        return CheckLogin.changePassword(username, newPassword);
    }
}