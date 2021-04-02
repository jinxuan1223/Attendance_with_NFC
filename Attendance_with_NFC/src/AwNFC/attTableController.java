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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javax.swing.JOptionPane;

public class attTableController implements Initializable {
    private String mode;

    @FXML
    private AnchorPane pane_AttDB;

    @FXML
    private Button btn_Back;

    @FXML
    private Button btn_Search;

    @FXML
    private Button btn_Back_Today;

    @FXML
    private Label label_Title;

    @FXML
    private TextField search_StaffID;

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
    private Button btn_Export;

    @FXML
    private TextField search_LeaveStatus;

    @FXML
    private Button btn_Import;

    @FXML
    private TableView<attDetails> table_AttDB;

    @FXML
    private TableColumn<attDetails, String> col_StaffID;

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

    ObservableList<attDetails> dataList;

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pstmt = null;

    @FXML
    void btn_Search(ActionEvent event) {
        try {
            label_Title.setText("Attendance Database");
            label_Title.setLayoutX(530.0);
            label_Title.setLayoutY(23.0);
            btn_Search.setVisible(false);
            btn_Back.setVisible(false);
            btn_Back_Today.setVisible(true);
            search_StaffID.setVisible(true);
            search_ArrStatus.setVisible(true);
            search_Date.setVisible(true);
            search_EmpName.setVisible(true);
            search_InTime.setVisible(true);
            search_OutTime.setVisible(true);
            search_LeaveStatus.setVisible(true);
            setMode("All");
            update_Table();
            search_Table();
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    @FXML
    void btn_Back(ActionEvent event) {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("admin_page.fxml"));
            pane_AttDB.getChildren().setAll(pane);
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    @FXML
    void btn_Back_Today(ActionEvent event) {
        try {
            label_Title.setText("Today's Attendance Database");
            label_Title.setLayoutX(465.0);
            label_Title.setLayoutY(23.0);
            btn_Search.setVisible(true);
            btn_Back.setVisible(true);
            btn_Back_Today.setVisible(false);
            search_StaffID.setVisible(false);
            search_ArrStatus.setVisible(false);
            search_Date.setVisible(false);
            search_EmpName.setVisible(false);
            search_InTime.setVisible(false);
            search_OutTime.setVisible(false);
            search_LeaveStatus.setVisible(false);
            setMode("Today");
            update_Table();
            search_Table();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    @FXML
    void search_Table() {
        update_Table();
        FilteredList<attDetails> filteredData = new FilteredList<>(dataList, b -> true);
        table_AttDB.setItems(filteredData);
        search_StaffID.textProperty().addListener((obsVal, oldValue, newValue) -> {
            filteredData.setPredicate(person ->
                    person.getStaffID().contains(search_StaffID.getText()) &&
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
                    person.getStaffID().contains(search_StaffID.getText()) &&
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
                    person.getStaffID().contains(search_StaffID.getText()) &&
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
                    person.getStaffID().contains(search_StaffID.getText()) &&
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
                    person.getStaffID().contains(search_StaffID.getText()) &&
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
                    person.getStaffID().contains(search_StaffID.getText()) &&
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
                    person.getStaffID().contains(search_StaffID.getText()) &&
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
    void btn_Export(ActionEvent event) {
        /*String sql, date, time, fileName, filePath;
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
        }*/
    }

    @FXML
    void btn_Import(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("import_csv.fxml"));
            AnchorPane pane = loader.load();
            ImportCsvController importCsvController = loader.getController();
            importCsvController.setTableName("attendance_Table");
            pane_AttDB.getChildren().setAll(pane);
        }catch (Exception e){
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
        col_StaffID.setCellValueFactory(new PropertyValueFactory<attDetails, String>("staffID"));
        dataList = DatabaseConnection.getAttData(getMode());
        table_AttDB.setItems(dataList);
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getMode() {
        return mode;
    }

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        setMode("Today");
        update_Table();
        search_Table();
    }
}