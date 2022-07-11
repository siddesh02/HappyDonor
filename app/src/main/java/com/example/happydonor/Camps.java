package com.example.happydonor;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class Camps  implements Serializable {
    public String orgBy, cmpDate, cmpTime, venue, contact, regLink, addedBy, addedOn;
    @Exclude
    private String id;

    public Camps() {}

    public Camps(String orgBy, String cmpDate, String cmpTime, String venue, String contact, String regLink, String addedBy, String addedOn) {
        this.orgBy = orgBy;
        this.cmpDate = cmpDate;
        this.cmpTime = cmpTime;
        this.venue = venue;
        this.contact = contact;
        this.regLink = regLink;
        this.addedBy = addedBy;
        this.addedOn = addedOn;
    }

    public String getAddedBy() { return addedBy; }

    public void setAddedBy(String addedBy) { this.addedBy = addedBy; }

    public String getAddedOn() { return addedOn; }

    public void setAddedOn(String addedOn) { this.addedOn = addedOn; }

    public String getOrgBy() {
        return orgBy;
    }

    public void setOrgBy(String orgBy) {
        this.orgBy = orgBy;
    }

    public String getCmpDate() {
        return cmpDate;
    }

    public void setCmpDate(String cmpDate) {
        this.cmpDate = cmpDate;
    }

    public String getCmpTime() {
        return cmpTime;
    }

    public void setCmpTime(String cmpTime) {
        this.cmpTime = cmpTime;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getRegLink() {
        return regLink;
    }

    public void setRegLink(String regLink) {
        this.regLink = regLink;
    }

    public void setId(String id) { this.id = id; }

    public String getId() { return id; }
}
