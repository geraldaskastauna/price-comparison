/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webscraping.coursework;

import java.util.List;

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
    public void startThreads() {
        for(int i = 0; i < scrapingThreads.size(); i++){
            scrapingThreads.get(i).start();
        }
        try{
            for(int i = 0; i < scrapingThreads.size();i++){
                scrapingThreads.get(i).join();
            } 
        }   catch(InterruptedException ex){
            System.out.println("Interrupted exception thrown: " + ex.getMessage());
        }
    }
}
