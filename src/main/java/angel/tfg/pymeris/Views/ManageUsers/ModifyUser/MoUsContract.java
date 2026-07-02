package angel.tfg.pymeris.Views.ManageUsers.ModifyUser;

import angel.tfg.pymeris.Model.Entities.Usuario;

public interface MoUsContract {
    interface View{
        void showModifyStatus(String message);
        void showError(String message);
        String getID();
        String getNombre();
        String getApellido();
        String getCorreo();
        String getTelefono();
        String getUsuario();
        boolean getAdmin();
        boolean getEstado();
        void setPresenter(MoUsContract.Presenter presenter);
        void getInfoUser(Usuario u);
        void btnOf(boolean b);
    }
    interface Presenter{
        void onModifyUserClick();
    }
}