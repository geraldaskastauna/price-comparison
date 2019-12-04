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
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
/**
 *
 * @author linux
 */
public class VeryScraper extends Thread {
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
            System.out.println("Scraping www.very.co.uk laptops...");
            
            //Download HTML document from website
            try{
                //Download HTML document from website to get the total pages
                Document docForPages = Jsoup.connect("https://www.very.co.uk/electricals/laptops/e/b/4873.end?pageNumber=1").get();
                Elements pagesClass = docForPages.select(".pagination");
                String pagesString = pagesClass.text();
                String[] pagesArray = pagesString.split("\\s+");
                String pagesInArray = pagesArray[9];
                int totalPages = Integer.parseInt(pagesInArray);
                
                for(int page = 1; page < totalPages; page++){
                    //HTML document from website to get the amount of pages
                    Document doc = Jsoup.connect("https://www.very.co.uk/electricals/laptops/e/b/4873.end?pageNumber=" + page).get();

                    //Get all of the products on the page
                    Elements prods = doc.select(".product");

                    //Work through the products
                    for(int i=0; i<prods.size(); i++){
                        //Get the product description
                        Elements descriptionClass = prods.get(i).select(".productBrandDesc");
                        String description = descriptionClass.text();
                        product.setDescription(description);
                        
                        //Get the product price
                        Elements priceClass = prods.get(i).select(".productPrice");
                        
                        //Remove all unneccessary symbols and words
                        String priceString = priceClass.text().replace("(", "")
                                                              .replace(")", "")
                                                              .replace("£", "")
                                                              .replace("Save", "")
                                                              .replace("From", "")
                                                              .replace("up to", "");  
                        
                        String[] priceArray = priceString.split("\\s+");
                        String priceArrayString = priceArray[1];
                        
                        // Get a double value from string array;
                        double price = Double.parseDouble(priceArrayString);
                        laptop.setPrice(price);
                        
                        //Get the brand
                        Elements brandClass = prods.get(i).select(".productBrand");
                        String brand = brandClass.text();
                        product.setBrand(brand);
                        
                        //Get the image url
                        Elements imageUrlClass = prods.get(i).select(".productMainImage");
                        Element imageUrlImg = imageUrlClass.get(0).select("img").last();
                        String imageUrl = imageUrlImg.attr("src");
                        product.setImageUrl(imageUrl);
                        
                        //Get the product url
                        Elements productUrlClass = prods.get(i).select(".productMainImage");
                        Element productUrlA = productUrlClass.get(0).select("a").first();
                        String productUrl = productUrlA.attr("href");
                        String domain = "http://www.very.co.uk";
                        url.setDomain(domain);
                        url.setPath(productUrl);
                        
                        //Output the data that we have downloaded
                        System.out.println("\n very.co.uk description: " + description + 
                                           ";\n very.co.uk price: " + price + 
                                           ";\n very.co.uk brand: " + brand +
                                           ";\n very.co.uk image url: " + imageUrl +
                                           ";\n very.co.uk product url: " + productUrl);
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
