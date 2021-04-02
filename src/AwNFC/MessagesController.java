package AwNFC;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Calendar;

public class MessagesController {

    @FXML
    private Label wlcBackLabel;
    @FXML
    private Label byeLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label invalidCardLabel;
    @FXML
    private Label clockedInLabel;
    @FXML
    private Label clockOutLabel;
    @FXML
    private Label clockedOutLabel;
    @FXML
    private AnchorPane rootPaneMsg;
    @FXML
    private Label notAuthorisedLabel;



    public void assignWlcBackLabel(String msg){
        wlcBackLabel.setText(msg);
    }

    public void assignByeLabel(String msg){
        byeLabel.setText(msg);
    }

    public void assignNameLabel(String msg){
        nameLabel.setText(msg);
    }

    public void assignInvalidCardLabel(String msg){
        invalidCardLabel.setText(msg);
    }

    public void assignClockedInLabel(String msg){
        clockedInLabel.setText(msg);
    }

    public void assignClockOutLabel(String msg){
        clockOutLabel.setText(msg);
    }

    public void assignClockedOutLabel(String msg){
        clockedOutLabel.setText(msg);
    }

    public void assignNotAuthorisedLabel(String msg){notAuthorisedLabel.setText(msg);}

    public void backHomeScene() throws IOException {


        FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
        AnchorPane pane = loader.load();
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                HomeController homeController = loader.getController();
                homeController.setDateLabel(Calendar.getInstance().getTime());
                homeController.setTimePane();
                rootPaneMsg.getChildren().setAll(pane);
            }
        }));
        timeline.play();


    }


}
