/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webscraping.coursework;

import javax.persistence.*;

/**
 *
 * @author linux
 */

@Entity
@Table(name = "laptops")
/** Holds details about laptop */
public class Laptop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    // Laptop id
    private int id;
    
    @Column(name = "url_id")
    // Laptop url ID
    private int urlId;
    
    @Column(name = "products_id")
    // Laptops product ID
    private int productsId;
    
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
    
    public int getUrlId() {
        return urlId;
    }
    
    public int getProductsId() {
        return productsId;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public void setUrlId(int urlId) {
        this.urlId = urlId;
    }
    
    public void setProductsId(int productsId) {
        this.productsId = productsId;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
}
