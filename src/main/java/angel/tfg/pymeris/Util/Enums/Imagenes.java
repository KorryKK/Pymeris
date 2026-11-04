package angel.tfg.pymeris.Util.Enums;

public enum Imagenes {
    Logo2048("/Imagenes/Logo/Logo_2048.png"),
    Logo1024("/Imagenes/Logo/Logo_1024.png"),
    Logo512("/Imagenes/Logo/Logo_512.png"),
    Logo256("/Imagenes/Logo/Logo_256.png"),
    Logo128("/Imagenes/Logo/Logo_128.png");

    private final String ruta;

    Imagenes(String path){
        ruta = path;
    }

    public String getImg(){
        return ruta;
    }
}
