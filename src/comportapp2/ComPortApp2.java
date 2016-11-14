/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comportapp2;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author WIN7x64
 */
public class ComPortApp2 extends Application {
    
    private Stage primaryStage;
    private AnchorPane rootLayout;
    private FXMLDocumentController controller;
    
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
            controller = loader.getController();
            controller.setMainApp(this);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    
    
    @Override
    public void stop() throws Exception{        
        controller.close();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);           
    }
    
    
    /**
     * Returns main stage
     * @return
     */
    public Stage getPrimaryStage(){
        return primaryStage;
    }
    
    
}
