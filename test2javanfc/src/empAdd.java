import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javax.swing.JOptionPane;

public class empAdd implements Initializable {
    String mode;
    String edit_EmpID;
    String edit_EmpName;
    String edit_EmpSerNum;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane pane_AddEmp;

    @FXML
    private Button btn_Back;

    @FXML
    private TextField txt_Name;

    @FXML
    private TextField txt_SerNum;

    @FXML
    private Button btn_Back1;

    ObservableList<empDetails> listM;

    int index = -1;

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pstmt = null;

    @FXML
    void btn_Back(ActionEvent event) {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("empTable.fxml"));
            pane_AddEmp.getChildren().setAll(pane);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    @FXML
    void btn_Submit(ActionEvent event) {
        conn = mysqlconnect.ConnectDb();
        String sql;
        String empName = txt_Name.getText();
        String empSerNum = txt_SerNum.getText();
        String buttonID = mode;
        System.out.println(buttonID);
        if(buttonID.equals("btn_Add")) {
            sql = "insert into employee (emp_name, nfc_num) values (?, ?)";
            try {
                if (empName.equals("") || empSerNum.equals("")) {
                    JOptionPane.showMessageDialog(null, "Name or Serial Number is empty.");
                }
                else {
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, empName);
                    pstmt.setString(2, empSerNum);
                    pstmt.execute();
                    AnchorPane pane = FXMLLoader.load(getClass().getResource("empTable.fxml"));
                    pane_AddEmp.getChildren().setAll(pane);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
        else if(buttonID.equals("btn_Update")) {
            System.out.println(edit_EmpID);
            System.out.println(empName);
            System.out.println(empSerNum);
            sql = "update employee set emp_id = '" + edit_EmpID + "', emp_name = '" + empName + "', nfc_num = '" + empSerNum + "' where emp_ID = '" + edit_EmpID + "'";
            try {
                if (empName.equals("") || empSerNum.equals("")) {
                    JOptionPane.showMessageDialog(null, "Name or Serial Number is empty.");
                }
                else {
                    pstmt = conn.prepareStatement(sql);
                    pstmt.execute();
                    AnchorPane pane = FXMLLoader.load(getClass().getResource("empTable.fxml"));
                    pane_AddEmp.getChildren().setAll(pane);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    @FXML
    void setText() {
        String buttonID = mode;
        if(buttonID.equals("btn_Update")) {
            txt_Name.setText(edit_EmpName);
            txt_SerNum.setText(edit_EmpSerNum);
        }
    }

    @FXML
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void setButtonID(String mode) {
        this.mode = mode;
    }

    public void setEmpID(String edit_EmpID) {
        this.edit_EmpID = edit_EmpID;
    }

    public void setEmpName(String edit_EmpName) {
        this.edit_EmpName = edit_EmpName;
    }

    public void setEmpSerNum(String edit_EmpSerNum) {
        this.edit_EmpSerNum = edit_EmpSerNum;
    }

}

