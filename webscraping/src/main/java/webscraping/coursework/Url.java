/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webscraping.coursework;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.*;

/**
 *
 * @author linux
 */
@Entity(name="Url")
@Table (name = "url")
public class Url implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    //@ManyToOne
    //@JoinColumn(name="url_id", nullable=false)
    private int id;
    
    @Column(name = "domain")
    private String domain;
    
    @Column(name = "query_string")
    private String queryString;
    
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
