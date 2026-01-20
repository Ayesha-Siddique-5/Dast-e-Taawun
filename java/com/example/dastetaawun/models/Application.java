package com.example.dastetaawun.models;

import java.util.Date;

public class Application {
    private String applicationId;
    private String applicantId;
    private String applicationType; // "financial", "food", "clothing", "furniture"
    private String description;
    private String status; // "pending", "under_review", "approved", "rejected", "fulfilled"
    private Date appliedDate;
    private double requestedAmount; // financial help ke liye

    public Application() {}

    public Application(String applicationId, String applicantId, String applicationType) {
        this.applicationId = applicationId;
        this.applicantId = applicantId;
        this.applicationType = applicationType;
        this.appliedDate = new Date();
        this.status = "pending";
    }

    // Getters and Setters...
}