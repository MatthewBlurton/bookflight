/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookflight.booking.exceptions;

/**
 * If a seat has already been booked, this exception will be thrown
 * @author Matthew Blurton
 */
public class SeatTakenException extends Exception {
    @Override
    public String getMessage() {
        return "The seat already has been booked";
    }
}
