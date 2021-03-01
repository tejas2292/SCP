package com.example.scp.Uploads;

public class PatientUpload {
    String class_std, name, password, username, rollNo;

    public PatientUpload() {
    }

    public PatientUpload(String class_std, String name, String password, String username, String rollNo) {
        this.class_std = class_std;
        this.name = name;
        this.password = password;
        this.username = username;
        this.rollNo = rollNo;
    }

    public String getClass_std() {
        return class_std;
    }

    public void setClass_std(String class_std) {
        this.class_std = class_std;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }
}
