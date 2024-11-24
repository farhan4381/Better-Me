package com.example.betterme.Models;

public class Instructor {

    public String name;
    public String mobile;
    public String email;
    public String password;
    public String certificate;
    public String age;
    public String experties;
    public String id;
    public int userType;
    public String image;



    public Instructor(){

    }

    public Instructor(String name, String mobile, String email, String password, String certificate, String age, String experties, String id, int userType, String image) {
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.password = password;
        this.certificate = certificate;
        this.age = age;
        this.experties = experties;
        this.id = id;
        this.userType = userType;
        this.image = image;
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

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getExperties() {
        return experties;
    }

    public void setExperties(String experties) {
        this.experties = experties;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
