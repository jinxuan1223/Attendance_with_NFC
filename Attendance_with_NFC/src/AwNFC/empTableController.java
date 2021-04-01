package AwNFC;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.sql.*;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;


public class empTableController implements Initializable {

    @FXML
    private AnchorPane pane_EmpDB;

    @FXML
    private Button btn_Add;

    @FXML
    private Button btn_Update;

    @FXML
    private Button btn_Back;

    @FXML
    private Button btn_Delete;

    @FXML
    private TextField search_SerNum;

    @FXML
    private TextField search_Name;

    @FXML
    private TextField search_StaffID;

    @FXML
    private Button btn_Export;

    @FXML
    private Button btn_cmpDB;

    @FXML
    private TextField search_JobTitle;

    @FXML
    private TextField search_CreatedAt;

    @FXML
    private TextField search_UpdatedAt;

    @FXML
    private Button btn_Import;

    @FXML
    private TableView<empDetails> table_EmpDB;

    @FXML
    private TableColumn<empDetails, String> col_StaffID;

    @FXML
    private TableColumn<empDetails, String> col_EmpName;

    @FXML
    private TableColumn<empDetails, String> col_EmpSerNum;

    @FXML
    private TableColumn<empDetails, String> col_JobTitle;

    @FXML
    private TableColumn<empDetails, String> col_CreatedAt;

    @FXML
    private TableColumn<empDetails, String> col_UpdatedAt;

    ObservableList<empDetails> dataList;

    int index = -1;

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pstmt = null;

    @FXML
    void add_Emp(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("empAdd.fxml"));
            AnchorPane pane = loader.load();
            pane_EmpDB.getChildren().setAll(pane);
            empAddController obj = loader.getController();
            obj.setButtonID(btn_Add.getId());
            startRead(loader);
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    @FXML
    void btn_Back(ActionEvent event) {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("admin_page.fxml"));
            pane_EmpDB.getChildren().setAll(pane);
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    @FXML
    void btn_Export(ActionEvent event) {
                /*
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
        }*/
    }

    @FXML
    void btn_Import(ActionEvent event) {

    }

    @FXML
    void btn_cmpDB(ActionEvent event) {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("admin_page.fxml"));
            pane_EmpDB.getChildren().setAll(pane);
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    @FXML
    void delete_Selected(ActionEvent event) {
        /*index = table_EmpDB.getSelectionModel().getSelectedIndex();
        if (index <= -1) {
            return;
        }
        conn = DatabaseConnection.getConnection();
        String sql1 = "delete from attendance where emp_id = ?";
        String sql2 = "delete from employee where emp_id = ?";
        String del_ID = col_EmpID.getCellData(index).toString();
        try {
            pstmt = conn.prepareStatement(sql1);
            pstmt.setString(1, del_ID);
            pstmt.execute();
            pstmt = conn.prepareStatement(sql2);
            pstmt.setString(1, del_ID);
            pstmt.execute();
            update_Table();
            search_Table();
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }*/
    }

    @FXML
    void edit_Selected() {
        index = table_EmpDB.getSelectionModel().getSelectedIndex();
        if (index <= -1) {
            return;
        }
        String edit_EmpID = col_StaffID.getCellData(index).toString();
        String edit_EmpName = col_EmpName.getCellData(index).toString();
        String edit_EmpSerNum = col_EmpSerNum.getCellData(index).toString();
        String edit_EmpJobTitle = col_JobTitle.getCellData(index).toString();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("empAdd.fxml"));
            AnchorPane pane = loader.load();
            pane_EmpDB.getChildren().setAll(pane);
            empAddController obj = loader.getController();
            obj.setButtonID(btn_Update.getId());
            obj.setEmpID(edit_EmpID);
            obj.setEmpName(edit_EmpName);
            obj.setEmpSerNum(edit_EmpSerNum);
            obj.setEmpJobTitle(edit_EmpJobTitle);
            startRead(loader);
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    @FXML
    void getSelected(MouseEvent event) {
        index = table_EmpDB.getSelectionModel().getSelectedIndex();
        if (index <= -1) {
            return;
        }
    }

    @FXML
    void update_Table() {
        col_StaffID.setCellValueFactory(new PropertyValueFactory<empDetails, String>("staffId"));
        col_EmpName.setCellValueFactory(new PropertyValueFactory<empDetails, String>("name"));
        col_EmpSerNum.setCellValueFactory(new PropertyValueFactory<empDetails, String>("serNum"));
        col_UpdatedAt.setCellValueFactory(new PropertyValueFactory<empDetails, String>("updatedAt"));
        col_JobTitle.setCellValueFactory(new PropertyValueFactory<empDetails, String>("jobTitle"));
        col_CreatedAt.setCellValueFactory(new PropertyValueFactory<empDetails, String>("createdAt"));
        dataList = DatabaseConnection.getEmpData();
        table_EmpDB.setItems(dataList);
    }

