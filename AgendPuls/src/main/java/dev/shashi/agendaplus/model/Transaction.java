package dev.shashi.agendaplus.model;

import java.math.BigDecimal;
import java.util.Date;

public class Transaction {
    private int transactionId;
    private BigDecimal amount;
    private int type;
    private Date date;
    private String description;
    private int categoryId;
    private String categoryName;

    private Atachment attachments;
    private Note notes;

    // Constructors


    public Transaction(int transactionId, BigDecimal amount, int type, Date date, String description, int categoryId) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.type = type;
        this.date = date;
        this.description = description;
        this.categoryId = categoryId;

    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    // Getters and Setters
    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }


    public Atachment getAttachments() {
        return attachments;
    }

    public void setAttachments(Atachment attachments) {
        this.attachments = attachments;
    }

    public Note getNotes() {
        return notes;
    }

    public void setNotes(Note notes) {
        this.notes = notes;
    }
}

