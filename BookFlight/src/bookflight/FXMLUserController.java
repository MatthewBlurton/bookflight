/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookflight;

import bookflight.booking.SeatClass;
import bookflight.booking.SeatType;
import bookflight.booking.objects.Customer;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 *
 * @author mjblu
 */
public class FXMLUserController implements Initializable {
    @FXML
    private TextField textFieldFirstName;
    @FXML
    private TextField textFieldLastName;
    @FXML
    private TextField textFieldAge;
    @FXML
    private ComboBox comboBoxSeatType;
    @FXML
    private ComboBox comboBoxSeatClass;
    
    private Customer customer;
    
    public FXMLUserController() {
        super();
        
        customer = new Customer("Matthew", "Blurton", 24);
    }
    
    public FXMLUserController(Customer customer) {
        this.customer = customer;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<SeatClass> seatClass = FXCollections.observableArrayList(SeatClass.ECONOMY, SeatClass.BUSINESS, SeatClass.FIRST_CLASS);
        comboBoxSeatClass.setItems(seatClass);
        
        ObservableList<SeatType> seatTypes = FXCollections.observableArrayList(SeatType.AISLE, SeatType.MIDDLE, SeatType.WINDOW);
        comboBoxSeatType.setItems(seatTypes);
        
        if (customer != null) {
            textFieldFirstName.setText(customer.getFirstName());
            textFieldLastName.setText(customer.getLastName());
            textFieldAge.setText(String.valueOf(customer.getAge()));
            comboBoxSeatType.setValue(customer.getSeatingType());
            comboBoxSeatClass.setValue(customer.getSeatingClass());
        }
    }
}
