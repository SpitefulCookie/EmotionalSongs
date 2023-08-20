package emotionalsongs;

/*
 * Progetto svolto da:
 *
 * Corallo Samuele 749719, Ateneo di Varese
 * Della Chiesa Mattia 749904, Ateneo di Varese
 *
 */

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for the server configuration dialog window.
 *
 * This class implements the JavaFX Initializable interface, which is automatically called when the associated FXML dialog
 * is loaded. It initializes the UI components and sets up their behavior for configuring server settings.
 * The class handles configuring the behavior of the "cancelButton" and "confirmButton" buttons to cancel or save changes
 * to the server settings, respectively.
 *
 * @author <a href="https://github.com/SpitefulCookie">Della Chiesa Mattia</a>
 */
public class ServerSettingController implements Initializable{

    @FXML private Button confirmButton;
    @FXML private Button cancelButton;
    @FXML private TextField dbNameField;
    @FXML private TextField dbPortField;
    @FXML private TextField dbAddressField;

    /**
     * Initializes the server's configuration dialog window.
     *
     * This method is called automatically when the JavaFX dialog associated with this controller is loaded.
     * It initializes various UI components and sets up their behavior.
     * This method is also responsible for configuring the behavior of the "cancelButton" and "confirmButton" buttons.
     *
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known. (unused within the code)
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized. (unused within the code)
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Nel campo porta verrà permesso d'immettere solamente valori numerici
        GUIUtilities.forceNumericInput(dbPortField);

        // Vengono aggiunti i tooltip che suggeriscono all'utente la possibilità di utilizzare i valori di default lasciando i campi vuoti

        dbPortField.setTooltip(new Tooltip("Leave blank to restore to default"));
        dbNameField.setTooltip(new Tooltip("Leave blank to restore to default"));
        dbAddressField.setTooltip(new Tooltip("Leave blank to restore to default"));

        // Valorizza i campi con i valori attuali

        dbNameField.setText(QueryHandler.getDB_Name());
        dbPortField.setText(QueryHandler.getDB_Port());
        dbAddressField.setText(QueryHandler.getDB_Address());

        // Chiude la finestra di dialogo una volta premuto il tasto 'Cancel'

        cancelButton.setOnAction(event -> {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        });

        // Salva le modifiche e chiude la finestra di dialogo una volta premuto 'Continue'

        confirmButton.setOnAction(event -> {

            // Di seguito verranno reperiti i dati immessi nei campi;
            // Nel caso uno di questi risulti essere vuoto, verrà utilizzato il valore di default memorizzato all'interno del promptText

            String name;
            if(dbNameField.getText().isBlank()){name = dbNameField.getPromptText();}
            else{name = dbNameField.getText();}

            String address;
            if(dbAddressField.getText().isBlank()){ address = dbAddressField.getPromptText();}
            else{address = dbAddressField.getText();}

            String port;
            if(dbPortField.getText().isBlank()){port = dbPortField.getPromptText();}
            else{port = dbPortField.getText();}

            // Questa è l'istruzione che va effettivamente a salvare le modifiche

            QueryHandler.setDBConnection(name, address, port);

            // Chiusura della finestra

            Stage stage = (Stage) confirmButton.getScene().getWindow();
            stage.close();

        });
    }

}
