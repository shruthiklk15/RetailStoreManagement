package com.myapp.spring.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="users")
public class User {
    
    @Id
    @Column(name = "EMAIL")
    private String email;
    
    
    @Column(name = "USER_NAME")
    private String username;
    
    @Column(name = "PASSWORD")
    private String password;
    
    public User( ) {
        
    }
    public User(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    
    
}

