/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webscraping.coursework;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author linux
 */
public class WebScraper extends Thread{
    BoxScraper box = new BoxScraper();
    EBuyerScraper eBuyer = new EBuyerScraper();
    LaptopOutletScraper laptopOutlet = new LaptopOutletScraper();
    LaptopsDirectScraper laptopsDirect = new LaptopsDirectScraper();
    VeryScraper very = new VeryScraper();
    Thread thread;
    
    WebScraper(Thread thread){
        this.thread = thread;
    }
    
    
}
