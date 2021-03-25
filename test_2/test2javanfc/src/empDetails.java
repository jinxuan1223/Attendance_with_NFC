public class empDetails {
    int id;
    String name, serNum;

    public void setId(int id) {
        this.id = id;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public void setSerNum(String serNum) {
        this.serNum = serNum;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSerNum() {
        return serNum;
    }

    public empDetails(){}

    public empDetails(int id, String name, String serNum) {
        this.id = id;
        this.name = name;
        this.serNum = serNum;
    }

}
