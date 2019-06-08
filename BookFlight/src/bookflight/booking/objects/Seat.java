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
public class Seat {
    public Seat() {
        super();
        seatClass = SeatClass.ECONOMY;
        seatType = SeatType.MIDDLE;
    }
    
    private Customer bookedBy;
    private SeatClass seatClass;
    private SeatType seatType;
    
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

    public Customer getBookedBy() {
        return bookedBy;
    }

    public void setBookedBy(Customer bookedBy) {
        this.bookedBy = bookedBy;
    }

    public SeatClass getSeatClass() {
        return seatClass;
    }

    public void setSeatClass(SeatClass seatClass) {
        this.seatClass = seatClass;
    }

    public SeatType getSeatType() {
        return seatType;
    }

    public void setSeatType(SeatType seatType) {
        this.seatType = seatType;
    }
}
