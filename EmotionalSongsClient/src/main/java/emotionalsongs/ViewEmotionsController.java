package emotionalsongs;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller class responsible for managing the display of emotion data in the user interface.
 * This class handles the interaction with emotion-related UI components and provides methods to set and display emotion data.
 */
public class ViewEmotionsController implements Initializable{

    @FXML private VBox emotionsPane;
    @FXML private Tooltip toolTipAmazement;
    @FXML private Tooltip toolTipCalmness;
    @FXML private Tooltip toolTipJoy;
    @FXML private Tooltip toolTipNostalgia;
    @FXML private Tooltip toolTipPower;
    @FXML private Tooltip toolTipSadness;
    @FXML private Tooltip toolTipSolemnity;
    @FXML private Tooltip toolTipTenderness;
    @FXML private Tooltip toolTipTension;
    @FXML private Label songNameLabel;
    @FXML private TextArea notesArea;
    @FXML private BarChart<String, Integer> barChart;

    private List<Emozione> emotions;

    /**
     * Initializes the controller when the corresponding FXML is loaded.
     * This method sets up tooltips for the emotion-related UI components.
     *
     * @param url The URL to the FXML document (unused).
     * @param resourceBundle The resources used for localization (unused).
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // add delay to each tooltip
        toolTipAmazement.setShowDelay(new Duration(1));
        toolTipCalmness.setShowDelay(new Duration(1));
        toolTipJoy.setShowDelay(new Duration(1));
        toolTipNostalgia.setShowDelay(new Duration(1));
        toolTipPower.setShowDelay(new Duration(1));
        toolTipSadness.setShowDelay(new Duration(1));
        toolTipSolemnity.setShowDelay(new Duration(1));
        toolTipTenderness.setShowDelay(new Duration(1));
        toolTipTension.setShowDelay(new Duration(1));
    }

    /**
     * Displays the notes for the "Amazement" emotion.
     */
    @FXML
    void handleAmazementNoteButtonAction() {
        if(emotions.get(0).getCommento().isEmpty()){
            notesArea.setText("Non hai inserito nessuna nota per questa emozione");
        }else{
            notesArea.setText(emotions.get(0).getCommento());
        }
    }

    /**
     * Displays the notes for the "Calmness" emotion.
     */
    @FXML
    void handleCalmnessNoteButtonAction() {
        if(emotions.get(4).getCommento().isEmpty()){
            notesArea.setText("Non hai inserito nessuna nota per questa emozione");
        }else{
            notesArea.setText(emotions.get(4).getCommento());
        }
    }

    /**
     * Displays the notes for the "Joy" emotion.
     */
    @FXML
    void handleJoyNoteButtonAction() {
        if(emotions.get(6).getCommento().isEmpty()){
            notesArea.setText("Non hai inserito nessuna nota per questa emozione");
        }else{
            notesArea.setText(emotions.get(6).getCommento());
        }
    }

    /**
     * Displays the notes for the "Nostalgia" emotion.
     */
    @FXML
    void handleNostalgiaNoteButtonAction() {
        if(emotions.get(3).getCommento().isEmpty()){
            notesArea.setText("Non hai inserito nessuna nota per questa emozione");
        }else{
            notesArea.setText(emotions.get(3).getCommento());
        }
    }

    /**
     * Displays the notes for the "Power" emotion.
     */
    @FXML
    void handlePowerNoteButtonAction() {
        if(emotions.get(5).getCommento().isEmpty()){
            notesArea.setText("Non hai inserito nessuna nota per questa emozione");
        }else{
            notesArea.setText(emotions.get(5).getCommento());
        }
    }

    /**
     * Displays the notes for the "Sadness" emotion.
     */
    @FXML
    void handleSadnessNoteButtonAction() {
        if(emotions.get(8).getCommento().isEmpty()){
            notesArea.setText("Non hai inserito nessuna nota per questa emozione");
        }else{
            notesArea.setText(emotions.get(8).getCommento());
        }
    }

    /**
     * Displays the notes for the "Solemnity" emotion.
     */
    @FXML
    void handleSolemnityNoteButtonAction() {
        if(emotions.get(1).getCommento().isEmpty()){
            notesArea.setText("Non hai inserito nessuna nota per questa emozione");
        }else{
            notesArea.setText(emotions.get(1).getCommento());
        }
    }

    /**
     * Displays the notes for the "Tenderness" emotion.
     */
    @FXML
    void handleTendernessNoteButtonAction() {
        if(emotions.get(2).getCommento().isEmpty()){
            notesArea.setText("Non hai inserito nessuna nota per questa emozione");
        }else{
            notesArea.setText(emotions.get(2).getCommento());
        }
    }

    /**
     * Displays the notes for the "Tension" emotion.
     */
    @FXML
    void handleTensionNoteButtonAction() {
        if(emotions.get(7).getCommento().isEmpty()){
            notesArea.setText("Non hai inserito nessuna nota per questa emozione");
        }else{
            notesArea.setText(emotions.get(7).getCommento());
        }
    }

    /**
     * Returns to the playlist view.
     */
    @FXML
    void handleReturnToPlaylistButtonAction() {

        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("selectedPlaylist.fxml"));
            Node playlist = fxmlLoader.load();

