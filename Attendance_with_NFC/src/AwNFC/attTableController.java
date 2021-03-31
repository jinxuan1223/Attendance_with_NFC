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

public class attTableController implements Initializable {

    @FXML
    private AnchorPane pane_AttDB;

    @FXML
    private Button btn_Back;

    @FXML
    private TextField search_EmpID;

    @FXML
    private TextField search_InTime;

    @FXML
    private TextField search_Date;

    @FXML
    private TextField search_OutTime;

    @FXML
    private TextField search_ArrStatus;

    @FXML
    private TextField search_EmpName;

    @FXML
    private Button btn_Print;

    @FXML
    private TextField search_LeaveStatus;

    @FXML
    private TableView<attDetails> table_AttDB;

    @FXML
    private TableColumn<attDetails, Integer> col_EmpID;

    @FXML
    private TableColumn<attDetails, String> col_EmpName;

    @FXML
    private TableColumn<attDetails, String> col_Date;

    @FXML
    private TableColumn<attDetails, String> col_inTime;

    @FXML
    private TableColumn<attDetails, String> col_outTime;

    @FXML
    private TableColumn<attDetails, String> col_ArrivalStatus;

    @FXML
    private TableColumn<attDetails, String> col_LeaveStatus;

    ObservableList<attDetails> listM;
    ObservableList<attDetails> dataList;

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pstmt = null;

