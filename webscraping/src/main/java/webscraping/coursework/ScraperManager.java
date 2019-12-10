package webscraping.coursework;

import java.util.List;

/**
 *
 * @author Geraldas Kastauna
 */

/**
 * 
 * ScraperManager class that hold and starts all scrapers
 */
public class ScraperManager {
    // Empty thread list
    List<Thread> scrapingThreads; // no initialization here: it's done in the constructor

    /**
     * Empty constructor
     */
    ScraperManager() {
    }
    
    // Method to get scraper list
    public List<Thread> getScraperList(){
        return scrapingThreads;
    }
    
    // Method to set scraper list
    public void setScraperList(List<Thread> scrapingThreads){
        this.scrapingThreads = scrapingThreads;
    }
    
    // Method to start the threads
    public void startScraping() {
        scrapingThreads.get(3).start();
        /*for(Thread scraper : scrapingThreads){
            scraper.start();
        } */
    }
}
