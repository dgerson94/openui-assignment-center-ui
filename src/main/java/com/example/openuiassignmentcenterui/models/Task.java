package com.example.openuiassignmentcenterui.models;


public class Task {
    private Integer id;
    private String submissionDeadline;
    private String checkDeadLine;
    private Double weightInGrade;
    private Boolean hasFile;

    private Integer courseId;

    public Task() {
    }

    public Task(Integer id, String submissionDeadline, String checkDeadLine, Double weightInGrade) {
        this.id = id;
        this.submissionDeadline = submissionDeadline;
        this.checkDeadLine = checkDeadLine;
        this.weightInGrade = weightInGrade;
        this.hasFile = false;
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

    public void setWeightInGrade(Double weightInGrade) {
        this.weightInGrade = weightInGrade;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public Boolean getHasFile() {
        return hasFile;
    }

    public void setHasFile(Boolean hasFile) {
        this.hasFile = hasFile;
    }
}
