package webscraping.coursework;

import java.util.ArrayList;
import java.util.Scanner;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;

/**
 * Contains main method for application
 */

public class Main {
    //private static SessionFactory sessionFactory;
    public static void main(String[] args) { 
        runThreadsAnnotations();
        /*
        //Create a new instance of the HibernateExample class
        HibernateAnnotationExample hibernateAnnotationExample = new HibernateAnnotationExample();
        
        //Set up the SessionFactory
        hibernateAnnotationExample.init();
        
        //Example operations
        hibernateAnnotationExample.addProduct();
        
        //Shut down Hibernate
        hibernateAnnotationExample.shutDown(); */
        
    }
    
    /** Uses Spring Annotation configuration to set up and run application */
    static void runThreadsAnnotations(){ 
               
    //Instruct Spring to create and wire beans using XML file
    ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
    
    //Get scraperManager bean which contains all scrapers
    ScraperManager scraperManager = (ScraperManager) context.getBean("scraperManager");
    
    // Start and join scrapers
    scraperManager.startThreads();
    scraperManager.joinThreads();
    }
}