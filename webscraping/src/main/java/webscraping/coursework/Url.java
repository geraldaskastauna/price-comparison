/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webscraping.coursework;

import javax.persistence.*;

/**
 *
 * @author linux
 */
@Entity
@Table (name = "url")
public class Url {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    
    @Column(name = "domain")
    private String domain;
    
    @Column(name = "path")
    private String path;
    
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
     
    public String getPath() {
        return path;
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
    
    public void setPath(String path) {
        this.path = path;
    }
    
    public void setQueryString() {
        this.queryString = queryString;
    }
}
