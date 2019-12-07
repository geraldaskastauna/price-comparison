/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webscraping.coursework;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author linux
 */

@Entity(name="Laptop")
@Table(name = "laptop")
/** Holds details about laptop */
public class Laptop implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    // Laptop id
    private int id;
    
    @Column(name="url_id")
    private Url url;
    
    @Column(name="product_id")
    private Product product;
    
    @Column(name = "price")
    // Laptops price
    private double price;
           
    /** Empty laptop constructor */
    public Laptop() {
        
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
}
