package com.example.betterme.Models;

public class User {

    public String name;
    public String mobile;
    public String email;
    public String password;
    public int userType;
    public String id;


    public User(){

    }

    public User(String name, String mobile, String email, String password, int userType, String id) {
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.password = password;
        this.userType = userType;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}


