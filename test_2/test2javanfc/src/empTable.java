import java.net.URL;
import java.util.ResourceBundle;
import java.sql.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    private TextField txt_EmpName;

    @FXML
    private Button btn_Delete;

    @FXML
    private Label txt_EmpSerNum;

    @FXML
    private Label txt_EmpID;

    @FXML
    private TableView<empDetails> table_EmpDB;

    @FXML
    private TableColumn<empDetails, Integer> col_EmpID;

    @FXML
    private TableColumn<empDetails, String> col_EmpName;

    @FXML
    private TableColumn<empDetails, String> col_EmpSerNum;

    ObservableList<empDetails> listM;

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
    public void initialize(URL url, ResourceBundle rb) {
        update_Table();
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
    void delete_Selected(ActionEvent event) {
        index = table_EmpDB.getSelectionModel().getSelectedIndex();
        if (index <= -1) {
            return;
        }
        conn = mysqlconnect.ConnectDb();
        String sql = "delete from emp_Details where emp_ID = ?";
        String del_ID = col_EmpID.getCellData(index).toString();
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, del_ID);
            pstmt.execute();
            update_Table();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
}