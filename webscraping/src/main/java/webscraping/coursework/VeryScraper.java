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
        
        // Class that generates sessionFactory
        Hibernate hibernate = null;
        
        public void run() {
            // Declare a domain name
            String domain = "http://www.very.co.uk";
            
            // Start thread
            runThread = true;
            System.out.println("Scraping " + domain + " laptops...");
            
            //Download HTML document from website
            try{
                //Download HTML document from website to get the total pages
                Document docForPages = Jsoup.connect("https://www.very.co.uk/electricals/laptops/e/b/4873.end?pageNumber=1").get();
                Elements pagesClass = docForPages.select(".pagination");
                String pagesString = pagesClass.text();
                String[] pagesArray = pagesString.split("\\s+");
                String pagesInArray = pagesArray[9];
                int totalPages = Integer.parseInt(pagesInArray);
                
                // Loop through pages
                for(int page = 1; page < totalPages; page++){
                    //HTML document from website to get the amount of pages
                    Document doc = Jsoup.connect("https://www.very.co.uk/electricals/laptops/e/b/4873.end?pageNumber=" + page).get();

                    //Get all of the products on the page
                    Elements prods = doc.select(".product");

                    //Work through the products
                    for(int i=0; i<prods.size(); i++){
                        // Creates new session
                        Session session = hibernate.getSessionFactory().getCurrentSession();
                        
                        //Get the product description
                        Elements descriptionClass = prods.get(i).select(".productBrandDesc");
                        String description = descriptionClass.text();
                        // Store into database
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
                        // Store into database
                        laptop.setPrice(price);
                        
                        //Get the brand
                        Elements brandClass = prods.get(i).select(".productBrand");
                        String brand = brandClass.text();
                        // Store into database
                        product.setBrand(brand);
                        
                        //Get the image url
                        Elements imageUrlClass = prods.get(i).select(".productMainImage");
                        Element imageUrlImg = imageUrlClass.get(0).select("img").last();
                        String imageUrl = imageUrlImg.attr("src");
                        // Store into database
                        product.setImageUrl(imageUrl);
                        
                        //Get the product url
                        Elements productUrlClass = prods.get(i).select(".productMainImage");
                        Element productUrlA = productUrlClass.get(0).select("a").first();
                        String productUrl = productUrlA.attr("href");
                        String queryString = productUrl.replace(domain, "");
                        // Store into database
                        url.setDomain(domain);
                        url.setQueryString(queryString);
                        
                        System.out.println("\n http://www.very.co.uk description: " + description + 
                                           ";\n http://www.very.co.uk price: " + price + 
                                           ";\n http://www.very.co.uk brand: " + brand +
                                           ";\n http://www.very.co.uk image url: " + imageUrl +
                                           ";\n http://www.very.co.uk product url: " + productUrl);
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
