package com.example.openuiassignmentcenterui.models;

public class Submission {
    private String id;
    private String submitDate;
    private Integer grade;
    private Boolean hasFeedbackFile;
    private String studentId;

    public Submission() {
    }

    public Submission(String id, String submitDate, Integer grade, Boolean hasFeedbackFile, String studentId) {
        this.id = id;
        this.submitDate = submitDate;
        this.grade = grade;
        this.hasFeedbackFile = hasFeedbackFile;
        this.studentId = studentId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(String submitDate) {
        this.submitDate = submitDate;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Boolean getHasFeedbackFile() {
        return hasFeedbackFile;
    }

    public void setHasFeedbackFile(Boolean hasFeedbackFile) {
        this.hasFeedbackFile = hasFeedbackFile;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
}
