package com.example.garbagecollectingapp;

public class Driver {
    private String driverid;
    private String drivername;
    private String driverphone;
    private String driveremail;
    private String trucknumber;
    private String driverpassword;
    private String role;

    public Driver() {
    }

    public Driver(String driverid, String drivername, String driverphone, String driveremail, String trucknumber, String role) {
        this.driverid = driverid;
        this.drivername = drivername;
        this.driverphone = driverphone;
        this.driveremail = driveremail;
        this.trucknumber = trucknumber;
        this.role = role;
    }

    public String getDriverid() {
        return driverid;
    }

    public void setDriverid(String driverid) {
        this.driverid = driverid;
    }

    public String getDrivername() {
        return drivername;
    }

    public void setDrivername(String drivername) {
        this.drivername = drivername;
    }

    public String getDriverphone() {
        return driverphone;
    }

    public void setDriverphone(String driverphone) {
        this.driverphone = driverphone;
    }

    public String getDriveremail() {
        return driveremail;
    }

    public void setDriveremail(String driveremail) {
        this.driveremail = driveremail;
    }

    public String getTrucknumber() {
        return trucknumber;
    }

    public void setTrucknumber(String trucknumber) {
        this.trucknumber = trucknumber;
    }

    public String getDriverpassword() {
        return driverpassword;
    }

    public void setDriverpassword(String driverpassword) {
        this.driverpassword = driverpassword;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
