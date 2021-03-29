package AwNFC;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.TerminalFactory;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        checkNullClockOut();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
        Parent root = (Parent) loader.load();
        HomeController homeController = loader.getController();
        homeController.setDateLabel(getCurrentDate());
        homeController.setTimePane();


        primaryStage.setTitle("Attendance with NFC");
        primaryStage.setScene(new Scene(root, 1000, 600));
        primaryStage.initStyle(StageStyle.UTILITY);
        primaryStage.show();

        primaryStage.setOnCloseRequest(windowEvent -> {
            Platform.exit();
            System.exit(0);
        });
    }


    public static void main(String[] args) {
        launch(args);
    }

    public void updateNullClockOut(int id){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String updateClockOut = "UPDATE attendance SET clockout_time = '23:59:59' WHERE att_id = ?";
        try{
            PreparedStatement ps = connectDB.prepareStatement(updateClockOut);
            ps.setInt(1,id);
            ps.executeUpdate();
            
        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    public void checkNullClockOut(){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        String retrieveID = "SELECT * FROM attendance WHERE clockout_time IS NULL";

        try{
            Statement statement = connectDB.createStatement();
            ResultSet rs = statement.executeQuery(retrieveID);

            while (rs.next()){
                if(getDate(rs.getDate("clockin_date")).before(getCurrentDate())){
                    updateNullClockOut(rs.getInt("att_id"));
                }

            }

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }

    }

    public Date getCurrentDate(){
        Calendar cCurrent = Calendar.getInstance();
        cCurrent.set(Calendar.HOUR_OF_DAY, 0);
        cCurrent.set(Calendar.MINUTE,0);
        cCurrent.set(Calendar.SECOND,0);
        cCurrent.set(Calendar.MILLISECOND,0);

        Date currentDate = cCurrent.getTime();
        return currentDate;
    }

    public Date getDate(java.sql.Date date){
        Calendar cSelected = Calendar.getInstance();
        cSelected.setTime(date);
        cSelected.set(Calendar.HOUR_OF_DAY, 0);
        cSelected.set(Calendar.MINUTE,0);
        cSelected.set(Calendar.SECOND,0);
        cSelected.set(Calendar.MILLISECOND,0);

        return cSelected.getTime();
    }
}
