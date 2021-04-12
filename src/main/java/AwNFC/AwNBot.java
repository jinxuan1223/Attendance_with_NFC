package AwNFC;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.sql.*;

public class AwNBot extends TelegramLongPollingBot {
    @Override
    public String getBotUsername() {
        return "AwNFC_bot";
    }

    @Override
    public String getBotToken() {
        return "1781978308:AAGIOJqs-fQQ6aFU_Z0LsT9W9pQwso8PfXw";
    }

    @Override
    public void onUpdateReceived(Update update) {
        String command = update.getMessage().getText();

        if(command.contains("/late")) {
            getEmpLate();
        }

        if(command.contains("/avgintime")) {
            getAveInTime();
        }

        if(command.contains("/avgouttime")) {
            getAveOutTime();
        }

        if(command.contains("/avgworktime")) {
            getAveWorkTime();
        }

        if(command.contains("/attpercent")) {
            getAttPercent();
        }

    }

    private void getAveInTime() {
        Connection conn = DatabaseConnection.getConnection();
        String currDate = DatabaseConnection.getCurrDate();
        String msg;
        String sql = "SELECT SEC_TO_TIME(AVG(TIME_TO_SEC(inTime))) as AverageTime FROM attendance_table where date = '" + currDate + "';";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String aveTime = rs.getString("AverageTime");
                msg = "*Average arrival time on* `" + currDate + "` is `" + aveTime + "`";
                sendMessage(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    private void getAveOutTime() {
        Connection conn = DatabaseConnection.getConnection();
        String currDate = DatabaseConnection.getCurrDate();
        String msg;
        String sql = "SELECT SEC_TO_TIME(AVG(TIME_TO_SEC(outTime))) as AverageTime FROM attendance_table where date = '" + currDate + "';";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String aveTime = rs.getString("AverageTime");
                if(aveTime == null) {
                    msg = "*Average leaving time on* `" + currDate + "` is `0`";
                    sendMessage(msg);
                }
                else {
                    msg = "*Average leaving time on* `" + currDate + "` is `" + aveTime + "`";
                    sendMessage(msg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    private void getEmpLate() {
        Connection conn = DatabaseConnection.getConnection();
        int i = 1;
        String currDate = DatabaseConnection.getCurrDate();
        String sql = "SELECT emp_table.name, attendance_table.inTime FROM emp_table INNER JOIN attendance_table ON emp_table.emp_id=attendance_table.emp_id WHERE attendance_table.isLate is true and attendance_table.date = '" + currDate + "';";
        String msg;
        msg = " ⏰ *Employees that came late on* `" + currDate + "` \n";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String name = rs.getString("name");
                String time = rs.getString("inTime");
                if(i == getCurrAtt()) {
                    msg = msg + "  *└* ";
                }
                else {
                    msg = msg + "  *├* ";
                }
                msg = msg + name + " " + time + "\n";
                i++;
            }
            sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void getAveWorkTime() {
        Connection conn = DatabaseConnection.getConnection();
        String currDate = DatabaseConnection.getCurrDate();
        String msg;
        String sql = "SELECT SEC_TO_TIME(AVG(TIME_TO_SEC(SUBTIME(outTime, inTime)))) as AverageTime FROM attendance_table where date = '" + currDate + "';";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String aveTime = rs.getString("AverageTime");
                if(aveTime == null) {
                    msg = "*Average working time on* `" + currDate + "` is `0`";
                    sendMessage(msg);
                }
                else {
                    System.out.println(aveTime);
                    msg = "*Average working time on* `" + currDate + "` is `" + aveTime + "`";
                    sendMessage(msg);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void getAttPercent() {
        String currDate = DatabaseConnection.getCurrDate();
        int numEmp = getNumEmp();
        int numCurrAtt = getCurrAtt();
        float percent;
        percent = (float) numCurrAtt / (float) numEmp * 100;
        String msg;
        msg = "`" + percent + "%` of employees attended to work today on `" + currDate + "`.";
        sendMessage(msg);
    }

    public int getNumEmp() {
        Connection connectDB = DatabaseConnection.getConnection();
        String sql = "select count(*) as 'Total Rows' from emp_Table";
        int numEmp;
        try {
            Statement ps = connectDB.createStatement();
            ResultSet rs = ps.executeQuery(sql);
            while (rs.next()) {
                numEmp = rs.getInt("Total Rows");
                return numEmp;
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        return 0;
    }

    private int getCurrAtt() {
        Connection conn = DatabaseConnection.getConnection();
        String currDate = DatabaseConnection.getCurrDate();
        String sql = "select count(att_ID) as 'Total Rows', date from attendance_table where date = '" + currDate +"' group by date;";
        int currNumAtt;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                currNumAtt = rs.getInt("Total Rows");
                return currNumAtt;
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        return 0;
    }

    // send message to the group using telegram bot
    // please change to ur respective chat id in order to receive the messages
    public void sendMessage(String msg){
        SendMessage message = new SendMessage();
        message.setText(msg);
        message.setChatId("-504972469");
        message.setParseMode("MARKDOWN");
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
