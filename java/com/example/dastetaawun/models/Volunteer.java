package com.example.dastetaawun.models;

import com.google.firebase.Timestamp;
import java.util.List;

public class Volunteer {
    private String volunteerId;
    private String userId;
    private String name;
    private String email;
    private String phone;
    private String location;
    private String availability;
    private List<String> skills;
    private Timestamp registrationDate;
    private boolean isActive;

    // Empty constructor required for Firestore
    public Volunteer() {
    }

    public Volunteer(String userId, String name, String email, String phone,
                     String location, String availability, List<String> skills) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.location = location;
        this.availability = availability;
        this.skills = skills;
        this.registrationDate = Timestamp.now();
        this.isActive = true;
    }

    // Getters and Setters
    public String getVolunteerId() {
        return volunteerId;
    }

    public void setVolunteerId(String volunteerId) {
        this.volunteerId = volunteerId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public Timestamp getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Timestamp registrationDate) {
        this.registrationDate = registrationDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    // Helper method to get skills as comma-separated string
    public String getSkillsAsString() {
        if (skills == null || skills.isEmpty()) {
            return "No skills listed";
        }
        return String.join(", ", skills);
    }
}