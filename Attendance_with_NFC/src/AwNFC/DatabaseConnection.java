package AwNFC;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DatabaseConnection {
    private static Connection databaseLink;

    public static Connection getConnection(){
        String databaseName = "emp_db";
        String databaseUser = "root";
        String databasePassword = "samchan1701";
        String url = "jdbc:mysql://localhost:3306/" + databaseName;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);
        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
        return databaseLink;
    }

    public static ObservableList<empDetails> getEmpData() {
        Connection conn = getConnection();
        ObservableList<empDetails> list = FXCollections.observableArrayList();
        try {
            String sql = "select * from employee";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                list.add(new empDetails(Integer.parseInt(rs.getString("emp_id")), rs.getString("emp_name"), rs.getString("nfc_num")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        return list;
    }

    public static ObservableList<attDetails> getAttData() {
        Connection conn = getConnection();
        ObservableList<attDetails> list = FXCollections.observableArrayList();
        String sql, outTime, arrStr;
        int arrStatus;
        try {
            sql = "select * from copyAttendance";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                outTime = rs.getString("clockout_time");
                arrStatus = Integer.parseInt(rs.getString("isLate"));
                if(outTime == null) {
                    if(arrStatus == 0) {
                        outTime = "-";
                        arrStr = "On Time";
                    }
                    else {
                        outTime = "-";
                        arrStr = "Late";
                    }
                }
                else {
                    if(arrStatus == 0) {
                        arrStr = "On Time";
                    }
                    else {
                        arrStr = "Late";
                    }
                }
                list.add(new attDetails(Integer.parseInt(rs.getString("att_id")), rs.getString("date"), rs.getString("clockin_time"), outTime, arrStr, Integer.parseInt(rs.getString("emp_id"))));
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        return list;
    }

    public static ObservableList<currAttDetails> getCurrAttData() {
        Connection conn = getConnection();
        ObservableList<currAttDetails> list = FXCollections.observableArrayList();
        String sql, outTime, arrStr, currDate;
        int arrStatus;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        currDate = dateFormat.format(Calendar.getInstance().getTime());
        System.out.println(currDate);
        try {
            sql = "select * from copyAttendance where clockin_date = " + "'" + currDate + "'";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                outTime = rs.getString("clockout_time");
                arrStatus = Integer.parseInt(rs.getString("isLate"));
                if(outTime == null) {
                    if(arrStatus == 0) {
                        outTime = "-";
                        arrStr = "On Time";
                    }
                    else {
                        outTime = "-";
                        arrStr = "Late";
                    }
                }
                else {
                    if(arrStatus == 0) {
                        arrStr = "On Time";
                    }
                    else {
                        arrStr = "Late";
                    }
                }
                list.add(new currAttDetails(Integer.parseInt(rs.getString("att_id")), rs.getString("date"), rs.getString("clockin_time"), outTime, arrStr, Integer.parseInt(rs.getString("emp_id"))));
            }
        } catch (Exception e) {
           e.printStackTrace();
           e.getCause();
        }
        return list;
    }
}

