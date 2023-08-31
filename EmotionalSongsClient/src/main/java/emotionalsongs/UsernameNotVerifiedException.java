package emotionalsongs;

/*
 * Progetto svolto da:
 *
 * Corallo Samuele 749719, Ateneo di Varese
 * Della Chiesa Mattia 749904, Ateneo di Varese
 *
 */

import java.io.Serializable;

/**
 * This exception is thrown when the server is unable to verify the availability of a provided username within the database.
 *
 * <p>{@code UsernameNotVerifiedException} is a subclass of {@link Exception} and indicates that
 * the server encountered an issue when attempting to verify the availability of a username during the
 * user registration process.
 * This is a fail-safe mechanism which ensures that, if an error were to occur during the verification process, the
 * client will be unable to proceed with the user registration process.
 *
 * @author <a href="https://github.com/SpitefulCookie">Della Chiesa Mattia</a>
 */
public class UsernameNotVerifiedException extends Exception implements Serializable {
    UsernameNotVerifiedException(){
        super("The server was unable to verify the provided username's availability.");
    }
}
