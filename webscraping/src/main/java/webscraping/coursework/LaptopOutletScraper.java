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
public class LaptopOutletScraper extends Thread{
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
            System.out.println("Scraping www.laptopoutlet.co.uk laptops...");
            
            try{
                // Scrape through 30 pages
                for(int pages = 1; pages <= 30; pages++){
                    
                    //Download HTML document from website
                    Document doc = Jsoup.connect("https://www.laptopoutlet.co.uk/laptops-and-notebooks.html?p=" + pages).get();
                    
                    //Get all of the products on the page
                    Elements prods = doc.select(".item");
                    
                    //Work through the products
                    for(int i=0; i<prods.size(); ++i){
                        
                        //Get the product description
                        Elements descriptionClass = prods.get(i).select(".product-name");
                        String description = descriptionClass.text();
                        product.setDescription(description);
                    
                        //Get the product price
                        Elements finalPrice = prods.get(i).select("div.price-box");
                        Element priceClass = finalPrice.get(0).select("span.price").first();
                        String priceString = priceClass.text().substring(1).replace(",","");
                        String[] priceArray = priceString.split("\\s+");
                        String priceArrayString = priceArray[0];
                        double price = Double.parseDouble(priceArrayString);
                        laptop.setPrice(price);
                        
                        //Get the brand
                        Elements brandA = descriptionClass.get(0).select("a");
                        String brand = brandA.text();
                        String[] brandArray = brand.split("\\s+");
                        brand = brandArray[0];
                        product.setBrand(brand);
                        
                        //Get image url
                        Elements imageUrlClass = prods.get(i).select(".product-image-wrap");
                        Elements imageUrlA = imageUrlClass.get(0).select("a");
                        Element imageUrlAClass = imageUrlA.get(0).select("img").last();
                        String imageUrl = imageUrlAClass.attr("src");
                        product.setImageUrl(imageUrl);
                        
                        //Get product url
                        Element productUrlHref = imageUrlA.get(0).select("a").first();
                        String productUrl = productUrlHref.attr("href");
                        String domain = "https://www.laptopoutlet.co.uk/";
                        url.setDomain(domain);
                        url.setPath(productUrl.replace("https://www.laptopoutlet.co.uk/", ""));
                    
                        //Output the data that we have downloaded
                        System.out.println("\n laptopoutlet.co.uk description: " + description + 
                                           ";\n laptopoutlet.co.uk price: " + price + 
                                           ";\n laptopoutlet.co.uk brand: " + brand +
                                           ";\n laptopoutlet.co.uk image url: " + imageUrl +
                                           ";\n laptopoutlet.co.uk product url: " + productUrl);
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
