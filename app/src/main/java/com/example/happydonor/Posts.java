package com.example.happydonor;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class Posts implements Serializable {
    public String bloodGrp, patient, hospital, postedBy, postedOn, lastDate, email, contact;
    public int donorsNeeded, donorsGot;

    // getter method for our id
    public String getId() {
        return id;
    }
    // setter method for our id
    public void setId(String id) {
        this.id = id;
    }
    // we are using exclude because
    // we are not saving our id
    @Exclude
    private String id;

    public Posts() {}
    public Posts(String bloodGrp, String patient, String hospital, String postedBy, String postedOn, String lastDate, String email, int donorsNeeded, int donorsGot, String contact) {
        this.bloodGrp = bloodGrp;
        this.patient = patient;
        this.hospital = hospital;
        this.postedBy = postedBy;
        this.postedOn = postedOn;
        this.lastDate = lastDate;
        this.email = email;
        this.donorsNeeded = donorsNeeded;
        this.donorsGot = donorsGot;
        this.contact = contact;
    }
    public String getBloodGrp() {
        return bloodGrp;
    }
    public void setBloodGrp(String bloodGrp) {
        this.bloodGrp = bloodGrp;
    }
    public String getPatient() {
        return patient;
    }
    public void setPatient(String patient) {
        this.patient = patient;
    }
    public String getHospital() {
        return hospital;
    }
    public void setHospital(String hospital) {
        this.hospital = hospital;
    }
    public String getPostedBy() {
        return postedBy;
    }
    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }
    public String getPostedOn() {
        return postedOn;
    }
    public void setPostedOn(String postedOn) {
        this.postedOn = postedOn;
    }
    public String getLastDate() {
        return lastDate;
    }
    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public int getDonorsNeeded() {
        return donorsNeeded;
    }
    public void setDonorsNeeded(int donorsNeeded) {
        this.donorsNeeded = donorsNeeded;
    }
    public int getDonorsGot() {
        return donorsGot;
    }
    public void setDonorsGot(int donorsGot) {
        this.donorsGot = donorsGot;
    }
    public String getContact() {
        return contact;
    }
    public void setContact(String contact) {
        this.contact = contact;
    }
}

