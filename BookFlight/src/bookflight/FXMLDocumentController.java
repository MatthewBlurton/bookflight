/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookflight;

import bookflight.booking.SeatClass;
import bookflight.booking.SeatType;
import bookflight.booking.exceptions.SeatTakenException;
import bookflight.booking.objects.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author j187411
 */
public class FXMLDocumentController implements Initializable {
    // Dialogue messages
    private final String ERROR_BAD_FIRST_NAME_HEADER = "The first name is incorrect";
    private final String ERROR_BAD_FIRST_NAME = "The input for the first name is incorrect, please ensure that the first name's size is above 3.";
    private final String ERROR_BAD_LAST_NAME_HEADER = "The last name is incorrect";
    private final String ERROR_BAD_LAST_NAME = "The input for the last name is incorrect, please ensure that the last name's size is above 3.";
    private final String ERROR_BAD_AGE_HEADER = "The age entered is incorrect";
    private final String ERROR_BAD_AGE = "The input for age is incorrect, please ensure that the age entered are numbers only";
    
    private final String ERROR_NO_CUSTOMER_HEADER = "No member selected";
    private final String ERROR_NO_CUSTOMER = "Can't book a flight without selecting a member first";
    private final String ERROR_BAD_ROW_COLUMN_HEADER = "Column or Row is not a number";
    private final String ERROR_BAD_ROW_COLUMN = "Couldn't book a seat because the column or row is not a number.";
    private final String ERROR_NULL_SEAT_HEADER = "Seat does not exist";
    private final String ERROR_NULL_SEAT = "Couldn't book a seat because the seat being booked does not exist. Please ensure you have selected an existing seat for your booking.";
    private final String ERROR_FULL_BOOKED_HEADER = "All flights booked";
    private final String ERROR_FULL_BOOKED = "All the seats based on your preferences have been taken. To book a flight, please do it manually.";
    private final String ERROR_ALREADY_BOOKED_HEADER = "Already booked";
    private final String ERROR_ALREADY_BOOKED = "Already booked, please cancel the current booking before proceeding to create a new booking.";
    private final String ERROR_SEAT_BOOKED_HEADER = "Seat has already been booked";
    private final String ERROR_SEAT_BOOKED = "Cannot book the selected seat as it has been already been booked. Please choose another seat";
    
    // TextArea used to display the seats of the plane
    @FXML
    private TextArea textAreaSeats;
    
    
    // Form for booking members
    @FXML
    private TableView tableViewMembers;
    @FXML
    private TableColumn<Customer, String> tableColumnMembersSeatAllocation;
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
    
    @FXML
    private TextField textFieldMemberColumn;
    @FXML
    private TextField textFieldMemberRow;
    
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
        refreshData();
        
