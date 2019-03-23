package com.example.grievanceapp.Model;

public class Complaint {
    private String complaint;
    private String status;

    public Complaint(String complaint) {
        this.complaint = complaint;
        this.status = "New Complaint";
    }

    public String getComplaint() {
        return complaint;
    }

    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
