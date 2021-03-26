import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javax.swing.JOptionPane;

public class choice implements Initializable {


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane pane_Choice;

    @FXML
    private Button btn_Att;

    @FXML
    private Button btn_Emp;

    @FXML
    void openAttDB(ActionEvent event) {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("attTable.fxml"));
            pane_Choice.getChildren().setAll(pane);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    @FXML
    void openEmpDB(ActionEvent event) {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("empTable.fxml"));
            pane_Choice.getChildren().setAll(pane);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    @FXML
    public void initialize(URL url, ResourceBundle rb) {

    }
}
