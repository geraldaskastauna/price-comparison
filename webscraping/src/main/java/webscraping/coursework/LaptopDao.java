package webscraping.coursework;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author Geraldas Kastauna
 */
public class LaptopDao {
    //Creates new Sessions when we need to interact with the database
    private SessionFactory sessionFactory;
    
    /**
    * Empty LaptopDao constructor 
    */
    LaptopDao() {
        
    }
    
    // Getters and setters
    public void setSessionFactory(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }
    public SessionFactory getSessionFactory(){
        return sessionFactory;
    }
        
    /**
     * Check if the duplicate of the product exists in the database
     *
     * @param name description of the product
     * @param weight weight of the product
     * @return true or false - duplicate exists or not
     */
    public Boolean duplicateExist(String domain, String queryString, Session session) {
        //Find matching products in the database
        List<Url> urlList = session.createQuery("from Url where domain='" + domain + 
                                                "' and query_string='" + queryString + "'")
                                                .getResultList();
        
        //If there is one or more products, the duplicate exists and there is no need to create a new product
        if (urlList.size() >= 1) {
            return true;
            //Else we return false - no duplicates    
        } else {
            return false;
        }
    }
    
    /** Closes hibernate down and stops its thread from running */
    public void shutDown() {
        // Close caches and connection pools
        getSessionFactory().close();
    }
}
