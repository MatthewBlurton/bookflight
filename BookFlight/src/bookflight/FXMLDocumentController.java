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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;

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
    
    @FXML
    private GridPane gridPaneAirplane;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        for (int i = 1; i < 8; i++) {
            for (int j = 1; j < 13; j++) {
                if (i != 4) {
                    if (i < 4) {
                        
                    }
                    gridPaneAirplane.add(new Button("Button"), i, j);
                }
            }
        }
        
    }
    
    private Airplane airplane;
}
