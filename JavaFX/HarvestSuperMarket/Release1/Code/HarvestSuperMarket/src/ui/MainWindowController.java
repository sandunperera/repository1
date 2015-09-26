/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Chinthaka
 */
public class MainWindowController implements Initializable {
    
    public static File lastDirectory; 
    @FXML
    private Menu mnuPeople;
    @FXML
    private MenuItem mtmEmployee;
    @FXML
    private SplitPane sptMain;
    @FXML
    private AnchorPane apnLeft;
    @FXML
    private ImageView imgLogo;
    @FXML
    private AnchorPane apnRight;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void mtmEmployeeAP(ActionEvent event) {
        try {            
            AnchorPane root = FXMLLoader.load(Main.class.getResource("EmployeeUI.fxml"));
            apnRight.getChildren().clear();
            apnRight.getChildren().add(root);            
        } catch (IOException ex) {
            System.out.println("Cant load EmployeeUI");
        }
    
    }
        
}
