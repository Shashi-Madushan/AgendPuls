package dev.shashi.agendaplus.model;

import java.time.LocalDate;
import java.util.Date;

public class Task {
    private int taskId;
    private String title;
    private String description;


    private boolean isDone;

    private LocalDate date;

    // Constructors


    public Task( String title, String description,boolean isDone, LocalDate date) {
        this.isDone=isDone;
        this.title = title;
        this.description = description;
        this.date= date;

    }

    // Getters and Setters
    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getDate() {
        return date.toString();
    }


    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

}
