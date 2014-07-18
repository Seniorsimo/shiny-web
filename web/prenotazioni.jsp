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

    /*public String visualizzazioneCatalogo() {
     String out = "<table><tr><th>Nome</th><th>Ingredienti</th><th>Prezzo</th><th>Quantit&agrave</th></tr>";
     int i = 0;
     for (Pizza p : pizze) {
     out += "<tr>";
     out += "<td class='c1'>" + p.getNome() + "</td>";
     out += "<td class='c2'>" + p.getIngredienti() + "</td>";
     out += "<td class='c3'>" + p.getPrezzo() + " €</td>";
     out += "<td class='c4'>"
     + "<span><img id='add" + i + "' name='add" + i + "' src='http://maps.simcoe.ca/TourismDataList/images/plusButton.gif' alt='+' onclick='sostituzione(" + i + ")'/></span>"
     + "<span><input id='q" + i + "' name='q" + i + "' type='number' style='display: none;' min='0'/></span>"
     + "</td>";
     out += "</tr>";
     i++;

     }
     out += "</table>";
     out += "Nominativo: <input type='text' name='nominativo' required='required'/>";
     out += "Telefono <input type='text' name='telefono' required='required'/>";
     out += "Recapito: <input type='text' name='recapito' required='required'/>";
     out += "Data: <input type='date' name='data' required='required'/>";
     out += "Ora: <input type='time' name='ora' required='required'/>";
     out += "<input type='text' name='visPr' value='3' hidden='hidden'/>"; //campo nascosto conferma a servlet esistenza di prenotazione
     return out;
     }*/
    public String visualizzazionePrenotazione(String username) {
        String out = "";

        //double tot = 0;
        prenotazione = DB.caricaPrenotazioni(username);
        
        String find = "";
        if (utenteCatalogo.isAdmin()) {
            
            out += "<form name='adminCommand' id='adminCommand' method='POST' action='?page=prenotazioni'>";
            out += "<input type='radio' name='scelta' value='all' checked='checked'>Tutti gli utenti</input><br>";
            out += "<input type='radio' name='scelta' value='single'>Utente: </input>";
       //     out += "<input select='nomi' name='listanomi' value='"+utenteCatalogo.getUsername()+"'>"; 
            out += "<select name='listanomi' id='nomi' value='"+utenteCatalogo.getUsername()+"'>";
            for(String nome : utenti){
              //  Logger.getGlobal().info("Un messaggio dentro"+nome);
                out += "<option value='"+nome+ "'";
                if(nome.equals(utenteCatalogo.getUsername())) out += " selected='selected'";
                out += ">" + nome + "</option>";
            }
            out += "</select>";
            out += "<input type='submit' value='Cerca'/>";
            out += "</form>";
            Logger.getGlobal().info(scelta);
            if(scelta!=null&&!scelta.equals("all")){
                find = listanomi;
            }
            if(scelta==null){
 //               find = utenteCatalogo.getUsername();
                //l'amministrazione vede tutte le pizze
                find = "";
            }
        }
        String outTemp = miePizze(find);
        if(outTemp.equals("")) out+="<p>Non hai prenotazioni. Per prenotare clicca <a href='" + context + "/?page=catalogo'>qui</a></p>";
        out += outTemp;
        
        
        // out += "<tr>";
        // out += "<td class='c1'></td>";
        //out += "<td class='c2'></td>";
        // out += "<td class='c3'><b>Totale:</b></td>";
        // out += "<td class='c4'>" + tot + " €</td>";
        // out += "</tr>";


        // out += "<input type='text' name='confPr' value='3' hidden='hidden'/>"; //conferma definitiva prenotazione
        return out;
    }
    
    public String miePizze(String find){
        String out = "";
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
                //int quan = pr.getQuantità().get(prenotazione.indexOf(pr));
                out += "<tr>";
                out += "<td class='c1'>" + p + "</td>";
                out += "<td class='c2'>" + pr.getQuantità().get(pr.getPizze().indexOf(p)) + "</td>";
                out += "</tr>";
                //out += "<td class='c3'>" + quan + "</td>";
            }
            out += "<tr><td></td><td>" + pr.getTotale() + " €</td></tr>";
            //double subtot = p.getPrezzo() * quan;
            //tot += subtot;
            out += "</table>";
            //out += "<td class='c4'>" + subtot + " €</td>";
 
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


