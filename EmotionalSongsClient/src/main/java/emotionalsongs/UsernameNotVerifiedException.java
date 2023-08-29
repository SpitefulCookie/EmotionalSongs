package emotionalsongs;

/*
 * Progetto svolto da:
 *
 * Corallo Samuele 749719, Ateneo di Varese
 * Della Chiesa Mattia 749904, Ateneo di Varese
 *
 */

import java.io.Serializable;

public class UsernameNotVerifiedException extends Exception implements Serializable {
    UsernameNotVerifiedException(){
        super("The server was unable to verify the provided username's availability.");
    }
}
