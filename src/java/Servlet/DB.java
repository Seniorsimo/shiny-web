/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Simone
 */
public class DB {

    static String url = "jdbc:derby://localhost:1527/dbpizza";
    static String user = "adminpizza";
    static String pwd = "adminpizza";
    
    public static void init(){
        //tabella prenotazioni
        try {
            Connection conn = DriverManager.getConnection(url, user, pwd);
            Statement st = conn.createStatement();
            String sql = "CREATE TABLE PRENOTAZIONI ("
                    + "USERNAME VARCHAR(30) NOT NULL,"
                    + "PIZZA VARCHAR(30) NOT NULL,"
                    + "QUANTITA INT NOT NULL,"
                    + "TOTALE DOUBLE NOT NULL,"
                    + "DATA VARCHAR(20) NOT NULL,"
                    + "ORA VARCHAR(5) NOT NULL,"
                    + "RECAPITO VARCHAR(100) NOT NULL,"
                    + "NOMINATIVO VARCHAR(50) NOT NULL,"
                    + "TELEFONO VARCHAR(12) NOT NULL,"
                    + "CONSEGNATA INT NOT NULL DEFAULT 0)";
            st.executeUpdate(sql);
            st.close();
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.INFO,"Tabella prenotazioni già esistente.");
        }
        
