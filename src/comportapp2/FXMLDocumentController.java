/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comportapp2;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import jssc.SerialPort;
import jssc.SerialPortList;

/**
 *
 * @author WIN7x64
 */
public class FXMLDocumentController {
    
    @FXML
    private ChoiceBox <String> comPortNum;      
    @FXML
    private ChoiceBox <Integer> comPortBaudRate;
    @FXML
    private Label pulseWidthLabel;
    @FXML
    private Label pulsesNumberLabel;
    @FXML
    private Label comPortLabel;
    @FXML
    private TextArea comPortCommunication;

    //reference to main application
    private ComPortApp2 mainApp;
    
    private String strComPortNum;
    private Integer iComPortBaudRate;
    
    
    @FXML
    private void handleSendCommand(ActionEvent event) {
        strComPortNum = comPortNum.getValue();
        iComPortBaudRate = comPortBaudRate.getValue();
        System.out.println("chooseComPortName " + strComPortNum);
        System.out.println("chooseBaudRate " + iComPortBaudRate);
        System.out.println("debug");
        
      
        if((null == strComPortNum)||(iComPortBaudRate == null)){
            //port or baud rate not selected.
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            if(null == strComPortNum){
                alert.setHeaderText("Не выбран порт");
                alert.setContentText("Выберите порт");
            }
            if(iComPortBaudRate == null){
                alert.setHeaderText("Не установлена скорость");
                alert.setContentText("Выберите скорость");
            }
            alert.showAndWait();
        }
        else{
            comPortCommunication.appendText("out: You clicked me!\n");
        }
        
    }
   
    /**
     * class-controller initialization 
     * method called after fxml-file is loaded
     */
    @FXML
    private void initialize() {
        
        comPortNum.getItems().addAll(SerialPortList.getPortNames());
        comPortBaudRate.getItems().addAll(SerialPort.BAUDRATE_9600,
                                          SerialPort.BAUDRATE_14400,
                                          SerialPort.BAUDRATE_19200,
                                          SerialPort.BAUDRATE_38400,
                                          SerialPort.BAUDRATE_57600,
                                          SerialPort.BAUDRATE_115200);
              
        
        
    }   
    
    
    public void setMainApp(ComPortApp2 mainApp){
        this.mainApp = mainApp;
        // adding data from observable list to the table
        //personTable.setItems(mainApp.getPersonData());
    }
    
}
