package emotionalsongs;

import java.io.Serializable;

public class UsernameNotVerifiedException extends Exception implements Serializable {
    UsernameNotVerifiedException(){
        super("The server was unable to verify the provided username's availability.");
    }
}
