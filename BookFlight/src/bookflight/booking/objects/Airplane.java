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
public class Airplane  {
    public Airplane() {
        this(6, 12);
    }
    
    private Airplane(int columns, int rows) {
        super();
        this.columns = columns;
        this.rows = rows;
        
        seats = new Seat[columns][rows];
        
        // Initiate seats
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                seats[i][j] = new Seat();
            }
        }
        
        // Generate all the seats for this airplane
        for (int column = 0; column < columns; column++) {
            for (int row = 0; row < rows; row++) {
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
                // Seat class for first class (rows 0 and 1)
                if (row >= 0 && row < 2) {
                    seats[column][row].setSeatClass(SeatClass.FIRST_CLASS);
                } // Seat class for business class (rows 2 to 5)
                else if (row >= 2 && row < 6) {
                    seats[column][row].setSeatClass(SeatClass.BUSINESS);
                } // Seat class for economy class (rows 6-*)
                else {
                    seats[column][row].setSeatClass(SeatClass.ECONOMY);
                }
            }
        }
    }

    private Seat[][] seats;
    private int columns;
    private int rows;
    
    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }
    
    public Seat getSeat(int column, int row) {
        return seats[column][row];
    }
    
    public void assignSeat(int column, int row, Customer customer) {
        if (checkForBooking(customer).length > 0) {
            // TODO: throw an exception so application can show an alert dialogue
            return;
        }
        seats[column][row].setBookedBy(customer);
    }
    
    public void cancelSeat(Customer customer) {
        int[] columnRow = checkForBooking(customer);
        if (columnRow.length > 0) {
            seats[columnRow[0]][columnRow[1]].setBookedBy(null);
        }
    }
    
    private int[] checkForBooking (Customer customer) {
        for (int column = 0; column < columns; column++) {
            for (int row = 0; row < rows; row++) {
                if (seats[column][row].getBookedBy().equals(customer)) {
                    int[] columnRow = {column, row};
                    return columnRow;
                }
            }
        }
        return new int[0];
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("       ABC DEF\r\n");
        for (int row = 0; row < getRows(); row++) {
            builder.append("Row ");
            builder.append(row + 1);
            if (row < 9) builder.append(" ");
            builder.append(" ");
            for (int column = 0; column < getColumns(); column++) {
                builder.append(seats[column][row].getBooking());
                if (column == 2) builder.append(' ');
            }
            if (row < 11) builder.append("\r\n");
        }
        return builder.toString();
    }
}
