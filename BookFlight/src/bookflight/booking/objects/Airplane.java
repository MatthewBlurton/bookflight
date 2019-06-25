/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookflight.booking.objects;

import bookflight.booking.SeatClass;
import bookflight.booking.SeatType;
import bookflight.booking.exceptions.SeatTakenException;

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
    
    public int[] findPreferredSeat(SeatClass seatClass, SeatType seatType) {
        for (int col = 0; col < columns; col++) {
            for (int row = 0; row < rows; row++) {
                if (seats[col][row].getSeatClass() != seatClass
                        || seats[col][row].getSeatType() != seatType) {
                    continue;
                }
                
                if (seats[col][row].getBookedBy() == null) {
                    return new int[] {col, row};
                }
            }
        }
        
        return null;
    }
    
    public void assignSeat(int column, int row, Customer customer) throws IndexOutOfBoundsException, SeatTakenException {
        if (seats[column][row].getBookedBy() != null) {
            throw new SeatTakenException();
        }
        seats[column][row].setBookedBy(customer);
        customer.setSeatAlloc(getSeatColumn(column) + row);
    }
    
    public void cancelSeat(Customer customer) {
        int[] ColumnRow = checkIfCustomerBooked(customer);
        if (ColumnRow != null) {
            int column = ColumnRow[0];
            int row = ColumnRow[1];
            seats[column][row].setBookedBy(null);
            customer.setSeatAlloc("");
        }
    }
    
    private String getSeatColumn(int column) {
        switch (column) {
            case 0:
                return "A";
            case 1:
                return "B";
            case 2:
                return "C";
            case 3:
                return "D";
                
            case 4:
                return "E";
                
            case 5:
                return "F";
            default:
                return "";
        }
    }
    
    private int[] checkIfCustomerBooked (Customer customer) {
        for (int column = 0; column < columns; column++) {
            for (int row = 0; row < rows; row++) {
                if (seats[column][row].getBookedBy() != null
                        && seats[column][row].getBookedBy().equals(customer)) {
                    return new int[] {column, row};
                }
            }
        }
        return null;
    }
    
    public boolean isBooked(Customer customer) {
        return checkIfCustomerBooked(customer) != null;
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
