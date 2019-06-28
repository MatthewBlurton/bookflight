/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookflight.booking.objects;

import java.util.Comparator;

/**
 * Used to compare the names of customers, useful for sorting and binary searches
 * @author Matthew Blurton
 */
public class FindCustomer implements Comparator<Customer>{
    /**
     * Returns an integer depending on whether the first name is higher or lower
     * than the second first name
     * @param c1 Customer 1, the source of the comparison
     * @param c2 Customer 2, the target of comparison
     * @return 0 = match, below 0 = c1 lower than c2, above 0 = c1 higher thatn c2
     */
    @Override
    public int compare(Customer c1, Customer c2) {
        return c1.getFirstName().compareToIgnoreCase(c2.getFirstName());
    }
}
