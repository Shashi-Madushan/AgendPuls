package dev.shashi.agendaplus.controllers.finance;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class FinanceViewController {

    @FXML
    private JFXButton addBtn;

    @FXML
    private JFXButton cancelBtn;

    @FXML
    private JFXButton cataroryBtn;

    @FXML
    private JFXButton deleteBtn;

    @FXML
    private AnchorPane leftAncherPane;

    @FXML
    private AnchorPane rightAncherPane;

    public void initialize() {
       loadChart();
       loadTabel();
    }

    public void loadChart() {
        try {
            System.out.println("Loading chart...");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/finance/chart.fxml"));
            AnchorPane chartPane = loader.load();
            System.out.println("Chart loaded successfully.");

            ChartViewController controller = loader.getController();
            controller.initialize();
            System.out.println("Chart controller initialized.");
            leftAncherPane.getChildren().setAll(chartPane);

            System.out.println("chart is loaded");
        } catch (Exception e) {
            System.out.println("Failed to load chart.");
            e.printStackTrace();
        }
    }

    public  void loadTabel(){
       try{
           FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/finance/tabelView.fxml"));
           AnchorPane tabelPane = loader.load();

           TabelViewController controller = loader.getController();
           rightAncherPane.getChildren().setAll(tabelPane);
       }catch (Exception e){
           e.printStackTrace();
       }

    }

    @FXML
    void addBtnOnClickAction(ActionEvent event) {

    }

    @FXML
    void cagagoryBtnOnAction(ActionEvent event) {

    }

    @FXML
    void cancelBtnOnAction(ActionEvent event) {

    }

    @FXML
    void deleteBtnOnAction(ActionEvent event) {

    }

}
