/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webscraping.coursework;

import java.io.IOException;
import static java.lang.Thread.sleep;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author linux
 */
public class EBuyerScraper extends Thread{
        //Specifies the interval between HTTP requests to the server in seconds.
        private int crawlDelay = 5;
    
        //Allows us to shut down our application cleanly
        volatile private boolean runThread = false;
        
        // Create objects to store info from website
        Product product = new Product();
        Laptop laptop = new Laptop();
        Url url = new Url();
        
        public void run() {
            runThread = true;
            System.out.println("Scraping www.ebuyer.com laptops...");
            
            //Download HTML document from website
            try{
                int startingPage = 1;
                int totalPages = 15;
                
                //HTML document from website to get the amount of pages
                Document docForPages = Jsoup.connect("https://www.ebuyer.com/store/Computer/cat/Laptops?page=" + startingPage).get();
                
             
                for(int pages = 1; pages <= 15; pages++){
                    //HTML document from website
                    Document doc = Jsoup.connect("https://www.ebuyer.com/store/Computer/cat/Laptops?page=" + pages).get();

                    //Get all of the products on the page
                    Elements prods = doc.select("div.grid-item");
                
                    //Work through the products
                    for(int i=0; i<prods.size(); ++i){

                        //Get the product description
                        Elements description = prods.get(i).select(".grid-item__ksp");
                    
                        //Get the product price
                        Elements priceClass = prods.get(i).select("p.price");
                       
                        String outOfStock = "Temporarily out of stock";
                        double price;
                        if(priceClass.text().isEmpty()){
                            //Out of stock
                            price = 0;
                        } else {
                            //In stock
                            String priceString = priceClass.text().substring(1).replace(",","");
                            String[] priceArray = priceString.split("\\s+");
                            String priceArrayString = priceArray[1];
                            price = Double.parseDouble(priceArrayString);
                        }
                        
                        //Get laptops brand
                        Elements brandClass = prods.get(i).select("h3.grid-item__title");
                        Elements brandA = brandClass.get(0).select("a");
                        String brand = brandA.text();
                        String[] brandArray = brand.split("\\s+");
                        brand = brandArray[0];
                    
                        //Get the image
                        Elements imageUrlDiv = prods.get(i).select("div.grid-item__img");
                        Elements imageUrlA = imageUrlDiv.get(0).select("a");
                        Element imageUrlAClass = imageUrlA.get(0).select("img").last();
                        String imageUrl = imageUrlAClass.attr("src");
                    
                        //Get the items url
                        Element productUrlA = brandClass.get(0).select("a").first();
                        String productUrlHref = productUrlA.attr("href");
                        String domain = "https://www.ebuyer.com";
                        String productUrl = domain.concat(productUrlHref);
            
                        //Output the data that we have downloaded
                        System.out.println("\n ebuyer.com description: " + description.text() + 
                                           ";\n ebuyer.com price: " + (price == 0 ? outOfStock : price) + 
                                           ";\n ebuyer.com brand: " + brand +
                                           ";\n ebuyer.com image url: " + imageUrl +
                                           ";\n ebuyer.com product url: " + productUrl);
                    }
                }
                sleep(1000 * crawlDelay);
            }   catch (IOException ex) {
                    System.out.println("Error while accessing the website");
            }   catch(InterruptedException ex){
                    System.err.println(ex.getMessage());
            }
        }
        // Other threads can stop this thread
        public void stopThread(){
            runThread = false;
        }
}
