/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webscraping.coursework;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author linux
 */
public class ScraperManager {
    List<WebScraper> scrapingThreads = new ArrayList(); // no initialization here: it's done in the constructor

    // Empty constructor
    ScraperManager() {
    }
    
    public void setScraperList(List<WebScraper> scrapingThreads){
        this.scrapingThreads = scrapingThreads;
    }
}
