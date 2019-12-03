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

//@Configuration
public class AppConfig {
    //SessionFactory sessionFactory;
 /*   
    @Bean
    public ScraperManager scraperManager(){
        ScraperManager scraperManager = new ScraperManager();
        
        //Create list of web scrapers and add to scraper manager
        List<WebScraper> scraperList = new ArrayList();
        scraperList.add(scraper1());
        scraperList.add(scraper2());
        scraperManager.setScraperList(scraperList);

        return scraperManager;
    }
    
    @Bean
    public Scraper1 scraper1(){
        Scraper1 scraper1 = new Scraper1();
        scraper1.setHatDao(hatDao());
        scraper1.setScrapeDelay_ms(1000);
        return scraper1;
    }
    
    @Bean
    public Scraper2 scraper2(){
        Scraper2 scraper2 = new Scraper2();
        scraper2.setHatDao(hatDao());
        scraper2.setScrapeDelay_ms(2000);
        return scraper2;
    }
    
    @Bean
    public HatDao hatDao(){
        HatDao hatDao = new HatDao();
        hatDao.setSessionFactory(sessionFactory());
        return hatDao;
    }
    
    @Bean
    public SessionFactory sessionFactory(){
        if(sessionFactory == null){//Build sessionFatory once only
            try {
                //Create a builder for the standard service registry
                StandardServiceRegistryBuilder standardServiceRegistryBuilder = new StandardServiceRegistryBuilder();

                //Load configuration from hibernate configuration file.
                //Here we are using a configuration file that specifies Java annotations.
                standardServiceRegistryBuilder.configure("resources/hibernate.cfg.xml"); 

                //Create the registry that will be used to build the session factory
                StandardServiceRegistry registry = standardServiceRegistryBuilder.build();
                try {
                    //Create the session factory - this is the goal of the init method.
                    sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
                }
                catch (Exception e) {
                        /* The registry would be destroyed by the SessionFactory, 
                            but we had trouble building the SessionFactory, so destroy it manually */
    /*
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
*/
}