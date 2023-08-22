package emotionalsongs;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/*
 TODO
    1- PROBLEMA: non riesco a visualizzare il colore della barra del sadness, sembra quasi che il numero
       totale di colori impostabili sia 8.
 */
public class ViewEmotionsController implements Initializable{

    @FXML
    private VBox emotionsPane;
    @FXML
    private Button amazementNoteBtn;
    @FXML
    private Button calmnessNoteBtn;
    @FXML
    private Button joyNoteBtn;
    @FXML
    private Button nostalgiaNoteBtn;
    @FXML
    private Button powerNoteBtn;
    @FXML
    private Button sadnessNoteBtn;
    @FXML
    private Button solemnityNoteBtn;
    @FXML
    private Button tendernessNoteBtn;
    @FXML
    private Button tensionNoteBtn;
    @FXML
    private Button returnToPlaylistBtn;
    @FXML
    private Tooltip toolTipAmazement;
    @FXML
    private Tooltip toolTipCalmness;
    @FXML
    private Tooltip toolTipJoy;
    @FXML
    private Tooltip toolTipNostalgia;
    @FXML
    private Tooltip toolTipPower;
    @FXML
    private Tooltip toolTipSadness;
    @FXML
    private Tooltip toolTipSolemnity;
    @FXML
    private Tooltip toolTipTenderness;
    @FXML
    private Tooltip toolTipTension;
    @FXML
    private Label songNameLabel;
    @FXML
    private TextArea notesArea;
    @FXML
    private BarChart<String, Integer> barChart;

    private List<Emozione> emotions;

    /**
     * TODO document
     * @param url
     * @param resourceBundle
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
     * TODO document
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
     * TODO document
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
     * TODO document
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
     * TODO document
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
     * TODO document
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
     * TODO document
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
     * TODO document
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
     * TODO document
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
     * TODO document
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
     * TODO document
     */
    @FXML
    void handleReturnToPlaylistButtonAction() {
        // TODO ritornare alla playlist in cui è contenuta la canzone di cui si è vista l'emozione

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
     * TODO document
     * @param emotions
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
        emotionsPane.getStyleClass().add(EmozioneEnum.getEmotionNameFromInstance(emotionWithHighestScore));

        // inizializzo la lista emotions con la lista passata come argomento
        this.emotions = emotions;

        // set the barChart
        initBarChart(emotions);
    }

    /**
     * TODO document
     * @param emotions
     */
    public void initBarChart(List<Emozione> emotions){

        XYChart.Series series = new XYChart.Series();
        series.getData().add(new XYChart.Data<>(EmozioneEnum.getEmotionNameFromInstance(emotions.get(0)), emotions.get(0).getPunteggio()));
        series.getData().add(new XYChart.Data<>(EmozioneEnum.getEmotionNameFromInstance(emotions.get(1)), emotions.get(1).getPunteggio()));
        series.getData().add(new XYChart.Data<>(EmozioneEnum.getEmotionNameFromInstance(emotions.get(2)), emotions.get(2).getPunteggio()));
        series.getData().add(new XYChart.Data<>(EmozioneEnum.getEmotionNameFromInstance(emotions.get(3)), emotions.get(3).getPunteggio()));
        series.getData().add(new XYChart.Data<>(EmozioneEnum.getEmotionNameFromInstance(emotions.get(4)), emotions.get(4).getPunteggio()));
        series.getData().add(new XYChart.Data<>(EmozioneEnum.getEmotionNameFromInstance(emotions.get(5)), emotions.get(5).getPunteggio()));
        series.getData().add(new XYChart.Data<>(EmozioneEnum.getEmotionNameFromInstance(emotions.get(6)), emotions.get(6).getPunteggio()));
        series.getData().add(new XYChart.Data<>(EmozioneEnum.getEmotionNameFromInstance(emotions.get(7)), emotions.get(7).getPunteggio()));
        series.getData().add(new XYChart.Data<>(EmozioneEnum.getEmotionNameFromInstance(emotions.get(8)), emotions.get(8).getPunteggio()));

        // add the series on the chart
        barChart.getData().add(series);

        // change color of each bar
        Node n = barChart.lookup(".data0.chart-bar");
        n.getStyleClass().add(EmozioneEnum.getEmotionNameFromInstance(emotions.get(0)) + "BarChart");
        n = barChart.lookup(".data1.chart-bar");
        n.getStyleClass().add(EmozioneEnum.getEmotionNameFromInstance(emotions.get(1)) + "BarChart");
        n = barChart.lookup(".data2.chart-bar");
        n.getStyleClass().add(EmozioneEnum.getEmotionNameFromInstance(emotions.get(2)) + "BarChart");
        n = barChart.lookup(".data3.chart-bar");
        n.getStyleClass().add(EmozioneEnum.getEmotionNameFromInstance(emotions.get(3)) + "BarChart");
        n = barChart.lookup(".data4.chart-bar");
        n.getStyleClass().add(EmozioneEnum.getEmotionNameFromInstance(emotions.get(4)) + "BarChart");
        n = barChart.lookup(".data5.chart-bar");
        n.getStyleClass().add(EmozioneEnum.getEmotionNameFromInstance(emotions.get(5)) + "BarChart");
        n = barChart.lookup(".data6.chart-bar");
        n.getStyleClass().add(EmozioneEnum.getEmotionNameFromInstance(emotions.get(6)) + "BarChart");
        n = barChart.lookup(".data7.chart-bar");
        n.getStyleClass().add(EmozioneEnum.getEmotionNameFromInstance(emotions.get(7)) + "BarChart");
        n = barChart.lookup(".data8.chart-bar");
        n.getStyleClass().add(EmozioneEnum.getEmotionNameFromInstance(emotions.get(8)) + "BarChart");

        // delate the lagend of chart
        barChart.setLegendVisible(false);

        // DEBUG TODO remove
        for(int i = 0; i < emotions.size(); i++){
            System.out.println("Visualizzo l'emozione " + EmozioneEnum.getEmotionNameFromInstance(emotions.get(i)) + " con punteggio: " + emotions.get(i).getPunteggio() + " --- note: " + emotions.get(i).getCommento());
        }

    }

    /**
     * TODO document
     * @param emotions
     * @return
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
