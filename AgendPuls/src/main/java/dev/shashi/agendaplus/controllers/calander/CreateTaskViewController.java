package dev.shashi.agendaplus.controllers.calander;


import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXListView;
import dev.shashi.agendaplus.controllers.calander.dayview.DayViewController;
import dev.shashi.agendaplus.dboperations.CalendarOperations;
import dev.shashi.agendaplus.model.Atachment;
import dev.shashi.agendaplus.model.Reminder;
import dev.shashi.agendaplus.model.Task;
import dev.shashi.agendaplus.reminders.ReminderManager;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.*;
import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.File;

import javafx.scene.layout.HBox;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.stream.IntStream;
import javafx.scene.control.Alert.AlertType;




public class CreateTaskViewController {
    @FXML
    private JFXCheckBox setReminderCheckBox;

    @FXML
    private JFXListView<Hyperlink> atachmentsListView;

    @FXML
    private Button addAtachmentsBtn;

    @FXML
    private Button addBtn;

    @FXML
    private Button closeButton;

    @FXML
    private TextField description;

    @FXML
    private JFXTextArea noteTextArea;

    @FXML
    private Button setTimeBtn;

    @FXML
    private TextField taskNAme;

    //String filePath;
    ArrayList<String> filePath = new ArrayList<>();
    Time pickedTime;

    LocalTime reminderTime;

    private LocalDate date;

    private DayViewController dayViewController;


    public void setDayViewController(DayViewController dayViewController) {
        this.dayViewController = dayViewController;
    }



    @FXML
    void addTask(ActionEvent event) throws SQLException {
        String taskName = taskNAme.getText().trim(); // Retrieve and trim text from taskNAme TextField
        String taskDescription = description.getText().trim(); // Retrieve and trim text from description TextField

        // Check if taskName or taskDescription is empty
        if (taskName.isEmpty() || taskDescription.isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Missing Information");
            alert.setContentText("Task title and description cannot be empty.");
            alert.showAndWait();
            return; // Exit the method without saving the task
        }

        String taskNote = noteTextArea.getText(); // Retrieve text from noteTextArea
        System.out.println(date);
        Task task = new Task(taskName, taskDescription, false, date);
        ArrayList<Atachment>atachments= new ArrayList<>();
        for(String path : filePath){
            Atachment atachment = new Atachment(path);
            atachments.add(atachment);
        }

        Reminder reminder = new Reminder(taskName, date, reminderTime);
        boolean isSaved = CalendarOperations.trySaveTest(task, atachments, reminder);

        if (isSaved) {
            dayViewController.initialize();

            resetFields();
            goBaack();
        }
    }


    @FXML
    void addAtachmentsBtnOnAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("All files (*.*)", "*.*");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            String path = file.getAbsolutePath();
            String fileName = file.getName();

            Hyperlink fileLink = new Hyperlink(fileName);
            fileLink.setContextMenu(createFileContextMenu(fileLink, path));

            atachmentsListView.getItems().add(fileLink);
            filePath.add(path);

            // Ensure the context menu is properly associated with the hyperlink
            fileLink.setOnMouseClicked(e -> {
                if (e.getButton() == MouseButton.PRIMARY) {
                    ContextMenu contextMenu = fileLink.getContextMenu();
                    if (contextMenu != null) {
                        contextMenu.show(fileLink, Side.RIGHT, 0, 0);
                    }
                }
            });
        }
    }
    private ContextMenu createFileContextMenu(Hyperlink fileLink, String filePath) {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem openItem = new MenuItem("Open");
        openItem.setOnAction(e -> {
            try {
                Desktop.getDesktop().open(new File(filePath));
            } catch (IOException ex) {
                Alert errorAlert = new Alert(AlertType.ERROR);
                errorAlert.setHeaderText("Cannot Open File");
                errorAlert.setContentText("The file could not be opened.");
                errorAlert.showAndWait();
            }
        });

        MenuItem removeItem = new MenuItem("Remove");
        removeItem.setOnAction(e -> {
            atachmentsListView.getItems().remove(fileLink);
            this.filePath.remove(filePath);
        });

        contextMenu.getItems().addAll(openItem, removeItem);

        return contextMenu;
    }
    @FXML
    void closePopup(ActionEvent event) {
      goBaack();
    }

    private void goBaack(){
        if (dayViewController != null) {
            dayViewController.unloadPopup();
        }
    }


    public void setTimeBtnOnAction(ActionEvent actionEvent) {
        Dialog<LocalTime> dialog = new Dialog<>();
        dialog.setTitle("Select Time");
        dialog.setHeaderText("Choose a time:");

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        ComboBox<Integer> hourBox = new ComboBox<>();
        IntStream.rangeClosed(0, 23).forEach(hourBox.getItems()::add);

        ComboBox<Integer> minuteBox = new ComboBox<>();
        IntStream.rangeClosed(1, 59).forEach(minuteBox.getItems()::add);

        HBox content = new HBox(10);
        content.getChildren().addAll(hourBox, minuteBox);
        dialogPane.setContent(content);


        dialog.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK) {
                if (hourBox.getValue() == null || minuteBox.getValue() == null) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Incomplete Time Selection");
                    alert.setContentText("Please select both hour and minute.");
                    alert.showAndWait();
                    return null; // Return null to prevent dialog from closing
                }
                return LocalTime.of(hourBox.getValue(), minuteBox.getValue());
            }
            return null;
        });

        try {
            dialog.showAndWait().ifPresent((LocalTime time) -> {
                System.out.println("Selected time: " + time);
                pickedTime = Time.valueOf(time);  // Store the time as a string if needed elsewhere
            });
        } catch (IllegalStateException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Incomplete Time Selection");
            alert.setContentText("Please select both hour and minute.");
            alert.showAndWait();
        }
    }

    @FXML
    void SetReminderCheckBoxOnAction(ActionEvent event) {
        if (setReminderCheckBox.isSelected() && pickedTime != null) {
            reminderTime = pickedTime.toLocalTime().minusMinutes(0);

            System.out.println("Reminder set for: " + reminderTime);

        } else {
            System.out.println("No time picked or checkbox not selected");
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Notification");
            alert.setHeaderText(null);
            alert.setContentText("No time picked or checkbox not selected");
            alert.showAndWait();
        }
    }


    public void setDate(LocalDate date) {
        this.date = date;

    }


    public void resetFields() {
        taskNAme.setText(""); // Reset the task name field
        description.setText(""); // Reset the description field
        noteTextArea.setText(""); // Reset the note text area
        filePath.clear(); // Reset the file path
        pickedTime = null; // Reset the picked time
        reminderTime = null; // Reset the reminder time
        setReminderCheckBox.setSelected(false); // Uncheck the set reminder checkbox
    }
}

