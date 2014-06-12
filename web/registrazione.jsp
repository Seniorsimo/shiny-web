<%-- 
    Document   : registrazione
    Created on : 27-nov-2013, 11.12.53
    Author     : Simone
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" session="true"%>
<% error = (String) request.getAttribute("errore");%>
<%!
    private String error;
    public String utentePresente(){
        String out = "";
        if(error!=null){
            out += "<label>L'utente &egrave gi&agrave presente nel database</label><br>";
        }
        return out;
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link href="stile.css" type="text/css" rel="stylesheet">
        <script lang="javascript">
            function controlloDati(){
                if(document.getElementById("password").value === document.getElementById("password2").value&&document.getElementById("username").value!==""){
                    document.getElementById("login").submit();
                    //var stateObj = { foo: "bar" };
                    history.replaceState(document.referrer);
                }
                else{
                    document.getElementById("error").style.display = "inline";
                }
                
            }
        </script>
    </head>
    <body>
        <div id="wrap">
        <%@include file = "header.html"%>
        <div id="content">
        <%@include file = "nav.html"%>
        <article class="article_registrazione">
            <h3 style="text-align: center;">Registrazione</h3>

            <fieldset>
                <form style="padding: 2%;" action="<%=request.getContextPath()%>/?page=registrazione" id="login" method="post">
                    <label>
                        Username: <input type="text" name="username" id="username" placeholder="Username" value="<%=request.getParameter("username")!=null?request.getParameter("username"):""%>" required="required"/>
                    </label><br>

                    <label>
                        Password: <input type="password" name="password" id="password" placeholder="Password" value="<%=request.getParameter("password")!=null?request.getParameter("password"):""%>" required="required"/>
                        Conferma password: <input type="password" name="password2" id="password2" placeholder="Ripeti password" value="<%=request.getParameter("password2")!=null?request.getParameter("password2"):""%>" required="required"/>
                    </label><br>
                    <label id="error" style="display: none">
                        Le due password non sono unguali
                    </label><br>
                    <%=utentePresente()%>
                    <input type="button" onclick="controlloDati();"  value="Registrati"/>
                    <input type="reset" value="Cancella"/>
                </form>
            </fieldset>
        </article>
        <!--<aside></aside>-->
        </div>
            <%@include file = "footer.html"%>
        </div>
    </body>
</html>
