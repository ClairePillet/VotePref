/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Chart;

import VoteSMA.CsvHelper;
import VoteSMA.VoteSMA;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import org.apache.logging.log4j.LogManager;

public class Chart extends Application {

    static String[] args;
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(Chart.class);
    File file;
    String dir;
    public void chartVoteEvol(Stage stage) {
        stage.setTitle("Line Chart Sample");
        Parameters parameters = this.getParameters();
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("turn");
        yAxis.setLabel("number of voter");

        List<String> t = parameters.getRaw();
        int countFile =1;
 
        while (countFile <= Integer.parseInt(t.get(2))) {
            try {
                final LineChart<Number, Number> lineChart
                        = new LineChart<Number, Number>(xAxis, yAxis);
                lineChart.setTitle("Opinion Evolution ");
                CsvHelper csv = new CsvHelper(",", t.get(1) + countFile + ".csv", false,dir);
                List<String> readFile = csv.readFile();
                List<XYChart.Series> lstSeries = new ArrayList<>();
                boolean firstLine = true;
                int count = 0;
              
                for (String s : readFile) {

                    String[] parsedLine = csv.parseLine(s);
                    if (firstLine) {
                        for (String col : parsedLine) {

                            firstLine = false;
                            XYChart.Series ser = new XYChart.Series();
                            ser.setName(col);
                            lstSeries.add(ser);

                        }
                    } else {
                        for (int i = 0; i < lstSeries.size(); i++) {
                            lstSeries.get(i).getData().add(new XYChart.Data(count, Integer.parseInt(parsedLine[i])));
                        }
                    }
                    count++;

                }
                Scene scene = new Scene(lineChart, 800, 600);
                for (int i = 0; i < lstSeries.size(); i++) {
                    lineChart.getData().add(lstSeries.get(i));
                }

                stage.setScene(scene);
                saveAsPng(scene, file.getAbsolutePath() + File.separator + dir + File.separator + "detailed" + new java.util.Date().getTime() + ".png");
                stage.show();
                stage.close();
                countFile++;
            } catch (Exception ex) {
                logger.error(ex);
            }
        }
    }

    public void chartTerminaisonSwitch(Stage stage) throws IOException {
        stage.setTitle("Line Chart Sample");
        Parameters parameters = this.getParameters();
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("number of end");
        yAxis.setLabel("average degree");
        final BarChart<String, Number> barChart = new BarChart<String, Number>(xAxis, yAxis);
        barChart.setTitle("Terminaison ");
        List<String> t = parameters.getRaw();
        CsvHelper csv = new CsvHelper(",", t.get(0), false,dir);
        List<String> readFile = csv.readFile();
        List<String> lstSeries = new ArrayList<>();
        boolean firstLine = true;
        int count = 0;
        HashMap<String, Integer> trueMap = new HashMap<String, Integer>();//degMo number
        HashMap<String, Integer> falseMap = new HashMap<String, Integer>();//degMo number
        for (String s : readFile) {

            String[] parsedLine = csv.parseLine(s);
            if (firstLine) {
                for (String col : parsedLine) {
                    firstLine = false;
                    lstSeries.add(col);

                }
            } else {
                if (parsedLine[lstSeries.indexOf("end")].equals("true")) {

                    trueMap.put(parsedLine[lstSeries.indexOf("degMoy")], trueMap.getOrDefault(parsedLine[lstSeries.indexOf("degMoy")], 0) + 1);
                } else {
                    falseMap.put(parsedLine[lstSeries.indexOf("degMoy")], trueMap.getOrDefault(parsedLine[lstSeries.indexOf("degMoy")], 0) + 1);
                }
            }
            count++;

        }

        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("true");
        for (Map.Entry<String, Integer> entry : trueMap.entrySet()) {
            String key = entry.getKey();
            Integer tmpValue = entry.getValue();
            XYChart.Data<String, Number> d = new XYChart.Data<>(key, tmpValue);

            series1.getData().add(d);
        }
        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        series2.setName("false");
        for (Map.Entry<String, Integer> entry : falseMap.entrySet()) {
            String key = entry.getKey();
            Integer tmpValue = entry.getValue();
            XYChart.Data<String, Number> d = new XYChart.Data<>(key, tmpValue);

            series2.getData().add(d);
        }
        Scene scene = new Scene(barChart, 800, 600);
        barChart.getData().add(series1);
        barChart.getData().add(series2);

        stage.setScene(scene);
        saveAsPng(scene, file.getAbsolutePath() + File.separator +dir + File.separator + "Global" + new java.util.Date().getTime() + ".png");
        stage.show();
        stage.close();
    }

    public void DeggreeChart(Stage stage) {

        stage.setTitle("Degree Distribution");
        Parameters parameters = this.getParameters();
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Degree");
        yAxis.setLabel("Number of nodes");
        final LineChart<Number, Number> lineChart
                = new LineChart<Number, Number>(xAxis, yAxis);

        lineChart.setTitle("Degree Distribution");
        XYChart.Series series = new XYChart.Series();
        List<String> t = parameters.getRaw();
        for (int i = 2; i < t.size() - 1; i++) {
            System.out.println(parameters.getRaw().get(i));
            series.getData().add(new XYChart.Data(Integer.parseInt(parameters.getRaw().get(i)), Integer.parseInt(parameters.getRaw().get(i + 1))));
            i++;
        }

        Scene scene = new Scene(lineChart, 800, 600);
        lineChart.getData().add(series);
        saveAsPng(scene, file.getAbsolutePath() + File.separator + "chartImage" + File.separator + "Chart" + new java.util.Date().getTime() + ".png");
        stage.show();
        stage.close();
    }

    @Override
    public void start(Stage stage) {

        try {
            file= new File("");
              dir=String.valueOf(Main.DIFFUSIONTYPE);
            if(Main.GRAPHETYPE==1){
              dir=  dir.concat(String.valueOf(Main.PROBAEDGE));
            }else{
               dir= dir.concat(String.valueOf(Main.NBBASENODE));
            }
            chartTerminaisonSwitch(stage);
            chartVoteEvol(stage);
            //  DeggreeChart(stage);
            System.exit(0);
        } catch (Exception ex) {
            logger.error(ex.toString());
        }
    }

    public void saveAsPng(Scene scene, String path) {
        WritableImage image = scene.snapshot(null);
        File file = new File(path);
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException e) {
            logger.error(e);
        }
    }

    public static void main(String[] args) {
        Chart.args = args;
        launch(args);
    }
}
