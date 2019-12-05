/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webscraping.coursework;

import java.io.IOException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
         
        private static SessionFactory sessionFactory;
        
        // Create objects to store info from website
        Product product = new Product();
        Laptop laptop = new Laptop();
        Url url = new Url();
        Hibernate hibernate = new Hibernate();
        
        public void run() {
            hibernate.setSessionFactory(sessionFactory);
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
                        Elements descriptionClass = prods.get(i).select(".productInfo");
                        String description = descriptionClass.text();
                        product.setDescription(description);
                        
                        //Get the product price
                        Elements priceClass = prods.get(i).select(".offerprice");
                        String priceString = priceClass.text().substring(1).replace(",","");
                        String[] priceArray = priceString.split("\\s+");
                        String priceArrayString = priceArray[0];
                        double price = Double.parseDouble(priceArrayString);
                        laptop.setPrice(price);
                        
                        //Get the brand
                        Elements brandClass = prods.get(i).select("a.offerboxtitle");
                        String brandA = brandClass.text();
                        String[] brandArray = brandA.split("\\s+");
                        String brand = brandArray[0];
                                                
                        // Check for word Refurbished
                        if(brand.contains("Refurbished"))
                            brand = brandArray[1];
                                
                        product.setBrand(brand);
                        
                        //Get the image
                        Elements imageUrlClass = prods.get(i).select(".sr_image");
                        Elements imageUrlA = imageUrlClass.get(0).select(".offerImage");
                        Element imageUrlImg = imageUrlA.get(0).select("img").last();
                        String imageUrl = "https://www.laptopsdirect.co.uk" + imageUrlImg.attr("src");
                        product.setImageUrl(imageUrl);
                        
                        //Get the items url
                        Element itemUrlA = imageUrlClass.get(0).select("a").last();
                        String domain = "https://www.laptopsdirect.co.uk";
                        String productUrl = domain.concat(itemUrlA.attr("href"));
                        url.setDomain(domain);
                        url.setPath(productUrl);
                        //Output the data that we have downloaded
                        System.out.println("\n https://www.laptopsdirect.co.uk description: " + description + 
                                           ";\n https://www.laptopsdirect.co.uk price: " + price + 
                                           ";\n https://www.laptopsdirect.co.uk brand: " + brand +
                                           ";\n https://www.laptopsdirect.co.uk image url: " + imageUrl +
                                           ";\n https://www.laptopsdirect.co.uk product url: " + productUrl);
                        Session session = sessionFactory.getCurrentSession();
                        
                        session.save(laptop);
                        session.save(url);
                        session.save(product);
                        
                        session.beginTransaction();
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
        public void setHibernate(Hibernate hibernate){
            this.hibernate = hibernate;
        }
}
