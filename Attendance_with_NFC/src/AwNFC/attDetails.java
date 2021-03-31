package AwNFC;

public class attDetails {
    String date, intime, outtime, islate, empName, leaveStatus, staffid;

    public void setEmpName(String empName) { this.empName = empName; }

    public String getEmpName() { return empName; }

    public void setIsLate(String islate) { this.islate = islate; }

    public String getIsLate() { return islate; }

    public void setStaffID(String empid) { this.staffid = staffid; }

    public String getStaffID() { return staffid; }

    public void setDate(String date) { this.date = date; }

    public String getDate() { return date; }

    public void setInTime(String intime) { this.intime = intime; }

    public String getInTime() { return intime; }

    public void setOutTime(String outtime) { this.outtime = outtime; }

    public String getOutTime() { return outtime; }

    public void setLeaveStatus(String leaveStatus) { this.leaveStatus = leaveStatus; }

    public String getLeaveStatus() { return leaveStatus; }

    public attDetails(){}

    public attDetails(String staffid, String empName, String date, String intime, String outtime, String islate, String leaveStatus) {
        this.empName = empName;
        this.date = date;
        this.intime = intime;
        this.outtime = outtime;
        this.islate = islate;
        this.staffid = staffid;
        this.leaveStatus = leaveStatus;
    }
}
