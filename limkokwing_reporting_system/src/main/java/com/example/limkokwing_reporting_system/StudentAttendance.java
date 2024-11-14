package com.example.limkokwing_reporting_system;

class StudentAttendance {
    private int studentId;
    private String fullName;
    private boolean isPresent;

    public StudentAttendance(int studentId, String fullName) {
        this.studentId = studentId;
        this.fullName = fullName;
        this.isPresent = false;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getFullName() {
        return fullName;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public void setPresent(boolean isPresent) {
        this.isPresent = isPresent;
    }
}
