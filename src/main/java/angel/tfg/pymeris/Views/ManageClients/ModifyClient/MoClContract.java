package angel.tfg.pymeris.Views.ManageClients.ModifyClient;

import angel.tfg.pymeris.Model.Entities.Cliente;

public interface MoClContract {
    interface View {
        void showModifyStatus(String message);
        void showError(String message);
        String getDNI_NFI();
        String getNombre();
        String getApellido();
        String getCorreo();
        String getTelefono();
        boolean getActivo();
        void setPresenter(MoClContract.Presenter presenter);
        void getInfoClient(Cliente c);
        void btnOff(boolean b);
    }
    interface Presenter {
        void onModifyClientClick(String dninifanterior);
    }
}
