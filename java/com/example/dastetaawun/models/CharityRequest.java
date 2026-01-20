package com.example.dastetaawun.models;

public class CharityRequest {
    private String requestId;
    private String applicantId;
    private String applicantName;
    private String requestType; // "financial", "food", "clothing", "furniture"
    private String description;
    private String status; // "pending", "under_review", "approved", "fulfilled", "rejected"
    private double amountNeeded;
    private long requestDate;
    private long lastUpdated;

    public CharityRequest() {
        // Required empty constructor for Firebase
    }

    public CharityRequest(String applicantId, String applicantName, String requestType,
                          String description, double amountNeeded) {
        this.applicantId = applicantId;
        this.applicantName = applicantName;
        this.requestType = requestType;
        this.description = description;
        this.amountNeeded = amountNeeded;
        this.status = "pending";
        this.requestDate = System.currentTimeMillis();
        this.lastUpdated = System.currentTimeMillis();
    }

    // Getters and Setters
    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(String applicantId) {
        this.applicantId = applicantId;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        this.lastUpdated = System.currentTimeMillis();
    }

    public double getAmountNeeded() {
        return amountNeeded;
    }

    public void setAmountNeeded(double amountNeeded) {
        this.amountNeeded = amountNeeded;
    }

    public long getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(long requestDate) {
        this.requestDate = requestDate;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}




