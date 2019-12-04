package webscraping.coursework;

import java.util.ArrayList;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;

/**
 * Contains main method for application
 */

public class Main {
    public static void main(String[] args) { 
        runThreadsAnnotations();
       
    }
    
    /** Uses Spring Annotation configuration to set up and run application */
    static void runThreadsAnnotations(){ 
               
    //Instruct Spring to create and wire beans using XML file
    ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
    
    // Get sessionFactory
    SessionFactory sessionFactory = (SessionFactory) context.getBean("sessionFactory");
    
    // Get LaptopsDirect bean
    LaptopsDirectScraper laptopsDirect = (LaptopsDirectScraper) context.getBean("laptopsDirectScraper");
    
    // Get EBuyer bean
    EBuyerScraper eBuyer = (EBuyerScraper) context.getBean("eBuyerScraper");
    
    // Get Very bean
    VeryScraper very = (VeryScraper) context.getBean("veryScraper");
    
    // Get LaptopOutlet bean
    LaptopOutletScraper laptopOutlet = (LaptopOutletScraper) context.getBean("laptopOutletScraper");

    // Get Box bean
    BoxScraper box = (BoxScraper) context.getBean("boxScraper");
    
    // Join threads
    try{
            laptopsDirect.join();
            eBuyer.join();
            very.join();
            laptopOutlet.join();
            box.join();
    }
    catch(InterruptedException ex){
            System.out.println("Interrupted exception thrown: " + ex.getMessage());
        }
    } 
}

