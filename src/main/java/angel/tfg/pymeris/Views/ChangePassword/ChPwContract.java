package angel.tfg.pymeris.Views.ChangePassword;

public interface ChPwContract{

    interface View{
        String getPassword1();
        String getPassword2();
        String getUsername();
        void changeSuccess();
        void changeError();
        void istext1Empty();
        void istext2Empty();
        void isbothtextEmpty();
        void istextUnmatch();
        void setPresenter(ChPwContract.Presenter presenter);
        void setChanging();
    }

    interface Presenter{
        void onChangeClicked();

    }
}