package com.example.garbagecollectingapp;

public class User {
    private String userid;
    private String username;
    private String userphone;
    private String useremail;
    private String useraddress;
    private String userdistrict;
    private String role;

    public User() {
    }

    public User(String userid, String username, String userphone, String useremail,  String role) {
        this.userid = userid;
        this.username = username;
        this.userphone = userphone;
        this.useremail = useremail;
        this.role = role;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserphone() {
        return userphone;
    }

    public void setUserphone(String userphone) {
        this.userphone = userphone;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getUseraddress() {
        return useraddress;
    }

    public void setUseraddress(String useraddress) {
        this.useraddress = useraddress;
    }

    public String getUserdistrict() {
        return userdistrict;
    }

    public void setUserdistrict(String userdistrict) {
        this.userdistrict = userdistrict;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
