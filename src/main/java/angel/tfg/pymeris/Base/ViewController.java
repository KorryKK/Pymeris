package angel.tfg.pymeris.Base;

import angel.tfg.pymeris.Util.Enums.Titulos;
import angel.tfg.pymeris.Util.Navegation.Navegable;
import angel.tfg.pymeris.Util.Navegation.Navegador;

public class ViewController implements Navegable, Titleable{

    protected Navegador navegador;

    @Override
    public void setNavegador(Navegador navegador) {
        this.navegador = navegador;
    }

    @Override
    public void setTitle(Titulos title) {
        if (navegador == null) {
            throw new IllegalStateException();
        } else {
            navegador.stage().setTitle(title.getTitle());
        }
    }
}
