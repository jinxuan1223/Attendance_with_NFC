package AwNFC;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


public class ImportCsvController {

    private String tableName;

    @FXML
    private Label staffTableGuide;

    @FXML
    private Label attTableGuide;

    @FXML
    private Label staffDataType;

    @FXML
    private Label attDataType;

    @FXML
    private TextField pathTextField;

    @FXML
    private Button browseBtn;

    @FXML
    private Button confirmBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    private Label errorLabel;

    @FXML
    public void initialize(){
        if(tableName.equals("staff_Table")){
            staffTableGuide.setVisible(true);
            staffDataType.setVisible(true);
        }else if(tableName.equals("attendance_Table")){
            attTableGuide.setVisible(true);
            attDataType.setVisible(true);
        }
    }

    public void setTableName(String tableName){
        this.tableName = tableName;
    }

    public void onBrowseBtnClicked(ActionEvent event){

    }

    public void onConfirmBtnClicked(ActionEvent event){

    }

    public void onCancelBtnClicked(ActionEvent event){

    }


}
