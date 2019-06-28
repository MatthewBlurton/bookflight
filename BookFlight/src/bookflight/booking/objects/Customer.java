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
 *
 * @author j187411
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
    
    public Customer(String firstName, String lastName, int age, SeatClass seatClass, SeatType seatType) {
        this(firstName, lastName, age, seatClass, seatType, 1);
    }
    
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
    
    public int getID() {
        return this.id;
    }
    
    private void setID(int id) {
        COUNT.set(Math.max(id, COUNT.get()));
        this.id = COUNT.getAndIncrement();
    }
    
    public String getFirstName() {
        return firstName;
    }

    public final void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public final void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public final void setAge(int age) {
        this.age = age;
    }

    public SeatClass getSeatingClass() {
        return seatingClass;
    }

    public final void setSeatingClass(SeatClass seatingClass) {
        this.seatingClass = seatingClass;
    }

    public SeatType getSeatingType() {
        return seatingType;
    }

    public final void setSeatingType(SeatType seatingType) {
        this.seatingType = seatingType;
    }
    
    public String getSeatAlloc() {
        return seatAlloc;
    }
    
    public void setSeatAlloc(String seatAlloc) {
        this.seatAlloc = seatAlloc;
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
        return String.valueOf(getID()).compareTo(String.valueOf(c.getID()));
    }
}