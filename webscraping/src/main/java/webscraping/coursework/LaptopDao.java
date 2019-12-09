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
    
    /** Closes hibernate down and stops its thread from running */
    public void shutDown() {
        // Close caches and connection pools
        getSessionFactory().close();
    }
}
