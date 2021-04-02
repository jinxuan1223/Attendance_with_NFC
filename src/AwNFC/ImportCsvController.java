package AwNFC;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;


public class ImportCsvController {

    FileChooser fil_chooser = new FileChooser();
    FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("CSV file", "*.csv");
    private String tableName= "";

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
    private AnchorPane rootPane;

    public void setTableName(String tableName){
        this.tableName = tableName;
        if(tableName.equals("staff_Table")){
            staffTableGuide.setVisible(true);
            staffDataType.setVisible(true);
        }else if(tableName.equals("attendance_Table")){
            attTableGuide.setVisible(true);
            attDataType.setVisible(true);
        }
    }

    public void onBrowseBtnClicked(ActionEvent event){
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        File file = fil_chooser.showOpenDialog(thisStage);
        if(file!= null){
            pathTextField.setText(file.getAbsolutePath());
        }
    }

    public void onConfirmBtnClicked(ActionEvent event){
        DatabaseConnection connectNow = new DatabaseConnection();
        if(pathTextField.getText().equals(""))
            errorLabel.setVisible(true);
        else if(tableName.equals("staff_Table")){
            if(!connectNow.loadCSVtoEmployee(pathTextField.getText())){
                errorLabel.setVisible(true);
                errorLabel.setText("Invalid File, Please check if the .csv is following the format or the important columns(*) are empty");
            }else{
                returnPage();
            }
        }if(!connectNow.loadCSVtoAttendance(pathTextField.getText())){
            errorLabel.setVisible(true);
            errorLabel.setText("Invalid File, Please check if the .csv is following the format or the important columns(*) are empty");
        }else{
            returnPage();
        }
    }

    public void onCancelBtnClicked(ActionEvent event){
        returnPage();
    }

    public void returnPage(){
        try {
            AnchorPane pane = null;
            if(tableName.equals("staff_Table")) {
                pane = FXMLLoader.load(getClass().getResource("empTable.fxml"));
            }else if(tableName.equals("attendance_Table")){
                pane = FXMLLoader.load(getClass().getResource("attTable.fxml"));
            }
            rootPane.getChildren().setAll(pane);
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }


}
