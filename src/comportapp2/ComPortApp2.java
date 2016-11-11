/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comportapp2;

import java.io.IOException;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import jssc.*;

/**
 *
 * @author WIN7x64
 */
public class ComPortApp2 extends Application {
    
    private static SerialPort serialPort;
    private Stage primaryStage;
    private AnchorPane rootLayout;
    
     @Override
    public void start(Stage primaryStage) {
       this.primaryStage = primaryStage;
       this.primaryStage.setTitle("Comport Loader");
       
       initRootLayout();
       
            
    }
    
    /**
     * Initialize root layout
     */
    public void initRootLayout(){
        try{
            //load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ComPortApp2.class.getResource("FXMLDocument.fxml"));
            rootLayout = (AnchorPane)loader.load();
            
            //Show scene, containing root layout 
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
            
            //give controller access to the main application
            FXMLDocumentController controller = loader.getController();
            controller.setMainApp(this);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    /**
     * @throws java.lang.Exception
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
        System.out.println("Stage is open");
        new ComPortApp2().go();   
        
    }
     */
    
    @Override
    public void stop() throws Exception{
        
        if((serialPort != null)&&(serialPort.isOpened())){
            try{
                serialPort.removeEventListener();
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
    
    public boolean connect(String portName, int baudRate){
        serialPort = new SerialPort(portName);
        try{          
            serialPort.openPort(); 
            serialPort.setParams(baudRate,
                                 SerialPort.DATABITS_8,
                                 SerialPort.STOPBITS_1,
                                 SerialPort.PARITY_NONE);
            //Включаем аппаратное управление потоком
            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN | 
                                          SerialPort.FLOWCONTROL_RTSCTS_OUT);
            //Устанавливаем ивент лисенер и маску
            serialPort.addEventListener(new PortReader(), SerialPort.MASK_RXCHAR);
            //Отправляем запрос устройству
            //serialPort.writeString("Get data");
            System.out.println("port opened");
            return true;
        }
        catch(SerialPortException ex){
            System.out.println("error 1");
            System.out.println(ex);
            return false;
        }
    }
     
    
    /**
     * Returns main stage
     * @return
     */
    public Stage getPrimaryStage(){
        return primaryStage;
    }
    
    private static class PortReader implements SerialPortEventListener {

        @Override
        public void serialEvent(SerialPortEvent event) {
            if(event.isRXCHAR() && event.getEventValue() > 0){
                try {
                    //Получаем ответ от устройства, обрабатываем данные и т.д.
                    String data = serialPort.readString(event.getEventValue());
                    //И снова отправляем запрос
                    //serialPort.writeString("Get data");
                }
                catch (SerialPortException ex) {
                    System.out.println(ex);
                }
            }
        }
    }
    
}
