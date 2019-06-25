/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookflight.booking.objects;

import bookflight.booking.SeatClass;
import bookflight.booking.SeatType;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author j187411
 */
public class Customer implements Comparable {
    public Customer() {
        this("", "", 20, SeatClass.ECONOMY, SeatType.AISLE);
    }
    
    public Customer(String firstName, int age) {
        this(firstName, "", age, SeatClass.ECONOMY, SeatType.AISLE);
    }
    
    public Customer(String firstName, String lastName, int age) {
        this(firstName, lastName, age, SeatClass.ECONOMY, SeatType.AISLE);
    }
    
    public Customer(String firstName, String lastName, int age, SeatClass seatClass, SeatType seatType) {
        setFirstName(firstName);
        setLastName(lastName);
        setAge(age);
        setSeatingClass(seatClass);
        setSeatingType(seatType);
    }
    
    private final SimpleStringProperty firstName = new SimpleStringProperty("");
    private final SimpleStringProperty lastName = new SimpleStringProperty("");
    private final SimpleIntegerProperty age = new SimpleIntegerProperty(20);
    private final SimpleStringProperty seatAlloc = new SimpleStringProperty("");
    private SeatClass seatingClass;
    private SeatType seatingType;

    public String getAll() {
        // Depending on what is returned first is the sort priority, in this case its:
        // firstName, lastName, age, seatingClass, seatingType, and the seatAlloc
        return getFirstName() + getLastName() + getAge() + getSeatingClass() + getSeatingType() + getSeatAlloc();
    }
    
    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public int getAge() {
        return age.get();
    }

    public void setAge(int age) {
        this.age.set(age);
    }

    public SeatClass getSeatingClass() {
        return seatingClass;
    }

    public void setSeatingClass(SeatClass seatingClass) {
        this.seatingClass = seatingClass;
    }

    public SeatType getSeatingType() {
        return seatingType;
    }

    public void setSeatingType(SeatType seatingType) {
        this.seatingType = seatingType;
    }
    
    public String getSeatAlloc() {
        return seatAlloc.get();
    }
    
    public void setSeatAlloc(String seatAlloc) {
        this.seatAlloc.set(seatAlloc);
    }
    
    @Override
    public String toString() {
        return "firstName: " + getFirstName() + "\r\nlastName: " + getLastName()
                + "\r\nage: " + getAge() + "\r\nseatingClass: " + getSeatingClass().toString()
                + "\r\nseatingType: " + getSeatingType().toString();
    }

    @Override
    public int compareTo(Object o) {
        Customer c = (Customer) o;
        return this.getAll().compareTo(c.getAll());
    }
}
