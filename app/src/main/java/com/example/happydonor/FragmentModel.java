package com.example.happydonor;

public class FragmentModel {
    private String fullname, bloodGrp, area, alternate, email, phone, lastDonated;

    public FragmentModel(String fullname, String bloodGrp, String area, String alternate, String email, String phone, String lastDonated) {
        this.fullname = fullname;
        this.bloodGrp = bloodGrp;
        this.area = area;
        this.alternate = alternate;
        this.email = email;
        this.phone = phone;
        this.lastDonated = lastDonated;
    }

    public FragmentModel() {}

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getBloodGrp() {
        return bloodGrp;
    }

    public void setBloodGrp(String bloodGrp) {
        this.bloodGrp = bloodGrp;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAlternate() {
        return alternate;
    }

    public void setAlternate(String alternate) {
        this.alternate = alternate;
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

    public String getLastDonated() {
        return lastDonated;
    }

    public void setLastDonated(String lastDonated) {
        this.lastDonated = lastDonated;
    }
}
