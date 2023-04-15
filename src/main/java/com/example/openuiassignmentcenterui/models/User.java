package com.example.openuiassignmentcenterui.models;


import java.util.Set;

public class User {

    private String id;

    private String password;
    private Set<Course> courses;

    public User() {
    }

    public User(String id, String password) {
        this.id = id;
        this.password = password;
        this.courses = null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    //not sure if we want to enable set password.
    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }


}