            SelectedPlaylistController selectedPlaylistController = fxmlLoader.getController();
            selectedPlaylistController.openPlaylist(SelectedPlaylistController.getPlaylistOpened());

            EmotionalSongsClientController.setDynamicPane(playlist);

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Sets the emotions data and displays it in the user interface.<br><br>
     *
     * This method populates the user interface with emotion-related data including song name, emotions with the highest score,
     * and a bar chart depicting emotion scores. It also applies appropriate styles to the UI components based on the dominant emotion.
     *
     * @param song The song for which emotion data is being displayed.
     * @param emotions The list of emotions associated with the song.
     */
    protected void setEmotions(Canzone song, List<Emozione> emotions){

        // set the songNameLabel
        songNameLabel.setText(song.getTitolo()  + " (" + song.getAutore() + " - " + song.getAnno() + ")");

        // find the emotion with the hightest score
        Emozione emotionWithHighestScore = findEmotionWithHighestScore(emotions);
        /*
         change the border color of the emotionsPane to the color of the emotion with the
         highest score
         */
        emotionsPane.getStyleClass().add(EmozioneEnum.getInstanceName(emotionWithHighestScore));

        // inizializzo la lista emotions con la lista passata come argomento
        this.emotions = emotions;

        // set the barChart
        initBarChart(emotions);
    }

    /**
     * Initializes the bar chart with emotion data and applies styles to individual bars.<br><br>
     *
     * This method creates a bar chart and populates it with emotion data, including scores for each emotion.
     * It also applies styles to the bars based on the associated emotion.
     *
     * @param emotions The list of emotions with scores for the chart.
     */
    public void initBarChart(List<Emozione> emotions){

        XYChart.Series series = new XYChart.Series();
        series.getData().add(new XYChart.Data<>(EmozioneEnum.getInstanceName(emotions.get(0)), emotions.get(0).getPunteggio()));
        series.getData().add(new XYChart.Data<>(EmozioneEnum.getInstanceName(emotions.get(1)), emotions.get(1).getPunteggio()));
        series.getData().add(new XYChart.Data<>(EmozioneEnum.getInstanceName(emotions.get(2)), emotions.get(2).getPunteggio()));
        series.getData().add(new XYChart.Data<>(EmozioneEnum.getInstanceName(emotions.get(3)), emotions.get(3).getPunteggio()));
        series.getData().add(new XYChart.Data<>(EmozioneEnum.getInstanceName(emotions.get(4)), emotions.get(4).getPunteggio()));
        series.getData().add(new XYChart.Data<>(EmozioneEnum.getInstanceName(emotions.get(5)), emotions.get(5).getPunteggio()));
        series.getData().add(new XYChart.Data<>(EmozioneEnum.getInstanceName(emotions.get(6)), emotions.get(6).getPunteggio()));
        series.getData().add(new XYChart.Data<>(EmozioneEnum.getInstanceName(emotions.get(7)), emotions.get(7).getPunteggio()));
        series.getData().add(new XYChart.Data<>(EmozioneEnum.getInstanceName(emotions.get(8)), emotions.get(8).getPunteggio()));

        // add the series on the chart
        barChart.getData().add(series);

        // change color of each bar
        Node n = barChart.lookup(".data0.chart-bar");
        n.getStyleClass().add(EmozioneEnum.getInstanceName(emotions.get(0)) + "BarChart");
        n = barChart.lookup(".data1.chart-bar");
        n.getStyleClass().add(EmozioneEnum.getInstanceName(emotions.get(1)) + "BarChart");
        n = barChart.lookup(".data2.chart-bar");
        n.getStyleClass().add(EmozioneEnum.getInstanceName(emotions.get(2)) + "BarChart");
        n = barChart.lookup(".data3.chart-bar");
        n.getStyleClass().add(EmozioneEnum.getInstanceName(emotions.get(3)) + "BarChart");
        n = barChart.lookup(".data4.chart-bar");
        n.getStyleClass().add(EmozioneEnum.getInstanceName(emotions.get(4)) + "BarChart");
        n = barChart.lookup(".data5.chart-bar");
        n.getStyleClass().add(EmozioneEnum.getInstanceName(emotions.get(5)) + "BarChart");
        n = barChart.lookup(".data6.chart-bar");
        n.getStyleClass().add(EmozioneEnum.getInstanceName(emotions.get(6)) + "BarChart");
        n = barChart.lookup(".data7.chart-bar");
        n.getStyleClass().add(EmozioneEnum.getInstanceName(emotions.get(7)) + "BarChart");
        n = barChart.lookup(".data8.chart-bar");
        n.getStyleClass().add(EmozioneEnum.getInstanceName(emotions.get(8)) + "BarChart");

        // delate the lagend of chart
        barChart.setLegendVisible(false);

    }

    /**
     * Finds the emotion with the highest score.<br><br>
     *
     * This method iterates through a list of emotions and identifies the emotion with the highest score.
     *
     * @param emotions The list of emotions to analyze.
     * @return The emotion with the highest score.
     */
    private Emozione findEmotionWithHighestScore(List<Emozione> emotions){
        // metodo che trova l'emozione con il punteggio più alto
        Emozione e = emotions.get(0);

        for (Emozione emotion : emotions){
            if (emotion.getPunteggio() > e.getPunteggio()){
                e = emotion;
            }
        }

        return e;
    }
}
