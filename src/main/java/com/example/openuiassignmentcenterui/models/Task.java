package com.example.openuiassignmentcenterui.models;

import java.io.File;
import java.time.LocalDate;
import java.util.Date;

public class Task {
    private String id;
    private Date submissionDeadline;
    private Date checkDeadLine;
    private Double weightInGrade;

    private transient File file;

    private Integer courseId;

    public Task(){}

    public Task(String id, Date submissionDeadline, Date checkDeadLine, Double weightInGrade) {
        this.id = id;
        this.submissionDeadline = submissionDeadline;
        this.checkDeadLine = checkDeadLine;
        this.weightInGrade = weightInGrade;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getSubmissionDeadline() {
        return submissionDeadline;
    }

    public void setSubmissionDeadline(Date submissionDeadline) {
        this.submissionDeadline = submissionDeadline;
    }

    public Date getCheckDeadLine() {
        return checkDeadLine;
    }

    public void setCheckDeadLine(Date checkDeadLine) {
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
