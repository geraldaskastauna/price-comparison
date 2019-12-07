/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webscraping.coursework;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class ScraperManagerTest {

    private final Laptop laptop = new Laptop();
    
    @Test
    void addition() {
        laptop.setPrice(2.0);
        
        assertEquals(2,laptop.getPrice());
        System.out.println("TEST PASSED");
    }

}