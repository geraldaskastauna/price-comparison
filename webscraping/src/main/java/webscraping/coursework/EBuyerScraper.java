/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webscraping.coursework;

import java.io.IOException;
import static java.lang.Thread.sleep;
import org.hibernate.Session;
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
        
        // Class that generates sessionFactory
        Hibernate hibernate = new Hibernate();
        
        public void run() {
            // Declare a domain name
            String domain = "https://www.ebuyer.com";
            
            // Start thread
            runThread = true;
            System.out.println("Scraping " + domain + " laptops...");
            
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
                        // Creates new session
                        Session session = hibernate.getSessionFactory().getCurrentSession();
                        
                        //Get the product description
                        Elements descriptionClass = prods.get(i).select(".grid-item__ksp");
                        String description = descriptionClass.text();
                        // Store into database
                        product.setDescription(description);
                        
                        //Get the product price
                        Elements priceClass = prods.get(i).select("p.price");
                        // If item is out of stock price will be set to 0
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
                        // Store into database
                        laptop.setPrice(price);
                        
                        //Get laptops brand
                        Elements brandClass = prods.get(i).select("h3.grid-item__title");
                        Elements brandA = brandClass.get(0).select("a");
                        String brand = brandA.text();
                        String[] brandArray = brand.split("\\s+");
                        brand = brandArray[0];
                        // Check for word Refurbished
                        if(brand.contains("REFURBISHED"))
                            brand = brandArray[1];
                        // Store into database
                        product.setBrand(brand);
                        
                        //Get the image
                        Elements imageUrlDiv = prods.get(i).select("div.grid-item__img");
                        Elements imageUrlA = imageUrlDiv.get(0).select("a");
                        Element imageUrlAClass = imageUrlA.get(0).select("img").last();
                        String imageUrl = imageUrlAClass.attr("src");
                        // Store into database
                        product.setImageUrl(imageUrl);
                        
                        //Get the items url
                        Element productUrlA = brandClass.get(0).select("a").first();
                        String productUrlHref = productUrlA.attr("href");
                        String productUrl = domain.concat(productUrlHref);
                        String queryString = productUrl.replace(domain, "");
                        // Store into database
                        url.setDomain(domain);
                        url.setQueryString(queryString);
                        
                        //Output the data that we have downloaded
                        System.out.println("\n https://www.ebuyer.com description: " + description + 
                                           ";\n https://www.ebuyer.com price: " + price + 
                                           ";\n https://www.ebuyer.com brand: " + brand +
                                           ";\n https://www.ebuyer.com image url: " + imageUrl +
                                           ";\n https://www.ebuyer.com product url: " + productUrl);
                        // Start transaction
                        session.beginTransaction();  
                        
                        // Add laptop, url and product to database (need to commit)
                        session.save(laptop);
                        session.save(url);
                        session.save(product);
                        
                        //Commit transaction to save it to database
                        session.getTransaction().commit();

                        //Close the session and release database connection
                        session.close();
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
        // Set hibernate class to get sessionFactory
        public void setHibernate(Hibernate hibernate){
            this.hibernate = hibernate;
        }
}
