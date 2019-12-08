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
    
    /** Empty constructor */
    LaptopDao() {
        
    }
   
    public void setSessionFactory(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }
    public SessionFactory getSessionFactory(){
        return sessionFactory;
    }
    
    /** Closes hibernate down and stops its thread from running */
    public void shutDown() {
        // Close caches and connection pools
        getSessionFactory().close();
    }
    
    public Boolean duplicateExist(Session session, String description, String imageUrl) {
    
    //Find matching products in the database
    List<Product> productList = session.createQuery(
        "from Product where description='" + description + 
        "' and image_url='" + imageUrl + "'").getResultList();
    
    //If there is one or more products, the duplicate exists and there is no need to create a new product
    if (productList.size() >= 1) {
        return true;
        
        //Else we return false - no duplicates    
    } else {
        return false;
        }
    }
}
