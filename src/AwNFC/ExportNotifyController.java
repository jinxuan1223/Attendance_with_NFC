package AwNFC;

import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class ExportNotifyController {
    @FXML
    private Label msgLabel;

    public void setMsgLabel(String msg){
        msgLabel.setText(msg);
    }
}
