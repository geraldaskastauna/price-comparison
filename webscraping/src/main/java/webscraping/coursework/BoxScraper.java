package webscraping.coursework;

import java.io.IOException;
import static java.lang.Thread.sleep;
import java.util.Arrays;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.hibernate.Session;

/**
 *
 * @author Geraldas Kastauna
 */
public class BoxScraper extends Thread {
        //Specifies the interval between HTTP requests to the server in seconds.
        private final int crawlDelay = 5;
        
        //Allows us to shut down our application cleanly
        volatile private boolean runThread = false;
        
        //Array of brands for scraping
        private String[] brands = new String[]{"hp","dell","lenovo","acer","asus","microsoft","apple"};
        
        // Create database objects to store info from website
        Product product = new Product();
        Laptop laptop = new Laptop();
        Url url = new Url();
        
        // Class that generates sessionFactory
        LaptopDao laptopDao = new LaptopDao();
        
        /**
         * Run method to start box.co.uk scraper
         */
        @Override
        public void run() {
            // Declare a domain name
            String domain = "https://www.box.co.uk";
            
            // Start thread
            runThread = true;
            
            System.out.println("Scraping " + domain + " laptops...");
            
            //Download HTML document from website
            try{
                while(runThread){
                    // Starting page
                    int startingPage = 1;

                    //HTML document from website to get the amount of pages
                    Document docForPages = Jsoup.connect("https://www.box.co.uk/laptops/page/" + startingPage).get();

                    //Get total amount of pages
                    Elements pagesClass = docForPages.select("div.pagination-top");
                    Elements pagesSecondClass = pagesClass.select("div.pq-pagination");
                    Elements pagesFinalClass = pagesSecondClass.select("div.pq-pagination-numbers");
                    Elements pagesP = pagesFinalClass.select("p");
                    int totalPages = Integer.parseInt(pagesP.text().substring(0, 2));

                    // Scrape through all pages
                    for(int pages = 1; pages < totalPages; pages++){
                        //HTML document from website
                        Document doc = Jsoup.connect("https://www.box.co.uk/laptops/page/" + pages).get();

                        //Get all of the products on the page
                        Elements prods = doc.select("div.product-list-item");

                        //Work through the products
                        for(int i=0; i<prods.size(); ++i){
                            // Creates new session
                            Session session = laptopDao.getSessionFactory().getCurrentSession();
                            
                            //Get laptops brand
                            Elements brandClass = prods.get(i).select("div.p-list-section.p-list-section-middle");
                            Elements brandH = brandClass.get(0).select("h3");
                            String brandA = brandH.text();
                            String[] brandArray = brandA.split("\\s+");
                            String brand = brandArray[0].toLowerCase();
                            
                            // Check for brand in brand array
                            if(Arrays.asList(brands).contains(brand)){
                                // Store into database
                                product.setBrand(brand);

                                //Get the product description
                                Elements descriptionClass = prods.get(i).select(".p-list-points");
                                String description = descriptionClass.text();

                                // Store into database
                                product.setDescription(description);

                                //Get the product price
                                Elements priceClass = prods.get(i).select(".p-list-sell");
                                String priceString = priceClass.text().substring(1).replace(",","");
                                String[] priceArray = priceString.split("\\s+");
                                String priceArrayString = priceArray[0];
                                double price = Double.parseDouble(priceArrayString);

                                // Store into database
                                laptop.setPrice(price);

                                //Get the image
                                Elements imageUrlDiv = prods.get(i).select("div.p-list");
                                Elements imageUrlClass = imageUrlDiv.get(0).select("div.p-list-section.p-list-section-left");
                                Elements imageUrlTable = imageUrlClass.get(0).select("table.p-list-image");
                                Elements imageUrlA = imageUrlTable.get(0).select("a");
                                Element imageUrlAClass = imageUrlTable.get(0).select(".lazyImage").last();
                                String imageUrl = domain.concat(imageUrlAClass.attr("data-src"));

                                // Store into database
                                product.setImageUrl(imageUrl);

                                //Get the items url
                                Elements productUrlTable = imageUrlClass.get(0).select("table.p-list-image");
                                Elements productUrlTbody = productUrlTable.get(0).select("tbody");
                                Elements productUrlTr = productUrlTbody.get(0).select("tr");
                                Elements productUrlTd = productUrlTr.get(0).select("td");
                                Element productUrlA = productUrlTd.get(0).select("a").first();
                                String productUrl = productUrlA.attr("href");
                                String queryString = productUrl.replace(domain, "");

                                // Store into database
                                url.setDomain(domain);
                                url.setQueryString(queryString);

                                //Output the data that we have downloaded
                                System.out.println("\n box.co.uk description: " + description + 
                                                   ";\n box.co.uk price: " + price + 
                                                   ";\n box.co.uk brand: " + brand +
                                                   ";\n box.co.uk image url: " + imageUrl +
                                                   ";\n box.co.uk product url: " + productUrl);

                                // Start transaction
                                session.beginTransaction();
                                
                                if(!laptopDao.duplicateExist(domain, queryString, session)){
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
                    System.out.println("Error while accessing the BOX.CO.UK website");
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

