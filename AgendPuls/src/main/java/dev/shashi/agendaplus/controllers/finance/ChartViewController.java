package dev.shashi.agendaplus.controllers.finance;

import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

public class ChartViewController {

    @FXML
    private PieChart chart;

    public void initialize() {

        chart.setPrefSize(300, 300);
        addDummyDataToChart();
        applyColorsToChart();
    }

    private void addDummyDataToChart() {

        ObservableList<Data> pieChartData = FXCollections.observableArrayList(
                new Data("Category A", 30),
                new Data("Category B", 20),
                new Data("Category C", 35),
                new Data("Category D", 15)
        );

        chart.setData(pieChartData);
        chart.setLabelsVisible(false);
        chart.getData().forEach(data ->
                data.nameProperty().bind(
                        javafx.beans.binding.Bindings.concat(
                                data.getName(), " ", javafx.beans.binding.Bindings.format("%.1f%%", data.pieValueProperty().divide(chart.getData().stream().mapToDouble(Data::getPieValue).sum()).multiply(100))
                        )
                )
        );
    }

    private void applyColorsToChart() {
        System.out.println("Applying colors to chart");
        Color[] colors = new Color[]{Color.RED, Color.GREEN, Color.BLUE};
        int colorIndex = 0;

        for (Data data : chart.getData()) {
            data.getNode().setStyle("-fx-pie-color: " + toRGBCode(colors[colorIndex % colors.length]) + ";");
            colorIndex++;
        }
    }

    private String toRGBCode(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }
}