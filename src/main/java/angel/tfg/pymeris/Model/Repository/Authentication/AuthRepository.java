package angel.tfg.pymeris.Model.Repository.Authentication;

import angel.tfg.pymeris.Model.Entities.Usuario;

public interface AuthRepository {
    Usuario login(String user, String pass);
    boolean checkAccess(int acceso);
    boolean checkStatus(int status);
    boolean changePass(String username, String newPassword);
}