package AwNFC;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Calendar;


public class FirstAdminController {

    private String staffID, name, serialNum;

    @FXML
    private Button doneBtn;

    @FXML
    private TextField staffID_tf;

    @FXML
    private TextField name_tf;

    @FXML
    private TextField serialNum_tf;

    @FXML
    private Label errorLabel;

    @FXML
    private AnchorPane rootPane;

    public void onDoneBtnClick(ActionEvent event){
        if(staffID_tf.getText().equals("") || name_tf.getText().equals("") || serialNum_tf.getText().equals(""))
            errorLabel.setVisible(true);
        else{
            staffID = staffID_tf.getText();
            name = name_tf.getText();
            serialNum = serialNum_tf.getText();

            java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTimeInMillis());

            Connection connectDB = DatabaseConnection.getConnection();
            String sql = "INSERT INTO emp_table(staff_ID,name, created_At, serial_Num, job_Title ) VALUES (?,?,?,?,?)";

            try{
                PreparedStatement ps = connectDB.prepareStatement(sql);
                ps.setString(1, staffID);
                ps.setString(2,name);
                ps.setDate(3,date);
                ps.setString(4, serialNum);
                ps.setString(5,"Admin");
                ps.executeUpdate();

                toHomePage();

            }catch (Exception e){
                e.printStackTrace();
                e.getCause();
            }


        }
    }

    public void toHomePage(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
            AnchorPane pane = loader.load();
            HomeController homeController = loader.getController();
            homeController.setDateLabel(Calendar.getInstance().getTime());
            homeController.setTimePane();

            rootPane.getChildren().setAll(pane);
        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    public void enterEmpSerNum(String empSerNum){
        serialNum_tf.setText(empSerNum);
    }
}
