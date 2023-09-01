package emotionalsongs;

/*
 * Progetto svolto da:
 *
 * Corallo Samuele 749719, Ateneo di Varese
 * Della Chiesa Mattia 749904, Ateneo di Varese
 *
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
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
 * @author <a href="https://github.com/SpitefulCookie"> Della Chiesa Mattia</a>
 */
public class ClientSettingController implements Initializable{

    @FXML private Button confirmButton;
    @FXML private Button cancelButton;
    @FXML private TextField serverAddressTF;
    @FXML private TextField serverPortTF;
    @FXML private HBox titleBar;

    private static double xOffset = 0;
    private static double yOffset = 0;
    private static Stage stage;

    private static String serverHostAddress = "localhost";
    private static int serverPortAddress = 6789;

    public static void setStage(Stage dialog) {
        stage = dialog;
    }

    /**
     * Initializes the configuration dialog window.
     *
     * This method is called automatically when the JavaFX dialog associated with this controller is loaded.
     * It initializes various UI components and sets up their behavior.
     * This method is also responsible for configuring the behavior of the "cancelButton" and "confirmButton" buttons.
     *
     * @param url The URL of the FXML file.
     * @param resourceBundle The ResourceBundle associated with the FXML file.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Nel campo porta verrà permesso d'immettere solamente valori numerici
        GUIUtilities.forceNumericInput(serverPortTF);

        // Vengono aggiunti i tooltip che suggeriscono all'utente la possibilità di utilizzare i valori di default lasciando i campi vuoti

        serverPortTF.setTooltip(new Tooltip("Leave blank to restore to default"));
        serverAddressTF.setTooltip(new Tooltip("Leave blank to restore to default"));

        // Valorizza i campi con i valori attuali

        serverAddressTF.setText(serverHostAddress);
        serverPortTF.setText(String.valueOf(serverPortAddress));

        // Chiude la finestra di dialogo una volta premuto il tasto 'Cancel'

        cancelButton.setOnAction(event -> {
            stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        });

        // Salva le modifiche e chiude la finestra di dialogo una volta premuto 'Continue'

        confirmButton.setOnAction(event -> {

            // Di seguito verranno reperiti i dati immessi nei campi;
            // Nel caso uno di questi risulti essere vuoto, verrà utilizzato il valore di default memorizzato all'interno del promptText

            if(serverAddressTF.getText().isBlank()){serverHostAddress = serverAddressTF.getPromptText();}
            else{serverHostAddress = serverAddressTF.getText();}

            if(serverPortTF.getText().isBlank()){serverPortAddress = Integer.parseInt(serverPortTF.getPromptText());}
            else{serverPortAddress = Integer.parseInt(serverPortTF.getText());}

            EmotionalSongs.initializeServerConnection(false);

            // Chiusura della finestra

            Stage stage = (Stage) confirmButton.getScene().getWindow();
            stage.close();

        });
    }

    /**
     * Closes the displayed window when the close button is clicked.<br><br>
     *
     * When the close button is clicked, the method closes the window containing the button.
     *
     * @param event The {@link ActionEvent} triggered by clicking the close button. (Parameter unused within the method)
     */
    @FXML
    public void handleCloseButtonAction(ActionEvent event){
        ((Stage)this.cancelButton.getScene().getWindow()).close();
    }

    /**
     * Calculates the gap between the mouse cursor position and the top-left corner of the application window.
     *
     * This method is used to calculate the gap between the current mouse cursor position and the top-left corner of
     * the application window.
     * The values updated within this method are used to move the application window.
     *
     * @param event The MouseEvent representing the mouse event that triggered the calculation.
     */
    private void calculateGap(MouseEvent event){
        xOffset = event.getScreenX() - stage.getX();
        yOffset = event.getScreenY() - stage.getY();
    }

    /**
     * Enables the dragging behavior of the application window using mouse events.
     *
     * This method sets up the dragging behavior of the application window using mouse events; When called, the method
     * creates the following event handlers for the application's UI components:
     * <ol>
     * <li> OnMousePressed: This event handler is triggered when the user presses the mouse button while the cursor is
     *    within the boundaries of the pane. It calculates the initial offset (`xOffset` and `yOffset`) between the
     *    mouse cursor position and the top-left corner of the application window.
     * <li> OnMouseDragged: This event handler is triggered when the user moves the mouse cursor after pressing the mouse
     *    button on the pane. It updates the position of the application window according to the cursor movement, thereby
     *    simulating the dragging behavior of the window.
     * </ol>
     *
     */
    @FXML protected void moveWindow() {

        this.titleBar.setOnMousePressed(event -> {

            xOffset = event.getScreenX() - stage.getX();
            yOffset = event.getScreenY() - stage.getY();

        });

        this.titleBar.setOnMouseDragged(event -> {

            if (xOffset == 0 && yOffset==0) {
                // questa porzione di codice è necessaria per evitare che la finestra snappi alle coordinate 0,0
                // (cosa che avviene alla primo trascinamento della schermata)
                calculateGap(event);
            }

            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });

    }

    /**
     * Retrieves the server host address.<br><br>
     *
     * This method returns the server host address used to establish a connection with the server.
     * @return A {@code String} representing the server's host address.
     */
    protected static String getServerAddress() {
        return serverHostAddress;
    }

    /**
     * Retrieves the server's port number.<br><br>
     *
     * This method returns the server port number used to establish a connection with the server.
     * @return A {@code String} representing the server's port number.
     */
    protected static int getServerPort(){
        return serverPortAddress;
    }

}
