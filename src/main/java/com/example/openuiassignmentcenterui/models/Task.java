package com.example.openuiassignmentcenterui.models;

import java.time.LocalDate;
import java.util.Date;

public class Task {
    private String id;
    private Date submissionDeadline;
    private Date checkDeadLine;
    private double weightInGrade;

//    private File file;
//
//    private Course course;

    public Task(){}

    public Task(String id, Date submissionDeadline, Date checkDeadLine, double weightInGrade) {
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

    public double getWeightInGrade() {
        return weightInGrade;
    }

    public void setWeightInGrade(double weightInGrade) {
        this.weightInGrade = weightInGrade;
    }

//    public File getFile() {
//        return file;
//    }
//
//    public void setFile(File file) {
//        this.file = file;
//    }
}
