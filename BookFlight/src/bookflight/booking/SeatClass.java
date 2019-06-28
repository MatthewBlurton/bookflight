/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookflight.booking;

/**
 * All possible seating classes
 * @author Matthew Blurton
 */
public enum SeatClass {
    ECONOMY, BUSINESS, FIRST_CLASS;
    
    @Override    
    public String toString() {
        switch(this) {
            case FIRST_CLASS:
                return "First class";
            case BUSINESS:
                return "Business";
            default:
                return "Economy";
        }
    }
}
