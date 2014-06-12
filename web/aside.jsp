<%-- 
    Document   : aside
    Created on : 27-nov-2013, 11.22.43
    Author     : Simone
--%>

<%@page import="Servlet.Utente" session="true"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%--<%String pagina = (request.getContextPath()).toString() + "/Ugo?page=" + request.getParameter("page");%>--%>
<%
    //this.session = session;
    this.request = request;
%>
<%!
    /*sei loggato? ti fa vedere credenziali
     non sei loggato? form per login
     */
    private Utente utente;
    private HttpServletRequest request;
    public String nascondiForm() {
        String out;
        utente = (Utente) request.getSession().getAttribute("user");
        if(request.getParameter("page")==null||!request.getParameter("page").equals("login")||!request.getParameter("page").equals("logout")){
                request.getSession().setAttribute("back", request.getParameter("page")!=null?request.getParameter("page"):"");
            }
        if (utente.getUsername().equals("Guest")) {
            out = "<form method='post' action='"+request.getContextPath()+"/?page=login' id='login'>"
                    + "<label>"
                    + "                Username: <input type='text' id='username' name='username' value='"+(request.getParameter("username")!=null?request.getParameter("username"):"")+"' placeholder='Username' required/>"
                    + "            </label>"
                    + "            "
                    + "            <label>"
                    + "                Password: <input type='password' id='password' name='password' value='"+(request.getParameter("password")!=null?request.getParameter("password"):"")+"'placeholder='Password' required/>"
                    + "            </label>"
                    + "            ";
                    if(request.getParameter("error")!=null){
                        out += "<label class='loginwarning'>Username o Password errati!</label>";
                    }
                    out += "            <input type='submit' value='Login'/>"
                    + "            <input type='reset' value='Cancella'/>"
                    + "            <a href='"+request.getContextPath()+"/?page=registrazione'>Registrati</a>"
                    + "        </form>";
            
        } else {
            out = "Ciao, " + utente.getUsername() + "!<br>"
                    + "<form method='post' action='"+request.getContextPath()+"/?page=logout' id='logout'>"
                    + "<input type='submit' value='Logout'/></form>";
            //request.getSession().setAttribute("back", null);
            //if(utente.isAdmin()){
                out += "<p><a href='?page=setting'>Impostazioni</a></p>";
           // }
        }
        return out;
    }
%>

<aside>
    <h3 style="text-align: center;">Login</h3>

    <fieldset>
        <%=nascondiForm()%>
    </fieldset>

</aside>
