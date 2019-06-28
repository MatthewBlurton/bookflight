/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookflight.booking;

/**
 * All possible seating types
 * @author Matthew Blurton
 */
public enum SeatType {
    AISLE, MIDDLE, WINDOW;
    
    @Override
    public String toString() {
        switch(this) {
            case WINDOW:
                return "Window";
            case MIDDLE:
                return "Middle";
            default:
                return "Aisle";
        }
    }
}
