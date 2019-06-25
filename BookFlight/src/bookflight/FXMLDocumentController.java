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
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
    
    // TextArea used to display the seats of the plane
    @FXML
    private TextArea textAreaSeats;
    
    
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
    
    // Form for adding members
    @FXML
    private TextField textFieldFirstName;
    @FXML
    private TextField textFieldLastName;
    @FXML
    private TextField textFieldAge;
    @FXML
    private ComboBox comboBoxSeatClass;
    @FXML
    private ComboBox comboBoxSeatType;
    
    // Program related data
    private Airplane airplane;
    private ObservableList<Customer> customers;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        textAreaSeats.setEditable(false);
        airplane = new Airplane();
        
        // Refresh all the seats
        refreshSeats();
        
        // Prepare tableViewMembers for displaying Customer objects
        tableColumnMembersFirstName.setCellValueFactory(
                new PropertyValueFactory<>("firstName"));
        tableColumnMembersLastName.setCellValueFactory(
                new PropertyValueFactory<>("lastName"));
        tableColumnMembersAge.setCellValueFactory(
                new PropertyValueFactory<>("age"));
        tableColumnMembersPrefClass.setCellValueFactory(
                new PropertyValueFactory<>("seatingClass"));
        tableColumnMembersPrefType.setCellValueFactory(
                new PropertyValueFactory<>("seatingType"));
        
        // Initialise customer collection and assign it to tableViewMembers
        customers = FXCollections.observableArrayList();
        tableViewMembers.setItems(customers);
        
        // Add placeholder customers
        customers.add(new Customer("Mario", "Mario", 64, SeatClass.BUSINESS, SeatType.WINDOW));
        customers.add(new Customer("Luigi", "Mario", 63, SeatClass.FIRST_CLASS, SeatType.MIDDLE));
        
        // Bind adding new member form
        ObservableList<SeatClass> seatClass = FXCollections.observableArrayList(SeatClass.ECONOMY, SeatClass.BUSINESS, SeatClass.FIRST_CLASS);
        comboBoxSeatClass.setItems(seatClass);
        comboBoxSeatClass.getSelectionModel().select(SeatClass.ECONOMY);
        
        ObservableList<SeatType> seatTypes = FXCollections.observableArrayList(SeatType.AISLE, SeatType.MIDDLE, SeatType.WINDOW);
        comboBoxSeatType.setItems(seatTypes);
        comboBoxSeatType.getSelectionModel().select(SeatType.AISLE);
    }
    
    @FXML
    private void addCustomerButtonAction(ActionEvent event) {
        String first = textFieldFirstName.getText();
        
        // Validate first name input
        if (first.length() < 3) {
            Alert error = new Alert(AlertType.ERROR);
            error.setHeaderText("The first name is incorrect");
            error.setContentText("The input for the first name is incorrect, "
                    + "please ensure that the first name's size is above 3.");
            error.showAndWait();
            return;
        }
        
        // Validate last name input
        String last = textFieldLastName.getText();
        if (last.length() < 3) {
            Alert error = new Alert(AlertType.ERROR);
            error.setHeaderText("The last name is incorrect");
            error.setContentText("The input for the last name is incorrect, "
                    + "please ensure that the last name's size is above 3.");
            error.showAndWait();
            return;
        }
        
        // Set an initial age number
        int age = -1;
        
        // Validate the age input
        try {
            age = Integer.parseInt(textFieldAge.getText());
        } catch (NumberFormatException nfe) {
            Alert error = new Alert(AlertType.ERROR);
            error.setHeaderText("The age entered is incorrect");
            error.setContentText("The input for age is incorrect, please ensure "
                    + "that the age entered are numbers only");
            error.showAndWait();
            return;
        }
        
        // No validation necessary as the comboBox items both have default values
        // making it impossible to select anything outside of the three valid options.
        SeatClass seatClass = (SeatClass) comboBoxSeatClass.getValue();
        SeatType seatType = (SeatType) comboBoxSeatType.getValue();
        
        // Add the new customer
        customers.add(new Customer(first,last,age,seatClass,seatType));
    }
    
    @FXML
    private void bookFlightButtonAction(ActionEvent event) {
        Customer selectedCustomer =
                (Customer) tableViewMembers.getSelectionModel().getSelectedItem();
        int[] position = airplane.findPreferredSeat(selectedCustomer.getSeatingClass(), selectedCustomer.getSeatingType());
        if (position != null) {
            airplane.assignSeat(position[0], position[1], selectedCustomer);
        }
        refreshSeats();
    }
    
    @FXML
    private void cancelFlightButtonAction(ActionEvent event) {
        Customer selectedCustomer =
                (Customer) tableViewMembers.getSelectionModel().getSelectedItem();
        airplane.cancelSeat(selectedCustomer);
        refreshSeats();
    }
    
    private void refreshSeats() {
        textAreaSeats.setText(airplane.toString());
    }
}
