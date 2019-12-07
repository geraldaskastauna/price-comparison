/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webscraping.coursework;

import javax.transaction.Transactional;
import org.hibernate.SessionFactory;

@Transactional
public class Hibernate {
    //Creates new Sessions when we need to interact with the database
    private SessionFactory sessionFactory;
    
    /** Empty constructor */
    Hibernate() {
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
}
