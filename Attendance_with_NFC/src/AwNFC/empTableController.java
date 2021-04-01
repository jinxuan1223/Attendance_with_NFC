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
import javafx.scene.chart.PieChart;
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
    private Button btn_BackDB;

    @FXML
    private Button btn_Back;

    @FXML
    private TextField search_JobTitle;

    @FXML
    private TextField search_CreatedAt;

    @FXML
    private TextField search_UpdatedAt;

    @FXML
    private TextField search_DeletedAt;

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

    @FXML
    private TableColumn<empDetails, String> col_DeletedAt;

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
    void btn_BackDB(ActionEvent event) {
        try {
            col_DeletedAt.setVisible(false);
            search_DeletedAt.setVisible(false);
            col_EmpName.setMaxWidth(400.0);
            col_EmpName.setMinWidth(400.0);
            col_EmpName.setPrefWidth(400.0);
            btn_cmpDB.setVisible(true);
            btn_BackDB.setVisible(false);
            btn_Back.setVisible(true);
            DatabaseConnection obj = new DatabaseConnection();
            obj.setButtonID(btn_cmpDB.getId());
            update_Table();
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
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("import_csv.fxml"));
            AnchorPane pane = loader.load();
            ImportCsvController importCsvController = loader.getController();
            importCsvController.setTableName("staff_Table");
            pane_EmpDB.getChildren().setAll(pane);
        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    @FXML
    void btn_cmpDB(ActionEvent event) {
        try {
            col_DeletedAt.setVisible(true);
            search_DeletedAt.setVisible(true);
            col_EmpName.setMaxWidth(250.0);
            col_EmpName.setMinWidth(250.0);
            col_EmpName.setPrefWidth(250.0);
            btn_cmpDB.setVisible(false);
            btn_BackDB.setVisible(true);
            btn_Back.setVisible(false);
            DatabaseConnection obj = new DatabaseConnection();
            obj.setButtonID(btn_cmpDB.getId());
            update_Table();
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    @FXML
    void disable_Selected(ActionEvent event) {
        index = table_EmpDB.getSelectionModel().getSelectedIndex();
        String sql, updatedAt, deletedAt, del_Staff_ID;
        if (index <= -1) {
            return;
        }
        conn = DatabaseConnection.getConnection();
        del_Staff_ID = col_StaffID.getCellData(index).toString();
        updatedAt = DatabaseConnection.getCurrDateTime();
        deletedAt = DatabaseConnection.getCurrDateTime();
        sql = "update emp_Table set updated_At = STR_TO_DATE('" + updatedAt + "' ,'%d-%m-%Y %H:%i:%s'), deleted_At = STR_TO_DATE('" + deletedAt + "' ,'%d-%m-%Y %H:%i:%s'), serial_Num = null where staff_ID = '" + del_Staff_ID + "';";
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.execute();
            update_Table();
            search_Table();
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    @FXML
    void edit_Selected() {
        index = table_EmpDB.getSelectionModel().getSelectedIndex();
        if (index <= -1) {
            return;
        }
        String edit_StaffID = col_StaffID.getCellData(index).toString();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("empAdd.fxml"));
            AnchorPane pane = loader.load();
            pane_EmpDB.getChildren().setAll(pane);
            empAddController obj = loader.getController();
            obj.setButtonID(btn_Update.getId());
            obj.setEditStaffID(edit_StaffID);
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
        col_DeletedAt.setCellValueFactory(new PropertyValueFactory<empDetails, String>("deletedAt"));
        dataList = DatabaseConnection.getEmpData();
        table_EmpDB.setItems(dataList);
    }

    @FXML
    void search_Table() {
        update_Table();
        FilteredList<empDetails> filteredData = new FilteredList<>(dataList, b -> true);
        table_EmpDB.setItems(filteredData);
        search_StaffID.textProperty().addListener((obsVal, oldValue, newValue) -> {
            filteredData.setPredicate(person ->
                person.getStaffId().contains(search_StaffID.getText()) &&
                person.getName().contains(search_Name.getText()) &&
                person.getUpdatedAt().contains(search_UpdatedAt.getText()) &&
                person.getJobTitle().contains(search_JobTitle.getText()) &&
                person.getCreatedAt().contains(search_CreatedAt.getText()) &&
                person.getDeletedAt().contains(search_DeletedAt.getText()) &&
                person.getSerNum().contains(search_SerNum.getText()));
        });
        search_Name.textProperty().addListener((obsVal, oldValue, newValue) -> {
            filteredData.setPredicate(person ->
                person.getStaffId().contains(search_StaffID.getText()) &&
                person.getName().contains(search_Name.getText()) &&
                person.getUpdatedAt().contains(search_UpdatedAt.getText()) &&
                person.getJobTitle().contains(search_JobTitle.getText()) &&
                person.getCreatedAt().contains(search_CreatedAt.getText()) &&
                person.getDeletedAt().contains(search_DeletedAt.getText()) &&
                person.getSerNum().contains(search_SerNum.getText()));
        });
        search_SerNum.textProperty().addListener((obsVal, oldValue, newValue) -> {
            filteredData.setPredicate(person ->
                person.getStaffId().contains(search_StaffID.getText()) &&
                person.getName().contains(search_Name.getText()) &&
                person.getUpdatedAt().contains(search_UpdatedAt.getText()) &&
                person.getJobTitle().contains(search_JobTitle.getText()) &&
                person.getCreatedAt().contains(search_CreatedAt.getText()) &&
                person.getDeletedAt().contains(search_DeletedAt.getText()) &&
                person.getSerNum().contains(search_SerNum.getText()));
        });
        search_CreatedAt.textProperty().addListener((obsVal, oldValue, newValue) -> {
            filteredData.setPredicate(person ->
                person.getStaffId().contains(search_StaffID.getText()) &&
                person.getName().contains(search_Name.getText()) &&
                person.getUpdatedAt().contains(search_UpdatedAt.getText()) &&
                person.getJobTitle().contains(search_JobTitle.getText()) &&
                person.getCreatedAt().contains(search_CreatedAt.getText()) &&
                person.getDeletedAt().contains(search_DeletedAt.getText()) &&
                person.getSerNum().contains(search_SerNum.getText()));
        });
        search_JobTitle.textProperty().addListener((obsVal, oldValue, newValue) -> {
            filteredData.setPredicate(person ->
                person.getStaffId().contains(search_StaffID.getText()) &&
                person.getName().contains(search_Name.getText()) &&
                person.getUpdatedAt().contains(search_UpdatedAt.getText()) &&
                person.getJobTitle().contains(search_JobTitle.getText()) &&
                person.getCreatedAt().contains(search_CreatedAt.getText()) &&
                person.getDeletedAt().contains(search_DeletedAt.getText()) &&
                person.getSerNum().contains(search_SerNum.getText()));
        });
        search_UpdatedAt.textProperty().addListener((obsVal, oldValue, newValue) -> {
            filteredData.setPredicate(person ->
                person.getStaffId().contains(search_StaffID.getText()) &&
                person.getName().contains(search_Name.getText()) &&
                person.getUpdatedAt().contains(search_UpdatedAt.getText()) &&
                person.getJobTitle().contains(search_JobTitle.getText()) &&
                person.getCreatedAt().contains(search_CreatedAt.getText()) &&
                person.getDeletedAt().contains(search_DeletedAt.getText()) &&
                person.getSerNum().contains(search_SerNum.getText()));
        });
        search_DeletedAt.textProperty().addListener((obsVal, oldValue, newValue) -> {
            filteredData.setPredicate(person ->
                person.getStaffId().contains(search_StaffID.getText()) &&
                person.getName().contains(search_Name.getText()) &&
                person.getUpdatedAt().contains(search_UpdatedAt.getText()) &&
                person.getJobTitle().contains(search_JobTitle.getText()) &&
                person.getCreatedAt().contains(search_CreatedAt.getText()) &&
                person.getDeletedAt().contains(search_DeletedAt.getText()) &&
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