/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ui;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author Chinthaka
 */
public class Main extends Application {
    
      public static Stage stage;
    
    @Override
    public void start(Stage primaryStage) {
        try {
            this.stage= stage;
            AnchorPane root = FXMLLoader.load(Main.class.getResource("MainWindow.fxml"));
            Scene scene = new Scene(root, 550, 325);
            primaryStage.setTitle("Harvest Super Market");
            primaryStage.setScene(scene); 
            primaryStage.setMaximized(true); 
            primaryStage.show();
        } catch (IOException ex) {
            System.out.println("\n\n MainWindow Loading Error \n\n");
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       // System.out.println(GenderDao.getAll().size());
        
        launch(args);
    }
    
}
