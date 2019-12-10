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
 *
 * @author Geraldas Kastauna
 */

/**
 * 
 * AppConfig class that holds SPRING beans.
 */
@Configuration
public class AppConfig {
    // Create sessionFactory with null value
    SessionFactory sessionFactory = null;

    /**
    * Scraper Manager Bean
    *
    * @return an instance of ScraperManager with a defined list of Web Scrapers
    */
    @Bean
    public ScraperManager scraperManager(){
    // Initialize ScraperManager object
    ScraperManager scraperManager = new ScraperManager();
        
    //Create list of web scrapers and add to scraper manager
    List<Thread> scraperList = new ArrayList();
    
    //Add scraper objects to scrapers list
    scraperList.add(boxScraper());
    scraperList.add(eBuyerScraper());
    scraperList.add(laptopOutletScraper());
    scraperList.add(laptopsDirectScraper());
    scraperList.add(veryScraper());
    
    // Send full scrapers list to scraperManager
    scraperManager.setScraperList(scraperList);
    
    // Return scraperManager object
    return scraperManager;
    }
    
    /**
    * laptopoutlet.co.uk Scraper bean
    *
    * @return an instance of laptopoutlet.co.uk scraper object with set Laptop DAO
    */
    @Bean
    public LaptopOutletScraper laptopOutletScraper(){
        LaptopOutletScraper laptopOutlet = new LaptopOutletScraper();
        laptopOutlet.setLaptopDao(laptopDao());
        return laptopOutlet;
    }
    
    /**
    * ebuyer.co.uk Scraper bean
    *
    * @return an instance of ebuyer.co.uk scraper object with set Laptop DAO
    */
    @Bean
    public EBuyerScraper eBuyerScraper(){
        EBuyerScraper eBuyer = new EBuyerScraper();
        eBuyer.setLaptopDao(laptopDao());
        return eBuyer;
    }
    
    /**
    * laptopsdirect.co.uk Scraper bean
    *
    * @return an instance of laptopsdirect.co.uk scraper object with set Laptop DAO
    */
    @Bean
    public LaptopsDirectScraper laptopsDirectScraper(){
        LaptopsDirectScraper laptopsDirect = new LaptopsDirectScraper();
        laptopsDirect.setLaptopDao(laptopDao());
        return laptopsDirect;
    }
    /**
    * very.co.uk Scraper bean
    *
    * @return an instance of very.co.uk scraper object with set Laptop DAO
    */
    @Bean
    public VeryScraper veryScraper(){
        VeryScraper very = new VeryScraper();
        very.setLaptopDao(laptopDao());
        return very;
    }
    
    /**
    * box.co.uk Scraper bean
    *
    * @return an instance of box.co.uk scraper object with set Laptop DAO
    */
    @Bean
    public BoxScraper boxScraper(){
        BoxScraper box = new BoxScraper();
        box.setLaptopDao(laptopDao());
        return box;
    }
    
    /**
    * Laptop DAO bean
    *
    * @return an instance of LaptopDao with session factory set.
    */
    @Bean
    public LaptopDao laptopDao(){
        LaptopDao laptopDao = new LaptopDao();
        laptopDao.setSessionFactory(sessionFactory());
        return laptopDao;
    }
        
    /**
    * Session Factory bean
    *
    * @return instance of session factory.
    */
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

