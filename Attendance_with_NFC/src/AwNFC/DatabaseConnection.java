package AwNFC;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.sql.*;

public class DatabaseConnection {
    private static Connection databaseLink;

    public static Connection getConnection(){
        createDB();
        String databaseName = "company_db";
        String databaseUser = "root";
        String databasePassword = "test_123";
        String url = "jdbc:mysql://127.0.0.1:3306/" + databaseName;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);
        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
        return databaseLink;
    }

    public static void createDB() {
        String databaseName = "company_db";
        String databaseUser = "root";
        String databasePassword = "test_123";
        String url = "jdbc:mysql://127.0.0.1:3306/";
        String sql;
        Statement stmt = null;
        try{
            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);
            stmt = databaseLink.createStatement();
            sql = "CREATE DATABASE IF NOT EXISTS " + databaseName;
            stmt.executeUpdate(sql);

            sql = "USE " + databaseName;
            stmt.execute(sql);

            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url + databaseName, databaseUser, databasePassword);
            stmt = databaseLink.createStatement();
            sql = "create table if not exists emp_table(emp_ID int auto_increment not null,staff_ID varchar(50) not null,name varchar(50),created_At datetime,updated_At datetime,deleted_At datetime,serial_Num varchar(50) unique,job_Title varchar(50),primary key (emp_ID));";
            stmt.execute(sql);

            sql = "create table if not exists attendance_table(att_ID int auto_increment not null,emp_ID int not null,date Date,inTime time,outTime time,isLate boolean default 0,leaving_Status VARCHAR(50),primary key(att_ID),unique index attendance_UNIQUE (Date, emp_ID),foreign key(emp_ID) references emp_table(emp_ID));";
            stmt.execute(sql);

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    public static ObservableList<empDetails> getEmpData() {
        Connection conn = getConnection();
        ObservableList<empDetails> list = FXCollections.observableArrayList();
        try {
            String sql = "select * from emp_table";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                list.add(new empDetails(Integer.parseInt(rs.getString("emp_id")), rs.getString("name"), rs.getString("serial_Num")));
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
        String sql, outTime, arrStr, leaveStatus;
        int arrStatus;
        try {
            sql = "SELECT emp_table.emp_id, emp_table.name, attendance_table.date, attendance_table.inTime, attendance_table.outTime, attendance_table.isLate, attendance_table.leaving_status FROM emp_table INNER JOIN attendance_table ON emp_table.emp_id=attendance_table.emp_id;";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                outTime = rs.getString("outTime");
                leaveStatus = rs.getString("leaving_status");
                arrStatus = Integer.parseInt(rs.getString("isLate"));
                if(outTime == null) {
                    if(arrStatus == 0) {
                        outTime = "-";
                        leaveStatus = "-";
                        arrStr = "On Time";
                    }
                    else {
                        outTime = "-";
                        leaveStatus = "-";
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
                list.add(new attDetails(Integer.parseInt(rs.getString("emp_id")), rs.getString("emp_name"), rs.getString("date"), rs.getString("clockin_time"), outTime, arrStr, leaveStatus));
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
        String sql, outTime, arrStr, leaveStatus, currDate;
        int arrStatus;
        currDate = getCurrDate();
        try {
            sql = "SELECT emp_table.emp_id, emp_table.emp_name, attendance_table.date, attendance_table.inTime, attendance_table.outTime, attendance_table.isLate, attendance_table.leaving_status FROM emp_table INNER JOIN attendance_table ON emp_table.emp_id=attendance_table.emp_id WHERE attendance_table.date = '" + currDate + "';";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                outTime = rs.getString("outTime");
                leaveStatus = rs.getString("leaving_status");
                arrStatus = Integer.parseInt(rs.getString("isLate"));
                if(outTime == null) {
                    if(arrStatus == 0) {
                        outTime = "-";
                        leaveStatus = "-";
                        arrStr = "On Time";
                    }
                    else {
                        outTime = "-";
                        leaveStatus = "-";
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
                list.add(new currAttDetails(Integer.parseInt(rs.getString("emp_id")), rs.getString("name"), rs.getString("date"), rs.getString("inTime"), outTime, arrStr, leaveStatus));
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        return list;
    }

    public static String getCurrDate() {
        String currDate;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.now();
        currDate = dtf.format(localDate);
        return currDate;
    }

    public static String getCurrTime() {
        String currTime;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime localTime = LocalTime.now();
        currTime = dtf.format(localTime);
        return currTime;
    }
}

