package com.example.betterme.Shared;

import com.example.betterme.Models.Booking;
import com.example.betterme.Models.Instructor;
import com.example.betterme.Models.User;

public class Shared {
    private static final Shared ourInstance = new Shared();

    public static Shared getInstance() {
        return ourInstance;
    }

    public Instructor selectedInstructor = new Instructor();
    public Booking selectedBooking = new Booking();
    public Instructor myInstructor = new Instructor();
    public User myUser = new User();
    public String selectedCategory = "";
}
