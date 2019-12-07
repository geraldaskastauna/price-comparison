/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webscraping.coursework;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.*;

/**
 *
 * @author linux
 */

@Entity
@Table(name = "laptop")
/** Holds details about laptop */
public class Laptop implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    // Laptop id
    private int id;
    
    @Column(name = "price")
    // Laptops price
    private double price;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "url_id", referencedColumnName = "id")
    private Url url;
    
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
