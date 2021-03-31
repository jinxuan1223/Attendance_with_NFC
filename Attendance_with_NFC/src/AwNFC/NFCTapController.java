package AwNFC;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.*;
import java.util.Calendar;
import java.util.Date;


public class NFCTapController {

    @FXML
    private Button homeBtn;
    @FXML
    private AnchorPane rootPane;

    private String mode;
    private String UID;

    public String getName()  {
        Connection connectDB = DatabaseConnection.getConnection();

        String retrieveName = "SELECT emp_name FROM employee WHERE nfc_num =?";

        try {
            PreparedStatement ps = connectDB.prepareStatement(retrieveName);
            ps.setString(1,UID);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                return rs.getString("emp_name");
            }

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
        return null;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getUID(){
        return UID;
    }

    public void setMode(String mode){
        this.mode = mode;
    }

    public String getMode() {
        return mode;
    }

    public void setPane(AnchorPane pane){
        rootPane.getChildren().setAll(pane);
    }

    public void homeBtnOnAction(ActionEvent event) throws IOException {
        mode = "";
        FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
        AnchorPane pane = loader.load();
        HomeController homeController = loader.getController();
        homeController.setDateLabel(Calendar.getInstance().getTime());
        homeController.setTimePane();

        rootPane.getChildren().setAll(pane);
    }

    public boolean validateEmployee(){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        String verifyAcc = "SELECT * FROM employee WHERE nfc_num = '"+UID + "'" ;

        try{
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyAcc);

            if(queryResult.next()){
                return true;
            }else{
                return false;
            }

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
        return false;
    }

    public boolean isClockInExist(){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        java.util.Date date=new java.util.Date();
        java.sql.Date currentDate =new java.sql.Date(date.getTime());
        String getAttendance = "SELECT nfc_num FROM attendance,employee WHERE employee.emp_id = attendance.emp_id AND date =? AND  nfc_num =? ";


        try{
            PreparedStatement ps = connectDB.prepareStatement(getAttendance);
            ps.setDate(1,currentDate);
            ps.setString(2, UID);
            ResultSet queryResult = ps.executeQuery();

            if(queryResult.next()){
                if(queryResult.getString("nfc_num").equals(UID)) {
                    System.out.println("Clocked in");
                    return true;
                }
            }else{
                System.out.println("No record");
                return false;
            }

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
        return true;

    }

    public boolean isClockOutExist(){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        java.util.Date date=new java.util.Date();
        java.sql.Date currentDate =new java.sql.Date(date.getTime());
        String getAttendance = "SELECT nfc_num FROM attendance,employee WHERE employee.emp_id = attendance.emp_id AND date =? AND  nfc_num =? AND clockout_time IS NOT NULL ";


        try{
            PreparedStatement ps = connectDB.prepareStatement(getAttendance);
            ps.setDate(1,currentDate);
            ps.setString(2, UID);
            ResultSet queryResult = ps.executeQuery();

            if(queryResult.next()){
                if(queryResult.getString("nfc_num").equals(UID)) {
                    System.out.println("Clocked out");
                    return true;
                }
            }else{
                System.out.println("No record");
                return false;
            }

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
        return true;

    }

    private int getEmp_id(){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        String getEmpID = "SELECT emp_id FROM employee WHERE nfc_num =?";
        try{
            PreparedStatement ps = connectDB.prepareStatement(getEmpID);
            ps.setString(1,UID);

            ResultSet queryResult = ps.executeQuery();

            if(queryResult.next()){
                return queryResult.getInt("emp_id");
            }
        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
        return 0;
    }

    public void insertClockIn(){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        Date date=new Date();
        java.sql.Date currentDate = new java.sql.Date(date.getTime());
        java.sql.Timestamp currentTime=new java.sql.Timestamp(date.getTime());

        String newAtt = "INSERT INTO attendance(date, clockin_time, isLate, emp_id) VALUES (?,?,?,?)";
        try {
            PreparedStatement ps = connectDB.prepareStatement(newAtt);
            ps.setDate(1, currentDate);
            ps.setTimestamp(2,currentTime);
            ps.setBoolean(3,isLate(currentTime));
            ps.setInt(4,getEmp_id());
            ps.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    private boolean isLate(Timestamp time){

        Calendar ctime = Calendar.getInstance();
        ctime.setTimeInMillis(time.getTime());

        Calendar cWorkingTime = Calendar.getInstance();
        cWorkingTime.set(Calendar.YEAR, ctime.get(Calendar.YEAR));
        cWorkingTime.set(Calendar.MONTH, ctime.get(Calendar.MONTH));
        cWorkingTime.set(Calendar.DAY_OF_MONTH, ctime.get(Calendar.DAY_OF_MONTH));
        cWorkingTime.set(Calendar.HOUR_OF_DAY, 9);
        cWorkingTime.set(Calendar.MINUTE, 0);
        cWorkingTime.set(Calendar.SECOND,0);
        cWorkingTime.set(Calendar.MILLISECOND,0);

        Date timeReached = ctime.getTime();
        Date workingTime = cWorkingTime.getTime();

        if(timeReached.after(workingTime)){
            return true;
        }else{
            return false;
        }
    }

    public boolean isEarlyLeave(){
        Calendar ctime = Calendar.getInstance();

        Calendar offTime = Calendar.getInstance();
        offTime.set(Calendar.YEAR, ctime.get(Calendar.YEAR));
        offTime.set(Calendar.MONTH, ctime.get(Calendar.MONTH));
        offTime.set(Calendar.DAY_OF_MONTH, ctime.get(Calendar.DAY_OF_MONTH));
        offTime.set(Calendar.HOUR_OF_DAY, 18);
        offTime.set(Calendar.MINUTE, 0);
        offTime.set(Calendar.SECOND,0);
        offTime.set(Calendar.MILLISECOND,0);

        Date timeLeave = ctime.getTime();
        Date workingLeaveTime = offTime.getTime();

        if(timeLeave.before(workingLeaveTime))
            return true;
        else
            return false;

    }

    public void updateClockOut(){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        Date date=new Date();
        java.sql.Date currentDate = new java.sql.Date(date.getTime());
        java.sql.Timestamp currentTime=new java.sql.Timestamp(date.getTime());

        String newAtt = "UPDATE attendance SET clockout_time =? WHERE emp_id =? AND date =?";
        try {
            PreparedStatement ps = connectDB.prepareStatement(newAtt);
            ps.setTimestamp(1,currentTime);
            ps.setInt(2,getEmp_id());
            ps.setDate(3,currentDate);
            ps.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void switchScene() {
        if(getMode().equals("Edit")){
            // open admin page
            if(isAdmin()){
                openAdminPage();
            }else{
                // YOU HAVE NO AUTHORITY TO ENTER
                openMessageScene(false,false,false,false,false,false,true);
            }
        }else{
            if(validateEmployee()){
                if(getMode().equals("Clock In")){
                    clockin();
                }else if(getMode().equals("Clock Out")){
                    clockout();
                }
            }else{
                //show invalid card, please try again
                openMessageScene(false,false,true,false,false,false,false);
            }
        }
    }

    public void clockin() {
        if(!isClockInExist()){
            insertClockIn();
            //show WELCOME BACK USER
            openMessageScene(true,false,false,false,false,false,false);
        }else{
            //show YOU HAVE CLOCKED IN
            openMessageScene(false,false,false,true,false,false,false);
        }
    }

    public void clockout(){
        if(isClockInExist()){
            if(!isClockOutExist()) {
                if(!isEarlyLeave()) {
                    updateClockOut();
                    //show GOODBYE USER
                    openMessageScene(false, true, false, false, false, false, false);
                }else{
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("prompt_reason.fxml"));
                    try{
                        AnchorPane pane = loader.load();
                        PromptReasonController promptReasonController = loader.getController();
                        promptReasonController.setUID(UID);
                        setPane(pane);
                    }catch (Exception e){
                        e.printStackTrace();
                        e.getCause();
                    }
                }
            }else{
                openMessageScene(false,false,false, false, false,true,false);
            }
        }else{
            //show YOU HAVE NOT clock in yet
            openMessageScene(false,false,false,false,true,false,false);
        }
    }

    public void openMessageScene(boolean isWelcome, boolean isGoodbye, boolean isInvalid, boolean isClockedIn, boolean isNotClockedIn, boolean isClockedOut, boolean isNotAdmin) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("messages.fxml"));
        try {
            AnchorPane pane = loader.load();
            MessagesController messagesController = loader.getController();

            if (isWelcome) {
                messagesController.assignWlcBackLabel("WELCOME BACK!");
                messagesController.assignNameLabel(getName().toUpperCase());
            } else if (isGoodbye) {
                messagesController.assignByeLabel("GOODBYE! TAKE CARE!");
                messagesController.assignNameLabel(getName().toUpperCase());
            } else if (isInvalid) {
                messagesController.assignInvalidCardLabel("INVALID CARD, PLEASE TRY AGAIN");
            } else if (isClockedIn) {
                messagesController.assignClockedInLabel("YOU HAVE CLOCKED IN TODAY");
            } else if (isNotClockedIn) {
                messagesController.assignClockOutLabel("YOU HAVE YET TO CLOCK IN TODAY");
            }else if(isClockedOut){
                messagesController.assignClockedOutLabel("YOU HAVE CLOCKED OUT TODAY");
            }else if(isNotAdmin){
                messagesController.assignNotAuthorisedLabel("YOU ARE NOT AUTHORISED TO ENTER");
            }
            setPane(pane);
            messagesController.backHomeScene();

        } catch (IOException e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public boolean isAdmin(){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        String verifyAcc = "SELECT * FROM employee,admin WHERE employee.emp_id = admin.emp_id AND nfc_num = '"+UID + "'" ;

        try{
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyAcc);

            if(queryResult.next()) {
                return true;
            }

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
        return false;
    }

    public void openAdminPage()  {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin_page.fxml"));
            AnchorPane pane = loader.load();
            rootPane.getChildren().setAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
