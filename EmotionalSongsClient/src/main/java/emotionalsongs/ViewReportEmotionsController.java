package emotionalsongs;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.List;

/*
TODO:
 1- finire implementazione
 2- creare metodo set emotions che mi va a settare il nome della canzone e tutto il resto
 */
public class ViewReportEmotionsController {

    @FXML private VBox reportEmotionsPane;
    @FXML private Button returnBtn;
    @FXML private BarChart<String, Integer> barChart;
    @FXML private Label songNameLabel;
    @FXML private Label infoUsersLabel;
    @FXML private Label infoAverageLabel;

    /**
     * TODO document
     */
    @FXML
    public void handleReturnButtonAction(){
        // TODO implementare cosa deve accadere quando viene premuto il pulsante
    }

    /**
     * TODO document
     * @param emotions
     */
    public void initBarChart(List<Emozione> emotions){

        /*
         TODO modificare metodo, come paramentro prendere l'array di double che mi restituisce il
            metodo del server
         */

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

    }

    /**
     * TODO document
     * @param emotions
     * @return
     */
    private Emozione findEmotionWithHighestAverage(List<Emozione> emotions){

        /* TODO modificare metodo, come paramentro prendere l'array di double che mi restituisce il
            metodo del server di conseguenza il confronto non deve avvenire sul punteggio delle emozioni ma
            sulle medie restituite

         */

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
