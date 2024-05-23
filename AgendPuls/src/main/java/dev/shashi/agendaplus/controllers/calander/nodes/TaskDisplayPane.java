package dev.shashi.agendaplus.controllers.calander.nodes;

import com.jfoenix.controls.JFXCheckBox;
import dev.shashi.agendaplus.controllers.calander.dayview.DayViewController;
import dev.shashi.agendaplus.model.Task;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseEvent;

import static dev.shashi.agendaplus.repo.TaskRepo.updateTaskStatus;

public class TaskDisplayPane extends AnchorPane {
    private Task task;
    private DayViewController dayViewController;
    private Text taskTitle; // Declare taskTitle as an instance variable

    public TaskDisplayPane(Task task, DayViewController dayViewController) {
        this.task = task;
        this.dayViewController = dayViewController;
        setupTaskTitle();
        setupDoneCheckBox();
        setupHoverEffect();
        setInitialStyle();
        updateTaskStyle();
    }

    private void setupTaskTitle() {
        taskTitle = new Text(task.getTitle().toUpperCase()); // Initialize taskTitle here
        taskTitle.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 16));
        taskTitle.setFill(Color.BLACK);
        double anchorPaneHeight = 50;
        double taskTitleHeight = 20;
        AnchorPane.setLeftAnchor(taskTitle, 10.0);
        AnchorPane.setTopAnchor(taskTitle, (anchorPaneHeight - taskTitleHeight) / 2);
        this.getChildren().add(taskTitle);
    }

    private void setupDoneCheckBox() {
        JFXCheckBox doneCheckBox = new JFXCheckBox();
        doneCheckBox.setSelected(task.isDone());
        double anchorPaneHeight = 50;
        double taskTitleHeight = 20;
        AnchorPane.setRightAnchor(doneCheckBox, 10.0);
        AnchorPane.setTopAnchor(doneCheckBox, (anchorPaneHeight - taskTitleHeight) / 2);
        this.getChildren().add(doneCheckBox);

        doneCheckBox.setOnAction(event -> {
            boolean doneStatus = doneCheckBox.isSelected();
            updateTaskStatus(task.getTaskId(), doneStatus);
            task.setDone(doneStatus);
            updateTaskStyle();
            dayViewController.initialize();
        });
    }

    private void setupHoverEffect() {
        this.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                this.setStyle("-fx-background-color: #f5da95; -fx-border-color: #FFA500; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 0; -fx-border-width: 2;");
            } else {
                updateTaskStyle(); // Call updateTaskStyle here to ensure correct color is maintained
            }
        });
    }

    private void setInitialStyle() {
        this.setPrefHeight(50);
        updateTaskStyle(); // Ensure initial style uses the correct background color
    }

    private void updateTaskStyle() {
        String backgroundColor = task.isDone() ? "gray" : "white";
        taskTitle.setFill(task.isDone() ? Color.WHITE : Color.BLACK); // Update the fill color based on task completion
        this.setStyle("-fx-background-color: " + backgroundColor + "; -fx-border-color: #FFA500; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 0; -fx-border-width: 2;");
    }
}