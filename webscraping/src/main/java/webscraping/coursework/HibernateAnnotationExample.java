/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webscraping.coursework;

//Hibernate imports
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;


/** Simple Hibernate example that uses annotation to specify the mapping between 
 *  a Cereal object and the cereals table in the price_comparison database. */
public class HibernateAnnotationExample {
    //Creates new Sessions when we need to interact with the database 
    private static SessionFactory sessionFactory;
    
    /** Empty constructor */
    HibernateAnnotationExample() {
    }

    
    /** Sets up the session factory.
     *  Call this method first.  */
    public SessionFactory init(){
        try {
            //Create a builder for the standard service registry
            StandardServiceRegistryBuilder standardServiceRegistryBuilder = new StandardServiceRegistryBuilder();

            //Load configuration from hibernate configuration file.
            //Here we are using a configuration file that specifies Java annotations.
            standardServiceRegistryBuilder.configure("hibernate.cfg.xml"); 

            //Create the registry that will be used to build the session factory
            StandardServiceRegistry registry = standardServiceRegistryBuilder.build();
            try {
                //Create the session factory - this is the goal of the init method.
                sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
            }
            catch (Exception e) {
                    /* The registry would be destroyed by the SessionFactory, 
                        but we had trouble building the SessionFactory, so destroy it manually */
                    System.err.println("Session Factory build failed.");
                    e.printStackTrace();
                    StandardServiceRegistryBuilder.destroy( registry );
            }

            //Ouput result
            System.out.println("Session factory built.");

        }
        catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("SessionFactory creation failed." + ex);
        }
        return sessionFactory;
    }
    
        /** Closes hibernate down and stops its thread from running */
    public void shutDown() {
        // Close caches and connection pools
        getSessionFactory().close();
    }
    
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    /** Adds a new cereal to the database */
    public void addProduct(){
        //Get a new Session instance from the session factory
        Session session = sessionFactory.getCurrentSession();

        //Create an instance of a Cereal class 
        Product product = new Product();

        //Set values of Cereal class that we want to add
        product.setBrand("GERAsasdasd");
        product.setDescription("GERAS LAPTOPASasd");
        product.setImageUrl("currys.example.co.uk");

        //Start transaction
        session.beginTransaction();

        //Add Cereal to database - will not be stored until we commit the transaction
        session.save(product);

        //Commit transaction to save it to database
        session.getTransaction().commit();
        
        //Close the session and release database connection
        session.close();
        System.out.println("Product added to database with ID: " + product.getId());
    }
}