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
public class Airplane {
    public Airplane() {
        super();
        seats = new Seat[6][12];
        
        // Initiate seats
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 12; j++) {
                seats[i][j] = new Seat();
            }
        }
        
        // Generate all the seats for this airplane
        for (int column = 0; column < 6; column++) {
            for (int row = 0; row < 12; row++) {
                // Set the seat types
                switch (column) {
                    // Seat type for window
                    // *-- --*
                    case 0:
                    case 5:
                        seats[column][row]
                                .setSeatType(SeatType.WINDOW);
                        break;
                    // Seat type for aisle
                    // --* *--
                    case 2:
                    case 3:
                        seats[column][row]
                                .setSeatType(SeatType.AISLE);
                        break;
                    // Seat type for middle
                    // -*- -*-
                    default:
                        seats[column][row].setSeatType(SeatType.MIDDLE);
                        break;
                }
                
                // Set the seat class
                // Seat class for first class (rows 1 & 2)
                if (row >= 0 && row <= 1) {
                    seats[column][row].setSeatClass(SeatClass.FIRST_CLASS);
                } // Seat class for business class (rows 3-6)
                else if (row > 1 && row <= 5) {
                    seats[column][row].setSeatClass(SeatClass.BUSINESS);
                } // Seat class for economy class (rows 7-*)
                else {
                    seats[column][row].setSeatClass(SeatClass.ECONOMY);
                }
            }
        }
    }
    private Seat[][] seats;
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("       ABC DEF\r\n");
        for (int row = 0; row < 12; row++) {
            builder.append("Row ");
            builder.append(row + 1);
            if (row < 9) builder.append(" ");
            builder.append(" ");
            for (int column = 0; column < 6; column++) {
                builder.append(seats[column][row].getBooking());
                if (column == 2) builder.append(' ');
            }
            if (row < 11) builder.append("\r\n");
        }
        return builder.toString();
    }
}
