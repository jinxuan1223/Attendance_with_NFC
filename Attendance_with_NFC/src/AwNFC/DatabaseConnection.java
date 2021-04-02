package AwNFC;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.sql.*;

public class DatabaseConnection {
    private static Connection databaseLink;
    private static String buttonID;

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
        Statement stmt;
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
        String buttonID = getButtonID(), sql = "select * from emp_table where deleted_At is null", deletedAt, updatedAt, serialNum;
        try {
            if(isNullOrEmpty(buttonID)) {
                sql = "select * from emp_table where deleted_At is null";
            }
            else {
                if(buttonID.equals("btn_cmpDB")) {
                    sql = "select * from emp_table";
                }
                else if(buttonID.equals("btn_BackDB") || buttonID.equals("btn_Emp")) {
                    sql = "select * from emp_table where deleted_At is null";
                }
            }
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                deletedAt = rs.getString("deleted_At");
                updatedAt = rs.getString("updated_At");
                serialNum = rs.getString("serial_Num");
                if (deletedAt == null) {
                    deletedAt = "-";
                }
                if (updatedAt == null) {
                    updatedAt = "-";
                }
                if (serialNum == null) {
                    serialNum = "-";
                }
                list.add(new empDetails(Integer.parseInt(rs.getString("emp_ID")), rs.getString("staff_ID"), rs.getString("name"), rs.getString("created_At"), updatedAt, deletedAt, serialNum, rs.getString("job_Title")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        return list;
    }

    public static ObservableList<attDetails> getAttData(String mode) {
        Connection conn = getConnection();
        ObservableList<attDetails> list = FXCollections.observableArrayList();
        String currDate = getCurrDate(),sql = "SELECT emp_table.staff_ID, emp_table.name, attendance_table.date, attendance_table.inTime, attendance_table.outTime, attendance_table.isLate, attendance_table.leaving_status FROM emp_table INNER JOIN attendance_table ON emp_table.emp_id=attendance_table.emp_id WHERE attendance_table.date = '" + currDate + "';", outTime, arrStr, leaveStatus;
        int arrStatus;
        try {
            if(mode.equals("Today")) {
                sql = "SELECT emp_table.staff_ID, emp_table.name, attendance_table.date, attendance_table.inTime, attendance_table.outTime, attendance_table.isLate, attendance_table.leaving_status FROM emp_table INNER JOIN attendance_table ON emp_table.emp_id=attendance_table.emp_id WHERE attendance_table.date = '" + currDate + "';";
            }
            else if(mode.equals("All")){
                sql = "SELECT emp_table.staff_ID, emp_table.name, attendance_table.date, attendance_table.inTime, attendance_table.outTime, attendance_table.isLate, attendance_table.leaving_status FROM emp_table INNER JOIN attendance_table ON emp_table.emp_id=attendance_table.emp_id;";
            }
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
                list.add(new attDetails(rs.getString("staff_ID"), rs.getString("name"), rs.getString("date"), rs.getString("inTime"), outTime, arrStr, leaveStatus));
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

    public static String getCurrDateTime() {
        String currDateTime;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        currDateTime = dtf.format(now);
        return currDateTime;
    }

    public void setButtonID(String buttonID) {
        this.buttonID = buttonID;
    }

    public static String getButtonID() {
        return buttonID;
    }

    public static boolean isNullOrEmpty(String str) {
        if(str != null && !str.trim().isEmpty())
            return false;
        return true;
    }

    public boolean loadCSVtoEmployee(String csvFile){
        String line;
        String insertQuery = "INSERT IGNORE INTO emp_table(staff_ID,name,created_At,deleted_At,updated_At,serial_Num,job_Title) VALUES (?,?,STR_TO_DATE(?,'%d/%m/%Y %H:%i:%s'),STR_TO_DATE(?,'%d/%m/%Y %H:%i:%s'),STR_TO_DATE(?,'%d/%m/%Y %H:%i:%s'),?,?)";
        Boolean success = false;

        try {
            BufferedReader br = new BufferedReader(new FileReader(csvFile));
            //delete data from table before loading csv
            while((line= br.readLine())!=null){
                if(!line.equals("staff_ID,name,created_At,deleted_At,updated_At,serial_Num,job_Title")) {
                    String[] value = line.split(",");
                    if(value.length==7) {
                        PreparedStatement ps = getConnection().prepareStatement(insertQuery);
                        if(value[0].equals(""))
                            break;
                        ps.setString(1, value[0]);

                        if(value[1].equals(""))
                            break;
                        ps.setString(2, value[1]);

                        if(value[2].equals(""))
                            break;
                        ps.setString(3, value[2]);

                        if(value[3].equals(""))
                            ps.setNull(4,Types.TIMESTAMP);
                        else
                            ps.setString(4, value[3]);

                        if(value[4].equals(""))
                            ps.setNull(5,Types.TIMESTAMP);
                        else
                            ps.setString(5, value[4]);

                        if(value[5].equals(""))
                            ps.setNull(6,Types.TIMESTAMP);
                        else
                            ps.setString(6, value[5]);

                        if(value[6].equals(""))
                            ps.setNull(7,Types.TIMESTAMP);
                        else
                            ps.setString(7, value[6]);

                        ps.executeUpdate();
                        success = true;
                    }else{
                        success = false;
                        break;
                    }
                }
            }
            br.close();

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
        return success;
    }

    public boolean loadCSVtoAttendance(String csvFile){

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Boolean success = false;

        String line;
        String insertQuery = "INSERT IGNORE INTO attendance_table(date,inTime,outTime, isLate,leaving_Status,emp_ID)VALUES(?,?,?,?,?,?)";
        try {
            BufferedReader br = new BufferedReader(new FileReader(csvFile));
            //delete data from table before loading csv
            while ((line = br.readLine()) != null) {
                if (!line.equals("date,inTime,outTime,isLate,leaving_Status,staff_ID")) {
                    String[] value = line.split(",");
                    if (value.length == 6) {

                        PreparedStatement ps = getConnection().prepareStatement(insertQuery);
                        if(value[0].equals("")||value[1].equals("")||value[5].equals("")){
                            success = false;
                            break;
                        }
                        System.out.println(value[0]);
                        ps.setDate(1, new java.sql.Date(dateFormat.parse(value[0]).getTime()));
                        ps.setTimestamp(2, new java.sql.Timestamp(timeFormat.parse(value[1]).getTime()));

                        if(value[2].equals(""))
                            ps.setNull(3, Types.TIMESTAMP);
                        else
                            ps.setTimestamp(3, new java.sql.Timestamp(timeFormat.parse(value[2]).getTime()));

                        if (value[3].equals("0"))
                            ps.setBoolean(4, false);
                        else if(value[3].equals("1"))
                            ps.setBoolean(4, true);
                        else
                            ps.setNull(4, Types.BOOLEAN);


                        ps.setString(5, value[4]);

                        if(getEmpID(value[5])!=0)
                            ps.setInt(6, getEmpID(value[5]));
                        else{
                            success = false;
                            break;
                        }
                        ps.executeUpdate();
                        System.out.println("Success");

                        success = true;
                    } else {
                        success = false;
                        break;
                    }
                }
                }
                br.close();

            }catch(Exception e){
                e.printStackTrace();
                e.getCause();
            }
            return success;
    }

    public int getEmpID(String staff_ID){
        String sql = "Select * from emp_table where staff_ID = '"+staff_ID+"'";
        try {
            Statement statement = getConnection().createStatement();
            ResultSet rs = statement.executeQuery(sql);

            if(rs.next()){
                return rs.getInt("emp_id");
            }
        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
        return 0;

    }
}

