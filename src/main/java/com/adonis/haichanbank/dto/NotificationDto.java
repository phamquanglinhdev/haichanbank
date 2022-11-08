package com.adonis.haichanbank.dto;

import java.sql.Timestamp;

public class NotificationDto {
    private String title;
    private String message;
    private String reading;
    private Timestamp created;

    public NotificationDto() {
    }

    public String getReading() {
        return reading;
    }

    public void setReading(String reading) {
        this.reading = reading;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }
}
