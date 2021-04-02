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
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
//import javax.swing.JOptionPane;

public class AdminPageController implements Initializable {
    String buttonID;

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
    private ImageView attendanceIV;

    @FXML
    void openAttDB(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("attTable.fxml"));
            AnchorPane pane = loader.load();
            pane_Choice.getChildren().setAll(pane);
            attTableController obj = loader.getController();
            obj.setMode("Today");
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    @FXML
    void openEmpDB(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("empTable.fxml"));
            AnchorPane pane = loader.load();
            pane_Choice.getChildren().setAll(pane);
            empTableController obj = loader.getController();
            obj.setButtonID(btn_Emp.getId());
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
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
        btn_Att.setContentDisplay(ContentDisplay.TOP);
        btn_Emp.setContentDisplay(ContentDisplay.TOP);
    }

    public void setButtonID(String buttonID) {
        this.buttonID = buttonID;
    }

    public String getButtonID() {
        return buttonID;
    }
}
