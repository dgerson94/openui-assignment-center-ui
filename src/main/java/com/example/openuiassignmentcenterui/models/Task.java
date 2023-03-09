package com.example.openuiassignmentcenterui.models;

import java.io.File;

public class Task {
    private Integer id;
    private String submissionDeadline;
    private String checkDeadLine;
    private Double weightInGrade;

    private transient File file;

    private Integer courseId;

    public Task(){}

    public Task(Integer id, String submissionDeadline, String checkDeadLine, Double weightInGrade) {
        this.id = id;
        this.submissionDeadline = submissionDeadline;
        this.checkDeadLine = checkDeadLine;
        this.weightInGrade = weightInGrade;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSubmissionDeadline() {
        return submissionDeadline;
    }

    public void setSubmissionDeadline(String submissionDeadline) {
        this.submissionDeadline = submissionDeadline;
    }

    public String getCheckDeadLine() {
        return checkDeadLine;
    }

    public void setCheckDeadLine(String checkDeadLine) {
        this.checkDeadLine = checkDeadLine;
    }

    public Double getWeightInGrade() {
        return weightInGrade;
    }

    public void setWeightInGrade(Double weightInGrade) { this.weightInGrade = weightInGrade; }

    public void setCourseId (Integer courseId) { this.courseId = courseId; }

    public Integer getCourseId() { return courseId; }

//    public File getFile() {
//        return file;
//    }
//
//    public void setFile(File file) {
//        this.file = file;
//    }
}
