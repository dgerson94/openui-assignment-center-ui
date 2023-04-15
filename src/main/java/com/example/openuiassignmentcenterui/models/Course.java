package com.example.openuiassignmentcenterui.models;

public class Course {
    private Integer id;
    private String name;

    private String professorId;
    private boolean tasksSet;

    public Course() {

    }

    public Course(Integer id, String name, String professorId) {
        this.id = id;
        this.name = name;
        this.professorId = professorId;
        this.tasksSet = false;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfessorId() {
        return professorId;
    }

    public void setProfessorId(String professorId) {
        this.professorId = professorId;
    }

    public boolean isTasksSet() {
        return tasksSet;
    }

    public void setTasksSet(boolean tasksSet) {
        this.tasksSet = tasksSet;
    }

}
