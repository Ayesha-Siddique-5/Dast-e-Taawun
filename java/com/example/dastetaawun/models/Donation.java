package com.example.dastetaawun.models;

import com.google.firebase.Timestamp;

public class Donation {
    private String donationId;
    private String donorId;
    private String donorName;
    private String recipientId;
    private String recipientName;
    private double amount;
    private String paymentMethod;
    private Timestamp donationDate;
    private boolean isAnonymous;
    private String status;
    private String notes;

    // Empty constructor required for Firestore
    public Donation() {
    }

    public Donation(String donorId, String donorName, String recipientId,
                    String recipientName, double amount, String paymentMethod,
                    boolean isAnonymous) {
        this.donorId = donorId;
        this.donorName = donorName;
        this.recipientId = recipientId;
        this.recipientName = recipientName;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.isAnonymous = isAnonymous;
        this.donationDate = Timestamp.now();
        this.status = "completed";
    }

    // Getters and Setters
    public String getDonationId() {
        return donationId;
    }

    public void setDonationId(String donationId) {
        this.donationId = donationId;
    }

    public String getDonorId() {
        return donorId;
    }

    public void setDonorId(String donorId) {
        this.donorId = donorId;
    }

    public String getDonorName() {
        return donorName;
    }

    public void setDonorName(String donorName) {
        this.donorName = donorName;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Timestamp getDonationDate() {
        return donationDate;
    }

    public void setDonationDate(Timestamp donationDate) {
        this.donationDate = donationDate;
    }

    public boolean isAnonymous() {
        return isAnonymous;
    }

    public void setAnonymous(boolean anonymous) {
        isAnonymous = anonymous;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}