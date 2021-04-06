package AwNFC;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.*;
import java.util.Calendar;
import java.util.Date;

public class PromptReasonController {

    private String reason,UID;
    private String defaultOption = "---------------------------------------------------------------------- Please Select A Reason ----------------------------------------------------------------------";
    private boolean isOther = false;
    private ObservableList<String> reasonList = FXCollections.observableArrayList(defaultOption, "Annual Leave", "Unpaid Leave", "Emergency Leave", "Medical Leave", "External Task", "Others");
    private AwNBot bot;

    @FXML
    private ChoiceBox reasonCB;

    @FXML
    private Label otherLabel;

    @FXML
    private TextField otherReasonTF;

    @FXML
    private Button submitBtn;

    @FXML
    private Button backBtn;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Label errorLabel;

    public void setUID(String UID){
        this.UID = UID;
    }

    @FXML
    public void initialize(){
        bot = Main.getBot();
        reasonCB.setValue(defaultOption);
        reasonCB.setItems(reasonList);
        otherLabel.setVisible(false);
        otherReasonTF.setVisible(false);
        reasonCB.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                if(reasonCB.getItems().get((Integer) t1).equals("Others")){
                    isOther = true;
                    otherLabel.setVisible(true);
                    otherReasonTF.setVisible(true);
                }else{
                    isOther = false;
                    otherLabel.setVisible(false);
                    otherReasonTF.setVisible(false);
                }
            }
        });
    }

    private String getMessage(){
        Connection connectDB = DatabaseConnection.getConnection();
        String currDate = DatabaseConnection.getCurrDate();
        String msg;
        String sql;
        String arrStatus;
            sql = "SELECT emp_table.emp_ID, emp_table.staff_ID, emp_table.name, attendance_table.date, attendance_table.inTime, attendance_table.outTime, attendance_table.isLate, attendance_table.leaving_status FROM emp_table INNER JOIN attendance_table ON emp_table.emp_id=attendance_table.emp_id WHERE attendance_table.emp_ID = '" + getEmp_id() + "' and attendance_table.date = STR_TO_DATE( '" + currDate + "','%Y-%m-%d')";
            try {
                Statement ps = connectDB.createStatement();
                ResultSet rs = ps.executeQuery(sql);
                while (rs.next()) {
                    if (rs.getBoolean("isLate") == false) {
                        arrStatus = "On time";
                    } else {
                        arrStatus = "Late";
                    }
                    msg = "\uD83D\uDD55 `" + rs.getString("name") + "` *clocked out*\n" +
                            " *├ Staff ID:* `" + rs.getString("staff_ID") + "` \n" +
                            " *├ Date:* `" + rs.getString("date") + "` \n" +
                            " *├ Clocked In Time:* `" + rs.getString("inTime") + "` \n" +
                            " *├ Clocked Out Time:* `" + rs.getString("outTime") + "` \n" +
                            " *├ Arrival Status:* `" + arrStatus + "` \n" +
                            " *└ Leave Status:* `" + rs.getString("leaving_Status") + "` \n";
                    System.out.println(msg);
                    return msg;
                }
            } catch (Exception e) {
                e.printStackTrace();
                e.getCause();
            }
        return "-";
    }

    public void btn_Submit(ActionEvent event){
        if(isOther){
            reason = otherReasonTF.getText();
        }else{
            reason = reasonCB.getValue().toString();
        }

        if(reason == null || reason.equals(defaultOption)){
            errorLabel.setVisible(true);
        }else{
            updateClockOut();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/messages.fxml"));
            try {
                AnchorPane pane = loader.load();
                MessagesController messagesController = loader.getController();
                messagesController.assignByeLabel("GOODBYE! TAKE CARE!");
                messagesController.assignNameLabel(getName().toUpperCase());
                bot.sendMessage(getMessage());
                rootPane.getChildren().setAll(pane);
                messagesController.backHomeScene();
            }catch (Exception e){
                e.printStackTrace();
                e.getCause();
            }
        }
    }

    public void btn_Back(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/home.fxml"));
        AnchorPane pane = loader.load();
        HomeController homeController = loader.getController();
        homeController.setDateLabel(Calendar.getInstance().getTime());
        homeController.setTimePane();

        rootPane.getChildren().setAll(pane);
    }

    public void updateClockOut(){
        Connection connectDB = DatabaseConnection.getConnection();

        Date date=new Date();
        java.sql.Date currentDate = new java.sql.Date(date.getTime());
        java.sql.Timestamp currentTime=new java.sql.Timestamp(date.getTime());

        String newAtt = "UPDATE attendance_table SET outTime =?, leaving_status =? WHERE emp_ID =? AND date =?";
        try {
            PreparedStatement ps = connectDB.prepareStatement(newAtt);
            ps.setTimestamp(1,currentTime);
            ps.setString(2,reason);
            ps.setInt(3,getEmp_id());
            ps.setDate(4,currentDate);
            ps.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private int getEmp_id(){
        Connection connectDB = DatabaseConnection.getConnection();
        String getEmpID = "SELECT emp_ID FROM emp_table WHERE serial_Num =?";
        try{
            PreparedStatement ps = connectDB.prepareStatement(getEmpID);
            ps.setString(1,UID);

            ResultSet queryResult = ps.executeQuery();

            if(queryResult.next()){
                return queryResult.getInt("emp_ID");
            }
        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
        return 0;
    }

    private String getName()  {
        Connection connectDB = DatabaseConnection.getConnection();

        String retrieveName = "SELECT name FROM emp_table WHERE serial_Num =?";

        try {
            PreparedStatement ps = connectDB.prepareStatement(retrieveName);
            ps.setString(1,UID);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                return rs.getString("name");
            }

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
        return null;
    }
}
