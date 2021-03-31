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
        String databaseName = "emp_db";
        String databaseUser = "root";
        String databasePassword = "HidayatJ48";
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

    public static void createDB() {
        String databaseName = "emp_db";
        String databaseUser = "root";
        String databasePassword = "HidayatJ48";
        String url = "jdbc:mysql://localhost:3306/";
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
            sql = "CREATE TABLE IF NOT EXISTS employee (emp_id INT NOT NULL AUTO_INCREMENT, emp_name VARCHAR(50) NOT NULL, nfc_num VARCHAR(45) NOT NULL, PRIMARY KEY (emp_id), UNIQUE INDEX emp_id_UNIQUE (emp_id ASC) VISIBLE, UNIQUE INDEX nfc_num_UNIQUE (nfc_num ASC) VISIBLE)";
            stmt.execute(sql);

            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url + databaseName, databaseUser, databasePassword);
            stmt = databaseLink.createStatement();
            sql = "CREATE TABLE IF NOT EXISTS attendance(att_id INT NOT NULL AUTO_INCREMENT UNIQUE, date DATE, clockin_time TIME, clockout_time TIME, isLate BOOLEAN DEFAULT 0, leaving_status VARCHAR(50), emp_id INT NOT NULL, PRIMARY KEY(att_id), FOREIGN KEY(emp_id) REFERENCES employee(emp_id));";
            stmt.execute(sql);

            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url + databaseName, databaseUser, databasePassword);
            stmt = databaseLink.createStatement();
            sql = "CREATE TABLE IF NOT EXISTS admin(admin_id INT AUTO_INCREMENT NOT NULL UNIQUE, emp_id INT NOT NULL, PRIMARY KEY(admin_id), FOREIGN KEY(emp_id) REFERENCES employee(emp_id));";
            stmt.execute(sql);

            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url + databaseName, databaseUser, databasePassword);
            stmt = databaseLink.createStatement();
            sql = "CREATE TABLE IF NOT EXISTS currAttendance(att_id INT, date DATE, clockin_time TIME, clockout_time TIME, isLate BOOLEAN DEFAULT 0, leaving_status VARCHAR(50), emp_id INT);";
            stmt.execute(sql);

            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url + databaseName, databaseUser, databasePassword);
            stmt = databaseLink.createStatement();
            sql = "INSERT INTO currAttendance (att_id, date, clockin_time, clockout_time, isLate, leaving_status, emp_id) SELECT DISTINCT att_id, date, clockin_time, clockout_time, isLate, leaving_status, emp_id FROM attendance;";
            stmt.executeUpdate(sql);

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
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
        String sql, outTime, arrStr, leaveStatus;
        int arrStatus;
        try {
            sql = "SELECT employee.emp_id, employee.emp_name, attendance.date, attendance.clockin_time, attendance.clockout_time, attendance.isLate, attendance.leaving_status FROM employee INNER JOIN attendance ON employee.emp_id=attendance.emp_id;";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                outTime = rs.getString("clockout_time");
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
            sql = "SELECT employee.emp_id, employee.emp_name, attendance.date, attendance.clockin_time, attendance.clockout_time, attendance.isLate, attendance.leaving_status FROM employee INNER JOIN attendance ON employee.emp_id=attendance.emp_id WHERE attendance.date = '" + currDate + "';";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                outTime = rs.getString("clockout_time");
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
                list.add(new currAttDetails(Integer.parseInt(rs.getString("emp_id")), rs.getString("emp_name"), rs.getString("date"), rs.getString("clockin_time"), outTime, arrStr, leaveStatus));
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

