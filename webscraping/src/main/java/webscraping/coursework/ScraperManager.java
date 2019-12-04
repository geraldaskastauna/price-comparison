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
    List<Thread> scrapingThreads; // no initialization here: it's done in the constructor

    // Empty constructor
    ScraperManager() {
    }
    
    public void setScraperList(List<Thread> scrapingThreads){
        this.scrapingThreads = scrapingThreads;
    }
    
    public void startThreads() {
        for(int i = 0; i < scrapingThreads.size(); i++){
            scrapingThreads.get(i).start();
        }
    }
    
    public void joinThreads() {
    // Join threads
    try{
        for(int i = 0; i < scrapingThreads.size();i++){
            scrapingThreads.get(i).join();
        } 
    }   catch(InterruptedException ex){
            System.out.println("Interrupted exception thrown: " + ex.getMessage());
        }
    }
    
    public void stopThread() {
       Scanner scanner = new Scanner(System.in);
       String input = scanner.nextLine();
       while(!input.equals("stop")){
           input = scanner.nextLine();
       }
       
    }
}
