package emotionalsongs;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ResourceBundle;

/*
TODO:
 1- finire implementazione
 2- creare metodo set emotions che mi va a settare il nome della canzone e tutto il resto
 */
public class ViewReportEmotionsController implements Initializable {

    @FXML private VBox reportEmotionsPane;
    @FXML private Button returnBtn;
    @FXML private BarChart<String, Double> barChart;
    @FXML private Label songNameLabel;
    @FXML private Label infoUsersLabel;
    @FXML private Label infoAverageLabel;

    private boolean fromSongController;

    private GUIUtilities guiUtilities;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // create a new guiUtilities instance with singleton pattern
        guiUtilities = GUIUtilities.getInstance();
    }

    /**
     * TODO document
     */
    @FXML
    public void handleReturnButtonAction(){
        /*
         TODO implementare cosa deve accadere quando viene premuto il pulsante, il suo funzionamento
            varia in base a se il viewReportEmotions è stato invocato da songController o da
            SongPlaylistController
         */

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
     * TODO document
     * @param song
     * @param fromSongController
     */
    public void setEmotions(Canzone song, boolean fromSongController){

        // set the songNameLabel
        songNameLabel.setText(song.getTitolo()  + " (" + song.getAutore() + " - " + song.getAnno() + ")");

        // set the value of the Boolean variable fromSongController
        this.fromSongController = fromSongController;

         /*
        TODO modificare in quanto il metogo getEmotionsFromDB mi restituirà una lista di emozioni
            appena mattia modifica il metodo
         */

        // get the song average emotions
        double[] emotionsAverage = getEmotionsAverageFromDB(song);

        // initialize the barChart
        initBarChart(emotionsAverage);

        String[] result = findEmotionWithHighestAverage(emotionsAverage);

        // get the total Users
        int totalUsers = getTotalUserFeedbacksForSongs(song);

        // set infoUsersLabel
        if(totalUsers != 1) {
            infoUsersLabel.setText(totalUsers + " utenti hanno aggiunto emozioni per questa canzone");
        }else{
            infoUsersLabel.setText(totalUsers + " utente ha aggiunto emozioni per questa canzone");
        }

        /*
         verifico se la canzone ha emozioni associate, la verifica avviene verificando se il valore
         contenuto nella posizione 1 dell'array result (tale posizione mi contiene il valore della media
         più alta) è != - 1.
         -- result[1] (valore della media più alta) != -1 --> la canzone ha emozioni associate
         -- result[1] (valore della media più alta) == -1 --> la canzone non ha emozioni associate
         */
        if(Double.parseDouble(result[1]) != -1) {
            // set the infoAverageLabel
            infoAverageLabel.setText("L'emozione con la media più alta è " + result[0] + " con una media di " + result[1]);
        }else{
            // set the infoAverageLabel
            infoAverageLabel.setText(" - ");
        }

    }

    /**
     * TODO document
     * @param song
     * @return
     */
    public double[] getEmotionsAverageFromDB(Canzone song){
        /*
        metodo che interroga il db per farsi restituire le medie totali delle emozioni della canzone passata
        come parametro
         */

        double[] emotionsAverage = null;

        try{
            // get the song average emotions
            emotionsAverage = EmotionalSongsClient.repo.getSongAverageEmotions(song.getSongUUID());

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
     * TODO document
     * @param song
     * @return
     */
    public int getTotalUserFeedbacksForSongs(Canzone song){

        /*
        metodo che interroga il db tramite l'apposito metodo del server, per farsi restituire il numero
        totali di utenti che hanno aggiunto delle emozioni a una specifica canzone
         */

        int totalUsers = 0;

        try{
            // get the song average emotions
            totalUsers = EmotionalSongsClient.repo.getTotalUserFeedbacksForSong(song.getSongUUID());

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
     * TODO document
     * @param emotionsAverage
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
     * TODO document
     * @param emotionsAverage
     * @return
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