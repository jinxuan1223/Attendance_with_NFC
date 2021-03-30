package AwNFC;

public class attDetails {
    int attid, empid; 
    String date, intime, outtime, islate;

    public void setAttId(int attid) {
        this.attid = attid;
    }

    public int getAttId() {
        return attid;
    }

    public void setIsLate(String islate) {
        this.islate = islate;
    }

    public String getIsLate() {
        return islate;
    }

    public void setEmpId(int empid) {
        this.empid = empid;
    }

    public int getEmpId() {
        return empid;
    }

    public void setDate(String indate) {
        this.date = indate;
    }

    public String getDate() {
        return date;
    }

    public void setInTime(String intime) {
        this.intime = intime;
    }

    public String getInTime() {
        return intime;
    }

    public void setOutTime(String outtime) {
        this.outtime = outtime;
    }

    public String getOutTime() {
        return outtime;
    }

    public attDetails(){}

    public attDetails(int attid, String date, String intime, String outtime, String islate, int empid) {
        this.attid = attid;
        this.date = date;
        this.intime = intime;
        this.outtime = outtime;
        this.islate = islate;
        this.empid = empid;
    }   
}
