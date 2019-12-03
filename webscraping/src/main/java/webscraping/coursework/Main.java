package webscraping.coursework;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
/**
 *
 * @author linux
 */
public class Main {
    public static void main(String[] args) { 
        LaptopsDirectScraper laptopsDirect = new LaptopsDirectScraper();
        EBuyerScraper eBuyer = new EBuyerScraper();
        VeryScraper very = new VeryScraper();
        LaptopOutletScraper laptopOutlet = new LaptopOutletScraper();
        BoxScraper box = new BoxScraper();
        
        laptopsDirect.start();
        eBuyer.start();
        very.start();
        laptopOutlet.start();
        box.start();
        
        //Wait for threads to finish - join can throw an InterruptedException
        try{
            laptopsDirect.join();
            eBuyer.join();
            very.join();
            laptopOutlet.join();
            box.join();
        }
        catch(InterruptedException ex){
            System.out.println("Interrupted exception thrown: " + ex.getMessage());
        }
        
        System.out.println("Web scraping complete");
        System.out.println("END OF PROGRAM");
        
    }
}
