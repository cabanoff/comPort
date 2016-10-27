/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comportapp2;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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
    @FXML
    private Slider pulseWidthSlider;
    @FXML
    private Slider pulsesNumSlider;
    
    private int pulseWidth = 5;
    private int pulsesNum = 5;

    //reference to main application
    private ComPortApp2 mainApp;
    
    private String strComPortNum;
    private Integer iComPortBaudRate;
    
    /**
     * Checks if comPortList isn't changed
     * if not checks if selected port still exists
     * if not set selected port to null.
     * @param event 
     */
    @FXML
    private void handleComPort(MouseEvent event){
       // assert false :"comboBox " + Arrays.toString(SerialPortList.getPortNames());
        System.out.println("comboBox " + Arrays.toString(SerialPortList.getPortNames()));
        String[] serialPortList = SerialPortList.getPortNames();
        ObservableList<String> newComPortList = FXCollections.observableArrayList(
                new ArrayList<String>(Arrays.asList(serialPortList)));
        if(!newComPortList.equals(comPortNum.getItems())){
            String comPortSelected = comPortNum.getValue();
            if(!comPortNum.getItems().contains(comPortSelected)){
                comPortNum.getItems().clear();
                comPortNum.getItems().addAll(serialPortList);
                strComPortNum = "";
            }
            else{
                comPortNum.getItems().clear();
                comPortNum.getItems().addAll(serialPortList);
                comPortNum.setValue(comPortSelected);
            }
        }
    }
    
    
    @FXML
    private void handleSendCommand(ActionEvent event) {
        strComPortNum = comPortNum.getValue();
        iComPortBaudRate = comPortBaudRate.getValue();
        System.out.println("chooseComPortName = " + strComPortNum);
        System.out.println("chooseBaudRate = " + iComPortBaudRate);
        System.out.println("Button "+ Arrays.toString(SerialPortList.getPortNames()));
        
      
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
            comPortLabel.setText(strComPortNum + ":" + iComPortBaudRate.toString());
            comPortCommunication.appendText("out: Pulse Width = " + pulseWidth + ", Pulses Number = " + pulsesNum + "\n");
        }
        
    }
    @FXML
    private void handleClearCommand(ActionEvent event){
        comPortCommunication.clear();
    }
   
    /**
     * class-controller initialization 
     * method called after fxml-file is loaded
     */
    @FXML
    private void initialize() {
        String[] serialPortList = SerialPortList.getPortNames();
        comPortNum.getItems().addAll(serialPortList);
        if(serialPortList.length != 0){
            comPortNum.setValue(serialPortList[0]);
            strComPortNum = serialPortList[0];
        }
        
        comPortBaudRate.getItems().addAll(SerialPort.BAUDRATE_9600,
                                          SerialPort.BAUDRATE_14400,
                                          SerialPort.BAUDRATE_19200,
                                          SerialPort.BAUDRATE_38400,
                                          SerialPort.BAUDRATE_57600,
                                          SerialPort.BAUDRATE_115200);
        comPortBaudRate.setValue(SerialPort.BAUDRATE_9600);
        pulseWidthLabel.setText(Integer.toString(pulseWidth));
        //pulseWidthLabel.fontProperty(Font.font(strComPortNum, FontWeight.LIGHT, 0));
        //pulseWidthLabel.fontProperty(Font.font("Papirus",FontWeight.BOLD,18));
        pulsesNumberLabel.setText(Integer.toString(pulsesNum));
        
        pulseWidthSlider.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                pulseWidth = ((Double)pulseWidthSlider.getValue()).intValue(); 
                pulseWidthLabel.setText(Integer.toString(pulseWidth));
            }
        });
         pulsesNumSlider.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                //pulsesNum = ((Double)pulseWidthSlider.getValue()).intValue();
                pulsesNum = ((Double)newValue).intValue();
                pulsesNumberLabel.setText(Integer.toString(pulsesNum));
            }
        });
    }   
    
    
    public void setMainApp(ComPortApp2 mainApp){
        this.mainApp = mainApp;
        // adding data from observable list to the table
        //personTable.setItems(mainApp.getPersonData());
    }

    
}