        //tabella pizze
        try {
            Connection conn = DriverManager.getConnection(url, user, pwd);
            Statement st = conn.createStatement();
            String sql = "CREATE TABLE PIZZE ("
                    + "NOME VARCHAR(30) PRIMARY KEY,"
                    + "INGREDIENTI VARCHAR(500) NOT NULL,"
                    + "PREZZO DOUBLE NOT NULL"
                    + ")";
            st.executeUpdate(sql);
            st.close();
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.INFO,"Tabella pizze già esistente.");
        }
        
        //tabella utenti
        try {
            Connection conn = DriverManager.getConnection(url, user, pwd);
            Statement st = conn.createStatement();
            String sql = "CREATE TABLE UTENTI ("
                    + "USERNAME VARCHAR(30) PRIMARY KEY,"
                    + "PASSWORD VARCHAR(20) NOT NULL,"
                    + "ADMIN INT NOT NULL DEFAULT 0"
                    + ")";
            st.executeUpdate(sql);
            
            sql = "INSERT INTO UTENTI (USERNAME, PASSWORD, ADMIN) VALUES ('admin','admin',1)";
            st.executeUpdate(sql);
            
            st.close();
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.INFO,"Tabella utenti già esistente.");
        }
    }

    public static ArrayList<Pizza> caricaCatalogo() throws SQLException {
        Connection conn = DriverManager.getConnection(url, user, pwd);
        Statement st = conn.createStatement();
        String sql = "SELECT * FROM PIZZE";
        sql = sql + " ORDER BY NOME";
        ResultSet rs = st.executeQuery(sql);
        ArrayList<Pizza> pizze = new ArrayList<>();
        while (rs.next()) {
            pizze.add(new Pizza(rs.getString("NOME"), rs.getString("INGREDIENTI"), rs.getDouble("PREZZO")));
        }
        rs.close();
        st.close();
        conn.close(); // chiusura connessione
        return pizze;
    }
    /*
     * se c'è un utente con quello username viene confrontata la pass. 
     * Se la pass è corretta viene chiesta un'istanza utente - verifica se admin e setta campo
     */

    /**
     * carica da db i dati di un utente e ne crea l'oggetto utente
     *
     * @param nome
     * @param pass
     * @return l'utente
     * @throws SQLException
     */
    public static Utente caricaUtente(String nome, String pass) throws SQLException {
        Utente ut = null;
        if (controllaUtente(nome)) {
            Connection conn = DriverManager.getConnection(url, user, pwd);
            Statement st = conn.createStatement();
            String sql = "SELECT * FROM UTENTI WHERE USERNAME = '" + nome + "'";
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                if (rs.getString("PASSWORD").equals(pass)) {
                    ut = new Utente();
                    ut.setUsername(nome);
                    if (rs.getInt("ADMIN") == 1) {
                        ut.setAdmin(true);
                    }

                }
            }

            rs.close();
            st.close();
            conn.close(); // chiusura connessione
        }
        return ut;
    }
    
    public static boolean passwordCorretta(String nome, String pass) throws SQLException {
        Utente ut = null;
        boolean correct = false;
            Connection conn = DriverManager.getConnection(url, user, pwd);
            Statement st = conn.createStatement();
            String sql = "SELECT * FROM UTENTI WHERE USERNAME = '" + nome + "'";
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                if (rs.getString("PASSWORD").equals(pass)) {
                    correct = true;

                }
            }

            rs.close();
            st.close();
            conn.close(); // chiusura connessione
        return correct;
    }

    /**
     * Controlla che l'utente sia presente nel database.
     * @param nome username utente da verificare
     * @return
     * @throws SQLException 
     */
    public static boolean controllaUtente(String nome) throws SQLException {
        Connection conn = DriverManager.getConnection(url, user, pwd);
        Statement st = conn.createStatement();
        if (nome.isEmpty()) {
            return false;
        }
        String sql = "SELECT * FROM UTENTI WHERE USERNAME = '" + nome + "'";
        ResultSet rs = st.executeQuery(sql);
        if (rs.next()) {
            rs.close();
            st.close();
            conn.close(); // chiusura connessione
            return true; //se c'è un utente con nme "nome" allora restituisce true.
        }
        rs.close();
        st.close();
        conn.close(); // chiusura connessione
        return false;
    }

    /**
     * precondizione: lo username non è già nel database
     *
     * @param username
     * @param password
     * @throws SQLException
     */
    static void aggiungiUtente(String username, String password) throws SQLException {
        Connection conn = DriverManager.getConnection(url, user, pwd);
        Statement st = conn.createStatement();

        String sql = "INSERT INTO UTENTI (USERNAME, PASSWORD, ADMIN) VALUES ('" + username + "','" + password + "',0)";

        st.executeUpdate(sql);
        st.close();
        conn.close(); // chiusura connessione
    }

    /**
     * La prenotazione viene aggiunta al database.
     * @param pizze
     * @param quantità
     * @param username
     * @param data
     * @param recapito
     * @param telefono
     * @param nominativo
     * @param ora
     * @param prezzo
     * @throws SQLException 
     */
    static void aggiungiPrenotazione(ArrayList<Pizza> pizze, ArrayList<Integer> quantità, String username, String data, String recapito, String telefono, String nominativo, String ora, String prezzo) throws SQLException {
        Connection conn = DriverManager.getConnection(url, user, pwd);
        Statement st = conn.createStatement();
        for (Pizza p : pizze) {
            String sql = "INSERT INTO PRENOTAZIONI (USERNAME, PIZZA, QUANTITA, TOTALE, DATA, ORA, RECAPITO, NOMINATIVO, TELEFONO, CONSEGNATA) VALUES("
                    + "'" + username + "','" + p.getNome() + "'," + quantità.get(pizze.indexOf(p)) + "," + Double.parseDouble(prezzo) + ",'" + data + "','" + ora + "','" + recapito + "','" + nominativo + "','" + telefono + "',0)";

            st.executeUpdate(sql);
        }

        st.close();
        conn.close();
    }

    /**
     * Il controllo che l'utente sia amministratore è già fatto in jsp
     *
     * @param nome username dell'utente o "admin" per selezionare le pizze da
     * visualizzare
     * @return ArrayList<Prenotazioni>
     * @throws SQLException
     */
    public static ArrayList<Prenotazione> caricaPrenotazioni(String nome) {
        try {
            ArrayList<Prenotazione> prenotazioni = new ArrayList<>();
            Connection conn = DriverManager.getConnection(url, user, pwd);
            Statement st = conn.createStatement();

            if (nome.equals("admin")) {
                String sql = "SELECT * FROM PRENOTAZIONI";
                String username = "";
                String data = "";
                String ora = "";
                Prenotazione prenotazione = null;
                //variabili che indicano la pizza analizzata al passo precedente
                String usernameTemp = "";
                String dataTemp = "";
                String oraTemp = "";
                ResultSet rs = st.executeQuery(sql);
                while (rs.next()) {
                    username = rs.getString("USERNAME");
                    data = rs.getString("DATA");
                    ora = rs.getString("ORA");
                    if (!username.equals(usernameTemp) || !data.equals(dataTemp) || !ora.equals(oraTemp)) {
                        prenotazione = new Prenotazione(rs.getString("USERNAME"), data, ora, rs.getString("RECAPITO"), rs.getString("NOMINATIVO"), rs.getString("TELEFONO"), rs.getDouble("TOTALE"), rs.getInt("CONSEGNATA"));
                        prenotazioni.add(prenotazione);
                    }
                    prenotazione.addPizza(rs.getString("PIZZA"), Integer.parseInt(rs.getString("QUANTITA")));
                    usernameTemp = username;
                    dataTemp = data;
                    oraTemp = ora;
                }
                rs.close();
            } else {
                String sql = "SELECT * FROM PRENOTAZIONI WHERE USERNAME = '" + nome + "'";
                String data = "";
                String ora = "";
                Prenotazione prenotazione = null;
                //varaiabili che indicano la pizza analizzata al passo precedente
                String dataTemp = "";
                String oraTemp = "";
                ResultSet rs = st.executeQuery(sql);
                while (rs.next()) {
                    data = rs.getString("DATA");
                    ora = rs.getString("ORA");
                    if (!data.equals(dataTemp) || !ora.equals(oraTemp)) {
                        prenotazione = new Prenotazione(rs.getString("USERNAME"), data, ora, rs.getString("RECAPITO"), rs.getString("NOMINATIVO"), rs.getString("TELEFONO"), rs.getDouble("TOTALE"), rs.getInt("CONSEGNATA"));
                        prenotazioni.add(prenotazione);
                    }
                    prenotazione.addPizza(rs.getString("PIZZA"), Integer.parseInt(rs.getString("QUANTITA")));
                    dataTemp = data;
                    oraTemp = ora;
                }
                rs.close();
            }
            st.close();
            conn.close(); // chiusura connessione
            return prenotazioni;
        } catch (SQLException ex) {
            return null;
        }
    }

    /**
     * Cancella la prenotazione dal database
     * @param utente utente che ha effettuato la prentazione.
     * @param data
     * @param ora
     * @throws SQLException 
     */
    static void cancellaPrenotazione(Utente utente, String data, String ora) throws SQLException {
        Connection conn = DriverManager.getConnection(url, user, pwd);
        Statement st = conn.createStatement();
        String sql = "DELETE FROM PRENOTAZIONI WHERE USERNAME='" + utente.getUsername() + "' AND DATA='" + data + "' AND ORA='" + ora + "'";
        st.executeUpdate(sql);
        st.close();
        conn.close();
    }

    /**
     * Carica la lista degli username.
     * @return lista username utenti nel database.
     * @throws SQLException 
     */
    static ArrayList<String> caricaNomiUtenti() throws SQLException {
        Connection conn = DriverManager.getConnection(url, user, pwd);
        Statement st = conn.createStatement();
        String sql = "SELECT USERNAME FROM UTENTI";
        ResultSet rs = st.executeQuery(sql);
        ArrayList<String> nomiUtenti = new ArrayList<>();
        while (rs.next()) {
            nomiUtenti.add(rs.getString("USERNAME"));
        }
        rs.close();
        st.close();
        conn.close();
        return nomiUtenti;
    }

    /**
     * Aggiunge o modifica una pizza
     * @param nomepizza
     * @param ingredienti
     * @param prezzo
     * @param nuova
     * @throws SQLException 
     */
    static void editPizza(String nomepizza, String ingredienti, String prezzo, boolean nuova) throws SQLException {
        Connection conn = DriverManager.getConnection(url, user, pwd);
        Statement st = conn.createStatement();
        String sql;
        if (nuova) {
            sql = "INSERT INTO PIZZE (NOME, INGREDIENTI, PREZZO) VALUES ('" + nomepizza + "','" + ingredienti + "'," + Double.parseDouble(prezzo) + ")";
        } else {
            sql = "UPDATE PIZZE SET INGREDIENTI='" + ingredienti + "',PREZZO=" + Double.parseDouble(prezzo) + " WHERE NOME='" + nomepizza + "'";
        }
        st.executeUpdate(sql);
        st.close();
        conn.close(); // chiusura connessione
    }
    
    /**
     * Modifica la password dell'utente indicato da nome
     * @param nome username dell'utente a cui cambiare password
     * @param pass password nuova
     * @throws SQLException 
     */
    static void editUser(String nome, String pass) throws SQLException {
        Connection conn = DriverManager.getConnection(url, user, pwd);
        Statement st = conn.createStatement();
        String sql;
        
            sql = "UPDATE UTENTI SET PASSWORD='" + pass + "' WHERE USERNAME='" + nome + "'";

        st.executeUpdate(sql);
        st.close();
        conn.close(); // chiusura connessione
    }
    
    /**
     * Elimina una pizza dal database.
     * @param nomePizza
     * @throws SQLException 
     */
    static void removePizza(String nomePizza) throws SQLException {
        Connection conn = DriverManager.getConnection(url, user, pwd);
        Statement st = conn.createStatement();
        String sql;
            sql = "DELETE FROM PIZZE WHERE(NOME='" + nomePizza + "')";
            st.executeUpdate(sql);
        st.close();
        conn.close(); // chiusura connessione
    }
    
    /**
     * Cambia lo stato della consegna.
     * @param username username dell'utente che ha effettuato la prenotazione.
     * @param data
     * @param ora
     * @param c stato da impostare.
     * @throws SQLException 
     */
    static void confermaConsegna(String username, String data, String ora,int c) throws SQLException {
        Connection conn = DriverManager.getConnection(url, user, pwd);
        Statement st = conn.createStatement();
        String sql;
            sql = "UPDATE PRENOTAZIONI "
                + "SET CONSEGNATA="+c+" "
                + "WHERE USERNAME='"+username+"' AND DATA='"+data+"' AND ORA='"+ora+"'";
            st.executeUpdate(sql);
        st.close();
        conn.close(); // chiusura connessione
    }
    
    
}
