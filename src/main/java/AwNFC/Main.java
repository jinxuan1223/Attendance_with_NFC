package AwNFC;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * The main driver to run the attendance system
 * @author Jin Xuan
 */

public class Main extends Application {

    private static AwNBot bot = new AwNBot();
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader;
        Parent root;
        HomeController homeController;
        initBot(bot);

        if(hasAdmin()){
            checkNullClockOut();
            loader = new FXMLLoader(getClass().getResource("/home.fxml"));
            root =  loader.load();
            homeController = loader.getController();
            homeController.setDateLabel(getCurrentDate());
            homeController.setTimePane();
        }else{
            loader = new FXMLLoader(getClass().getResource("/first_admin.fxml"));
            root = loader.load();
            startRead(loader);
        }



        primaryStage.setTitle("Attendance with NFC");
        primaryStage.setScene(new Scene(root, 1400, 750));
        primaryStage.setResizable(false);
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
        Connection connectDB = DatabaseConnection.getConnection();

        String updateClockOut = "UPDATE attendance_table SET outTime = '23:59:59', leaving_Status = null WHERE att_id = ?";
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
        Connection connectDB = DatabaseConnection.getConnection();
        String retrieveID = "SELECT * FROM attendance_table WHERE outTime IS NULL";

        try{
            Statement statement = connectDB.createStatement();
            ResultSet rs = statement.executeQuery(retrieveID);

            while (rs.next()){
                if(getDate(rs.getDate("date")).before(getCurrentDate())){
                    updateNullClockOut(rs.getInt("att_ID"));
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

        return cCurrent.getTime();
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

    public boolean hasAdmin(){
        Connection connectDB = DatabaseConnection.getConnection();
        String getAdmin = "Select * from emp_table where job_Title = 'Admin' and deleted_At is null";

        try{
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(getAdmin);

            if(queryResult.next())
                return true;
        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
        return false;
    }

    private void startRead(FXMLLoader loader){
        try {
            Timer timer = new Timer(); //At this line a new Thread will be created
            timer.scheduleAtFixedRate(new NFCRead(loader, "FirstAdminController"), 0, 500);

        } catch (Exception ex) {
            Logger.getLogger(NFCRead.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initBot(AwNBot bot){
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public static AwNBot getBot(){
        return bot;
    }


}
