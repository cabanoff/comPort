/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comportapp2;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import jssc.*;

/**
 *
 * @author WIN7x64
 */
public class ComPortApp2 extends Application {
    
    private static SerialPort serialPort;
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
        System.out.println("Stage is open");
        new ComPortApp2().go();   
//        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
//          @Override
//          public void handle(WindowEvent we) {
//              System.out.println("Stage is closing");
//          }
//      });   
        
    }
      
    
    @Override
    public void stop() throws Exception{
        
        if((serialPort != null)&&(serialPort.isOpened())){
            try{
                serialPort.closePort();
                System.out.println("port closed");
            }catch(SerialPortException spe){
                System.out.println("unable to open " + spe.getLocalizedMessage() );          
            }
            
        }
        System.out.println("stage is closed");            
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
            
    }
     
    public void go(){
               
        serialPort = new SerialPort("COM4");
        try{          
            serialPort.openPort(); 
            serialPort.setParams(SerialPort.BAUDRATE_9600,
                                 SerialPort.DATABITS_8,
                                 SerialPort.STOPBITS_1,
                                 SerialPort.PARITY_NONE);
            //Включаем аппаратное управление потоком
            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN | 
                                          SerialPort.FLOWCONTROL_RTSCTS_OUT);
            //Устанавливаем ивент лисенер и маску
            serialPort.addEventListener(new PortReader(), SerialPort.MASK_RXCHAR);
            //Отправляем запрос устройству
            serialPort.writeString("Get data");
            System.out.println("port opened");
        }
        catch(SerialPortException ex){
            System.out.println("error 1");
            System.out.println(ex);
        }

    }
    
    private static class PortReader implements SerialPortEventListener {

        @Override
        public void serialEvent(SerialPortEvent event) {
            if(event.isRXCHAR() && event.getEventValue() > 0){
                try {
                    //Получаем ответ от устройства, обрабатываем данные и т.д.
                    String data = serialPort.readString(event.getEventValue());
                    //И снова отправляем запрос
                    serialPort.writeString("Get data");
                }
                catch (SerialPortException ex) {
                    System.out.println(ex);
                }
            }
        }
    }
    
}
