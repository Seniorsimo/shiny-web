/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet;

/**
 *
 * @author Simone
 */
public class Pizza {
    private String nome;
    private String ingredienti;
    private double prezzo;

    public Pizza(String nome, String ingredienti, double prezzo) {
        this.nome = nome;
        this.ingredienti = ingredienti;
        this.prezzo = prezzo;
    }
   
    public String getNome() {
        return nome;
    }

    public String getIngredienti() {
        return ingredienti;
    }

    public double getPrezzo() {
        return prezzo;
    }
    
    
}
