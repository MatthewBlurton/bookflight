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
import bookflight.utilities.DataManagement;
import bookflight.utilities.FileManagement;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
 * The main front-end to the application
 * @author Matthew Blurton
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
    
    private final String FILE_SEAT = "." + File.separator + "seats";
    private final String FILE_CUSTOMER = "." + File.separator + "customers";
    
    // TextArea used to display the seats of the plane
    @FXML
    private TextArea textAreaSeats;
    
    
    // Table for members
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
    private TextField textFieldMemberSearch;
    
    // Form for selecting seat to book
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
    
    private Customer[] customers;
    private ObservableList<Customer> customerGuiList;
    
    // UI related data
    private String searchTerm = "";
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize all visual elements
        textAreaSeats.setEditable(false);
        airplane = new Airplane();
        
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
        
        // Add search listener
        textFieldMemberSearch.textProperty().addListener((Observable, oldValue, newValue) -> {
            newValue = newValue.toUpperCase();
            searchTerm = newValue.trim();
            refreshData();
        });
        
        // Initialise customer collection and assign it to tableViewMembers
        customerGuiList = FXCollections.observableArrayList();
        tableViewMembers.setItems(customerGuiList);
        
        Customer[] customerFile = FileManagement.readCustomersFromFile(FILE_CUSTOMER);
        // Quick hacky solution to ensure that the customer Id's are appropriately set.
        if (customerFile != null) {
            for (Customer customer : customerFile) {
            addCustomer(new Customer(customer.getFirstName(), customer.getLastName(),
                            customer.getAge(), customer.getSeatingClass(),
                            customer.getSeatingType(), customer.getID()));
            }
        }
        
        // If no customers where read, create new placeholder customers
        if (customers == null) {
            addCustomer(new Customer("Mario", "Mario", 64, SeatClass.FIRST_CLASS, SeatType.WINDOW));
            addCustomer(new Customer("Luigi", "Mario", 63, SeatClass.FIRST_CLASS, SeatType.MIDDLE));
            addCustomer(new Customer("Wario", "Wario", 62, SeatClass.FIRST_CLASS, SeatType.AISLE));
        } else { // Allocate all the seats to the appropriate customer
            for(int col = 0; col < airplane.getColumns(); col++) {
                for (int row = 0; row < airplane.getRows(); row++) {
                    int customerID = FileManagement.readSeatFromFile(FILE_SEAT, col, row);
                    if (customerID > -1) {
                        Customer customerIDOnly = new Customer(customerID);
                        int customerIndex = Arrays.binarySearch(customers, customerIDOnly);
                        try {
                            if (customerIndex > -1) airplane.assignSeat(col, row, customers[customerIndex]);
                        }catch (SeatTakenException ex) { // Shouldn't happen
                            System.err.println(ex.getMessage());
                        }
                    }
                }
            }
        }
        
        // Bind adding new member form
        ObservableList<SeatClass> seatClass = FXCollections.observableArrayList(SeatClass.ECONOMY, SeatClass.BUSINESS, SeatClass.FIRST_CLASS);
        comboBoxSeatClass.setItems(seatClass);
        comboBoxSeatClass.getSelectionModel().select(SeatClass.ECONOMY);
        
        ObservableList<SeatType> seatTypes = FXCollections.observableArrayList(SeatType.AISLE, SeatType.MIDDLE, SeatType.WINDOW);
        comboBoxSeatType.setItems(seatTypes);
        comboBoxSeatType.getSelectionModel().select(SeatType.AISLE);
        
        refreshData();
    }
    
    /**
     * Creates a new customer and adds it to the passenger list
     * @param event 
     */
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
        
        int lastId = customers[customers.length-1].getID();
        
        
        // Add the new customer
        addCustomer(new Customer(first,last,age,seatClass,seatType, lastId));
    }
    
    /**
     * uses tableViewMembers to find the selected customer. If a customer is selected, run the removeCustomer method
     * remove the customer from the list of customers
     * @param event 
     */
    @FXML
    private void removeCustomerButtonAction(ActionEvent event) {
        Customer selectedCustomer = (Customer) tableViewMembers.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null) {
            removeCustomer(selectedCustomer);
        }
    }
    
    /**
     * Automatically book a flight depending on the preferences of the selected
     * customer in the tableViewMembers table. If it cannot book a flight, display
     * and error message
     * @param event 
     */
    @FXML
    private void bookFlightAutoButtonAction(ActionEvent event) {
        Customer selectedCustomer =
                (Customer) tableViewMembers.getSelectionModel().getSelectedItem();
        
        // Check if there is a selected customer
        if (selectedCustomer == null) {
            Alert error = new Alert(AlertType.ERROR);
            error.setHeaderText(ERROR_NO_CUSTOMER_HEADER);
            error.setContentText(ERROR_NO_CUSTOMER);
            error.showAndWait();
            return;
        // If there is no customer selected, display an error message and exit the function
        } else if (airplane.isBooked(selectedCustomer)){
            Alert error = new Alert(AlertType.ERROR);
            error.setHeaderText(ERROR_ALREADY_BOOKED_HEADER);
            error.setContentText(ERROR_ALREADY_BOOKED);
            error.showAndWait();
            return;
        }
        
        // Get a seat position based on the tableViewCustomer's seat preferences
        int[] position = airplane.findPreferredSeat(selectedCustomer.getSeatingClass(), selectedCustomer.getSeatingType());
        if (position != null) {
            bookFlight(position[0], position[1]);
        // If there are no seats available show an error message saying all preferred seats are taken
        } else {
            Alert allBooked = new Alert(AlertType.ERROR);
            allBooked.setHeaderText(ERROR_FULL_BOOKED_HEADER);
            allBooked.setContentText(ERROR_FULL_BOOKED);
            allBooked.showAndWait();
        }
    }
    
    /**
     * Manually assign a flight, avoids taking preferences into consideration.
     * @param event 
     */
    @FXML
    private void bookFlightManualButtonAction(ActionEvent event) {
        // Get the selected customer, if there is none throw a no member selected error
        // Otherwise if there is already a booking in the manually designated seat, throw an already booked error.
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
        
        // Book a flight for the selected customer using their specific location
        try {
            int column = Integer.parseInt(textFieldMemberColumn.getText()) - 1;
            int row = Integer.parseInt(textFieldMemberRow.getText()) - 1;
                
            bookFlight(column, row);
        } catch (NumberFormatException ex) {// Called if the user entered a bad row or column (used letters instead of numbers as an example)
            errorOccurred = true;
            errorHeaderText = ERROR_BAD_ROW_COLUMN_HEADER;
            errorContentText = ERROR_BAD_ROW_COLUMN;
        } catch (ArrayIndexOutOfBoundsException ex) {// Called if the user entered a row and column that doesn't exist
            errorOccurred = true;
            errorHeaderText = ERROR_NULL_SEAT_HEADER;
            errorContentText = ERROR_NULL_SEAT;
        }
        
        // If any errors occurred during the runtime of this method, throw an error with the appropriate message
        if (errorOccurred) {
            Alert error = new Alert(AlertType.ERROR);
            error.setHeaderText(errorHeaderText);
            error.setContentText(errorContentText);
            error.showAndWait();
        }
    }
    
    /**
     * If the user cancels their flight, deallocate the seat and update the seats file
     * @param event 
     */
    @FXML
    private void cancelFlightButtonAction(ActionEvent event) {
        Customer selectedCustomer =
                (Customer) tableViewMembers.getSelectionModel().getSelectedItem();
        int[] colRow = airplane.cancelSeat(selectedCustomer);
        if (colRow != null) { // if colRow returned nothing then the customer already booked, no need to proceed
            try { // attempt to write the newly de-allocated seat location to file
                FileManagement.writeSeatToFile(FILE_SEAT, colRow[0], colRow[1], -1);
            } catch (IOException ex) {
                Alert error = new Alert(AlertType.ERROR);
                error.setHeaderText("Couldn't undo seat change to file");
                error.setContentText(ex.getMessage());
            }
        }
        refreshData();
    }
    
    /**
     * Adds a new customer to the customers array, and saves the changes to a customers file
     * @param customer The new customer to add to customers
     */
    private void addCustomer(Customer customer) {
        if (customers == null) {// If customers is empty, then create one new customer for that array
            customers = new Customer[] {customer};
            FileManagement.writePassengersToFile(FILE_CUSTOMER, customers);
        } else {// Otherwise replace the array with a new array, adding the new customer to the end of the array
            Customer[] originalCustomers = customers;
            customers = new Customer[originalCustomers.length + 1];
            System.arraycopy(originalCustomers, 0, customers, 0, originalCustomers.length);
            customers[customers.length - 1] = customer;
            Arrays.sort(customers);
            
            // Write the updated customers to file
            FileManagement.writePassengersToFile(FILE_CUSTOMER, customers);
        }
        refreshData();
    }
    
    /**
     * Remove a customer from the customers array by creating a new array that does not include it,
     * implements binary search to find the appropriate customer index
     * @param customer The customer to delete
     */
    private void removeCustomer(Customer customer) {
        // Sort the customers, then try to find the customer to delete using binary search
        Arrays.sort(customers);
        int indexFound = Arrays.binarySearch(customers, customer);
        if (indexFound > -1) { // If the customer is found with a specific ID, remove any bookings, and delete the customer
            Customer[] newCustomers = new Customer[customers.length - 1];
            airplane.cancelSeat(customer);
            System.arraycopy(customers, 0, newCustomers, 0, indexFound);
            System.arraycopy(customers, indexFound + 1, newCustomers, indexFound, newCustomers.length - indexFound);
            customers = newCustomers;
            
            // Write the newly updated customers to file
            FileManagement.writePassengersToFile(FILE_CUSTOMER, customers);
            refreshData();
        }
    }
    
    /**
     * The user attempts to book a seat for the flight
     * @param column the column seat
     * @param row the row seat
     */
    private void bookFlight(int column, int row) throws IndexOutOfBoundsException {
        Customer selectedCustomer = 
                (Customer) tableViewMembers.getSelectionModel().getSelectedItem();
        try {
            // Attempt to assign a seat then update the seats file
            airplane.assignSeat(column, row, selectedCustomer);
            int customerId = selectedCustomer.getID();
            FileManagement.writeSeatToFile(FILE_SEAT, column, row, customerId);
        } catch (SeatTakenException ste) {// Error occurs when the customer attempts to book an already booked seat
            Alert error = new Alert(AlertType.ERROR);
            error.setHeaderText(ERROR_SEAT_BOOKED_HEADER);
            error.setContentText(ERROR_SEAT_BOOKED);
            error.showAndWait();
        } catch (IOException ex) {// This error occurs if the seat couldn't be saved to file.
            Alert error = new Alert(AlertType.ERROR);
            error.setHeaderText("Couldn't save passenger to file");
            error.setContentText(ex.getMessage());
            error.showAndWait();
            
            // de-allocate seat if error occurs
            airplane.cancelSeat(selectedCustomer);
        }
        
        // update views
        refreshData();
    }
    
    /**
     * Update all GUI components of the application with updated data from the bookings
     */
    private void refreshData() {
        textAreaSeats.setText(airplane.toString());
        
        // If there is text in the searchTerm attempt to filter based on the customer name
        if (!searchTerm.isEmpty()) {
            int found = DataManagement.searchCustomer(customers, searchTerm);
            if (found > -1) { // The first customer that is found with the matching first name is displayed
                // If I where to make any improvements, it would be to use a package that someone else has made
                // for doing binary searches
                customerGuiList.clear();
                customerGuiList.add(customers[found]);
                return;
            } else { // sort the customers array in it's proper form
                Arrays.sort(customers);
            }
        }
        
        // Apply all the customers to the gui for customers
        customerGuiList.setAll(customers);
    }
}
