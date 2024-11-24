package com.example.betterme.Models;

public class Booking {

    public String userId;
    public String instructorId;
    public String date;
    public String time;
    public String category;
    public String id;
    public String status;


    public Booking() {

    }

    public Booking(String userId, String instructorId, String date, String time, String category, String id, String status) {
        this.userId = userId;
        this.instructorId = instructorId;
        this.date = date;
        this.time = time;
        this.category = category;
        this.id = id;
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(String instructorId) {
        this.instructorId = instructorId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}