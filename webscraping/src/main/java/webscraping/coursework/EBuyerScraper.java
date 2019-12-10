package webscraping.coursework;

import java.io.IOException;
import static java.lang.Thread.sleep;
import java.util.Arrays;
import org.hibernate.Session;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Geraldas Kastauna
 */
public class EBuyerScraper extends Thread{
        //Specifies the interval between HTTP requests to the server in seconds.
        private int crawlDelay = 5;
        
        //Allows us to shut down our application cleanly
        volatile private boolean runThread = false;
        
        //Array of brands for scraping
        private String[] brands = new String[]{"hp","dell","lenovo","acer","asus","microsoft","apple"};
        
        // Create objects to store info from website
        Product product = new Product();
        Laptop laptop = new Laptop();
        Url url = new Url();
        
        // Class that generates sessionFactory
        LaptopDao laptopDao = new LaptopDao();
        
        /**
         * Run method to start box.co.uk scraper
         */
        public void run() {
            // Declare a domain name
            String domain = "https://www.ebuyer.com";
            
            // Start thread
            runThread = true;
            
            System.out.println("Scraping " + domain + " laptops...");
            
            //Download HTML document from website
            try{
                while(runThread){
                    //Set starting page and total pages to for scraping
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
                            Session session = laptopDao.getSessionFactory().getCurrentSession();

                            //Get laptops brand
                            Elements brandClass = prods.get(i).select("h3.grid-item__title");
                            Elements brandA = brandClass.get(0).select("a");
                            String brand = brandA.text();
                            String[] brandArray = brand.split("\\s+");
                            brand = brandArray[0].toLowerCase();
                            // Check for word Refurbished
                            if(brand.contains("refurbished"))
                                brand = brandArray[1].toLowerCase();
                            
                            //Check for brand in brands array
                            if(Arrays.asList(brands).contains(brand)){
                                // Store into database
                                product.setBrand(brand);

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

                                // Check if duplicate exists
                                if(!laptopDao.duplicateExist(domain, queryString, session)){
                                    // Duplicate doesnt exist
                                    // Foreign keys
                                    laptop.setProduct(product);
                                    laptop.setUrl(url);

                                    // Add laptop, url and product to database (need to commit)
                                    session.save(url);
                                    session.save(product);
                                    session.save(laptop);

                                    //Commit transaction to save it to database
                                    session.getTransaction().commit();

                                    //Close the session and release database connection
                                    session.close();
                                } else {
                                    // Duplicate exists
                                    // Update laptop
                                    session.update(laptop);
                                    session.update(product);
                                    session.update(url);
                                    session.close();
                                }
                            }
                        }
                    }
                }
                    sleep(1000 * crawlDelay);
            }   catch (IOException ex) {
                    System.out.println("Error while accessing the EBUYER.COM website");
            }   catch(InterruptedException ex){
                    System.err.println(ex.getMessage());
            }
        }
        
        // Other threads can stop this thread
        public void stopThread(){
            runThread = false;
        }
        
        // Set laptopDao class to get sessionFactory
        public void setLaptopDao(LaptopDao laptopDao){
            this.laptopDao = laptopDao;
        }
}