package com.example.betterme.Models;

public class BookingHistory {

    public String bookingId;
    public String instructorName;
    public String category;
    public String bookingDate;
    public String bookingTime;
    public String status;

    public BookingHistory(){


    }

    public BookingHistory(String bookingId, String instructorName, String category, String bookingDate, String bookingTime, String status) {
        this.bookingId = bookingId;
        this.instructorName = instructorName;
        this.category = category;
        this.bookingDate = bookingDate;
        this.bookingTime = bookingTime;
        this.status = status;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(String bookingTime) {
        this.bookingTime = bookingTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
