package AwNFC;

import java.net.URL;
import java.util.ResourceBundle;
import java.sql.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javax.swing.JOptionPane;

public class currAttTableController implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane pane_CurrAttDB;

    @FXML
    private Button btn_Back;

    @FXML
    private Button btn_Search;

    @FXML
    private TableView<currAttDetails> table_CurrAttDB;

    @FXML
    private TableColumn<currAttDetails, Integer> col_AttID;

    @FXML
    private TableColumn<currAttDetails, String> col_inTime;

    @FXML
    private TableColumn<currAttDetails, String> col_inDate;

    @FXML
    private TableColumn<currAttDetails, String> col_outTime;

    @FXML
    private TableColumn<currAttDetails, String> col_outDate;

    @FXML
    private TableColumn<currAttDetails, String> col_ArrivalStatus;

    @FXML
    private TableColumn<currAttDetails, Integer> col_EmpID;

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
            JOptionPane.showMessageDialog(null, e);
        }
    }

    @FXML
    void btn_Search(ActionEvent event) {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("attTable.fxml"));
            pane_CurrAttDB.getChildren().setAll(pane);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    @FXML
    void update_Table() {          
        col_AttID.setCellValueFactory(new PropertyValueFactory<currAttDetails, Integer>("attId"));
        col_inTime.setCellValueFactory(new PropertyValueFactory<currAttDetails, String>("inTime"));
        col_inDate.setCellValueFactory(new PropertyValueFactory<currAttDetails, String>("inDate"));
        col_outTime.setCellValueFactory(new PropertyValueFactory<currAttDetails, String>("outTime"));
        col_outDate.setCellValueFactory(new PropertyValueFactory<currAttDetails, String>("outDate"));
        col_ArrivalStatus.setCellValueFactory(new PropertyValueFactory<currAttDetails, String>("isLate"));
        col_EmpID.setCellValueFactory(new PropertyValueFactory<currAttDetails, Integer>("empId"));
        dataList = DatabaseConnection.getCurrAttData();
        table_CurrAttDB.setItems(dataList);
    }

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        update_Table();
    }
}