package AwNFC;

public class attDetails {
    int empid;
    String date, intime, outtime, islate, empName, leaveStatus;

    public void setEmpName(String empName) { this.empName = empName; }

    public String getEmpName() { return empName; }

    public void setIsLate(String islate) { this.islate = islate; }

    public String getIsLate() { return islate; }

    public void setEmpId(int empid) { this.empid = empid; }

    public int getEmpId() { return empid; }

    public void setDate(String date) { this.date = date; }

    public String getDate() { return date; }

    public void setInTime(String intime) { this.intime = intime; }

    public String getInTime() { return intime; }

    public void setOutTime(String outtime) { this.outtime = outtime; }

    public String getOutTime() { return outtime; }

    public void setLeaveStatus(String leaveStatus) { this.leaveStatus = leaveStatus; }

    public String getLeaveStatus() { return leaveStatus; }

    public attDetails(){}

    public attDetails(int empid, String empName, String date, String intime, String outtime, String islate, String leaveStatus) {
        this.empName = empName;
        this.date = date;
        this.intime = intime;
        this.outtime = outtime;
        this.islate = islate;
        this.empid = empid;
        this.leaveStatus = leaveStatus;
    }
}
