/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webscraping.coursework;

import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author linux
 */
public class ScraperManager {
    List<Thread> scrapingThreads; // no initialization here: it's done in the constructor

    // Empty constructor
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
        
        //Wait until the user types stop
        Scanner scanner = new Scanner(System.in);
        String userInput = scanner.nextLine();
        while (!userInput.equals("stop")) {
            userInput = scanner.nextLine();
        }
 /*       
        //Wait for web scrapers to stop
        for (Thread scraper : scrapingThreads) {
            try {
                scraper.
                scraper.join();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        System.out.println("Scraping terminated");
*/
        }
    }
