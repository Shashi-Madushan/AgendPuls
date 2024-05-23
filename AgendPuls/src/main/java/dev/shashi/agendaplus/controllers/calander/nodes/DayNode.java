package dev.shashi.agendaplus.controllers.calander.nodes;


import dev.shashi.agendaplus.controllers.DashBoardController;

import javafx.animation.TranslateTransition;

import javafx.scene.Node;

import javafx.scene.layout.AnchorPane;

import javafx.stage.Stage;

import javafx.util.Duration;


import java.time.LocalDate;

public class DayNode extends AnchorPane {


    DashBoardController dashBoardController;
    private LocalDate date;


    private String fromeMonthOrWeekView = "today";


    public DayNode(Node... children) {
        super(children);


        this.setOnMouseClicked(e -> {
            System.out.println("This pane's date is: " + date);
            System.out.println(fromeMonthOrWeekView);

            try {
                dashBoardController.setFromfromeMonthOrWeekView(fromeMonthOrWeekView);
                dashBoardController.showTodaysTask(date);


            } catch (Exception ex) {
                ex.printStackTrace();
            }


        });


    }

    private void shakeStage(Stage stage) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(50), stage.getScene().getRoot());
        tt.setFromX(0);
        tt.setByX(10);
        tt.setCycleCount(6);
        tt.setAutoReverse(true);
        tt.play();
    }

    public void updateStyle(boolean isCurrentMonth) {
        this.getStyleClass().removeAll("day-today", "day-current-month", "day-other-month");

        boolean isToday = LocalDate.now().equals(date);
        if (isToday) {
            this.getStyleClass().add("day-today");
        } else if (isCurrentMonth) {
            this.getStyleClass().add("day-current-month");
        } else {
            this.getStyleClass().add("day-other-month");
        }
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setDashBoardController(DashBoardController dashBoardController) {
        this.dashBoardController = dashBoardController;
    }

    public void setFromeMonthOrWeekView(String fromeMonthOrWeekView) {
        this.fromeMonthOrWeekView = fromeMonthOrWeekView;
    }


}