package AwNFC;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javax.swing.JOptionPane;

public class empAddController implements Initializable {
    String buttonID;
    String edit_StaffID;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane pane_AddEmp;

    @FXML
    private Button btn_Back;

    @FXML
    private TextField txt_ID;

    @FXML
    private TextField txt_Name;

    @FXML
    private TextField txt_JobTitle;

    @FXML
    private TextField txt_SerNum;

    @FXML
    private Label errorLabel;

    ObservableList<empDetails> listM;

    int index = -1;

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pstmt = null;

    @FXML
    void btn_Back(ActionEvent event) {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("empTable.fxml"));
            pane_AddEmp.getChildren().setAll(pane);
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    @FXML
    void btn_Submit(ActionEvent event) {
        conn = DatabaseConnection.getConnection();
        String sql;
        String name = txt_Name.getText();
        String serialNum = txt_SerNum.getText();
        String staffID = txt_ID.getText();
        String jobTitle = txt_JobTitle.getText();
        String createdAt = DatabaseConnection.getCurrDateTime();
        String deletedAt = null;
        String updatedAt;
        if(buttonID.equals("btn_Add")) {
            updatedAt = null;
            sql = "insert into emp_Table (staff_ID, name, created_At, updated_At, deleted_At, serial_Num, job_Title) values (?, ?, STR_TO_DATE(?,'%d-%m-%Y %H:%i:%s'), ?, ?, ?, ?)";
            try {
                if (name.equals("") || serialNum.equals("") || staffID.equals("") || jobTitle.equals("")) {
                    errorLabel.setText("Check if any of the entries are empty.");
                }else if (isSerNumExist(serialNum)) {
                    errorLabel.setText("This Serial Number has been taken.");
                }else if (isStaffIDExist(staffID)) {
                    errorLabel.setText("This Staff ID has been taken.");
                }else {
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, staffID);
                    pstmt.setString(2, name);
                    pstmt.setString(3, createdAt);
                    pstmt.setString(4, updatedAt);
                    pstmt.setString(5, deletedAt);
                    pstmt.setString(6, serialNum);
                    pstmt.setString(7, jobTitle);
                    pstmt.execute();
                    AnchorPane pane = FXMLLoader.load(getClass().getResource("empTable.fxml"));
                    pane_AddEmp.getChildren().setAll(pane);
                }
            } catch (Exception e) {
                e.printStackTrace();
                e.getCause();
            }
        }
        else if(buttonID.equals("btn_Update")) {
            updatedAt = DatabaseConnection.getCurrDateTime();
            System.out.println(name);
            System.out.println(serialNum);
            System.out.println(staffID);
            System.out.println(jobTitle);
            System.out.println(updatedAt);
            sql = "update emp_Table set updated_At = STR_TO_DATE('" + updatedAt + "' ,'%d-%m-%Y %H:%i:%s'), staff_ID = '" + staffID + "', name = '" + name + "', serial_Num = '" + serialNum + "', job_Title = '" + jobTitle + "' where staff_ID = '" + edit_StaffID + "';";
            try {
                if (name.equals("") || serialNum.equals("") || staffID.equals("") || jobTitle.equals("")) {
                    errorLabel.setText("Check if any of the entries are empty.");
                }else if (isSerNumExist(serialNum)) {
                   errorLabel.setText("This Serial Number has been taken.");
                }else if (isStaffIDExist(staffID)) {
                    errorLabel.setText("This Staff ID has been taken.");
                }else {
                    pstmt = conn.prepareStatement(sql);
                    pstmt.execute();
                    AnchorPane pane = FXMLLoader.load(getClass().getResource("empTable.fxml"));
                    pane_AddEmp.getChildren().setAll(pane);
                }
            } catch (Exception e) {
                e.printStackTrace();
                e.getCause();
            }
        }
    }

    @FXML
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void setButtonID(String buttonID) {
        this.buttonID = buttonID;
    }

    public void setEditStaffID(String edit_StaffID) {
        this.edit_StaffID = edit_StaffID;
    }

    public void enterEmpSerNum(String edit_SerialNum){
        txt_SerNum.setText(edit_SerialNum);
    }

    public boolean isSerNumExist(String UID){
        conn = DatabaseConnection.getConnection();
        String verifyAcc = "SELECT * FROM emp_Table WHERE serial_Num = '" + UID + "'" ;

        try{
            Statement statement = conn.createStatement();
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

    public boolean isStaffIDExist(String staffID){
        conn = DatabaseConnection.getConnection();
        String verifyAcc = "SELECT * FROM emp_Table WHERE staff_ID = '" + staffID + "'" ;

        try{
            Statement statement = conn.createStatement();
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
}

