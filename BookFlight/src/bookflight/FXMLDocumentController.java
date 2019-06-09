/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookflight;

import bookflight.booking.objects.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

/**
 *
 * @author j187411
 */
public class FXMLDocumentController implements Initializable {
    
//    @FXML
//    private Label label;
    
//    @FXML
//    private void handleButtonAction(ActionEvent event) {
//        System.out.println("You clicked me!");
//        label.setText("Hello World!");
//    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Airplane plane = new Airplane();
        
        Customer child = new Customer("Mourine", "Redmond", 7);
        Customer adult = new Customer("Peter", "Redmond", 24);
        
        System.out.println(plane);
    }
}
