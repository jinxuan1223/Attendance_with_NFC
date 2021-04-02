package AwNFC;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.sql.*;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import javax.swing.JOptionPane;

public class attTableController implements Initializable {
    private String mode;
    private BufferedWriter fileWriter;

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
            AnchorPane pane = FXMLLoader.load(getClass().getResource("/admin_page.fxml"));
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
    void btn_Export(ActionEvent event) throws IOException {
        if(getMode().equals("Today"))
            if(export("AttendanceToday")==true)
                showExportMsg("Successfully Exported");
            else
                showExportMsg("Opps! Something went wrong");
        else if(getMode().equals("All"))
            if(export("AttendanceFull") == true)
                showExportMsg("Successfully Exported");
            else
                showExportMsg("Opps! Something went wrong");
    }

    @FXML
    void btn_Import(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/import_csv.fxml"));
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

    public boolean export(String table){
        String csvFileName = getFileName(table);
        String sql = "";
        if(table.equals("AttendanceToday"))
            sql = "SELECT emp_table.emp_ID, emp_table.staff_ID, emp_table.name, attendance_table.date, attendance_table.inTime, attendance_table.outTime, attendance_table.isLate, attendance_table.leaving_status FROM emp_table INNER JOIN attendance_table ON emp_table.emp_id=attendance_table.emp_id WHERE attendance_table.date = STR_TO_DATE( '"+ currDate() + "','%d-%m-%Y')" ;
        else if(table.equals("AttendanceFull"))
            sql = "SELECT emp_table.emp_ID, emp_table.staff_ID, emp_table.name, attendance_table.date, attendance_table.inTime, attendance_table.outTime, attendance_table.isLate, attendance_table.leaving_status FROM emp_table INNER JOIN attendance_table ON emp_table.emp_id=attendance_table.emp_id order by att_ID;";

        try{
            Connection connectDB = DatabaseConnection.getConnection();
            Statement statement = connectDB.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            fileWriter = new BufferedWriter(new FileWriter(csvFileName));
            int columnCount = writeHeaderLine(rs);

            while (rs.next()) {
                String line = "";

                for (int i = 2; i <= columnCount; i++) {
                    Object valueObject = rs.getObject(i);
                    String valueString = "";

                    if (valueObject != null) {
                        valueString = (rs.getString(getColumnName(rs,i)));
                        System.out.println(valueString);
                    };

                    line = line.concat(valueString);

                    if (i != columnCount) {
                        line = line.concat(",");
                    }
                }

                fileWriter.newLine();
                fileWriter.write(line);
            }
            statement.close();
            fileWriter.close();

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
            return false;
        }
        return true;

    }

    private String currDate() {
        return new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
    }

    private String getFileName(String baseName) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String dateTimeInfo = dateFormat.format(new Date());
        return baseName.concat(String.format("_%s.csv", dateTimeInfo));
    }

    private int writeHeaderLine(ResultSet result) throws SQLException, IOException {
        // write header line containing column names
        ResultSetMetaData metaData = result.getMetaData();
        int numberOfColumns = metaData.getColumnCount();
        String headerLine = "";

        // exclude the first column which is the ID field
        for (int i = 2; i <= numberOfColumns; i++) {
            String columnName = metaData.getColumnName(i);
            headerLine = headerLine.concat(columnName).concat(",");
        }

        fileWriter.write(headerLine.substring(0, headerLine.length() - 1));

        return numberOfColumns;
    }

    private String getColumnName(ResultSet rs, int i) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        return metaData.getColumnName(i);
    }

    private void showExportMsg(String msg) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/export_notify.fxml"));
        Parent root = loader.load();
        ExportNotifyController exportNotifyController = loader.getController();
        exportNotifyController.setMsgLabel(msg);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(new Scene(root, 700, 100));
        stage.show();

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
            }
        }));
        timeline.play();
    }

}