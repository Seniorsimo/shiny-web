/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet;

import java.util.ArrayList;

/**
 *
 * @author Simone
 */
public class Prenotazione {
    private ArrayList<String> pizze;
    private ArrayList<Integer> quantità;
    private String username; //utilizzato dall'admin per le prenotazioni
    private String data;
    private String ora;
    private String recapito;
    private String nominativo;
    private String telefono;
    private double totale;

    public Prenotazione(String username, String data, String ora, String recapito, String nominativo, String telefono, double tot) {
        this.data = data;
        this.ora = ora;
        this.recapito = recapito;
        this.nominativo = nominativo;
        this.telefono = telefono;
        this.username = username;
        pizze = new ArrayList<>();
        quantità = new ArrayList<>();
        totale = tot;
    }

    public String getUsername() {
        return username;
    }

    public double getTotale() {
        return totale;
    }
    
    public ArrayList<String> getPizze() {
        return pizze;
    }

    public void addPizza(String pizza, int quantità){
        pizze.add(pizza);
        this.quantità.add(quantità);
    }

    public ArrayList<Integer> getQuantità() {
        return quantità;
    }

    public String getData() {
        return data;
    }

    public String getOra() {
        return ora;
    }

    public String getRecapito() {
        return recapito;
    }

    public String getNominativo() {
        return nominativo;
    }

    public String getTelefono() {
        return telefono;
    }

    
    
    
}
