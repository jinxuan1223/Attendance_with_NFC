public class attDetails {
    int attid, empid; 
    String indate, intime, outdate, outtime, islate; 

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

    public void setInDate(String indate) {
        this.indate = indate;
    }

    public String getInDate() {
        return indate;
    }

    public void setInTime(String intime) {
        this.intime = intime;
    }

    public String getInTime() {
        return intime;
    }

    public void setOutDate(String outdate) {
        this.outdate = outdate;
    }

    public String getOutDate() {
        return outdate;
    }

    public void setOutTime(String outtime) {
        this.outtime = outtime;
    }

    public String getOutTime() {
        return outtime;
    }

    public attDetails(){}

    public attDetails(int attid, String indate, String intime, String outdate, String outtime, String islate, int empid) {
        this.attid = attid;
        this.indate = indate;
        this.intime = intime;
        this.outdate = outdate;
        this.outtime = outtime;
        this.islate = islate;
        this.empid = empid;
    }   
}
