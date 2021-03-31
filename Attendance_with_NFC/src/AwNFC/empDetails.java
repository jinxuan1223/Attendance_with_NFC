package AwNFC;

public class empDetails {
    int empid;
    String name, serNum, createdAt, deletedAt, updatedAt, jobTitle, staffid;

    public void setEmpId(int empid) {
        this.empid = empid;
    }

    public void setStaffId(String staffid) {
        this.staffid = staffid;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public void setSerNum(String serNum) {
        this.serNum = serNum;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public int getEmpId() {
        return empid;
    }

    public String getStaffId() {
        return staffid;
    }

    public String getName() {
        return name;
    }

    public String getSerNum() {
        return serNum;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getJobTitle() {
        return jobTitle;
    }
    public empDetails(){}

    public empDetails(int empid, String staffid, String name, String createdAt, String updatedAt, String deletedAt, String serNum, String jobTitle) {
        this.empid = empid;
        this.staffid = staffid;
        this.name = name;
        this.serNum = serNum;
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
        this.updatedAt = updatedAt;
        this.jobTitle = jobTitle;
    }

}
