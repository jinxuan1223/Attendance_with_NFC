import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.*;
import javax.swing.JOptionPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.text.SimpleDateFormat;
import java.util.*;

public class mysqlconnect {
    Connection conn = null;
    public static Connection ConnectDb(){
        try {
            String url = "jdbc:mysql://localhost:3306/emp_db";
            String hostUser = "root";
            String hostPword = "HidayatJ48";
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, hostUser, hostPword);
            return conn;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            return null;
        } 
    }

    public static ObservableList<empDetails> getEmpData() {
        Connection conn = ConnectDb();
        ObservableList<empDetails> list = FXCollections.observableArrayList();
        try {
            String sql = "select * from employee";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                list.add(new empDetails(Integer.parseInt(rs.getString("emp_id")), rs.getString("emp_name"), rs.getString("nfc_num")));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return list;
    }

    public static ObservableList<attDetails> getAttData() {
        Connection conn = ConnectDb();
        ObservableList<attDetails> list = FXCollections.observableArrayList();
        String sql, outDate, outTime, arrStr;
        int arrStatus;
        try {
            sql = "select * from copyAttendance";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                outDate = rs.getString("clockout_date");
                outTime = rs.getString("clockout_time");
                arrStatus = Integer.parseInt(rs.getString("isLate"));
                if(outDate == null ||  outTime == null) {
                    if(arrStatus == 0) {
                        outDate = "-";
                        outTime = "-";
                        arrStr = "On Time";
                    }
                    else {
                        outDate = "-";
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
                list.add(new attDetails(Integer.parseInt(rs.getString("att_id")), rs.getString("clockin_date"), rs.getString("clockin_time"), outDate, outTime, arrStr, Integer.parseInt(rs.getString("emp_id"))));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return list;
    }

    public static ObservableList<currAttDetails> getCurrAttData() {
        Connection conn = ConnectDb();
        ObservableList<currAttDetails> list = FXCollections.observableArrayList();
        String sql, outDate, outTime, arrStr, currDate;
        int arrStatus;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        currDate = dateFormat.format(Calendar.getInstance().getTime());
        System.out.println(currDate);
        try {
            sql = "select * from copyAttendance where clockin_date = " + "'" + currDate + "'";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                outDate = rs.getString("clockout_date");
                outTime = rs.getString("clockout_time");
                arrStatus = Integer.parseInt(rs.getString("isLate"));
                if(outDate == null ||  outTime == null) {
                    if(arrStatus == 0) {
                        outDate = "-";
                        outTime = "-";
                        arrStr = "On Time";
                    }
                    else {
                        outDate = "-";
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
                list.add(new currAttDetails(Integer.parseInt(rs.getString("att_id")), rs.getString("clockin_date"), rs.getString("clockin_time"), outDate, outTime, arrStr, Integer.parseInt(rs.getString("emp_id"))));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return list;
    }
    
}
