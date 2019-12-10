package webscraping.coursework;

import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Geraldas Kastauna
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
        for(Thread scraper : scrapingThreads){
            scraper.start();
        }
    }
}
