/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comportapp2;

/**
 *
 * @author WIN7x64
 */
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jssc.*;

public class ComPort {
    private static SerialPort serialPort;
    private static FXMLDocumentController controller;
        
    public enum BaudRate{
        BAUDRATE_110{
            @Override
            public int value(){return 110;}
        },
        BAUDRATE_300{@Override public int value(){return 300;}},
        BAUDRATE_600{@Override public int value(){return 600;}},
        BAUDRATE_1200{@Override public int value(){return 1200;}},
        BAUDRATE_4800{@Override public int value(){return 4800;}},
        BAUDRATE_9600{@Override public int value(){return 9600;}},
        BAUDRATE_14400{@Override public int value(){return 14400;}},
        BAUDRATE_19200{@Override public int value(){return 19200;}},
        BAUDRATE_38400{@Override public int value(){return 38400;}},
        BAUDRATE_57600{@Override public int value(){return 57600;}},
        BAUDRATE_115200{@Override public int value(){return 115200;}},
        BAUDRATE_128000{@Override public int value(){return 128000;}},
        BAUDRATE_256000{@Override public int value(){return 256000;}};

        public int value() {
            return 9600;
        }
        @Override
        public String toString(){
            return Integer.toString(value());
        }
    }
    private String portName;
    private ComPort.BaudRate baudRate;
    private List<ComPort.BaudRate> baudRateList;
    private ObservableList<ComPort.BaudRate> baudRateOList;
    
    public ObservableList<ComPort.BaudRate> getBaudRateList(){
        baudRateList = new ArrayList<>();
        
        baudRateList.add(ComPort.BaudRate.BAUDRATE_9600);
        baudRateList.add(ComPort.BaudRate.BAUDRATE_14400);
        baudRateList.add(ComPort.BaudRate.BAUDRATE_19200);
        baudRateList.add(ComPort.BaudRate.BAUDRATE_38400);
        baudRateList.add(ComPort.BaudRate.BAUDRATE_57600);
        baudRateList.add(ComPort.BaudRate.BAUDRATE_115200);
        
        
        baudRateOList = FXCollections.observableList(baudRateList);
        System.out.println(baudRateOList);
        return baudRateOList;
    }
    
    public boolean connect(String portName, ComPort.BaudRate baudRate){
        System.out.println("open port check 0");
        if(portName != null){
            System.out.println("open port check 1");
            if((!portName.isEmpty())&&(portName.regionMatches(0, "COM", 0, 3))){
                if(serialPort == null){
                    serialPort = new SerialPort(portName);
                    System.out.println("open port check 3");
                    return openPort(portName,baudRate);
                }
                if(serialPort.isOpened()){
                    if((this.portName.equals(portName))&&(this.baudRate == baudRate)){// the same port is open
                        System.out.println("open port check 4");
                        return true;
                    }
                    closePort();
                    System.out.println("open port check 5");
                    return openPort(portName,baudRate);
                }
                System.out.println("open port check 6");
                return openPort(portName,baudRate);
            }
        }
        System.out.println("open port check 2");
        closePort();
        serialPort=null;
        return false;
    }
    
    public boolean writeString(String string){
        if(connect(this.portName,this.baudRate)){
            try{
                serialPort.writeString("Get data");
                return true;
            }catch(SerialPortException ex){
                return false;
            }
        }
        else return false;
    }
    
    public void setController(FXMLDocumentController controller){
        this.controller = controller;
    }
    
    public void close(){
        closePort();
    }
    
    private boolean openPort(String portName, ComPort.BaudRate baudRate){
        try{
            
            serialPort.openPort(); 
            serialPort.setParams(baudRate.value(),
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
            this.portName = portName;
            this.baudRate = baudRate;
            System.out.println("port opened");
            return true;
        }
        catch(SerialPortException ex){
            System.out.println("open port error");
            System.out.println(ex);
            return false;
        }
    }
    private void closePort(){
        if((serialPort != null)&&(serialPort.isOpened())){
            try{
                serialPort.removeEventListener();
                serialPort.closePort();
                System.out.println("port closed");
            }catch(SerialPortException spe){
                System.out.println("unable to close port " + spe.getLocalizedMessage() );          
            }           
        }
        //System.out.println("stage is closed");    
    }
    
    private static class PortReader implements SerialPortEventListener {

        @Override
        public void serialEvent(SerialPortEvent event) {
            if(event.isRXCHAR() && event.getEventValue() > 0){
                try {
                    //Получаем ответ от устройства, обрабатываем данные и т.д.
                    //String data = serialPort.readString(event.getEventValue());
                    byte[] data = serialPort.readBytes(event.getEventValue());
                    StringBuilder builder = new StringBuilder();
                    for(byte b:data){
                        builder.append(String.format("%02x", b));
                    }
                    if(controller != null){
                        controller.printString(builder.toString());
                    }
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
