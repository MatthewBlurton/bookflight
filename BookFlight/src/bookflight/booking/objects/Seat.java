/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookflight.booking.objects;

import bookflight.booking.SeatClass;
import bookflight.booking.SeatType;

/**
 * The seat associated with planes
 * @author Matthew Blurton
 */
public class Seat {
    /**
     * Automatically set the seat class to economy, and the seat type to middle
     */
    public Seat() {
        super();
        seatClass = SeatClass.ECONOMY;
        seatType = SeatType.MIDDLE;
    }
    
    private Customer bookedBy;
    private SeatClass seatClass;
    private SeatType seatType;
    
    /**
     * Show if someone has already booked this seat
     * @return * if no-one has booked, A if an adult has booked, C if a child or teenager has booked.
     */
    public char getBooking() {
        char bookee = '*';
        if (getBookedBy() != null) {
            if (getBookedBy().getAge() >= 18) {
                return 'A';
            } else {
                return 'C';
            }
        }
        return bookee;
    }

    /**
     * Get the customer who made the booking
     * @return the Customer who made the booking
     */
    public Customer getBookedBy() {
        return bookedBy;
    }

    /**
     * Assign a customer to this seat
     * @param bookedBy the customer who made the booking
     */
    public void setBookedBy(Customer bookedBy) {
        this.bookedBy = bookedBy;
    }

    /**
     * Get type of class of this seat
     * @return the seat class
     */
    public SeatClass getSeatClass() {
        return seatClass;
    }

    /**
     * Change the seat class to something else
     * @param seatClass the provided seat class to change to.
     */
    public void setSeatClass(SeatClass seatClass) {
        this.seatClass = seatClass;
    }

    /**
     * Get this seat's type
     * @return the seat type
     */
    public SeatType getSeatType() {
        return seatType;
    }

    /**
     * Change the seat type to something else
     * @param seatType the provided seat type to change to.
     */
    public void setSeatType(SeatType seatType) {
        this.seatType = seatType;
    }
}
