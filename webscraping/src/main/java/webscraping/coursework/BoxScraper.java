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
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author linux
 */
public class BoxScraper extends Thread{
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
            System.out.println("Scraping www.box.co.uk laptops...");
            
            //Download HTML document from website
            try{
                
                int startingPage = 1;
                
                //HTML document from website to get the amount of pages
                Document docForPages = Jsoup.connect("https://www.box.co.uk/laptops/page/" + startingPage).get();
                
                //Get total amount of pages
                Elements pagesClass = docForPages.select("div.pagination-top");
                Elements pagesSecondClass = pagesClass.select("div.pq-pagination");
                Elements pagesFinalClass = pagesSecondClass.select("div.pq-pagination-numbers");
                Elements pagesP = pagesFinalClass.select("p");
                int totalPages = Integer.parseInt(pagesP.text().substring(0, 2));
                
                for(int pages = 1; pages < totalPages; pages++){
                    //HTML document from website
                    Document doc = Jsoup.connect("https://www.box.co.uk/laptops/page/" + pages).get();
                    
                    //Get all of the products on the page
                    Elements prods = doc.select("div.product-list-item");
                
                    //Work through the products
                    for(int i=0; i<prods.size(); ++i){

                        //Get the product description
                        Elements descriptionClass = prods.get(i).select(".p-list-points");
                        String description = descriptionClass.text();
                        product.setDescription(description);
                        
                        
                        //Get the product price
                        Elements priceClass = prods.get(i).select(".p-list-sell");
                        String priceString = priceClass.text().substring(1).replace(",","");
                        String[] priceArray = priceString.split("\\s+");
                        String priceArrayString = priceArray[0];
                        double price = Double.parseDouble(priceArrayString);
                        laptop.setPrice(price);
                        
                        //Get laptops brand
                        Elements brandClass = prods.get(i).select("div.p-list-section.p-list-section-middle");
                        Elements brandH = brandClass.get(0).select("h3");
                        String brand = brandH.text();
                        product.setBrand(brand);
                        
                        //Get the image
                        Elements imageUrlDiv = prods.get(i).select("div.p-list");
                        Elements imageUrlClass = imageUrlDiv.get(0).select("div.p-list-section.p-list-section-left");
                        Elements imageUrlTable = imageUrlClass.get(0).select("table.p-list-image");
                        Elements imageUrlA = imageUrlTable.get(0).select("a");
                        Element imageUrlAClass = imageUrlTable.get(0).select("img").last();
                        String imageUrl = imageUrlAClass.attr("data-src");
                        product.setImageUrl(imageUrl);
                        
                        //Get the items url
                        Elements productUrlTable = imageUrlClass.get(0).select("table.p-list-image");
                        Elements productUrlTbody = productUrlTable.get(0).select("tbody");
                        Elements productUrlTr = productUrlTbody.get(0).select("tr");
                        Elements productUrlTd = productUrlTr.get(0).select("td");
                        Element productUrlA = productUrlTd.get(0).select("a").first();
                        String domain = "https://www.box.co.uk";
                        String productUrl = domain.concat(productUrlA.attr("href"));
                        url.setDomain(domain);
                        url.setPath(productUrlA.attr("href"));
                        
                        //Output the data that we have downloaded
                        System.out.println("\n box.co.uk description: " + description + 
                                           ";\n box.co.uk price: " + price + 
                                           ";\n box.co.uk brand: " + brand +
                                           ";\n box.co.uk image url: " + imageUrl +
                                           ";\n box.co.uk product url: " + productUrl);
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

