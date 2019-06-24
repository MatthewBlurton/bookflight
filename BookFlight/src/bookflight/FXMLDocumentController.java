/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookflight;

import bookflight.booking.SeatClass;
import bookflight.booking.SeatType;
import bookflight.booking.objects.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
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
    private TextArea textAreaSeats;
    
    @FXML
    private Button buttonNewMember;
    
    @FXML
    private Button buttonBookFlight;
    
    // Tableview for members
    @FXML
    private TableView tableViewMembers;
    @FXML
    private TableColumn<Customer, String> tableColumnMembersFirstName;
    @FXML
    private TableColumn<Customer, String> tableColumnMembersLastName;
    @FXML
    private TableColumn<Customer, Integer> tableColumnMembersAge;
    @FXML
    private TableColumn<Customer, String> tableColumnMembersPrefClass;
    @FXML
    private TableColumn<Customer, String> tableColumnMembersPrefType;
    
    
    private Airplane airplane;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        textAreaSeats.setEditable(false);
        airplane = new Airplane();
        
        // Refresh all the seats
        refreshSeats();
        
        // Generate a temporary customer list
        tableColumnMembersFirstName.setCellValueFactory(
                new PropertyValueFactory<>("firstName"));
        tableColumnMembersLastName.setCellValueFactory(
                new PropertyValueFactory<>("lastName"));
        tableColumnMembersAge.setCellValueFactory(
                new PropertyValueFactory<>("age"));
        
        ListProperty<Customer> customers = new SimpleListProperty<>(
                FXCollections.observableArrayList());
        customers.add(new Customer("Mario", "Mario", 64, SeatClass.BUSINESS, SeatType.WINDOW));
        tableViewMembers.getItems().setAll(customers);
        
    }
    
    private void refreshSeats() {
        textAreaSeats.setText(airplane.toString());
    }
}
