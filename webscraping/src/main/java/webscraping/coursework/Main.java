package webscraping.coursework;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
/**
 * @author Geraldas Kastauna
 */

/**
 *
 * Contains main method for application
 */
public class Main {
    public static void main(String[] args) { 
        runThreadsAnnotations();
    }
    
    /** 
     *
     * Uses Spring Annotation configuration to set up and run application 
     */
    static void runThreadsAnnotations(){ 
    //Instruct Spring to create and wire beans using XML file
    ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
    
    //Get scraperManager bean which contains all scrapers
    ScraperManager scraperManager = (ScraperManager) context.getBean("scraperManager");
    
    // Start and join scrapers
    scraperManager.startScraping();
    }
}