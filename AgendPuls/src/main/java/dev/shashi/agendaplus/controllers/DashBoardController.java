package dev.shashi.agendaplus.controllers;

import com.jfoenix.controls.JFXButton;
import dev.shashi.agendaplus.controllers.calander.dayview.DayViewController;
import dev.shashi.agendaplus.controllers.calander.monthview.MonthView;
import dev.shashi.agendaplus.controllers.calander.monthview.MonthViewController;
import dev.shashi.agendaplus.controllers.calander.weekview.WeekView;
import dev.shashi.agendaplus.controllers.calander.weekview.WeekViewController;
import dev.shashi.agendaplus.controllers.finance.FinanceViewController;
import dev.shashi.agendaplus.reminders.ReminderManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.Initializable;

import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ResourceBundle;



public class DashBoardController implements Initializable{

    @FXML
    private JFXButton calenderDayViewBtn;

    @FXML
    private JFXButton calenderMonthViewBtn;

    @FXML
    private JFXButton calenderWeekViewBtn;

    @FXML
    public  AnchorPane centerViewAnchorPane;

    @FXML
    private JFXButton financeViewBtn;


    String fromfromeMonthOrWeekView = "today";


    @Override
    public void initialize(URL location, ResourceBundle resources) {


        homeButtonOnclick(null);
    }
    private LocalDate date = LocalDate.now();

    @FXML
    public void homeButtonOnclick(ActionEvent event) {
        try {
            // Load the FXML for the day view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/dashboard/home.fxml"));
            Node dayView = loader.load();

            HomeController controller= loader.getController();
            controller.setDashBoardController(this);

            centerViewAnchorPane.getChildren().setAll(dayView);

            // Optionally, if the day view has specific styles, apply them
            Scene scene = centerViewAnchorPane.getScene();
            if (scene != null) {
                scene.getStylesheets().add(getClass().getResource("").toExternalForm());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setFromfromeMonthOrWeekView(String fromfromeMonthOrWeekView) {
        this.fromfromeMonthOrWeekView = fromfromeMonthOrWeekView;
    }

    public void showTodaysTask(LocalDate date ){

        try {
            // Load the FXML for the day view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/calander/dayView.fxml"));
            Node dayView = loader.load();

            DayViewController controller = loader.getController();
            controller.setFromeMonthOrWeekView(fromfromeMonthOrWeekView);
            controller.setDashBoardController(this);


            controller.setDate(date);
            controller.initialize();
            // Clear existing content and add the new day view
            centerViewAnchorPane.getChildren().setAll(dayView);


            // Optionally, if the day view has specific styles, apply them
            Scene scene = centerViewAnchorPane.getScene();
            if (scene != null) {
                scene.getStylesheets().add(getClass().getResource("/stylees/dayViewStyles.css").toExternalForm());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void showCalenderMonthViewOnClick(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/calander/moth_view.fxml"));
            Node monthView = loader.load();

            MonthViewController controller = loader.getController();

            MonthView monthView1 = new MonthView(YearMonth.now(),this);

            controller.monthViewCalendarPane.getChildren().add(monthView1.getView());

            centerViewAnchorPane.getChildren().setAll(monthView);


            Scene scene = centerViewAnchorPane.getScene();
            if (scene != null) {
                scene.getStylesheets().add(getClass().getResource("/stylees/monthViewStyles.css").toExternalForm());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showCalenderWeekViewOnClick(ActionEvent event) {
        try {
            LocalDate weekStart = LocalDate.now();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/calander/week_view.fxml"));
            Node weekView = loader.load();
            WeekViewController controller = loader.getController();

            WeekView weekView1=new WeekView(weekStart,this);


            controller.weekViewContainer.getChildren().add(weekView1.getView());
            centerViewAnchorPane.getChildren().setAll(weekView);

            Scene scene = centerViewAnchorPane.getScene();

            if (scene != null) {
                scene.getStylesheets().add(getClass().getResource("/stylees/weekViewStyles.css").toExternalForm());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    void showFinanceViewOnClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/finance/financeView.fxml"));
            Node financeView = loader.load();
            FinanceViewController controller= loader.getController();
            controller.initialize();
            centerViewAnchorPane.getChildren().setAll(financeView);

            Scene scene = centerViewAnchorPane.getScene();
            if (scene != null) {
                scene.getStylesheets().add(getClass().getResource("/stylees/financceViewStyles.css").toExternalForm());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
