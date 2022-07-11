package com.example.happydonor;

import com.google.firebase.firestore.Exclude;

public class User {
    public String id, fullname, email, newPass, phone, alternate, area, bloodGrp, lastDonated;

    public User(String id, String fullname, String email, String newPass, String phone, String alternate, String area, String bloodGrp, String lastDonated) {
        this.id = id;
        this.fullname = fullname;
        this.email = email;
        this.newPass = newPass;
        this.phone = phone;
        this.alternate = alternate;
        this.area = area;
        this.bloodGrp = bloodGrp;
        this.lastDonated = lastDonated;
    }
    public User() {}
    public String getId() { return id; }
    public void setId(String id) {
        this.id = id;
    }
    public String getFullname() { return fullname; }
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getNewPass() {
        return newPass;
    }
    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getAlternate() {
        return alternate;
    }
    public void setAlternate(String alternate) {
        this.alternate = alternate;
    }
    public String getArea() {
        return area;
    }
    public void setArea(String area) {
        this.area = area;
    }
    public String getBloodGrp() {
        return bloodGrp;
    }
    public void setBloodGrp(String bloodGrp) {
        this.bloodGrp = bloodGrp;
    }
    public String getLastDonated() {
        return lastDonated;
    }
    public void setLastDonated(String lastDonated) {
        this.lastDonated = lastDonated;
    }
}
