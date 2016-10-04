/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comportapp2;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import jssc.SerialPortList;

/**
 *
 * @author WIN7x64
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private ChoiceBox <String> comPortNum;
        
    @FXML
    private ChoiceBox <String> comPortBaudRate;

    
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        //label.setText("Hello World!");
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //comPortNum.getItems().addAll(SerialPortList.getPortNames());
        
    }    
    
}
