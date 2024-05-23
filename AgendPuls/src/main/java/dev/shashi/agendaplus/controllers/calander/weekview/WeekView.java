package dev.shashi.agendaplus.controllers.calander.weekview;

import dev.shashi.agendaplus.controllers.DashBoardController;
import dev.shashi.agendaplus.controllers.calander.nodes.DayNode;
import dev.shashi.agendaplus.model.Task;
import dev.shashi.agendaplus.repo.TaskRepo;
import javafx.geometry.Pos;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import com.jfoenix.controls.JFXButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.time.LocalDate;
import java.util.ArrayList;
import java.time.DayOfWeek;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import static dev.shashi.agendaplus.repo.TaskRepo.getTaskDates;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

public class WeekView {
    private ArrayList<DayNode> weekDays = new ArrayList<>(7);
    private VBox view;
    private Text weekTitle;
    private LocalDate currentWeekStart;
    private GridPane dayLabels;



    DashBoardController dashBoardController;
    public WeekView(LocalDate weekStart ,DashBoardController dashBoardController ) {
        this.dashBoardController =dashBoardController;

        currentWeekStart = weekStart.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));// Ensure the week starts on Sunday
        GridPane weekGrid = new GridPane();
        weekGrid.setPrefSize(738, 410);

        for (int i = 0; i < 7; i++) {
            DayNode dayNode = new DayNode();
            dayNode.setDashBoardController(this.dashBoardController);
            dayNode.setFromeMonthOrWeekView("week");
            dayNode.setPrefSize(105, 410);

            dayNode.getStyleClass().add("day-node");
            weekGrid.add(dayNode, i, 0);
            weekDays.add(dayNode);
        }

        initializeDayLabels();

        weekTitle = new Text();
        weekTitle.getStyleClass().add("calendar-title");
       weekTitle.setFill(Color.WHITE);
        weekTitle.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 16px; -fx-font-weight: bold;");
        weekTitle.setTextAlignment(TextAlignment.CENTER);

        JFXButton previousWeek = createNavigationButton("/assets/leftArrow.png", this::previousWeek);
        JFXButton nextWeek = createNavigationButton("/assets/rightArrow.png", this::nextWeek);

        HBox titleBar = new HBox(previousWeek, weekTitle, nextWeek);
        titleBar.getStyleClass().add("title-bar");
        titleBar.setAlignment(Pos.CENTER);
        titleBar.setSpacing(10);

        populateWeek(currentWeekStart);
        view = new VBox(titleBar, dayLabels, weekGrid);
    }


    private void initializeDayLabels() {
        String[] dayNames = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        dayLabels = new GridPane();
        dayLabels.setPrefWidth(600);
        Integer col = 0;

        for (String dayName : dayNames) {
            AnchorPane ap = new AnchorPane();
            ap.setPrefSize(200, 10);
            Text dayText = new Text(dayName);
            dayText.getStyleClass().add("day-labels");
            dayText.getStyleClass().add("custom-day-name"); // Additional class for further customization
            dayText.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 14px; -fx-fill: #333;");
            AnchorPane.setBottomAnchor(dayText, 5.0);
            ap.getChildren().add(dayText);
            dayLabels.add(ap, col++, 0);
        }
    }

    private JFXButton createNavigationButton(String imagePath, Runnable action) {
        JFXButton button = new JFXButton();
        button.getStyleClass().addAll("button");
        ImageView icon = new ImageView();
        try {
            Image image = new Image(imagePath);
            icon.setImage(image);
            icon.setFitHeight(20);
            icon.setFitWidth(20);
        } catch (Exception e) {
            System.out.println("Error loading image: " + e.getMessage());
            icon.setImage(new Image("path/to/default/icon.png"));
        }
        button.setGraphic(icon);
        button.setOnAction(e -> action.run());
        return button;
    }

    private void populateWeek(LocalDate startOfWeek) {
        for (int i = 0; i < weekDays.size(); i++) {
            DayNode dayNode = weekDays.get(i);
            LocalDate date = startOfWeek.plusDays(i);

            dayNode.getChildren().clear(); // Clear previous children if any
            Text dayText = new Text(String.valueOf(date.getDayOfMonth())); // Create text with day number
            dayNode.getChildren().add(dayText); // Add text to DayNode
            AnchorPane.setTopAnchor(dayText, 5.0); // Position the text within the DayNode
            AnchorPane.setLeftAnchor(dayText, 5.0);

            List<Task> tasks = TaskRepo.getTasks(date); // Get the tasks for the date
            if (!tasks.isEmpty()) {
                VBox taskContainer = new VBox();
                taskContainer.setSpacing(5.0);
                AnchorPane.setTopAnchor(taskContainer, 30.0);
                AnchorPane.setLeftAnchor(taskContainer, 5.0);

                for (Task task : tasks) {
                    AnchorPane taskPane = new AnchorPane();
                    taskPane.setPrefSize(80, 20);
                    taskPane.setStyle("-fx-background-color: #3167f1; -fx-background-radius: 10; -fx-border-radius: 10");

                    Text taskText = new Text(task.getTitle());
                    taskText.setFill(Color.WHITE);
                    taskText.setTextAlignment(TextAlignment.CENTER);
                    AnchorPane.setLeftAnchor(taskText, 10.0);
                    AnchorPane.setTopAnchor(taskText, 0.0);
                    taskPane.getChildren().add(taskText);

                    taskContainer.getChildren().add(taskPane);
                }

                dayNode.getChildren().add(taskContainer);// Add the container to the DayNode
            }

            dayNode.setDate(date);
            dayNode.updateStyle(true); // Update style now that the date is set
        }
        weekTitle.setText("Week of " + startOfWeek.toString());
    }
    private void previousWeek() {
        currentWeekStart = currentWeekStart.minusWeeks(1);
        populateWeek(currentWeekStart);
    }

    private void nextWeek() {
        currentWeekStart = currentWeekStart.plusWeeks(1);
        populateWeek(currentWeekStart);
    }

    public VBox getView() {
        return view;
    }


}