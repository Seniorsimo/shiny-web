<%-- 
    Document   : index
    Created on : 26-nov-2013, 15.09.03
    Author     : Simone
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" session="true"%>
<% u = (Utente) request.getSession().getAttribute("user");%>
<% msg = (String) request.getAttribute("result");%>
<%!    private Utente u;
    private String msg;

    private String visualizzaSetting() {
        String out = "";
        out += "<form action='?page=setting' method='post'>"
                + "<fieldset>"
                + "<label>Modifica password</label><br>"
                + "<label>Vecchia password:<input type='password' name='oldpwd' required='required'/></label><br>"
                + "<label>Nuova password:<input type='password' name='newpwd' required='required'/></label><br>"
                + "<label>Ripeti password:<input type='password' name='newpwd2' required='required'/></label><br>"
                + "<input type='submit' value='Modifica'/>";
        if(msg!=null&&!msg.equals("")){
            if(msg.equals("badpwd")){
                out +="La password inserita non è valida";
            }
            else if(msg.equals("badnew")){
                out +="I campi password e conferma password non sono corretti.";
            }
            else{
                out +="Password modificata correttamente.";
            }
        }
               out += "</fieldset>"
                + "</form>";
        if (u.isAdmin()) {
            out += "Altre opzioni di modifica solo per l'admin";
        }

        return out;
    }


%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Impostazioni</title>
        <link href="stile.css" type="text/css" rel="stylesheet">
    </head>
    <body>
        <div id="wrap">
        <%@include file = "header.html"%>
        <div id="content" class="setting">
        <%@include file = "nav.html"%>
        <article>
            <%=visualizzaSetting()%>
        </article>
        <%@include file = "aside.jsp"%>
        </div>
        <%@include file = "footer.html"%>



        </div>
    </body>
</html>
