package com.example.openuiassignmentcenterui.models;

import java.util.Collection;

public class Course {
    public Integer id;
    public String name;

    public String professorId;
    public boolean tasksSet;
    //TODO: Need to learn what to do with null values so there won't be an error.
    private transient Collection<Task> tasks;

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

//    public Collection<Task> getTasks() {
//        return tasks;
//    }
//
//    public void setTasks(Collection<Task> tasks) {
//        this.tasks = tasks;
//    }
}
