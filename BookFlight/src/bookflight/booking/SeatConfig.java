/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookflight.booking;

/**
 *
 * @author j187411
 */
public abstract class SeatConfig {
    // Seat classes
    /**
     * First class value for seats
     */
    public static final int SEAT_C_FIRST = 0;
    /**
     * Business class value for seats
     */
    public static final int SEAT_C_BUSINESS = 1;
    /**
     * Economy class value for seats
     */
    public static final int SEAT_C_ECONOMY = 2;
    
    // Seat types
    /**
     * Window type position for seats
     */
    public static final int SEAT_T_WINDOW = 0;
    /**
     * Middle type position for seats
     */
    public static final int SEAT_T_MIDDLE = 1;
    /**
     * Aisle type position for seats
     */
    public static final int SEAT_T_AISLE = 2;
    
}
