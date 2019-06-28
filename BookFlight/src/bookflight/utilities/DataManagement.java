/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookflight.utilities;

import bookflight.booking.objects.Airplane;
import bookflight.booking.objects.Customer;
import bookflight.booking.objects.FindCustomer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Handles anything for getting data
 * @author Matthew
 */
public abstract class DataManagement {
    /**
     * Searches for a customer based on their name
     * @param customers the list of customers to sort through
     * @param name the name of the customer that is being searched
     * @return null if there is no customers, Customer if there is a match
     */
    public static int searchCustomer(Customer[] customers, String name) {
        Arrays.sort(customers, new FindCustomer());
        return Arrays.binarySearch(customers, new Customer(name, 0), new FindCustomer());
    }
    
    /**
     * Selects a unique number based on two values (column, and row)
     * @param column The column used to generate a unique number
     * @param row The row used to generate a unique number
     * @return 
     */
    public static long getCantorPair(int column, int row) {
        // Original cantor formulae 0.5(n1n2)(n1n2+1)+n2
        // (the 1000* gives an extra 1000 bytes that can be input)
        return Math.round(1000*(0.5 * (column + row) * (column + row + 1) + row));
    }
}