package AwNFC;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.sql.*;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.input.MouseEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;


public class empTableController implements Initializable {
    private String mode;
    private BufferedWriter fileWriter;

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/empAdd.fxml"));
            AnchorPane pane = loader.load();
            pane_EmpDB.getChildren().setAll(pane);
            empAddController obj = loader.getController();
            obj.setMode("Add");
            startRead(loader);
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    @FXML
    void edit_Selected() {
        index = table_EmpDB.getSelectionModel().getSelectedIndex();
        String edit_StaffID = col_StaffID.getCellData(index).toString();
        String edit_DeletedAt = col_DeletedAt.getCellData(index).toString();
        String edit_EmpName = col_EmpName.getCellData(index).toString();

        if (index <= -1) {
            return;
        }

        if(edit_DeletedAt.equals("-")) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/empAdd.fxml"));
                AnchorPane pane = loader.load();
                pane_EmpDB.getChildren().setAll(pane);
                empAddController obj = loader.getController();
                obj.setMode("Update");
                obj.setEditStaffID(edit_StaffID);
                startRead(loader);
            } catch (Exception e) {
                e.printStackTrace();
                e.getCause();
            }
        }
        else {
            try {
                showExportMsg(edit_EmpName + " has been deleted.");
            }catch (Exception e) {
                e.printStackTrace();
                e.getCause();
            }
        }
    }

    @FXML
    void btn_Back(ActionEvent event) {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("/admin_page.fxml"));
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
            setMode("Current");
            update_Table();
            search_Table();
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    @FXML
    void btn_Export(ActionEvent event) throws IOException {
        if(export("Staff") == true)
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
            setMode("All");
            update_Table();
            search_Table();
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    @FXML
    void disable_Selected(ActionEvent event) {
        index = table_EmpDB.getSelectionModel().getSelectedIndex();
        String disable_DeletedAt = col_DeletedAt.getCellData(index).toString();
        String disable_EmpName = col_EmpName.getCellData(index).toString();
        String del_Staff_ID = col_StaffID.getCellData(index).toString();
        String sql, updatedAt, deletedAt;
        updatedAt = DatabaseConnection.getCurrDateTime();
        deletedAt = DatabaseConnection.getCurrDateTime();

        if (index <= -1) {
            return;
        }
        conn = DatabaseConnection.getConnection();

        sql = "update emp_Table set updated_At = STR_TO_DATE('" + updatedAt + "' ,'%d-%m-%Y %H:%i:%s'), staff_ID = '_" + del_Staff_ID + "', deleted_At = STR_TO_DATE('" + deletedAt + "' ,'%d-%m-%Y %H:%i:%s'), serial_Num = null where staff_ID = '" + del_Staff_ID + "';";
        if(disable_DeletedAt.equals("-")) {
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
        else {
            try {
                showExportMsg(disable_EmpName + " has been deleted.");
            }catch (Exception e) {
                e.printStackTrace();
                e.getCause();
            }
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
        dataList = DatabaseConnection.getEmpData(getMode());
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
        setMode("Current");
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

    public boolean export(String table){
        String csvFileName = getFileName(table);
        System.out.println(csvFileName);
        try{
            Connection connectDB = DatabaseConnection.getConnection();
            String sql = "Select * from emp_table";

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

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getMode() {
        return mode;
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