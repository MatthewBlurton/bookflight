/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookflight.utilities;

import bookflight.booking.objects.Customer;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;

/**
 *
 * @author Matthew Blurton
 */
public abstract class FileManagement {
    /**
     * Using the cantor formulae, Write the id of a customer directly to a specific location in a file.
     * Supports customer id's from 0 to 9999
     * @param path The location and name of the file
     * @param column The column to be written to
     * @param row The row to be written to
     * @param customerID The unique id from the customer
     * @exception IOException is thrown when writing to file is unsuccessful
     */
    public static void writeSeatToFile(String path, int column, int row, int customerID) throws IOException {
        RandomAccessFile file = null;
        
        IOException exStore = null;
        
        try {
            file = new RandomAccessFile(path, "rw");
            // Use cantor mapping to get a unique location to store the customer id
            long cantor = DataManagement.getCantorPair(column, row);
            file.seek(cantor);

            // Write the customerID to the unique position
            file.writeInt(customerID);
        } catch (IOException ex) {
            exStore = ex;
        } finally {// Do cleanup
            if (file != null) {
                try {
                    file.close();
                } catch (IOException ex) {}
            }
        }
        
        // If there was an exception thrown during writing, it is a good idea to let the caller know
        if (exStore != null) throw exStore;
    }
    
    /**
     * Using the cantor formulae, reads a unique seat location in the file
     * @param path The location and name of the file
     * @param column The column that was set for the seat
     * @param row The row that was set for the seat
     * @return The id of the customer being returned. -1 if error
     */
    public static int readSeatFromFile(String path, int column, int row) {
        RandomAccessFile file = null;
        int result = -1;
        try {
            file = new RandomAccessFile(path, "r");
                // Use cantor mapping to find the desired int
                long cantor = DataManagement.getCantorPair(column, row);
        file.seek(cantor);
        } catch (IOException ex) {
            // Expected (occurs when reading parts of the file that don't exist)
        } finally {
            if (file != null) {
                try {
                    // Get the id from the file and return the result
                    result = file.readInt();
                } catch (IOException ex) {}// expected
            }
        }
        
        return result;
    }
    
    /**
     * Write a set of customers to file
     * @param path The location and name of the file
     * @param customers The customers going to be written to file
     */
    public static void writePassengersToFile(String path, Customer[] customers) {
        FileOutputStream fout = null;
        ObjectOutputStream oos = null;
        
        try {
            fout = new FileOutputStream(path);
            oos = new ObjectOutputStream(fout);
            oos.writeObject(customers);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException ex) {
                    System.err.println(ex.getMessage());
                }
            }
            
            if (fout != null) {
                try {
                    fout.close();
                } catch (IOException ex) {
                    System.err.println(ex.getMessage());
                }
            }
        }
    }
    
    /**
     * Attempt to read all customers from file
     * @param path the directory and name of the file
     * @return a collection of customers from file (null if failed)
     */
    public static Customer[] readCustomersFromFile(String path) {
        FileInputStream fis = null;
        ObjectInputStream iis = null;
        Customer[] customers = null;
        try {
            fis = new FileInputStream(path);
            iis = new ObjectInputStream(fis);
            customers = (Customer[]) iis.readObject();
        } catch (IOException ex) {
        } catch (ClassNotFoundException ex) {}
        return customers;
    }
}
