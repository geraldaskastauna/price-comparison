package webscraping.coursework;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cascade;


@Entity(name="Product")
@Table(name = "product")
/** Holds details about product */
public class Product implements Serializable {
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
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Laptop laptop;
    
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
