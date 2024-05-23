package dev.shashi.agendaplus.controllers;

import com.jfoenix.controls.JFXListView;
import dev.shashi.agendaplus.model.Reminder;
import dev.shashi.agendaplus.reminders.ReminderManager;
import dev.shashi.agendaplus.repo.TaskRepo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class HomeController implements Initializable {

    @FXML
    private JFXListView<String> remindersListView;

    @FXML
    private Label doneTaskCountShowLbl2;

    @FXML
    private Label totalTaskCountShowLbl2;

    DashBoardController dashBoardController;

    @FXML
    private Rectangle allTaskShowRect;

    @FXML
    private Label dateLabel;

    @FXML
    private Label doneTaskCountShowLbl;


    @FXML
    private ProgressBar taskProgressBar;


    @FXML
    private Label hoursLabel;

    @FXML
    private Label minutesLabel;  // Corrected label name

    @FXML
    private Label secondsLabel;

    @FXML
    private Label totalTaskCountShowLbl;

    @FXML
    private Label colonLabel1;   // Label for the first colon

    private LocalDate currentDate = LocalDate.now();
    int totalTaskCount = TaskRepo.getTaskCount(currentDate);
    int doneTaskCount = TaskRepo.getDoneTaskCount(currentDate);


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateTodaysTaskCount();
        updateTodaysDoneTaskCount();
        showCurrentDate();
        showCurrentTime();
        ReminderManager.homeController= this;
        setRemindersListView();
        setupBlinkingColons();
        updateTaskProgressBar();
    }

    public void setRemindersListView() {
        List<Reminder> reminders = ReminderManager.getReminders();
        ObservableList<String> items = FXCollections.observableArrayList();
        if (reminders != null) {
            for (Reminder reminder : reminders) {
                items.add(reminder.getTaskTitle() + " - " + reminder.getReminderTime().toString());
            }
        } else {
            items.add("null");
        }
        remindersListView.setItems(items);
    }
    private void updateTodaysDoneTaskCount() {

        doneTaskCountShowLbl.setText(String.valueOf(doneTaskCount));
        doneTaskCountShowLbl2.setText(String.valueOf(doneTaskCount));

    }

    private void updateTodaysTaskCount() {

        totalTaskCountShowLbl.setText(String.valueOf(totalTaskCount));
        totalTaskCountShowLbl2.setText(String.valueOf(totalTaskCount));

    }

    private void updateTaskProgressBar() {


        if (totalTaskCount > 0) {
            double progress = (double) doneTaskCount / totalTaskCount;
            taskProgressBar.setProgress(progress);
        } else {
            taskProgressBar.setProgress(0);
        }
    }

    @FXML
    void doneTaskShowRectOnAction(MouseEvent event) {
    }

    public DashBoardController getDashBoardController() {
        return dashBoardController;
    }

    public void setDashBoardController(DashBoardController dashBoardController) {
        this.dashBoardController = dashBoardController;
    }

    public void showCurrentDate() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        dateLabel.setText(currentDate.format(dateFormatter));
    }

    public void showCurrentTime() {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime currentTime = LocalTime.now();
            hoursLabel.setText(String.format("%02d", currentTime.getHour()));
            minutesLabel.setText(String.format("%02d", currentTime.getMinute()));
            secondsLabel.setVisible(false);
            secondsLabel.setText(String.format("%02d", currentTime.getSecond()));
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Timeline.INDEFINITE);
        clock.play();
    }

    public void setupBlinkingColons() {
        colonLabel1.setText(":");

        Timeline blinker = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            colonLabel1.setVisible(!colonLabel1.isVisible());

        }), new KeyFrame(Duration.seconds(1)));
        blinker.setCycleCount(Timeline.INDEFINITE);
        blinker.play();
    }

    public void allTAskShowRectOnMouseClick(MouseEvent mouseEvent) {
        dashBoardController.showTodaysTask(currentDate);
    }
}