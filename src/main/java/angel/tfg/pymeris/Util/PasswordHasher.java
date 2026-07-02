package angel.tfg.pymeris.Util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHasher {

    // Genera el hash para guardar en la base de datos
    public static String hashPassword(String password) {
        // gensalt() crea la semilla aleatoria única para esta contraseña
        // Cuanto mayor sea el factor, más se tarda en generar una nueva contraseña
        // Pero también es más seguro
        // Se deja en 15, lo vamos a considerar el "SweetPoint", suficientemente rápido y seguro
        return BCrypt.hashpw(password, BCrypt.gensalt(15));
    }

    // Comprueba si una contraseña en texto plano coincide con el hash de la BD
    public static boolean checkPassword(String candidate, String hashed) {
        // No se puede comparar con ==, se usa esta función que extrae el salt del hash
        try {
            return BCrypt.checkpw(candidate, hashed);
        } catch (Exception e) {
            return false;
        }
    }
}