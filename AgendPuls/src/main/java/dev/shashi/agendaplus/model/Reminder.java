package dev.shashi.agendaplus.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

public class Reminder {
    private int taskId;
    private LocalDate date;

    private boolean shown;

    public Reminder(int taskId, String taskTitle, LocalDate localDate, LocalTime localTime) {
        this.taskId =taskId;
        this.taskTitle=taskTitle;
        this.date = localDate;
        this.reminderTime= localTime;
    }

    public LocalTime getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(LocalTime reminderTime) {
        this.reminderTime = reminderTime;
    }

    private LocalTime reminderTime;
    private String taskTitle;

    public Reminder() {}

    public Reminder(int taskId,String taskTitle, LocalDate date) {
        this.taskId = taskId;
        this.taskTitle=taskTitle;
        this.date = date;
    }
    public Reminder(String taskTitle, LocalDate date ,LocalTime time) {
        this.reminderTime=time ;
        this.taskTitle=taskTitle;
        this.date = date;
    }

    // Getters and Setters
    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public java.sql.Date getDate() {
        return java.sql.Date.valueOf(date);
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public boolean isShown() {
        return shown;
    }

    public void setShown(boolean shown) {
        this.shown= shown;
    }
}
