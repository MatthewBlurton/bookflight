/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookflight.booking.objects;

import bookflight.booking.Exceptions.SeatClassException;
import bookflight.booking.SeatConfig;

/**
 *
 * @author j187411
 */
public class Customer {
    public Customer(String name, int age) {
        this.name = name;
        this.age = age;
        seatingClass = SeatConfig.SEAT_C_ECONOMY;
        prefSeatingType = SeatConfig.SEAT_T_AISLE;
    }
    
    private String name;
    private int age;
    private int seatingClass;
    private int prefSeatingType;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return true if customers age is above or equal to 18
     */
    public boolean isAdult() {
        return age >= 18;
    }

    /**
     * @param age the age to set
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * @return the seatingClass
     */
    public int getSeatingClass() {
        return seatingClass;
    }

    /**
     * @param seatingClass the seatingClass to set
     * @throws bookflight.booking.Exceptions.SeatClassException
     */
    public void setSeatingClass(int seatingClass) throws SeatClassException {
        if (seatingClass != SeatConfig.SEAT_C_ECONOMY
                || seatingClass != SeatConfig.SEAT_C_BUSINESS
                || seatingClass != SeatConfig.SEAT_C_FIRST) {
            throw new SeatClassException();
        }
        this.seatingClass = seatingClass;
    }

    /**
     * @return the prefSeatingType
     */
    public int getPrefSeatingType() {
        return prefSeatingType;
    }

    /**
     * @param prefSeatingType the prefSeatingType to set
     */
    public void setPrefSeatingType(int prefSeatingType) {
        this.prefSeatingType = prefSeatingType;
    }
}
