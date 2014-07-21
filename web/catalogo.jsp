<%-- 
    Document   : catalogo
    Created on : 26-nov-2013, 15.20.21
    Author     : Simone
--%>

<%@page import="java.util.Collections"%>
<%@page import="java.util.ArrayList" session="true"%>
<%@page import="Servlet.Pizza"%>
<%@page import="Servlet.Utente"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<% pizze = (ArrayList<Pizza>) request.getAttribute("pizze");%>
<% prenotazione = (ArrayList<Pizza>) request.getSession().getAttribute("prenotazione");%>
<% quantità = (ArrayList<Integer>) request.getSession().getAttribute("quantità");%>
<% utenteCatalogo = (Utente) request.getSession().getAttribute("user");%>
<% nominativo = (String) request.getSession().getAttribute("nominativo");%>
<% telefono = (String) request.getSession().getAttribute("telefono");%>
<% recapito = (String) request.getSession().getAttribute("recapito");%>
<% data = (String) request.getSession().getAttribute("data");%>
<% ora = (String) request.getSession().getAttribute("ora");%>
<% context = request.getContextPath();%>
<%!    private ArrayList<Pizza> pizze;
    private ArrayList<Pizza> prenotazione;
    private ArrayList<Integer> quantità;
    private Utente utenteCatalogo;
    private String nominativo;
    private String telefono;
    private String recapito;
    private String data;
    private String ora;
    private String context;

    /**
     * Vengono visualizzate le pizze prenotabili.
     * L'Admin ha la possibilità di aggiungere o rimuovere pizze dall'elenco.
     * 
     * Gli utenti registrati possono prenotare le pizze e visualizzare la loro scelta.
     * Il catalogo viene visualizzato nuovamente alla conferma o all'annullamento della prenotazione pendente.
     */
    public String visualizzazioneCatalogo() {
        String out = "<label>Nominativo: <input type='text' name='nominativo' id='nominativo' required='required'/></label>";
        out += "<label>Telefono: <input type='text' name='telefono' id='telefono' required='required'/></label><br>";
        out += "<label>Recapito: <input type='text' name='recapito' id='recapito' required='required'/></label><br>";
        out += "<label>Data: <input type='date' name='data' id='data' required='required'/></label>";
        out += "<label>Ora: <input type='time' name='ora' id='ora' required='required'/><br></label>";
        out += "<input type='text' name='visPr' value='3' hidden='hidden'/>"; //campo nascosto conferma a servlet esistenza di prenotazione
        out += "<table><tr><th>Nome</th><th>Ingredienti</th><th>Prezzo</th><th>Quantit&agrave</th>";
        if (utenteCatalogo.isAdmin()) {
            out += "<th></th><th></th>";
        }
        out += "</tr>";
        int i = 0;
        for (Pizza p : pizze) {
            out += "<tr>";
            out += "<td class='c1' id='nomeP" + i + "'>" + p.getNome() + "</td>";
            out += "<td class='c2' id='ingredientiP" + i + "'>" + p.getIngredienti() + "</td>";
            out += "<td class='c3' id='prezzoP" + i + "'>" + p.getPrezzo() + " €</td>";
            out += "<td class='c4'>"
                    + "<span><img id='add" + i + "' name='add" + i + "' src='IMG/plusButton.gif' alt='+' onclick='sostituzione(" + i + ")'/></span>"
                    + "<span><input id='q" + i + "' name='q" + i + "' type='number' style='display: none;' min='0'/></span>"
                    + "</td>";
            if (utenteCatalogo.isAdmin()) {
                out += "<td class='c5'><img src='IMG/cross.png' alt='X' onclick='removePizza(" + i + ")'/></td>";
                out += "<td class='c6'><img src='IMG/pencil.gif' alt='M' onclick='modificaPizza(" + i + ")'/></td>";
            }
            out += "</tr>";
            i++;
        }
        out += "</table>";
        return out;
    }
    
    /**
     * Visualizza la prenotazione da confermare.
     * Vengono visualizzati un tasto per confermare e uno per annullare la prenotazione pendente.
     */
    public String visualizzazionePrenotazione() {
        String out = "<label>Nominativo: " + nominativo + "</label>";
        out += "<label>Telefono: " + telefono + "</label>";
        out += "<label>Recapito: " + recapito + "</label>";
        out += "<label>Data: " + data + "</label>";
        out += "<label>Ora: " + ora + "</label>";
        out += "<table><tr><th>Nome</th><th>Ingredienti</th><th>Quantit&agrave</th><th>Subtotale</th></tr>";
        int i = 0;
        double tot = 0;
        for (Pizza p : prenotazione) {
            int quan = quantità.get(prenotazione.indexOf(p));
            double subtot = p.getPrezzo() * quan;
            tot += subtot;
            out += "<tr>";
            out += "<td class='c1'>" + p.getNome() + "</td>";
            out += "<td class='c2'>" + p.getIngredienti() + "</td>";
            out += "<td class='c3'>" + quan + "</td>";
            out += "<td class='c4'>" + subtot + " €</td>";
            out += "</tr>";
            i++;
        }
        out += "<tr>";
        out += "<td class='c1' style='border-width: 0px;'></td>";
        out += "<td class='c2' style='border-width: 0px;'></td>";
        out += "<td class='c3' style='border-width: 0px;'><b>Totale:</b></td>";
        out += "<td class='c4' style='border-width: 0px;'>" + tot + " €</td>";
        out += "</tr>";
        out += "</table>";

        out += "<input type='text' name='confPr' value='" + tot + "' hidden='hidden'/>"; //conferma definitiva prenotazione
        return out;
    }
    
    /**
     * Se ci sono prenotazioni da confermare, vengono visualizzate queste.
     * Viene visualizzato il catalogo delle pizze prenotabili altrimenti.
     */
    public String selezionaVisualizzazione() {
        if (prenotazione == null || prenotazione.isEmpty()) {
            return visualizzazioneCatalogo();
        } else {
            return visualizzazionePrenotazione();
        }
    }

    /**
     * Visualizza i bottone per aggiungere la prenotazione o cancellarla.
     */
    public String visualizzaBottoni() {
        String out = "";
        if (prenotazione == null || prenotazione.isEmpty()) {
            out += "<label><input type='button' onclick='controlloDati(" + pizze.size() + ")' value='Aggiungi alla prenotazione'/></label>";
        } else {
            out += "<label><input type='button' onclick='isLogged(\"" + utenteCatalogo.getUsername() + "\")' value='Conferma'/></label>";
            out += "<label><input type='button' onclick='window.location.href=\"?page=elimina\"' value='Cancella prenotazione'/></label>";
        }

        return out;
    }

    /**
     * Se l'utente un amministratore ha possibilità di aggiungere nuove pizze al catalogo.
     */
    public String aggiungiPizza() {
        String out = "";
        if (utenteCatalogo.isAdmin() && (prenotazione == null || prenotazione.isEmpty())) {
            out += "<button onclick='visualizzaForm()' style='float:left' >Aggiungi una pizza al Catalogo</button><br>";
            out += "<form  id='divFormAddPizza' method='POST' action='" + context + "/?page=catalogo' style='display: none'>";
            out += "<fieldset>Nome: <input type='text' name='nomeAddPizza' id='nomeAddPizza' required='required'/><br>";
            out += "<input type='hidden' name='actionFormAdd' id='actionFormAdd' value='new'>";
            out += "Ingredienti: <input type='text' name='ingredientiAddPizza' id='ingredientiAddPizza' required='required'/><br>";
            out += "Prezzo: <input type='text' name='prezzoAddPizza' id='prezzoAddPizza' required='required'/><br>";
            out += "<input type='submit' onclick='controlloPrezzo()'><input type='button' onclick='nascondiForm()' value='Chiudi'>";

            out += "</fieldset></form>";
        }
        return out;
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Catalogo</title>
        <link href="stile.css" type="text/css" rel="stylesheet">
        <script src='js_code.js' language="javascript"></script>
    </head>
    <body>
        <div id="wrap">
        <%@include file = "header.html"%>
        <div id="content">
        <%@include file = "nav.html"%>
        <article class="catalogo">
            <p>Visualizzazione catalogo:</p>
            <form id="vis" method="post" action="<%=request.getContextPath()%>/?page=catalogo">

                <%=selezionaVisualizzazione()%>
                <%=visualizzaBottoni()%>
            </form>
            <%=aggiungiPizza()%>
        </article>
        <%@include file = "aside.jsp"%>
        </div>
        <%@include file = "footer.html"%>
        </div>
    </body>
</html>


