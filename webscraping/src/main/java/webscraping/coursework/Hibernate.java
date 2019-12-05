/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webscraping.coursework;

import javax.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

@Transactional
public class Hibernate {
    //Creates new Sessions when we need to interact with the database
    private SessionFactory sessionFactory = null;
    
    /** Empty constructor */
    Hibernate() {
    }
   
    public SessionFactory setSessionFactory(SessionFactory sessionFactory){
        this.sessionFactory = null;
        return sessionFactory;
    }
    public SessionFactory getSessionFactory(){
        return sessionFactory;
    }
    
    /** Closes hibernate down and stops its thread from running */
    public void shutDown() {
        // Close caches and connection pools
        getSessionFactory().close();
    }
 
    
     /** Adds a new university to the database */
    public void addLaptop(){
        //Get a new Session instance from the session factory
        Session session = sessionFactory.getCurrentSession();

        //Create an instance of a University class
        Laptop laptop = new Laptop();
        Product product = new Product();
        Url url = new Url();

        //Set values of University class that we want to add
        laptop.setPrice(12312);
        laptop.setProductsId(5);
        laptop.setUrlId(5);

        //Start transaction
        session.beginTransaction();

        //Add university to database - will not be stored until we commit the transaction
        session.save(laptop);

        //Commit transaction to save it to database
        session.getTransaction().commit();

        //Close the session and release database connection
        session.close();
        System.out.println("University added to database with ID: " + laptop.getId());
    }
}
