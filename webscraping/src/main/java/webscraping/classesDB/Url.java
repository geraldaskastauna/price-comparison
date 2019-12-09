package webscraping.coursework;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.*;

/**
 *
 * @author Geraldas Kastauna
 */

/**
 * 
 * Class that represents url table in database
 */
@Entity
@Table (name = "url")
public class Url implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    
    @Column(name = "domain")
    private String domain;
    
    @Column(name = "query_string")
    private String queryString;
    
    // Foreign key mapping
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "url_id")
    Set<Laptop> laptop;
    
    /** Empty url constructor */
    public Url() {
        
    }
    
    // Getters and setters
    public int getId() {
        return id;
    }
    
    public String getDomain() {
        return domain;
    }
    
    public String getQueryString() {
        return queryString;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public void setDomain(String domain) {
        this.domain = domain;
    }
    
    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }
}
