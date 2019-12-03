/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webscraping.coursework;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author linux
 */
public class LaptopsDirectScraper extends Thread{
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
            System.out.println("Scraping www.laptopsdirect.co.uk laptops...");
            
            //Download HTML document from website
            try{
                Document docForPages = Jsoup.connect("https://www.laptopsdirect.co.uk/ct/laptops-and-netbooks/laptops?pageNumber=1").get();
                
                //Get total amount of productcs
                Element products = docForPages.select(".sr_numresults > b").last();
                int productsAmount = Integer.parseInt(products.text());
                
                //Find total amount of pages (20 products per page)
                int totalPages;
                int productsPerPage = 24;
                
                //If there is a reminder after dividing productsAmount by 20 add 1(page)
                if(productsAmount%24 == 0){
                    totalPages = (productsAmount / productsPerPage);
                } else {
                    totalPages = (productsAmount / productsPerPage) + 1;
                }
                
                for(int page = 1; page <= totalPages; page++){
                    Document doc = Jsoup.connect("https://www.laptopsdirect.co.uk/ct/laptops-and-netbooks/laptops?pageNumber=" + page).get();  
            
                    //Get all of the products on the page
                    Elements prods = doc.select(".OfferBox");
  
                    //Work through the products
                    for(int i=0; i<prods.size(); ++i){
            
                        //Get the product description
                        Elements description = prods.get(i).select(".productInfo");
                    
                        //Get the product price
                        Elements priceClass = prods.get(i).select(".offerprice");
                        String priceString = priceClass.text().substring(1).replace(",","");
                        String[] priceArray = priceString.split("\\s+");
                        String priceArrayString = priceArray[0];
                        double price = Double.parseDouble(priceArrayString);
 
                        //Get the brand
                        Elements brandClass = prods.get(i).select("a.offerboxtitle");
                        String brandA = brandClass.text();
                        String[] brandArray = brandA.split("\\s+");
                        String brand = brandArray[0];
                        
                        // Check for word Refurbished
                        if(brand.contains("Refurbished"))
                            brand = brandArray[1];
                                            
                        //Get the image
                        Elements imageUrlClass = prods.get(i).select(".sr_image");
                        Elements imageUrlA = imageUrlClass.get(0).select(".offerImage");
                        Element imageUrlImg = imageUrlA.get(0).select("img").last();
                        String imageUrl = "https://www.laptopsdirect.co.uk" + imageUrlImg.attr("src");
                    
                        //Get the items url
                        Element itemUrlA = imageUrlClass.get(0).select("a").last();
                        String domain = "https://www.laptopsdirect.co.uk";
                        String productUrl = domain.concat(itemUrlA.attr("href"));
            
                        //Output the data that we have downloaded
                        System.out.println("\n LaptopsDirect description: " + description.text() + 
                                           ";\n LaptopsDirect price: " + price + 
                                           ";\n LaptopsDirect brand: " + brand +
                                           ";\n LaptopsDirect image url: " + imageUrl +
                                           ";\n LaptopsDirect item url: " + productUrl);
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
