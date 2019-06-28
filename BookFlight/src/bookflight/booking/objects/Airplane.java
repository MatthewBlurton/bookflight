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
 * This is the airplane that users can book seats on
 * @author Matthew Blurton
 */
public class Airplane  {
    /**
     * Automatically create this plane with 6 columns and 12 rows
     */
    public Airplane() {
        this(6, 12);
    }
    
    /**
     * Sets up the plain with a specific amount of seats
     * @param columns The amount of seat columns on the plane
     * @param rows The amount of seat rows on the plane
     */
    private Airplane(int columns, int rows) {
        super();
        this.columns = columns;
        this.rows = rows;
        
        // Initialise and create all the seats
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
    
    /**
     * Get the amount of seat columns associated with this plane
     * @return amount of seat columns
     */
    public int getColumns() {
        return columns;
    }

    /**
     * Get the amount of seat rows associated with this plane
     * @return amount of seat rows
     */
    public int getRows() {
        return rows;
    }
    
    /**
     * Get a specific seat on this plane
     * @param column the column that the seat is located in
     * @param row the row that the seat is located in
     * @return the seat associated with the provided column and row
     */
    public Seat getSeat(int column, int row) {
        return seats[column][row];
    }
    
    /**
     * Uses a linear search to find a seat of the preferred type and class
     * @param seatClass Type of seating class the customer would prefer
     * @param seatType The seating type the customer would prefer
     * @return the location of the available seat, null if none are available.
     */
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
    
    /**
     * Assign a specific seat to a customer
     * @param column the column that the seat is located in
     * @param row the row that the seat is located in
     * @param customer the customer to assign
     * @throws IndexOutOfBoundsException occurs when the seat selected is not associate with this plane
     * @throws SeatTakenException occurs when another customer has already booked the plane
     */
    public void assignSeat(int column, int row, Customer customer) throws IndexOutOfBoundsException, SeatTakenException {
        if (seats[column][row].getBookedBy() != null) {
            throw new SeatTakenException();
        }
        seats[column][row].setBookedBy(customer);
        customer.setSeatAlloc(getSeatColumn(column) + row);
    }
    
    /**
     * Cancels the booking set by the customer
     * @param customer the customer that wants to cancel their booking
     * @return if the customer has booked, return the column and row that was booked
     */
    public int[] cancelSeat(Customer customer) {
        // Check if the customer has booked and if so, unbook them from that seat
        int[] columnRow = checkIfCustomerBooked(customer);
        if (columnRow != null) {
            int column = columnRow[0];
            int row = columnRow[1];
            seats[column][row].setBookedBy(null);
            customer.setSeatAlloc("");
        }
        
        return columnRow;
    }
    
    /**
     * Converts columns from numbers to letters
     * @param column the column to convert.
     * @return the letter to return. It can be A, ..F, in ascending order
     */
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
    
    /**
     * Check if the customer has booked a flight
     * @param customer the customer to check for a booking
     * @return the column and row of where the customer booked
     */
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
    
    /**
     * Checks if a customer has made a booking
     * @param customer the customer to check for a booking
     * @return true if the customer has made a booking
     */
    public boolean isBooked(Customer customer) {
        return checkIfCustomerBooked(customer) != null;
    }
    
    /**
     * Show a textual representation of the plane, and all it's bookings.
     * @return String representation of all the bookings
     */
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
