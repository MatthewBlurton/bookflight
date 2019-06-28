/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookflight.booking.objects;

import bookflight.booking.SeatClass;
import bookflight.booking.SeatType;
import java.beans.Transient;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * The customer class, used to designate seats as booked
 * @author Matthew Blurton
 */
public class Customer implements Comparable, Serializable {
    public Customer() {
        this("", 20);
    }
    
    // If this where in production I would reccomend a different solution. This directly sets the id for the initial load
    public Customer(int id) {
        this.id = id;
    }
    
    public Customer(String firstName, int age) {
        this(firstName, "", age);
    }
    
    public Customer(String firstName, String lastName, int age) {
        this(firstName, lastName, age, SeatClass.ECONOMY, SeatType.AISLE);
    }
    
    /**
     * Assign all the main attributes of the customer
     * @param firstName The first name of the customer
     * @param lastName The last name of the customer
     * @param age The age of the customer
     * @param seatClass The seat class preference for the customer
     * @param seatType The seat type preference for the customer
     */
    public Customer(String firstName, String lastName, int age, SeatClass seatClass, SeatType seatType) {
        this(firstName, lastName, age, seatClass, seatType, 1);
    }
    
    /**
     * Assign every attribute of the customer manually
     * @param firstName The first name of the customer
     * @param lastName The last name of the customer
     * @param age The age of the customer
     * @param seatClass The seat class preference for the customer
     * @param seatType The seat type preference for the customer
     * @param id The id for the customer (should only be used in initialization
     */
    public Customer(String firstName, String lastName, int age, SeatClass seatClass, SeatType seatType, int id) {
        setFirstName(firstName);
        setLastName(lastName);
        setAge(age);
        setSeatingClass(seatClass);
        setSeatingType(seatType);
        setID(id);
    }
    
    private transient static final AtomicInteger COUNT = new AtomicInteger(1);
    private int id = -1;
    private String firstName;
    private String lastName;
    private int age;
    private transient String seatAlloc;
    private SeatClass seatingClass;
    private SeatType seatingType;
    
    /**
     * Get the id for the customer
     * @return the customer id
     */
    public int getID() {
        return this.id;
    }
    
    /**
     * Set the id for the customer. Used in the constructor
     * @param id the id to set the user to, will not go below the atomic integer counter
     */
    private void setID(int id) {
        COUNT.set(Math.max(id, COUNT.get()));
        this.id = COUNT.getAndIncrement();
    }
    
    /**
     * Get the first name of the customer
     * @return the first name of the customer
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Set the first name of the customer
     * @param firstName what to set the first name to
     */
    public final void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Get the first name of the customer
     * @return the first name of the customer
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Set the first name of the customer
     * @param firstName what to set the first name to
     */
    public final void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Get the age of the customer
     * @return the age of the customer
     */
    public int getAge() {
        return age;
    }

    /**
     * Sets the age for the customer
     * @param age what to set the age to
     */
    public final void setAge(int age) {
        this.age = age;
    }

    /**
     * Get an enum relating to the preferred seating class of the customer
     * @return preferred seating class
     */
    public SeatClass getSeatingClass() {
        return seatingClass;
    }

    /**
     * Change the preferred seating class for the customer
     * @param seatingClass what to change the preferred seating class to.
     */
    public final void setSeatingClass(SeatClass seatingClass) {
        this.seatingClass = seatingClass;
    }

    /**
     * Get an enum relating to the preferred seating type of the customer
     * @return preferred seating type
     */
    public SeatType getSeatingType() {
        return seatingType;
    }

    /**
     * Change the preferred seating type for the customer
     * @param seatingType what to change the preferred seating type to.
     */
    public final void setSeatingType(SeatType seatingType) {
        this.seatingType = seatingType;
    }
    
    /**
     * Get a String representation of the seat currently booked by the user
     * Appears like (A4, or C11)
     * @return String representation of the seat allocation
     */
    public String getSeatAlloc() {
        return seatAlloc;
    }
    
    /**
     * Change the String representation of the currently booked seat by the user
     * @param seatAlloc what to change the represented seat allocation to. Note: this can be set to anything, good practice is to set the column name first then the row number
     */
    public void setSeatAlloc(String seatAlloc) {
        this.seatAlloc = seatAlloc;
    }
    
    @Override
    public String toString() {
        return "firstName: " + getFirstName() + "\r\nlastName: " + getLastName()
                + "\r\nage: " + getAge() + "\r\nseatingClass: " + getSeatingClass().toString()
                + "\r\nseatingType: " + getSeatingType().toString();
    }

    /**
     * Compares the current customer's id with another separate customer
     * @param o The customer key to compare the id with
     * @return a negative integer, zero, or a positive integer as this Customer is less than, equal to, or greater than the specified Customer.
     */
    @Override
    public int compareTo(Object o) {
        Customer c = (Customer) o;
        return String.valueOf(getID()).compareTo(String.valueOf(c.getID()));
    }
}