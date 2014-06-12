/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet;

/**
 *
 * @author Simone
 */
public class Utente {
    private String username;
    private boolean admin;
    
    public Utente() {
        admin = false;
    }
    
    public Utente(String nome){
        username = nome;
        admin = false;
    }
            

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
    
    
}
