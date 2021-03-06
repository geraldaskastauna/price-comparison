package webscraping.coursework;

import java.io.IOException;
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

/**
 * 
 * Thread that scrapes laptopsdirect.co.uk website for laptops
 */
public class LaptopsDirectScraper extends Thread{
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
         * Run method to start laptopsdirect.co.uk scraper
         */
        public void run() {
            // Declare a domain name
            String domain = "https://www.laptopsdirect.co.uk";
            
            // Start thread
            runThread = true;
            
            System.out.println("Scraping " + domain + " laptops...");
            
            //Download HTML document from website
            try{
                while(runThread){
                    Document docForPages = Jsoup.connect("https://www.laptopsdirect.co.uk/ct/laptops-and-netbooks/laptops?pageNumber=1").get();

                    //Get total amount of productcs
                    Element products = docForPages.select(".sr_numresults > b").last();
                    int productsAmount = Integer.parseInt(products.text());

                    //Find total amount of pages (20 products per page)
                    int totalPages;
                    int productsPerPage = 24;

                    //If there is a reminder after dividing productsAmount by 24 add 1(page)
                    if(productsAmount%24 == 0){
                        totalPages = (productsAmount / productsPerPage);
                    } else {
                        totalPages = (productsAmount / productsPerPage) + 1;
                    }

                    // Loop through all pages
                    for(int page = 1; page <= totalPages; page++){

                        //Download HTML document from website
                        Document doc = Jsoup.connect("https://www.laptopsdirect.co.uk/ct/laptops-and-netbooks/laptops?pageNumber=" + page).get();  

                        //Get all of the products on the page
                        Elements prods = doc.select(".OfferBox");

                        //Work through the products
                        for(int i=0; i<prods.size(); ++i){
                            // Creates new session
                            Session session = laptopDao.getSessionFactory().getCurrentSession();

                            //Get the brand
                            Elements brandClass = prods.get(i).select("a.offerboxtitle");
                            String brandA = brandClass.text();
                            String[] brandArray = brandA.split("\\s+");
                            String brand = brandArray[0].toLowerCase();
                            // Check for word Refurbished
                            if(brand.contains("refurbished"))
                                brand = brandArray[1].toLowerCase();
                            
                            // Check for brand in brand array
                            if(Arrays.asList(brands).contains(brand)){
                                // Store into database
                                product.setBrand(brand);

                                //Get the product description
                                Elements descriptionClass = prods.get(i).select("div.productInfo");
                                Elements descriptionUl = descriptionClass.get(0).select("ul");
                                String description = descriptionClass.text();

                                // Store into database
                                product.setDescription(description);

                                //Get the product price
                                Elements priceClass = prods.get(i).select(".offerprice");
                                String priceString = priceClass.text().substring(1).replace(",","");
                                String[] priceArray = priceString.split("\\s+");
                                String priceArrayString = priceArray[0];
                                double price = Double.parseDouble(priceArrayString);

                                // Store into database
                                laptop.setPrice(price);

                                //Get the image
                                Elements imageUrlClass = prods.get(i).select(".sr_image");
                                Elements imageUrlA = imageUrlClass.get(0).select(".offerImage");
                                Element imageUrlImg = imageUrlA.get(0).select("img").last();
                                String imageUrl = "https://www.laptopsdirect.co.uk" + imageUrlImg.attr("src");

                                // Store into database
                                product.setImageUrl(imageUrl);

                                //Get the items url
                                Element itemUrlA = imageUrlClass.get(0).select("a").last();
                                String productUrl = domain.concat(itemUrlA.attr("href"));
                                String queryString = itemUrlA.attr("href");

                                // Store into database
                                url.setDomain(domain);
                                url.setQueryString(queryString);

                                //Output the data that we have downloaded
                                System.out.println("\n https://www.laptopsdirect.co.uk description: " + description + 
                                                   ";\n https://www.laptopsdirect.co.uk price: " + price + 
                                                   ";\n https://www.laptopsdirect.co.uk brand: " + brand +
                                                   ";\n https://www.laptopsdirect.co.uk image url: " + imageUrl +
                                                   ";\n https://www.laptopsdirect.co.uk product url: " + productUrl);

                                // Start transaction
                                session.beginTransaction();

                                // Check if duplicate exist
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
                                    session.close();
                                }
                            }
                        }
                    }
                }
                sleep(1000 * crawlDelay);
            }   catch (IOException ex) {
                    System.out.println("Error while accessing the LAPTOPSDIRECT.CO.UK website");
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