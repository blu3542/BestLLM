package com.example.bestllm.models;

import com.google.firebase.Timestamp;

public class User {
    private String userId;
    private String name;
    private String studentId;
    private String email;
    private String department;
    private String school;
    private String bio;
    private Timestamp birthDate;
    private int reputationScore;
    private boolean upvotableStatus;
    private Timestamp createdAt;

    // Empty constructor required for Firestore
    public User() {}

    public User(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.bio = "";
        this.reputationScore = 0;
        this.upvotableStatus = true;
        this.createdAt = Timestamp.now();
    }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getSchool() { return school; }
    public void setSchool(String school) { this.school = school; }

    // Getters and Setters
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

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Timestamp getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Timestamp birthDate) {
        this.birthDate = birthDate;
    }

    public int getReputationScore() {
        return reputationScore;
    }

    public void setReputationScore(int reputationScore) {
        this.reputationScore = reputationScore;
    }

    public boolean isUpvotableStatus() {
        return upvotableStatus;
    }

    public void setUpvotableStatus(boolean upvotableStatus) {
        this.upvotableStatus = upvotableStatus;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}