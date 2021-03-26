package AwNFC;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import javafx.event.ActionEvent;

import java.io.IOException;
import java.sql.Connection;
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

    private String UID;

    public void setUID(String UID) {
        this.UID = UID;
    }

    public void editBtnOnAction(ActionEvent event) throws IOException {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("nfc_tap.fxml"));
            AnchorPane pane = loader.load();

            NFCTapController nfcTapController = loader.getController();
            nfcTapController.setMode("Edit");
            rootPane.getChildren().setAll(pane);
            startRead(loader,nfcTapController);
    }

    public void clockinBtnOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("nfc_tap.fxml"));
        AnchorPane pane = loader.load();

        NFCTapController nfcTapController = loader.getController();
        nfcTapController.setMode("Clock In");
        rootPane.getChildren().setAll(pane);
        startRead(loader,nfcTapController);
        System.out.println("HERE");

    }

    public void clockoutBtnOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("nfc_tap.fxml"));
        AnchorPane pane = loader.load();

        NFCTapController nfcTapController = loader.getController();
        nfcTapController.setMode("Clock Out");
        rootPane.getChildren().setAll(pane);
        startRead(loader,nfcTapController);
        System.out.println("HELLO");
    }

    private void startRead(FXMLLoader loader, NFCTapController nfcTapController){
        try {
            Timer timer = new Timer(); //At this line a new Thread will be created
            timer.scheduleAtFixedRate(new NFCRead(loader), 0, 500);

        } catch (Exception ex) {
            Logger.getLogger(NFCRead.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


}
