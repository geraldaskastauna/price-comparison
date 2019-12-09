/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webscraping.coursework;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

/**
 *
 * @author linux
 */
public class LaptopDao {
    //Creates new Sessions when we need to interact with the database
    private SessionFactory sessionFactory;
    
    /**
    * Empty LaptopDao constructor 
    */
    LaptopDao() {
        
    }
    
    public void setSessionFactory(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }
    public SessionFactory getSessionFactory(){
        return sessionFactory;
    }
    
    /**
     * Save laptop to database
     *
     * @param product product object to be saved
     * @param laptop laptop object to be saved
     * @param url url object to be saved
     */
    public void saveToDatabase(Product product, Laptop laptop, Url url, Session session) {
        //Start transaction
        session.beginTransaction();

        //Add Product to database
        session.saveOrUpdate(product);
        session.saveOrUpdate(laptop);
        session.saveOrUpdate(url);

        //Commit transaction to save it to database
        session.getTransaction().commit();
    }

     /**
     * Save product to the database
     *
     * @param product product to be saved
     * @param laptop laptop to be saved
     * @param url url to be saved
     */
    
    public void saveProduct(Product product, Laptop laptop, Url url) {
        //Get a new Session instance from the session factory and start transaction
        Session session = sessionFactory.getCurrentSession();
        
        //Start transaction
        session.beginTransaction();
        
        //Save the product to the database
        session.save(product);
        session.save(laptop);
        session.save(url);
        
        //Commit transaction
        session.getTransaction().commit();
    }
    
        /**
     * Get the retailer object from the database
     *
     * @param retailerStr name of the retailer
     * @return the matching Retailer object
     */
    public Url getUrl(Url url) {
        //Get a new Session instance from the session factory and start transaction
        Session session = sessionFactory.getCurrentSession();
        
        //Start transaction
        session.beginTransaction();
        
        //Get a list of retailers with the same name
        List<Url> urlList = session.createQuery("from Url where query_string='" + url.getQueryString() + "'").getResultList();
        Url urlObject;
        if (urlList.size() != 1) //Should be a single class if query is successful
        {
            System.out.println("More than one class: " + urlList.size());
        }
        //Get the retailer object
        urlObject = urlList.get(0);
        
        //Comit the transaction
        session.getTransaction().commit();
        
        session.close();
        return urlObject;
    }
    
        /**
     * Check if the duplicate of the product exists in the database
     *
     * @param name description of the product
     * @param weight weight of the product
     * @return true or false - duplicate exists or not
     */
    public Boolean duplicateExist(String query_string, String description, Session session) {
        session.beginTransaction();
        //Find matching products in the database
        
        List<Product> productList = session.createQuery("from Product where description='" + description + "'").getResultList();
        List<Url> urlList = session.createQuery("from Url where query_string = '" + query_string + "'").getResultList();
        session.getTransaction().commit();
        //If there is one or more products, the duplicate exists and there is no need to create a new product
        if (productList.size() >= 1 && urlList.size() >= 1) {
            return true;
            //Else we return false - no duplicates    
        } else {
            return false;
        }
    }
    
     /**
     * Search for product on the database
     *
     * @param name product name
     * @param weight product weight
     * @param img product image
     * @return matching product
     */
    public Product searchForProduct(String brand, String description, String imgUrl) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        //Look for matching products
        List<Product> productList = session.createQuery("from Product where description='" + description + 
                                                        "' and brand='" + brand + 
                                                        "' and img_url='" + imgUrl).getResultList();
        
        //Alert if there's a duplicate
        if (productList.size() >= 1) {
            System.out.println("***ATTENTION****DUPLICATE DUPLICATE DUPLICATE");
        }
        //Get the product
        Product product = productList.get(0);
        //If the image is not defined, overwrite it with the new data
        if ("".equals(product.getImageUrl())) {
            product.setImageUrl(imgUrl);
        }
        
        //Commit transaction
        session.getTransaction().commit();
        
        session.close();
        return product;
    }
    
    
    /**
     * Update the prices of the product
     *
     * @param price price object
     */
    public void updatePrices(Laptop price) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        //Get a list of prices for the product to which @param price is linked
        List<Laptop> laptopList = session.createQuery("from Laptop where price='" + price + "'").getResultList();
        //Keep a track of whether the prices were updated or not
        Boolean updated = false;
        //Itterate through all the appropriate prices
        for (Laptop p : laptopList) {
            //If the @param price and the price in the list have matching retailers, update the price
            if (p.getPrice() == price.getPrice()) {
                p.setPrice(price.getPrice());
                session.update(p);
                updated = true;
                continue;
            }
    }
        //If there are no prices with the matching retailers, insert a new price
        if (!updated) 
            session.save(price);

        //Comit the transaction
        session.getTransaction().commit();
        
        session.close();
    }
    
    
     public void deleteLaptop(Laptop laptop, Url url, Product product, Session session) {
        session.beginTransaction();
        session.delete(laptop);
        session.delete(url);
        session.delete(product);
        session.getTransaction().commit();
    }
     
    /** Closes hibernate down and stops its thread from running */
    public void shutDown() {
        // Close caches and connection pools
        getSessionFactory().close();
    }
}
