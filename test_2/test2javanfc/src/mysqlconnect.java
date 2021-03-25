import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.*;
import javax.swing.JOptionPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class mysqlconnect {
    Connection conn = null;
    public static Connection ConnectDb(){
        try {
            String url = "jdbc:mysql://localhost:3306/test";
            String hostUser = "root";
            String hostPword = "HidayatJ48";
            Class.forName("com.mysql.jdbc.Driver");
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
            String sql = "select * from emp_Details";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                list.add(new empDetails(Integer.parseInt(rs.getString("emp_ID")), rs.getString("emp_Name"), rs.getString("emp_SerNum")));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return list;
    }

}
