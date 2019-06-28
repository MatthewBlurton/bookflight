/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookflight.booking.objects;

import java.util.Comparator;

/**
 * Used to compare the names of customers
 * @author mjblu
 */
public class FindCustomer implements Comparator<Customer>{
    @Override
    public int compare(Customer c1, Customer c2) {
        return c1.getFirstName().compareToIgnoreCase(c2.getFirstName());
    }
}
