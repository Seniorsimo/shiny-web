<%-- 
    Document   : catalogo
    Created on : 26-nov-2013, 15.20.21
    Author     : Simone, Davide, Stefania.
--%>

<%@page import="java.util.logging.Logger"%>
<%@page import="Servlet.DB"%>
<%@page import="Servlet.Prenotazione"%>
<%@page import="java.util.ArrayList" session="true"%>
<%@page import="Servlet.Pizza"%>
<%@page import="Servlet.Utente"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<% utenteCatalogo = (Utente) request.getSession().getAttribute("user");%>
<% utenti = (ArrayList<String>) request.getSession().getAttribute("utenti");%>
<% scelta = request.getParameter("scelta");%>
<% listanomi = request.getParameter("listanomi");%>
<% context = request.getContextPath();%>
<%!    private ArrayList<Prenotazione> prenotazione;
    private Utente utenteCatalogo;
    private ArrayList<String> utenti;
    private String scelta;
    private String listanomi;
    private String context;

    /**
     * Visualizza le prenotazioni dell'utente identificato con username.
     * Se l'utente è admin visualizza tutte le prenotazioni nel database.
     */
    public String visualizzazionePrenotazione(String username) {
        String out = "";

        prenotazione = DB.caricaPrenotazioni(username);
        
        String find = "";
        if (utenteCatalogo.isAdmin()) {
            
            out += "<form name='adminCommand' id='adminCommand' method='POST' action='?page=prenotazioni'>";
            out += "<input type='radio' name='scelta' value='all' checked='checked'>Tutti gli utenti</input><br>";
            out += "<input type='radio' name='scelta' value='single'>Utente: </input>"; 
            out += "<select name='listanomi' id='nomi' value='"+utenteCatalogo.getUsername()+"'>";
            for(String nome : utenti){
                out += "<option value='"+nome+ "'";
                if(nome.equals(utenteCatalogo.getUsername())) out += " selected='selected'";
                out += ">" + nome + "</option>";
            }
            out += "</select>";
            out += "<input type='submit' value='Cerca'/>";
            out += "</form>";
            if(scelta!=null&&!scelta.equals("all")){
                find = listanomi;
            }
        }
        String outTemp = miePizze(find);
        if(outTemp.equals("")) out+="<p>Non hai prenotazioni. Per prenotare clicca <a href='" + context + "/?page=catalogo'>qui</a></p>";
        out += outTemp;
        return out;
    }
    
    /**
     * Visualizza le pizze prenotate dall'utente.
     * @param find identifica l'utente che ha effettuato le prenotazioni da visualizzare. Se find è "" allora vengono visualizzate tutte.
     */
    public String miePizze(String find){
        String out = "";
        //se non devono essere visualizzate tutte le prenotazioni, vengono tolte dalla lista quelle che non sono dell'utente scelto.
        if(!find.equals("")){
            int index = 0;
            while(index<prenotazione.size()){
                if(!prenotazione.get(index).getUsername().equals(find)){
                    prenotazione.remove(index);
                }
                else{
                    index++;
                }
            }
        }
        
        //vengono visualizzate le prenotazioni in lista
        for (Prenotazione pr : prenotazione) {
            out += "<p><pre>";
            if(utenteCatalogo.isAdmin()) {
                out += "Utente: " + pr.getUsername() + "<br>";
            }
            out += "Data: " + pr.getData() + "   " + pr.getOra() + "<br>";
            out += "Recapito: " + pr.getRecapito() + "<br>";
            out += "Telefono: " + pr.getTelefono() + "<br>";
            out += "<table><tr><th>Nome</th><th>Quantit&agrave</th></tr>";
            for (String p : pr.getPizze()) {
                out += "<tr>";
                out += "<td class='c1'>" + p + "</td>";
                out += "<td class='c2'>" + pr.getQuantità().get(pr.getPizze().indexOf(p)) + "</td>";
                out += "</tr>";
            }
            out += "<tr><td></td><td>" + pr.getTotale() + " €</td></tr>";
            out += "</table>";
 
           //se prenotazione è confermata -> visualizza tasto Annulla conferma consegna
            if(utenteCatalogo.isAdmin()) {
                out += "<form id='formConfermaConsegna' method='POST' action='"+context+"/?page=confermaConsegna'>";
                
                out += "<input type='text' name='usernamePrenotazione' value='"+pr.getUsername()+"' style='display:none'/>";
                out += "<input type='text' name='dataPrenotazione' value='"+pr.getData()+"' style='display:none'/>";
                out += "<input type='text' name='oraPrenotazione' value='"+pr.getOra()+"' style='display:none'/>";
                
                
                    if(pr.getConsegnato()==0) {
                        //il valore di confermaConsegna viene impostato ad 1
                        out += "<br>Non &egrave; stata confermata la consegna della prenotazione<br>";
                        out += "<input type='text' id='confermaConsegna' name='confermaConsegna' value='1' hidden='hidden'/>";
                        out += "</pre><button onclick='confermaConsegna()'>Conferma consegna</button>";
                    }    //onclick='confermaConsegna()'        
                    else {
                        //il valore di confermaConsegna viene impostato ad 0
                        out += "<br>La consegna della prenotazione &egrave; stata confermata<br>";
                        out += "<input type='text' id='confermaConsegna' name='confermaConsegna' value='0' hidden='hidden'/>";
                        out += "</pre><button onclick='confermaConsegna()'>Annulla conferma consegna</button>";
                    }
                
                out += "</form>";
            }
            
            
            out += "<button onclick='cancellaPrenotazione(\"" + pr.getData() + "\",\"" + pr.getOra() + "\",\"" + pr.getUsername()+ "\")'>Cancella Prenotazione</button>"
                    + "</p><br>";
        }
        return out;
    }

    /**
     * Seleziona la visualizzazione:
     *  -Viene richiesto il login agli utenti non autenticati.
     *  -Se l'utente autenticato è l'admin, vengono visualizzate tutte le prenotazioni in automatico. Può scegliere di visualizzare le prenotazioni
     *  di un utente specifico.
     *  -Se l'utente autenticato non è admin, visualizza le sue prenotazioni.
     */
    public String selezionaVisualizzazione() {
        if (utenteCatalogo.getUsername().equals("Guest")) {
            return "Devi loggarti per poter visualizzare questa pagina.";
        } else if (utenteCatalogo.isAdmin()) {
            return visualizzazionePrenotazione("admin");
        } else {
            return visualizzazionePrenotazione(utenteCatalogo.getUsername());
        }
    }


%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Prenotazioni</title>
        <link href="stile.css" type="text/css" rel="stylesheet">
        <script src='js_code.js' language="javascript"></script>
    </head>
    <body>
        <div id="wrap">
        <%@include file = "header.html"%>
        <div id="content">
        <%@include file = "nav.html"%>
        <article>
            <p>Visualizzazione Prenotazioni:</p>
            <%=selezionaVisualizzazione()%>
            <form id="vis" method="post" action="<%=request.getContextPath()%>/?page=prenotazioni">
                <input type='text' id='prenotazioneCancellataOra' name='prenotazioneCancellataOra' hidden='hidden'/>
                <input type='text' id='prenotazioneCancellataData' name='prenotazioneCancellataData' hidden='hidden'/>
                <input type='text' id='prenotazioneCancellataName' name='prenotazioneCancellataName' hidden='hidden'/>
            </form>
        </article>
        <%@include file = "aside.jsp"%>
        </div>
        <%@include file = "footer.html"%>
        </div>
    </body>
</html>


