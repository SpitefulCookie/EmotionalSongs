package emotionalsongs;

/*
 * Progetto svolto da:
 *
 * Corallo Samuele 749719, Ateneo di Varese
 * Della Chiesa Mattia 749904, Ateneo di Varese
 *
 */

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

/*
TODO:
 1- finire implementazione
 2- creare metodo set emotions che mi va a settare il nome della canzone e tutto il resto
 */
/**
 * Controller class responsible for managing the display of emotion report data in the user interface.
 * This class handles interaction with UI components related to displaying emotion reports for songs.
 *
 * @author <a href="https://github.com/samuk52">Corallo Samuele</a>
 */
public class ViewReportEmotionsController implements Initializable {

    @FXML private VBox reportEmotionsPane;
    @FXML private BarChart<String, Double> barChart;
    @FXML private Label songNameLabel;
    @FXML private Label infoUsersLabel;
    @FXML private Label infoAverageLabel;

    private boolean fromSongController;

    private GUIUtilities guiUtilities;

    /**
     * Initializes the controller when the corresponding FXML is loaded.
     * This method initializes the `guiUtilities` instance using the singleton pattern.
     *
     * @param url The URL to the FXML document (unused).
     * @param resourceBundle The resources used for localization (unused).
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // create a new guiUtilities instance with singleton pattern
        guiUtilities = GUIUtilities.getInstance();
    }

    /**
     * Handles the action of returning to the previous view (search or playlist) depending from which controller this method was invoked from.
     */
    @FXML
    public void handleReturnButtonAction(){

        // check if the viewReportEmotions was invoked by songController or songPlaylistController
        if(fromSongController){ // if the viewReportEmotions was invoked by songController

            // open the search pane
            EmotionalSongsClientController.setDynamicPane(guiUtilities.getNode("search.fxml"));

        }else{ // the viewReportEmotions was invoked by songPlaylistController

            // open the playlist
            try{
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("selectedPlaylist.fxml"));
                Node playlist = fxmlLoader.load();

                SelectedPlaylistController selectedPlaylistController = fxmlLoader.getController();
                selectedPlaylistController.openPlaylist(SelectedPlaylistController.getPlaylistOpened());

                EmotionalSongsClientController.setDynamicPane(playlist);

            }catch(IOException e){
                e.printStackTrace(); // TODO remove
            }

        }

    }

    /**
     * Sets and displays emotion report data for a song.<br><br>
     *
     * This method populates the user interface with emotion report-related data.
     * It is also responsible for initializing and displaying a bar chart containing the report's data.
     *
     * @param song The song for which the emotion report is being displayed.
     * @param fromSongController Specifies if the invocation is from the `SongController`.
     * @return `true` if the emotion data was set and displayed successfully, otherwise `false`.
     */
    public boolean setEmotions(Canzone song, boolean fromSongController){

        // set the songNameLabel
        songNameLabel.setText(song.getTitolo()  + " (" + song.getAutore() + " - " + song.getAnno() + ")");

        // set the value of the Boolean variable fromSongController
        this.fromSongController = fromSongController;

        // get the song average emotions
        double[] emotionsAverage = getEmotionsAverageFromDB(song);

        if(emotionsAverage != null) {

            // initialize the barChart
            initBarChart(emotionsAverage);

            String[] result = findEmotionWithHighestAverage(emotionsAverage);

            // get the total Users
            int totalUsers = getTotalUserFeedbacksForSongs(song);

            // set infoUsersLabel
            if (totalUsers != 1) {
                infoUsersLabel.setText(totalUsers + " utenti hanno aggiunto emozioni per questa canzone");
            } else {
                infoUsersLabel.setText(totalUsers + " utente ha aggiunto emozioni per questa canzone");
            }

            /*
            verifico se la canzone ha emozioni associate, la verifica avviene verificando se il valore
            contenuto nella posizione 1 dell'array result (tale posizione contiene il valore della media
            più alta) è != - 1.
            -- result[1] (valore della media più alta) != -1 --> la canzone ha emozioni associate
            -- result[1] (valore della media più alta) == -1 --> la canzone non ha emozioni associate
            */
            if (Double.parseDouble(result[1]) != -1) {
                // set the infoAverageLabel
                infoAverageLabel.setText("L'emozione con la media più alta è " + result[0] + " con una media di " + result[1]);
            /*
            change the border color of the reportEmotionsPane to the color of the emotion with the
            highest average.
            */
                reportEmotionsPane.getStyleClass().add(result[0]); // NOTA: result[0] contiene il nome dell'emozione con la media più alta
            } else {
                // set the infoAverageLabel
                infoAverageLabel.setText(" - ");
            }

            return true;

        }

        return false;

    }

    /**
     * Retrieves emotion average scores from the database for a given song.<br><br>
     *
     * This method queries the database to retrieve average emotion scores for the specified song.
     *
     * @param song The song for which emotion average scores are to be retrieved.
     * @return An array of doubles representing the average emotion scores for the song.
     */
    public double[] getEmotionsAverageFromDB(Canzone song){
        /*
        metodo che interroga il db per farsi restituire le medie totali delle emozioni della canzone passata
        come parametro
         */

        double[] emotionsAverage = null;

        try{
            // get the song average emotions
            emotionsAverage = EmotionalSongs.repo.getSongAverageEmotions(song.getSongUUID());

        }catch (RemoteException e){

            Stage connectionFailedStage = new Stage();

            connectionFailedStage.setScene(GUIUtilities.getInstance().getScene("connectionFailed.fxml"));
            connectionFailedStage.initStyle(StageStyle.UNDECORATED);
            connectionFailedStage.initModality(Modality.APPLICATION_MODAL);
            connectionFailedStage.setResizable(false);
            connectionFailedStage.show();

        }

        return emotionsAverage;
    }

    /**
     * Retrieves the total number of user feedbacks for a given song.<br><br>
     *
     * This method queries the database to retrieve the total number of users who provided emotion feedback
     * for the specified song.
     *
     * @param song The song for which the total user feedbacks are to be retrieved.
     * @return The total number of users who provided emotion feedback for the song.
     */
    public int getTotalUserFeedbacksForSongs(Canzone song){

        /*
        metodo che interroga il db tramite l'apposito metodo del server, per farsi restituire il numero
        totali di utenti che hanno aggiunto delle emozioni a una specifica canzone
         */

        int totalUsers = 0;

        try{
            // get the song average emotions
            totalUsers = EmotionalSongs.repo.getTotalUserFeedbacksForSong(song.getSongUUID());

        }catch (RemoteException e){

            Stage connectionFailedStage = new Stage();

            connectionFailedStage.setScene(GUIUtilities.getInstance().getScene("connectionFailed.fxml"));
            connectionFailedStage.initStyle(StageStyle.UNDECORATED);
            connectionFailedStage.initModality(Modality.APPLICATION_MODAL);
            connectionFailedStage.setResizable(false);
            connectionFailedStage.show();

        }

        return totalUsers;

    }

    /**
     * Initializes and displays the bar chart with emotion average scores.<br><br>
     *
     * This method initializes a bar chart with emotion average scores and applies styles to the chart bars.
     *
     * @param emotionsAverage An array of doubles representing the emotion average scores.
     */
    public void initBarChart(double[] emotionsAverage) {

        XYChart.Series series = new XYChart.Series();
        series.getData().add(new XYChart.Data<>(EmozioneEnum.values()[0].toString(), emotionsAverage[0]));
        series.getData().add(new XYChart.Data<>(EmozioneEnum.values()[1].toString(), emotionsAverage[1]));
        series.getData().add(new XYChart.Data<>(EmozioneEnum.values()[2].toString(), emotionsAverage[2]));
        series.getData().add(new XYChart.Data<>(EmozioneEnum.values()[3].toString(), emotionsAverage[3]));
        series.getData().add(new XYChart.Data<>(EmozioneEnum.values()[4].toString(), emotionsAverage[4]));
        series.getData().add(new XYChart.Data<>(EmozioneEnum.values()[5].toString(), emotionsAverage[5]));
        series.getData().add(new XYChart.Data<>(EmozioneEnum.values()[6].toString(), emotionsAverage[6]));
        series.getData().add(new XYChart.Data<>(EmozioneEnum.values()[7].toString(), emotionsAverage[7]));
        series.getData().add(new XYChart.Data<>(EmozioneEnum.values()[8].toString(), emotionsAverage[8]));

        // add the series on the chart
        barChart.getData().add(series);

        // change color of each bar
        Node n = barChart.lookup(".data0.chart-bar");
        n.getStyleClass().add(EmozioneEnum.values()[0].toString() + "BarChart");
        n = barChart.lookup(".data1.chart-bar");
        n.getStyleClass().add(EmozioneEnum.values()[1].toString() + "BarChart");
        n = barChart.lookup(".data2.chart-bar");
        n.getStyleClass().add(EmozioneEnum.values()[2].toString() + "BarChart");
        n = barChart.lookup(".data3.chart-bar");
        n.getStyleClass().add(EmozioneEnum.values()[3].toString() + "BarChart");
        n = barChart.lookup(".data4.chart-bar");
        n.getStyleClass().add(EmozioneEnum.values()[4].toString() + "BarChart");
        n = barChart.lookup(".data5.chart-bar");
        n.getStyleClass().add(EmozioneEnum.values()[5].toString() + "BarChart");
        n = barChart.lookup(".data6.chart-bar");
        n.getStyleClass().add(EmozioneEnum.values()[6].toString() + "BarChart");
        n = barChart.lookup(".data7.chart-bar");
        n.getStyleClass().add(EmozioneEnum.values()[7].toString() + "BarChart");
        n = barChart.lookup(".data8.chart-bar");
        n.getStyleClass().add(EmozioneEnum.values()[8].toString() + "BarChart");

        // delate the lagend of chart
        barChart.setLegendVisible(false);

    }

    /**
     * Finds the emotion with the highest average score.<br><br>
     *
     * This method identifies the emotion with the highest average score from the provided array.
     *
     * @param emotionsAverage An array of doubles representing emotion average scores.
     * @return An array of strings where the first element is the emotion with the highest average score,
     * and the second element is the value of the highest average score.
     */
    private String[] findEmotionWithHighestAverage(double[] emotionsAverage){

        /*
         metodo che trova la media più alta, esso restituisce un array di stringhe, dove la prima
         posizione dell'array contiene il nome dell'emozione con la media più alta e la seconda posizione
         contiene il valore della media più alta
         */

        double maxAverage = emotionsAverage[0];
        int idx_maxAverage = 0;
        for (int i = 0; i < emotionsAverage.length; i++){
            if (emotionsAverage[i] > maxAverage){
                maxAverage = emotionsAverage[i];
                idx_maxAverage = i;
            }
        }

        return new String[]{String.valueOf(EmozioneEnum.values()[idx_maxAverage]), String.valueOf(maxAverage)};
    }
}
