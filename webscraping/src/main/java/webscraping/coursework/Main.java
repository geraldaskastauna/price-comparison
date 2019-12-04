package webscraping.coursework;

import java.util.ArrayList;
import java.util.Scanner;
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
    
    ScraperManager scraperManager = (ScraperManager) context.getBean("scraperManager");
    
    scraperManager.startThreads();
    scraperManager.joinThreads();
    }
}