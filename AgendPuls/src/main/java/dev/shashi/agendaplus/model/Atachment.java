package dev.shashi.agendaplus.model;

public class Atachment {
    private int taskId;
    private String filePath;



    private int atachmentId;


    // Constructors
    public Atachment() {}

    public Atachment(int taskId, String filePath) {
        this.taskId = taskId;
        this.filePath = filePath;

    }
    public Atachment( String filePath) {

        this.filePath = filePath;

    }

    // Getters and Setters


    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getAtachmentId() {
        return atachmentId;
    }

    public void setAtachmentId(int atachmentId) {
        this.atachmentId = atachmentId;
    }


}

