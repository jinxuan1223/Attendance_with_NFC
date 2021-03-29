package AwNFC;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import javafx.event.ActionEvent;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;

import java.io.IOException;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HomeController {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Button clockinBtn;

    @FXML
    private Button clockoutBtn;

    @FXML
    private Button editBtn;

    @FXML
    private AnchorPane timePane;

    @FXML
    private Label dateLabel;

    private String UID;

    public void setUID(String UID) {
        this.UID = UID;
    }

    public void editBtnOnAction(ActionEvent event) throws IOException {
        switchScene("Edit");
    }

    public void clockinBtnOnAction(ActionEvent event) throws IOException {
       switchScene("Clock In");
    }

    public void clockoutBtnOnAction(ActionEvent event) throws IOException {
        switchScene("Clock Out");
    }

    public void setDateLabel(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEEE, dd MMMM yyyy");
        dateLabel.setText(dateFormat.format(date));
    }

    public void setTimePane(){
        Clock clock = new Clock(Color.BLACK, Color.LIGHTGRAY, timePane);
        timePane.setLayoutX(140);
        timePane.setLayoutY(186);
        timePane.getTransforms().add(new Scale(1.5f, 1.5f, 0, 0));
        System.out.println("SET TIME PANE");
        //timePane.getChildren().setAll();
    }

    private void startRead(FXMLLoader loader){
        try {
            Timer timer = new Timer(); //At this line a new Thread will be created
            timer.scheduleAtFixedRate(new NFCRead(loader, "NFCTapController"), 0, 500);

        } catch (Exception ex) {
            Logger.getLogger(NFCRead.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void switchScene(String mode) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("nfc_tap.fxml"));
        AnchorPane pane = loader.load();

        NFCTapController nfcTapController = loader.getController();
        nfcTapController.setMode(mode);
        rootPane.getChildren().setAll(pane);
        startRead(loader);
    }

}
