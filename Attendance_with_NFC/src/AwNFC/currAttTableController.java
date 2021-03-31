package AwNFC;

import java.net.URL;
import java.util.ResourceBundle;
import java.sql.*;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javax.swing.JOptionPane;

public class currAttTableController implements Initializable {

    @FXML
    private AnchorPane pane_CurrAttDB;

    @FXML
    private Button btn_Back;

    @FXML
    private Button btn_Print;

    @FXML
    private TableView<currAttDetails> table_CurrAttDB;

    @FXML
    private TableColumn<currAttDetails, Integer> col_EmpID;

    @FXML
    private TableColumn<currAttDetails, String> col_EmpName;

    @FXML
    private TableColumn<currAttDetails, String> col_Date;

    @FXML
    private TableColumn<currAttDetails, String> col_inTime;

    @FXML
    private TableColumn<currAttDetails, String> col_outTime;

    @FXML
    private TableColumn<currAttDetails, String> col_ArrivalStatus;

    @FXML
    private TableColumn<currAttDetails, String> col_LeaveStatus;

    ObservableList<currAttDetails> listM;
    ObservableList<currAttDetails> dataList;

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pstmt = null;

    @FXML
    void btn_Back(ActionEvent event) {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("admin_page.fxml"));
            pane_CurrAttDB.getChildren().setAll(pane);

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    @FXML
    void btn_Print(ActionEvent event) {
        String sql, date, time, fileName, filePath;
        PreparedStatement pstmt;
        try {
            Connection conn = DatabaseConnection.getConnection();
            date = DatabaseConnection.getCurrDate();
            time = DatabaseConnection.getCurrTime();
            fileName = date + "_" + time + "_employee.csv";
            System.out.print(fileName);
            filePath = "C:/ProgramData/MySQL/MySQL Server 8.0/Data/" + fileName;
            sql = "SELECT * FROM (SELECT 'Emp ID', 'Emp Name', 'NFC Serial Number' UNION ALL (SELECT emp_id, emp_name, nfc_num FROM employee)) resulting_set INTO OUTFILE '" + filePath + "' FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '\"\' LINES TERMINATED BY '\n'";
            pstmt = conn.prepareStatement(sql);
            pstmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    @FXML
    void btn_Search(ActionEvent event) {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("attTable.fxml"));
            pane_CurrAttDB.getChildren().setAll(pane);

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    @FXML
    void update_Table() {
        col_EmpName.setCellValueFactory(new PropertyValueFactory<currAttDetails, String>("empName"));
        col_inTime.setCellValueFactory(new PropertyValueFactory<currAttDetails, String>("inTime"));
        col_Date.setCellValueFactory(new PropertyValueFactory<currAttDetails, String>("date"));
        col_outTime.setCellValueFactory(new PropertyValueFactory<currAttDetails, String>("outTime"));
        col_ArrivalStatus.setCellValueFactory(new PropertyValueFactory<currAttDetails, String>("isLate"));
        col_LeaveStatus.setCellValueFactory(new PropertyValueFactory<currAttDetails, String>("leaveStatus"));
        col_EmpID.setCellValueFactory(new PropertyValueFactory<currAttDetails, Integer>("empId"));
        dataList = DatabaseConnection.getCurrAttData();
        table_CurrAttDB.setItems(dataList);
    }

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        update_Table();
    }
}