package webscraping.coursework;

import javax.persistence.*;

@Entity
@Table(name = "products")
/** Holds details about product */
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    // Product ID
    private int id;
    
    @Column(name = "brand")
    // Product brand
    private String brand;
    
    @Column(name = "description")
    // Product description
    private String description;
    
    @Column(name = "image_url")
    // Laptops image url
    private String imageUrl;
    
    /** Empty constructor */
    public Product(){

    }
    
    // Getters and setters
    public int getId() {
        return id;
    }
    
    public String getBrand() {
        return brand;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public void setBrand(String brand) {
        this.brand = brand;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}