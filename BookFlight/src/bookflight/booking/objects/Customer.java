/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookflight.booking.objects;

import bookflight.booking.SeatClass;
import bookflight.booking.SeatType;

/**
 *
 * @author j187411
 */
public class Customer {
    public Customer() {
        firstName = "";
        lastName = "";
        age = 20;
        seatingClass = SeatClass.ECONOMY;
        seatingType = SeatType.AISLE;
    }
    
    public Customer(String firstName, int age) {
        this();
        this.firstName = firstName;
        this.age = age;
    }
    
    public Customer(String firstName, String lastName, int age) {
        this(firstName, age);
        this.lastName = lastName;
    }
    
    private String firstName;
    private String lastName;
    private int age;
    private SeatClass seatingClass;
    private SeatType seatingType;

    public String getName() {
        return lastName.isEmpty() ? firstName : firstName + ", " + lastName;
    }
    
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
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

    @Override
    public String toString() {
        return "firstName: " + getFirstName() + "\r\nlastName: " + getLastName()
                + "\r\nage: " + getAge() + "\r\nseatingClass: " + getSeatingClass().toString()
                + "\r\nseatingType: " + getSeatingType().toString();
    }
}
