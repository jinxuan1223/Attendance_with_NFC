package AwNFC;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
//import javax.swing.JOptionPane;

public class AdminPageController implements Initializable {


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
    private Button homeBtn;

    @FXML
    void openAttDB(ActionEvent event) {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("currAttTable.fxml"));
            pane_Choice.getChildren().setAll(pane);
        } catch (Exception e) {
            //JOptionPane.showMessageDialog(null, e);
        }
    }

    @FXML
    void openEmpDB(ActionEvent event) {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("empTable.fxml"));
            pane_Choice.getChildren().setAll(pane);
        } catch (Exception e) {
            //JOptionPane.showMessageDialog(null, e);
        }
    }

    @FXML
    void homeBtnOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
        AnchorPane pane = loader.load();
        HomeController homeController = loader.getController();
        homeController.setDateLabel(Calendar.getInstance().getTime());
        homeController.setTimePane();
        pane_Choice.getChildren().setAll(pane);
    }

    @FXML
    public void initialize(URL url, ResourceBundle rb) {

    }
}
