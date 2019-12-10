/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webscraping.coursework;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

class ScrapingTest {
    
    @BeforeAll
    static void initAll() {
    }

    @BeforeEach
    void init() {
    }
    
    @Test
    @DisplayName("Test setting classes elements")
    void settingProductElements() {
        // Instance of classes that we want to test
        Laptop laptop = new Laptop();
        Product product = new Product();
        Url url = new Url();
        
        try{
            //Setting class elements to random values
            url.setDomain("https://test.test.test");
            url.setQueryString("/testing_query_string");
            product.setBrand("Test brand");
            product.setDescription("Test description");
            product.setImageUrl("https://test.image.url");
            laptop.setProduct(product);
            laptop.setUrl(url);
            laptop.setPrice(2.0);
        } catch(Exception ex) {
            fail("Failed to set the elements. Exception thrown: " + ex.getMessage());
        }
        
        // Check if the value in the class match values that was set in this method
        assertEquals(2,laptop.getPrice());
        assertEquals(url,laptop.getUrlObj());
        assertEquals(product,laptop.getProductObj());
    }
    
    @Test
    @DisplayName("Test sessionFactory method from LaptopDao class")
    void sessionFactoryMethod() {
        // Instance of a class that we want to test
        LaptopDao laptopDao = new LaptopDao();
        AppConfig app = new AppConfig();
        
        try{
            // Create a sessionFactory
            laptopDao.setSessionFactory(app.sessionFactory());
        } catch(Exception ex) {
            fail("Failed setting sessionFactory" + ex.getMessage());
        }
        
        // Check if sessionFactory is not null
        assertNotNull(laptopDao.getSessionFactory());
    }
    
    @Test
    @DisplayName("Test setting threads in ScraperManager class")
    void scraperManagerList(){
        // Instance of the class we want to test
        ScraperManager manager = new ScraperManager();
        
        // Creating 3 new threads
        Thread scraper1 = new Thread();
        Thread scraper2 = new Thread();
        Thread scraper3 = new Thread();
        
        // Empty list to store threads
        List<Thread> testList = new ArrayList();
        
        // Adding threads to empty list
        testList.add(scraper1);
        testList.add(scraper2);
        testList.add(scraper3);
        
        try{
            // Adding list to our ScraperManager
            manager.setScraperList(testList);
        } catch(Exception ex){
            fail("Failed to set the scraperList in ScaperManager class" + ex.getMessage());
        }
        
        // Chech if the testList matches ScraperManagers list
        assertEquals(testList, manager.getScraperList());
    }
    
    @Test
    @DisplayName("Test if threads are running")        
    void scrapingThreads(){
        // Instance of threads that we want to test (All of them)
        BoxScraper box = new BoxScraper();
        LaptopOutletScraper laptopOutlet = new LaptopOutletScraper();
        LaptopsDirectScraper laptopsDirect = new LaptopsDirectScraper();
        VeryScraper very = new VeryScraper();
        EBuyerScraper eBuyer = new EBuyerScraper();
        
        // Empty list to store threads
        List<Thread> threadList = new ArrayList();
        
        // Add threads to the list
        threadList.add(box);
        threadList.add(very);
        threadList.add(laptopsDirect);
        threadList.add(laptopOutlet);
        threadList.add(eBuyer);
        
        try{
            // Start threads
            for(Thread thread : threadList)
                thread.start();
        } catch(Exception ex) {
            fail("Failed to start the threads" + ex.getMessage());
        }
        
        // Test to see if threads are running
        for(Thread test : threadList)
            assertEquals(true,test.isAlive());
    }
    
    @Test
    @DisplayName("Test scraperManager bean from AppConfig class")
    void test() {
        //Instruct Spring to create and wire beans using XML file
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        //Get scraperManager bean which contains all scrapers
        ScraperManager scraperManager = (ScraperManager) context.getBean("scraperManager");
        
        assertNotNull(scraperManager.getScraperList());
    }
        
    @AfterEach
    void tearDown() {
    }

    @AfterAll
    static void tearDownAll() {
    }
}