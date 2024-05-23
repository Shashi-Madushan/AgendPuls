package dev.shashi.agendaplus.controllers.calander.monthview;

import dev.shashi.agendaplus.controllers.DashBoardController;
import dev.shashi.agendaplus.controllers.calander.nodes.DayNode;
import dev.shashi.agendaplus.repo.TaskRepo;
import javafx.geometry.Pos;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import com.jfoenix.controls.JFXButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

import static dev.shashi.agendaplus.repo.TaskRepo.getTaskDates;

public class MonthView {
    private ArrayList<DayNode> allCalendarDays = new ArrayList<>(35);
    private VBox view;
    private Text calendarTitle;
    private YearMonth currentYearMonth;







    DashBoardController dashBoardController;
    // Existing constructor
    public MonthView(YearMonth yearMonth , DashBoardController dashBoardController) {
        this.dashBoardController=dashBoardController;
        initializeView(yearMonth);
    }



    // New constructor that does not require a YearMonth parameter
    public MonthView() {
        // Use the current year and month
    }

    private void initializeView(YearMonth yearMonth) {
        currentYearMonth = yearMonth;
        GridPane calendar = new GridPane();
        calendar.setPrefSize(738, 410);

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                DayNode ap = new DayNode();
                ap.setDashBoardController(this.dashBoardController);
                ap.setFromeMonthOrWeekView("month");
                ap.setPrefSize(150,150);
                ap.getStyleClass().add("day-node");
                calendar.add(ap,j,i);
                allCalendarDays.add(ap);
            }
        }

        Text[] dayNames = new Text[]{ new Text("Sunday"), new Text("Monday"), new Text("Tuesday"),
                new Text("Wednesday"), new Text("Thursday"), new Text("Friday"),
                new Text("Saturday") };
        GridPane dayLabels = new GridPane();
        dayLabels.setPrefWidth(600);
        Integer col = 0;

        for (Text txt : dayNames) {
            AnchorPane ap = new AnchorPane();
            ap.setPrefSize(200, 10);

            ap.setBottomAnchor(txt, 5.0);
            txt.getStyleClass().add("day-labels");
            txt.getStyleClass().add("custom-day-name");
            txt.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 14px; -fx-fill: #333;");
            ap.getChildren().add(txt);
            dayLabels.add(ap, col++, 0);
        }

        calendarTitle = new Text();
        calendarTitle.getStyleClass().add("calendar-title");
        calendarTitle.setFill(Color.WHITE);
        calendarTitle.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 16px; -fx-font-weight: bold;");
        calendarTitle.setWrappingWidth(200);
        calendarTitle.setTextAlignment(TextAlignment.CENTER);

        JFXButton previousMonth = new JFXButton();
        previousMonth.getStyleClass().add("button");
        ImageView prevIcon = new ImageView(new Image("/assets/leftArrow.png"));
        prevIcon.setFitHeight(20);
        prevIcon.setFitWidth(20);
        Circle clipPrev = new Circle(10, 10, 10);
        prevIcon.setClip(clipPrev);
        previousMonth.setGraphic(prevIcon);
        previousMonth.setOnAction(e -> previousMonth());

        JFXButton nextMonth = new JFXButton();
        nextMonth.getStyleClass().add("button");
        ImageView nextIcon = new ImageView(new Image("/assets/rightArrow.png"));
        nextIcon.setFitHeight(20);
        nextIcon.setFitWidth(20);
        Circle clipNext = new Circle(10, 10, 10);
        nextIcon.setClip(clipNext);
        nextMonth.setGraphic(nextIcon);
        nextMonth.setOnAction(e -> nextMonth());

        HBox titleBar = new HBox(previousMonth, calendarTitle, nextMonth);
        titleBar.setAlignment(Pos.CENTER);
        titleBar.setSpacing(10);
        titleBar.getStyleClass().add("title-bar");

        populateCalendar(yearMonth);
        view = new VBox(titleBar, dayLabels, calendar);
    }



    public void populateCalendar(YearMonth yearMonth) {
        LocalDate firstDayOfMonth = LocalDate.of(yearMonth.getYear(), yearMonth.getMonthValue(), 1);
        List<LocalDate> taskDates = TaskRepo.getTaskDates(yearMonth, firstDayOfMonth);

        LocalDate calendarDate = firstDayOfMonth;
        while (!calendarDate.getDayOfWeek().toString().equals("SUNDAY")) {
            calendarDate = calendarDate.minusDays(1);
        }
        for (DayNode ap : allCalendarDays) {
            // Clear existing children from the DayNode
            ap.getChildren().clear();
            ap.setDashBoardController(dashBoardController);
            Text txt = new Text(String.valueOf(calendarDate.getDayOfMonth()));
            ap.setDate(calendarDate);
            ap.setTopAnchor(txt, 5.0);
            ap.setLeftAnchor(txt, 5.0);
            ap.getChildren().add(txt);
            ap.updateStyle(calendarDate.getMonthValue() == yearMonth.getMonthValue());

            // Only add a rectangle if there are tasks and the date matches
            if (!taskDates.isEmpty() && taskDates.contains(calendarDate) && calendarDate.getMonthValue() == yearMonth.getMonthValue()) {
                Rectangle taskIndicator = new Rectangle(50, 20, Color.DODGERBLUE);
                taskIndicator.setArcWidth(15);
                taskIndicator.setArcHeight(15);
                ap.getChildren().add(taskIndicator);
                ap.setBottomAnchor(taskIndicator, 30.0);
                ap.setRightAnchor(taskIndicator, 10.0);
            }

            calendarDate = calendarDate.plusDays(1);
        }
        calendarTitle.setText(yearMonth.getMonth().toString() + " " + yearMonth.getYear());
    }

    private void previousMonth() {
        currentYearMonth = currentYearMonth.minusMonths(1);
        populateCalendar(currentYearMonth);
    }

    private void nextMonth() {
        currentYearMonth = currentYearMonth.plusMonths(1);
        populateCalendar(currentYearMonth);
    }

    public VBox getView() {
        return view;
    }

    public ArrayList<DayNode> getAllCalendarDays() {
        return allCalendarDays;
    }

    public void setAllCalendarDays(ArrayList<DayNode> allCalendarDays) {
        this.allCalendarDays = allCalendarDays;
    }



}