        // Prepare tableViewMembers for displaying Customer objects
        tableColumnMembersSeatAllocation.setCellValueFactory(
                new PropertyValueFactory<>("seatAlloc"));
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
        customers.add(new Customer("Mario", "Mario", 64, SeatClass.FIRST_CLASS, SeatType.WINDOW));
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
            error.setHeaderText(ERROR_BAD_FIRST_NAME_HEADER);
            error.setContentText(ERROR_BAD_FIRST_NAME);
            error.showAndWait();
            return;
        }
        
        // Validate last name input
        String last = textFieldLastName.getText();
        if (last.length() < 3) {
            Alert error = new Alert(AlertType.ERROR);
            error.setHeaderText(ERROR_BAD_LAST_NAME_HEADER);
            error.setContentText(ERROR_BAD_LAST_NAME);
            error.showAndWait();
            return;
        }
        
        // Set an initial age number
        int age;
        
        // Validate the age input
        try {
            age = Integer.parseInt(textFieldAge.getText());
        } catch (NumberFormatException nfe) {
            Alert error = new Alert(AlertType.ERROR);
            error.setHeaderText(ERROR_BAD_AGE_HEADER);
            error.setContentText(ERROR_BAD_AGE);
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
    private void bookFlightAutoButtonAction(ActionEvent event) {
        Customer selectedCustomer =
                (Customer) tableViewMembers.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            Alert error = new Alert(AlertType.ERROR);
            error.setHeaderText(ERROR_NO_CUSTOMER_HEADER);
            error.setContentText(ERROR_NO_CUSTOMER);
            error.showAndWait();
            return;
        } else if (airplane.isBooked(selectedCustomer)){
            Alert error = new Alert(AlertType.ERROR);
            error.setHeaderText(ERROR_ALREADY_BOOKED_HEADER);
            error.setContentText(ERROR_ALREADY_BOOKED);
            error.showAndWait();
            return;
        }
        int[] position = airplane.findPreferredSeat(selectedCustomer.getSeatingClass(), selectedCustomer.getSeatingType());
        if (position != null) {
            bookFlight(position[0], position[1]);
        } else {
            Alert allBooked = new Alert(AlertType.ERROR);
            allBooked.setHeaderText(ERROR_FULL_BOOKED_HEADER);
            allBooked.setContentText(ERROR_FULL_BOOKED);
            allBooked.showAndWait();
        }
    }
    
    @FXML
    private void bookFlightManualButtonAction(ActionEvent event) {
        Customer selectedCustomer = (Customer) tableViewMembers.getSelectionModel().getSelectedItem();
        if ( selectedCustomer == null) {
            Alert error = new Alert(AlertType.ERROR);
            error.setHeaderText(ERROR_NO_CUSTOMER_HEADER);
            error.setContentText(ERROR_NO_CUSTOMER);
            error.showAndWait();
            return;
        } else if (airplane.isBooked(selectedCustomer)){
            Alert error = new Alert(AlertType.ERROR);
            error.setHeaderText(ERROR_ALREADY_BOOKED_HEADER);
            error.setContentText(ERROR_ALREADY_BOOKED);
            error.showAndWait();
            return;
        }
        
        boolean errorOccurred = false;
        String errorHeaderText = "";
        String errorContentText = "";
        try {
            int column = Integer.parseInt(textFieldMemberColumn.getText()) - 1;
            int row = Integer.parseInt(textFieldMemberRow.getText()) - 1;
                
            bookFlight(column, row);
        } catch (NumberFormatException ex) {
            errorOccurred = true;
            errorHeaderText = ERROR_BAD_ROW_COLUMN_HEADER;
            errorContentText = ERROR_BAD_ROW_COLUMN;
        } catch (ArrayIndexOutOfBoundsException ex) {
            errorOccurred = true;
            errorHeaderText = ERROR_NULL_SEAT_HEADER;
            errorContentText = ERROR_NULL_SEAT;
        }
        
        if (errorOccurred) {
            Alert error = new Alert(AlertType.ERROR);
            error.setHeaderText(errorHeaderText);
            error.setContentText(errorContentText);
            error.showAndWait();
        }
    }
    
    @FXML
    private void cancelFlightButtonAction(ActionEvent event) {
        Customer selectedCustomer =
                (Customer) tableViewMembers.getSelectionModel().getSelectedItem();
        airplane.cancelSeat(selectedCustomer);
        refreshData();
    }
    
    private void bookFlight(int column, int row) throws IndexOutOfBoundsException {
        Customer selectedCustomer = 
                (Customer) tableViewMembers.getSelectionModel().getSelectedItem();
        try {
            airplane.assignSeat(column, row, selectedCustomer);
        } catch (SeatTakenException ste) {
            Alert error = new Alert(AlertType.ERROR);
            error.setHeaderText(ERROR_SEAT_BOOKED_HEADER);
            error.setContentText(ERROR_SEAT_BOOKED);
            error.showAndWait();
        }
        
        refreshData();
    }
    
    private void refreshData() {
        textAreaSeats.setText(airplane.toString());
        tableViewMembers.refresh();
    }
}