    @FXML
    void btn_Back(ActionEvent event) {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("currAttTable.fxml"));
            pane_AttDB.getChildren().setAll(pane);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    @FXML
    void search_Table() {
        col_EmpName.setCellValueFactory(new PropertyValueFactory<attDetails, String>("empName"));
        col_inTime.setCellValueFactory(new PropertyValueFactory<attDetails, String>("inTime"));
        col_Date.setCellValueFactory(new PropertyValueFactory<attDetails, String>("date"));
        col_outTime.setCellValueFactory(new PropertyValueFactory<attDetails, String>("outTime"));
        col_ArrivalStatus.setCellValueFactory(new PropertyValueFactory<attDetails, String>("isLate"));
        col_LeaveStatus.setCellValueFactory(new PropertyValueFactory<attDetails, String>("leaveStatus"));
        col_EmpID.setCellValueFactory(new PropertyValueFactory<attDetails, Integer>("empId"));
        dataList = DatabaseConnection.getAttData();
        table_AttDB.setItems(dataList);
        FilteredList<attDetails> filteredData = new FilteredList<>(dataList, b -> true);
        table_AttDB.setItems(filteredData);
        search_EmpID.textProperty().addListener((obsVal, oldValue, newValue) -> {
            filteredData.setPredicate(person ->
                    String.valueOf(person.getEmpId()).contains(search_EmpID.getText()) &&
                            person.getIsLate().contains(search_ArrStatus.getText()) &&
                            person.getLeaveStatus().contains(search_LeaveStatus.getText()) &&
                            person.getInTime().contains(search_InTime.getText()) &&
                            person.getOutTime().contains(search_OutTime.getText()) &&
                            person.getEmpName().contains(search_EmpName.getText()) &&
                            person.getDate().contains(search_Date.getText())
            );
        });
        search_ArrStatus.textProperty().addListener((obsVal, oldValue, newValue) -> {
            filteredData.setPredicate(person ->
                    String.valueOf(person.getEmpId()).contains(search_EmpID.getText()) &&
                            person.getIsLate().contains(search_ArrStatus.getText()) &&
                            person.getLeaveStatus().contains(search_LeaveStatus.getText()) &&
                            person.getInTime().contains(search_InTime.getText()) &&
                            person.getOutTime().contains(search_OutTime.getText()) &&
                            person.getEmpName().contains(search_EmpName.getText()) &&
                            person.getDate().contains(search_Date.getText())
            );
        });
        search_EmpName.textProperty().addListener((obsVal, oldValue, newValue) -> {
            filteredData.setPredicate(person ->
                    String.valueOf(person.getEmpId()).contains(search_EmpID.getText()) &&
                            person.getIsLate().contains(search_ArrStatus.getText()) &&
                            person.getLeaveStatus().contains(search_LeaveStatus.getText()) &&
                            person.getInTime().contains(search_InTime.getText()) &&
                            person.getOutTime().contains(search_OutTime.getText()) &&
                            person.getEmpName().contains(search_EmpName.getText()) &&
                            person.getDate().contains(search_Date.getText())
            );
        });
        search_InTime.textProperty().addListener((obsVal, oldValue, newValue) -> {
            filteredData.setPredicate(person ->
                    String.valueOf(person.getEmpId()).contains(search_EmpID.getText()) &&
                            person.getIsLate().contains(search_ArrStatus.getText()) &&
                            person.getLeaveStatus().contains(search_LeaveStatus.getText()) &&
                            person.getInTime().contains(search_InTime.getText()) &&
                            person.getOutTime().contains(search_OutTime.getText()) &&
                            person.getEmpName().contains(search_EmpName.getText()) &&
                            person.getDate().contains(search_Date.getText())
            );
        });
        search_Date.textProperty().addListener((obsVal, oldValue, newValue) -> {
            filteredData.setPredicate(person ->
                    String.valueOf(person.getEmpId()).contains(search_EmpID.getText()) &&
                            person.getIsLate().contains(search_ArrStatus.getText()) &&
                            person.getLeaveStatus().contains(search_LeaveStatus.getText()) &&
                            person.getInTime().contains(search_InTime.getText()) &&
                            person.getOutTime().contains(search_OutTime.getText()) &&
                            person.getEmpName().contains(search_EmpName.getText()) &&
                            person.getDate().contains(search_Date.getText())
            );
        });
        search_OutTime.textProperty().addListener((obsVal, oldValue, newValue) -> {
            filteredData.setPredicate(person ->
                    String.valueOf(person.getEmpId()).contains(search_EmpID.getText()) &&
                            person.getIsLate().contains(search_ArrStatus.getText()) &&
                            person.getLeaveStatus().contains(search_LeaveStatus.getText()) &&
                            person.getInTime().contains(search_InTime.getText()) &&
                            person.getOutTime().contains(search_OutTime.getText()) &&
                            person.getEmpName().contains(search_EmpName.getText()) &&
                            person.getDate().contains(search_Date.getText())
            );
        });
        search_LeaveStatus.textProperty().addListener((obsVal, oldValue, newValue) -> {
            filteredData.setPredicate(person ->
                    String.valueOf(person.getEmpId()).contains(search_EmpID.getText()) &&
                            person.getIsLate().contains(search_ArrStatus.getText()) &&
                            person.getLeaveStatus().contains(search_LeaveStatus.getText()) &&
                            person.getInTime().contains(search_InTime.getText()) &&
                            person.getOutTime().contains(search_OutTime.getText()) &&
                            person.getEmpName().contains(search_EmpName.getText()) &&
                            person.getDate().contains(search_Date.getText())
            );
        });
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
    void update_Table() {
        col_EmpName.setCellValueFactory(new PropertyValueFactory<attDetails, String>("empName"));
        col_inTime.setCellValueFactory(new PropertyValueFactory<attDetails, String>("inTime"));
        col_Date.setCellValueFactory(new PropertyValueFactory<attDetails, String>("date"));
        col_outTime.setCellValueFactory(new PropertyValueFactory<attDetails, String>("outTime"));
        col_ArrivalStatus.setCellValueFactory(new PropertyValueFactory<attDetails, String>("isLate"));
        col_LeaveStatus.setCellValueFactory(new PropertyValueFactory<attDetails, String>("leaveStatus"));
        col_EmpID.setCellValueFactory(new PropertyValueFactory<attDetails, Integer>("empId"));
        dataList = DatabaseConnection.getAttData();
        table_AttDB.setItems(dataList);
    }

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        update_Table();
        search_Table();
    }
}