    @FXML
    void search_Table() {
        col_StaffID.setCellValueFactory(new PropertyValueFactory<empDetails, String>("staffId"));
        col_EmpName.setCellValueFactory(new PropertyValueFactory<empDetails, String>("name"));
        col_EmpSerNum.setCellValueFactory(new PropertyValueFactory<empDetails, String>("serNum"));
        col_UpdatedAt.setCellValueFactory(new PropertyValueFactory<empDetails, String>("updatedAt"));
        col_JobTitle.setCellValueFactory(new PropertyValueFactory<empDetails, String>("jobTitle"));
        col_CreatedAt.setCellValueFactory(new PropertyValueFactory<empDetails, String>("createdAt"));
        dataList = DatabaseConnection.getEmpData();
        table_EmpDB.setItems(dataList);
        FilteredList<empDetails> filteredData = new FilteredList<>(dataList, b -> true);
        table_EmpDB.setItems(filteredData);
        search_StaffID.textProperty().addListener((obsVal, oldValue, newValue) -> {
            filteredData.setPredicate(person ->
                person.getStaffId().contains(search_StaffID.getText()) &&
                person.getName().contains(search_Name.getText()) &&
                person.getUpdatedAt().contains(search_UpdatedAt.getText()) &&
                person.getJobTitle().contains(search_JobTitle.getText()) &&
                person.getCreatedAt().contains(search_CreatedAt.getText()) &&
                person.getSerNum().contains(search_SerNum.getText()));
        });
        search_Name.textProperty().addListener((obsVal, oldValue, newValue) -> {
            filteredData.setPredicate(person ->
                person.getStaffId().contains(search_StaffID.getText()) &&
                person.getName().contains(search_Name.getText()) &&
                person.getUpdatedAt().contains(search_UpdatedAt.getText()) &&
                person.getJobTitle().contains(search_JobTitle.getText()) &&
                person.getCreatedAt().contains(search_CreatedAt.getText()) &&
                person.getSerNum().contains(search_SerNum.getText()));
        });
        search_SerNum.textProperty().addListener((obsVal, oldValue, newValue) -> {
            filteredData.setPredicate(person ->
                person.getStaffId().contains(search_StaffID.getText()) &&
                person.getName().contains(search_Name.getText()) &&
                person.getUpdatedAt().contains(search_UpdatedAt.getText()) &&
                person.getJobTitle().contains(search_JobTitle.getText()) &&
                person.getCreatedAt().contains(search_CreatedAt.getText()) &&
                person.getSerNum().contains(search_SerNum.getText()));
        });
        search_CreatedAt.textProperty().addListener((obsVal, oldValue, newValue) -> {
            filteredData.setPredicate(person ->
                person.getStaffId().contains(search_StaffID.getText()) &&
                person.getName().contains(search_Name.getText()) &&
                person.getUpdatedAt().contains(search_UpdatedAt.getText()) &&
                person.getJobTitle().contains(search_JobTitle.getText()) &&
                person.getCreatedAt().contains(search_CreatedAt.getText()) &&
                person.getSerNum().contains(search_SerNum.getText()));
        });
        search_JobTitle.textProperty().addListener((obsVal, oldValue, newValue) -> {
            filteredData.setPredicate(person ->
                person.getStaffId().contains(search_StaffID.getText()) &&
                person.getName().contains(search_Name.getText()) &&
                person.getUpdatedAt().contains(search_UpdatedAt.getText()) &&
                person.getJobTitle().contains(search_JobTitle.getText()) &&
                person.getCreatedAt().contains(search_CreatedAt.getText()) &&
                person.getSerNum().contains(search_SerNum.getText()));
        });
        search_UpdatedAt.textProperty().addListener((obsVal, oldValue, newValue) -> {
            filteredData.setPredicate(person ->
                person.getStaffId().contains(search_StaffID.getText()) &&
                person.getName().contains(search_Name.getText()) &&
                person.getUpdatedAt().contains(search_UpdatedAt.getText()) &&
                person.getJobTitle().contains(search_JobTitle.getText()) &&
                person.getCreatedAt().contains(search_CreatedAt.getText()) &&
                person.getSerNum().contains(search_SerNum.getText()));
        });
    }

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        update_Table();
        search_Table();
    }

    private void startRead(FXMLLoader loader){
        try {
            Timer timer = new Timer(); //At this line a new Thread will be created
            timer.scheduleAtFixedRate(new NFCRead(loader, "empAddController"), 0, 500);

        } catch (Exception ex) {
            Logger.getLogger(NFCRead.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}