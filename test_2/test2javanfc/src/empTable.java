import java.net.URL;
import java.util.ResourceBundle;
import java.sql.*;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import javax.swing.JOptionPane;

public class empTable implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

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
    private TextField filterField;

    @FXML
    private TableView<empDetails> table_EmpDB;

    @FXML
    private TableColumn<empDetails, Integer> col_EmpID;

    @FXML
    private TableColumn<empDetails, String> col_EmpName;

    @FXML
    private TableColumn<empDetails, String> col_EmpSerNum;

    ObservableList<empDetails> listM;
    ObservableList<empDetails> dataList;

    int index = -1;

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pstmt = null;

    @FXML
    void update_Table() {
        col_EmpID.setCellValueFactory(new PropertyValueFactory<empDetails, Integer>("id"));
        col_EmpName.setCellValueFactory(new PropertyValueFactory<empDetails, String>("name"));
        col_EmpSerNum.setCellValueFactory(new PropertyValueFactory<empDetails, String>("serNum"));
        listM = mysqlconnect.getEmpData();
        table_EmpDB.setItems(listM);
    }

    @FXML
    void add_Emp(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("empAdd.fxml"));
            AnchorPane pane = loader.load();
            pane_EmpDB.getChildren().setAll(pane);
            empAdd obj = loader.getController();
            obj.setButtonID(btn_Add.getId());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
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
    void edit_Selected() {
        index = table_EmpDB.getSelectionModel().getSelectedIndex();
        if (index <= -1) {
            return;
        }
        String edit_EmpID = col_EmpID.getCellData(index).toString();
        String edit_EmpName = col_EmpName.getCellData(index).toString();
        String edit_EmpSerNum = col_EmpSerNum.getCellData(index).toString();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("empAdd.fxml"));
            AnchorPane pane = loader.load();
            pane_EmpDB.getChildren().setAll(pane);
            empAdd obj = loader.getController();
            obj.setButtonID(btn_Update.getId());
            obj.setEmpID(edit_EmpID);
            obj.setEmpName(edit_EmpName);
            obj.setEmpSerNum(edit_EmpSerNum);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    @FXML
    void btn_Back(ActionEvent event) {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("admin_page.fxml"));
            pane_EmpDB.getChildren().setAll(pane);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    @FXML
    void delete_Selected(ActionEvent event) {
        index = table_EmpDB.getSelectionModel().getSelectedIndex();
        if (index <= -1) {
            return;
        }
        conn = mysqlconnect.ConnectDb();
        //String sql1 = "delete from attendance where emp_id = ?";
        String sql2 = "delete from employee where emp_id = ?";
        String del_ID = col_EmpID.getCellData(index).toString();
        try {
            //pstmt = conn.prepareStatement(sql1);
            //pstmt.setString(1, del_ID);
            //pstmt.execute();
            pstmt = conn.prepareStatement(sql2);
            pstmt.setString(1, del_ID);
            pstmt.execute();
            update_Table();
            search_Table();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    @FXML
    void search_Table() {          
        col_EmpID.setCellValueFactory(new PropertyValueFactory<empDetails, Integer>("id"));
        col_EmpName.setCellValueFactory(new PropertyValueFactory<empDetails, String>("name"));
        col_EmpSerNum.setCellValueFactory(new PropertyValueFactory<empDetails, String>("serNum"));
        
        dataList = mysqlconnect.getEmpData();
        table_EmpDB.setItems(dataList);
        FilteredList<empDetails> filteredData = new FilteredList<>(dataList, b -> true);  
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(person -> {
                if (newValue == null || newValue.isEmpty()) {
                return true;
                }    
                String lowerCaseFilter = newValue.toLowerCase();
                
                if (person.getName().toLowerCase().indexOf(lowerCaseFilter) != -1 ) {
                    return true;
                } 
                else if (person.getSerNum().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                }                       
                else {
                    return false;
                }
            });
        });  
        SortedList<empDetails> sortedData = new SortedList<>(filteredData);  
        sortedData.comparatorProperty().bind(table_EmpDB.comparatorProperty());  
        table_EmpDB.setItems(sortedData);      
    }

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        update_Table();
        search_Table();
    }
}