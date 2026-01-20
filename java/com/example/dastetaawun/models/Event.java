package com.example.dastetaawun.models;

import com.google.firebase.Timestamp;

public class Event {
    private String eventId;
    private String title;
    private String description;
    private String location;
    private Timestamp date;
    private double targetAmount;
    private double currentAmount;
    private String imageUrl;
    private String organizerId;

    // Empty constructor required for Firestore
    public Event() {
    }

    public Event(String title, String description, String location, Timestamp date,
                 double targetAmount, double currentAmount, String organizerId) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.date = date;
        this.targetAmount = targetAmount;
        this.currentAmount = currentAmount;
        this.organizerId = organizerId;
    }

    // Getters and Setters
    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public double getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(double targetAmount) {
        this.targetAmount = targetAmount;
    }

    public double getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(double currentAmount) {
        this.currentAmount = currentAmount;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(String organizerId) {
        this.organizerId = organizerId;
    }

    // Helper method to get progress percentage
    public int getProgressPercentage() {
        if (targetAmount == 0) return 0;
        return (int) ((currentAmount / targetAmount) * 100);
    }
}


