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
        
        customers = FileManagement.readCustomersFromFile(FILE_CUSTOMER);
        if (customers == null) {// If no customers where read, create new placeholder customers
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
        
        // TODO: Allocate seats from file if there are none
//        for (int i = 1; i <= 3; i++) {
//            System.out.println(FileManagement.readSeatFromFile("C:/temp/seats.txt", i, 1));
//            System.out.println(customers[i-1].getID());
//        }
        
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
     * uses tableViewMembers to find the selected customer. If a customer is selected
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
        int[] colRow = airplane.cancelSeat(selectedCustomer);
        if (colRow != null) {
            try {
                FileManagement.writeSeatToFile(FILE_SEAT, colRow[0], colRow[1], -1);
            } catch (IOException ex) {
                Alert error = new Alert(AlertType.ERROR);
                error.setHeaderText("Couldn't undo seat change to file");
                error.setContentText(ex.getMessage());
            }
        }
        refreshData();
    }
    
    private void addCustomer(Customer customer) {
        if (customers == null) {
            customers = new Customer[] {customer};
            FileManagement.writePassengersToFile(FILE_CUSTOMER, customers);
        } else {
            Customer[] originalCustomers = customers;
            customers = new Customer[originalCustomers.length + 1];
            System.arraycopy(originalCustomers, 0, customers, 0, originalCustomers.length);
            customers[customers.length - 1] = customer;
            Arrays.sort(customers);
            FileManagement.writePassengersToFile(FILE_CUSTOMER, customers);
        }
        refreshData();
    }
    
    private void removeCustomer(Customer customer) {
        int indexFound = Arrays.binarySearch(customers, customer);
        if (indexFound > -1) { // If the customer is found with a specific ID, remove any bookings, and delete the customer
            Customer[] newCustomers = new Customer[customers.length - 1];
            airplane.cancelSeat(customer);
            System.arraycopy(customers, 0, newCustomers, 0, indexFound);
            System.arraycopy(customers, indexFound + 1, newCustomers, indexFound, newCustomers.length - indexFound);
            customers = newCustomers;
            FileManagement.writePassengersToFile(FILE_CUSTOMER, customers);
            refreshData();
        }
    }
    
    /**
     * Attempt to book a flight at a specific seat in a certain location
     * @param column the column seat
     * @param row the row seat
     */
    private void bookFlight(int column, int row) throws IndexOutOfBoundsException {
        Customer selectedCustomer = 
                (Customer) tableViewMembers.getSelectionModel().getSelectedItem();
        try {
            airplane.assignSeat(column, row, selectedCustomer);
            int customerId = selectedCustomer.getID();
            FileManagement.writeSeatToFile(FILE_SEAT, column, row, customerId);
        } catch (SeatTakenException ste) {// Error occurs when 
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
