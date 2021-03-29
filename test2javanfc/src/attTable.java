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

public class attTable implements Initializable {

    @FXML
    private AnchorPane pane_AttDB;

    @FXML
    private Button btn_Back;

    @FXML
    private TextField search_AttID;

    @FXML
    private TextField search_InTime;

    @FXML
    private TextField search_InDate;

    @FXML
    private TextField search_OutTime;

    @FXML
    private TextField search_OutDate;

    @FXML
    private TextField search_ArrStatus;

    @FXML
    private TextField search_EmpID;

    @FXML
    private TableView<attDetails> table_AttDB;

    @FXML
    private TableColumn<attDetails, Integer> col_AttID;

    @FXML
    private TableColumn<attDetails, String> col_inTime;

    @FXML
    private TableColumn<attDetails, String> col_inDate;

    @FXML
    private TableColumn<attDetails, String> col_outTime;

    @FXML
    private TableColumn<attDetails, String> col_outDate;

    @FXML
    private TableColumn<attDetails, String> col_ArrivalStatus;

    @FXML
    private TableColumn<attDetails, Integer> col_EmpID;

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
        col_AttID.setCellValueFactory(new PropertyValueFactory<attDetails, Integer>("attId"));
        col_inTime.setCellValueFactory(new PropertyValueFactory<attDetails, String>("inTime"));
        col_inDate.setCellValueFactory(new PropertyValueFactory<attDetails, String>("inDate"));
        col_outTime.setCellValueFactory(new PropertyValueFactory<attDetails, String>("outTime"));
        col_outDate.setCellValueFactory(new PropertyValueFactory<attDetails, String>("outDate"));
        col_ArrivalStatus.setCellValueFactory(new PropertyValueFactory<attDetails, String>("isLate"));
        col_EmpID.setCellValueFactory(new PropertyValueFactory<attDetails, Integer>("empId"));
        dataList = mysqlconnect.getAttData();
        table_AttDB.setItems(dataList);
        FilteredList<attDetails> filteredData = new FilteredList<>(dataList, b -> true);  
        table_AttDB.setItems(filteredData);  
        search_AttID.textProperty().addListener((obsVal, oldValue, newValue) -> {
            filteredData.setPredicate(person -> 
                String.valueOf(person.getAttId()).contains(search_AttID.getText()) &&
                String.valueOf(person.getEmpId()).contains(search_EmpID.getText()) &&
                person.getIsLate().contains(search_ArrStatus.getText()) &&
                person.getInTime().contains(search_InTime.getText()) &&
                person.getInDate().contains(search_InDate.getText()) &&
                person.getOutTime().contains(search_OutTime.getText()) &&
                person.getOutDate().contains(search_OutDate.getText()) 
               );
        });
        search_ArrStatus.textProperty().addListener((obsVal, oldValue, newValue) -> {
            filteredData.setPredicate(person -> 
                person.getIsLate().contains(search_ArrStatus.getText()) &&
                String.valueOf(person.getAttId()).contains(search_AttID.getText()) &&
                String.valueOf(person.getEmpId()).contains(search_EmpID.getText()) &&
                person.getInTime().contains(search_InTime.getText()) &&
                person.getInDate().contains(search_InDate.getText()) &&
                person.getOutTime().contains(search_OutTime.getText()) &&
                person.getOutDate().contains(search_OutDate.getText()) 
               );
        });
        search_EmpID.textProperty().addListener((obsVal, oldValue, newValue) -> {
            filteredData.setPredicate(person -> 
                String.valueOf(person.getEmpId()).contains(search_EmpID.getText()) &&
                String.valueOf(person.getAttId()).contains(search_AttID.getText()) &&
                person.getIsLate().contains(search_ArrStatus.getText()) && 
                person.getInTime().contains(search_InTime.getText()) &&
                person.getInDate().contains(search_InDate.getText()) &&
                person.getOutTime().contains(search_OutTime.getText()) &&
                person.getOutDate().contains(search_OutDate.getText()) 
               );
        });
        search_InTime.textProperty().addListener((obsVal, oldValue, newValue) -> {
            filteredData.setPredicate(person -> 
                person.getInTime().contains(search_InTime.getText()) &&
                String.valueOf(person.getAttId()).contains(search_AttID.getText()) &&
                person.getIsLate().contains(search_ArrStatus.getText()) &&
                String.valueOf(person.getEmpId()).contains(search_EmpID.getText()) &&                
                person.getInDate().contains(search_InDate.getText()) &&
                person.getOutTime().contains(search_OutTime.getText()) &&
                person.getOutDate().contains(search_OutDate.getText())  
               );
        });
        search_InDate.textProperty().addListener((obsVal, oldValue, newValue) -> {
            filteredData.setPredicate(person -> 
                person.getInDate().contains(search_InDate.getText()) &&
                String.valueOf(person.getAttId()).contains(search_AttID.getText()) &&
                person.getIsLate().contains(search_ArrStatus.getText()) &&
                String.valueOf(person.getEmpId()).contains(search_EmpID.getText()) &&
                person.getInTime().contains(search_InTime.getText()) &&                
                person.getOutTime().contains(search_OutTime.getText()) &&
                person.getOutDate().contains(search_OutDate.getText())  
               );
        });
        search_OutTime.textProperty().addListener((obsVal, oldValue, newValue) -> {
            filteredData.setPredicate(person -> 
                person.getOutTime().contains(search_OutTime.getText()) &&
                String.valueOf(person.getAttId()).contains(search_AttID.getText()) &&
                person.getIsLate().contains(search_ArrStatus.getText()) &&
                String.valueOf(person.getEmpId()).contains(search_EmpID.getText()) &&
                person.getInTime().contains(search_InTime.getText()) &&
                person.getInDate().contains(search_InDate.getText()) &&            
                person.getOutDate().contains(search_OutDate.getText()) 
               );
        }); 
        search_OutDate.textProperty().addListener((obsVal, oldValue, newValue) -> {
            filteredData.setPredicate(person -> 
                person.getOutDate().contains(search_OutDate.getText()) &&
                String.valueOf(person.getAttId()).contains(search_AttID.getText()) &&
                person.getIsLate().contains(search_ArrStatus.getText()) &&
                String.valueOf(person.getEmpId()).contains(search_EmpID.getText()) &&
                person.getInTime().contains(search_InTime.getText()) &&
                person.getInDate().contains(search_InDate.getText()) &&
                person.getOutTime().contains(search_OutTime.getText())                
               );
        }); 
    }

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        search_Table();
    }
}