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

public class attTable implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane pane_AttDB;

    @FXML
    private Button btn_Back;

    @FXML
    private TextField filterField;

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
    private TableColumn<attDetails, Integer> col_ArrivalStatus;

    @FXML
    private TableColumn<attDetails, Integer> col_EmpID;

    ObservableList<attDetails> listM;
    ObservableList<attDetails> dataList;

    int index = -1;

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pstmt = null;

    @FXML
    void btn_Back(ActionEvent event) {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("choice.fxml"));
            pane_AttDB.getChildren().setAll(pane);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    @FXML
    void update_Table() {
        col_AttID.setCellValueFactory(new PropertyValueFactory<attDetails, Integer>("attId"));
        col_inTime.setCellValueFactory(new PropertyValueFactory<attDetails, String>("inTime"));
        col_inDate.setCellValueFactory(new PropertyValueFactory<attDetails, String>("inDate"));
        col_outTime.setCellValueFactory(new PropertyValueFactory<attDetails, String>("outTime"));
        col_outDate.setCellValueFactory(new PropertyValueFactory<attDetails, String>("outDate"));
        col_ArrivalStatus.setCellValueFactory(new PropertyValueFactory<attDetails, Integer>("isLate"));
        col_EmpID.setCellValueFactory(new PropertyValueFactory<attDetails, Integer>("empId"));
        listM = mysqlconnect.getAttData();
        table_AttDB.setItems(listM);
    }

    @FXML
    void search_Table() {          
        col_AttID.setCellValueFactory(new PropertyValueFactory<attDetails, Integer>("attId"));
        col_inTime.setCellValueFactory(new PropertyValueFactory<attDetails, String>("inTime"));
        col_inDate.setCellValueFactory(new PropertyValueFactory<attDetails, String>("inDate"));
        col_outTime.setCellValueFactory(new PropertyValueFactory<attDetails, String>("outTime"));
        col_outDate.setCellValueFactory(new PropertyValueFactory<attDetails, String>("outDate"));
        col_ArrivalStatus.setCellValueFactory(new PropertyValueFactory<attDetails, Integer>("isLate"));
        col_EmpID.setCellValueFactory(new PropertyValueFactory<attDetails, Integer>("empId"));
        
        dataList = mysqlconnect.getAttData();
        table_AttDB.setItems(dataList);
        FilteredList<attDetails> filteredData = new FilteredList<>(dataList, b -> true);  
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(person -> {
                if (newValue == null || newValue.isEmpty()) {
                return true;
                }    
                String lowerCaseFilter = newValue.toLowerCase();
                
                if (person.getInDate().toLowerCase().indexOf(lowerCaseFilter) != -1 ) {
                    return true;
                } 
                else if (person.getInTime().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                }
                else if (person.getOutDate().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                }  
                else if (person.getOutTime().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                }                    
                else {
                    return false;
                }
            });
        });  
        SortedList<attDetails> sortedData = new SortedList<>(filteredData);  
        sortedData.comparatorProperty().bind(table_AttDB.comparatorProperty());  
        table_AttDB.setItems(sortedData);      
    }

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        update_Table();
        search_Table();
    }
}