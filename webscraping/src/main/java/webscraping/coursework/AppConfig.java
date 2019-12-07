/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webscraping.coursework;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AppConfig class that holds SPRING beans.
 */
@Configuration
public class AppConfig {
    // Create sessionFactory with null value
    SessionFactory sessionFactory;

    @Bean
    public ScraperManager scraperManager(){
    ScraperManager scraperManager = new ScraperManager();
        
    //Create list of web scrapers and add to scraper manager
    List<Thread> scraperList = new ArrayList();
        
    scraperList.add(boxScraper());
    scraperList.add(eBuyerScraper());
    scraperList.add(laptopOutletScraper());
    scraperList.add(laptopsDirectScraper());
    scraperList.add(veryScraper());
    
    scraperManager.setScraperList(scraperList);
    
    return scraperManager;
    }
    
    @Bean
    public BoxScraper boxScraper(){
        BoxScraper box = new BoxScraper();
        box.setHibernate(hibernate());
        return box;
    }
    
    @Bean
    public EBuyerScraper eBuyerScraper(){
        EBuyerScraper eBuyer = new EBuyerScraper();
        eBuyer.setHibernate(hibernate());
        return eBuyer;
    }
    
    @Bean
    public LaptopOutletScraper laptopOutletScraper(){
        LaptopOutletScraper laptopOutlet = new LaptopOutletScraper();
        laptopOutlet.setHibernate(hibernate());
        return laptopOutlet;
    }
    
    @Bean
    public LaptopsDirectScraper laptopsDirectScraper(){
        LaptopsDirectScraper laptopsDirect = new LaptopsDirectScraper();
        laptopsDirect.setHibernate(hibernate());
        return laptopsDirect;
    }
    
    @Bean
    public VeryScraper veryScraper(){
        VeryScraper very = new VeryScraper();
        very.setHibernate(hibernate());
        return very;
    }
    
    @Bean
    public Hibernate hibernate(){
        Hibernate hibernate = new Hibernate();
        hibernate.setSessionFactory(sessionFactory());
        return hibernate;
    }
    
    @Bean
    public SessionFactory sessionFactory(){
        if(sessionFactory == null){
            //Build sessionFatory once only
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
                ex.printStackTrace();
            }
        }
        return sessionFactory;
    }
}

