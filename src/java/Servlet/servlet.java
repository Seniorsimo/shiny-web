package Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(
        urlPatterns = {""})
/**
 * @author Noce Davide
 * @author Picco Simone
 * @author Vacca Stafania
 */
public class servlet extends HttpServlet {

    private ArrayList<Pizza> pizze; //Il catalogo delle pizze
    private ArrayList<String> utenti; //La lista degli utenti

    /**
     * Il metodo carica il catalogo delle pizze da database, in modo da non
     * dovergli effettuare l'accesso ad ogni operazione
     */
    @Override
    public void init() {
        try {
            DB.init();
            pizze = DB.caricaCatalogo();
            utenti = DB.caricaNomiUtenti();
        } catch (SQLException ex) {
            Logger.getLogger(servlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        RequestDispatcher rd;
        try {
            String page = request.getParameter("page");
            /*
             * Casi particolari:
             * se l'attributo page manca, effettuo un forward alla index
             * se l'attributo page specifica una pagina non prevista, effettuo un forward su error
             */
            //controllo per il funzionamento dello switch: se page = null, allora lo imposto a "" (stringa vuota)
            if (page == null) {
                page = "";
            }
            
           if(request.getSession().getAttribute("user") == null)
               request.getSession().setAttribute("user", new Utente("Guest"));
               
            switch (page) {
                case "":
                    rd = goIndex(request, response);
                    break;
                case "setting":
                    rd = goSetting(request,response);
                    break;
                case "registrazione":
                    rd = goRegistrazione(request, response);
                    break;
                case "prenotazioni":
                    rd = goPrenotazioni(request, response);
                    break;
                case "login":
                    rd = goLogin(request, response);
                    break;
                case "logout":
                    rd = goLogout(request, response);
                    break;
                case "catalogo":
                    rd = goCatalogo(request, response);
                    break;
                case "elimina":
                    rd = goElimina(request, response);
                    break;
                case "prenotazione":
                    rd = goPrenotazioni(request, response);
                    break;
                case "confermaConsegna":
                    rd = goConfermaConsegna(request, response);
                    break;
                case "cancellazionePizza":
                    rd = goEliminaPizza(request, response);
                    break;
                default:
                    rd = goErrore(request, response);
            }
            rd.forward(request, response);
        } finally {
            out.close();
        }
    }
// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Il metodo imposta index.jsp come target per l'RD
     *
     * @param request
     * @param response
     * @return RequestDispatcher settato a index.jsp
     * @throws ServletException
     * @throws IOException
     */
    private RequestDispatcher goIndex(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        return getServletContext().getRequestDispatcher("/index.jsp");
    }
    
    /**
     * Permette all'utente di modificare la password.
     * @param request
     * @param response
     * @return RequestDispatcher settato a setting.jsp
     * @throws ServletException 
     */
    private RequestDispatcher goSetting(HttpServletRequest request, HttpServletResponse response) throws ServletException{
        if(request.getParameter("oldpwd")!=null&&request.getParameter("newpwd")!=null&&request.getParameter("newpwd2")!=null){
            try {
                if(DB.passwordCorretta(((Utente)request.getSession().getAttribute("user")).getUsername(), request.getParameter("oldpwd"))){
                    if(request.getParameter("newpwd").equals(request.getParameter("newpwd2"))){
                        DB.editUser(((Utente)request.getSession().getAttribute("user")).getUsername(), request.getParameter("newpwd"));
                    request.setAttribute("result", "ok");
                    }
                    else{
                         request.setAttribute("result", "badnew");
                    }
                    
                }
                else{
                    request.setAttribute("result", "badpwd");
                }
            } catch (SQLException ex) {
               throw new ServletException(ex);
            }
        }    
        return getServletContext().getRequestDispatcher("/setting.jsp");
    }

    /**
     * Il metodo gestisce le richieste di registrazione.
     *
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    private RequestDispatcher goRegistrazione(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("username") == null || request.getParameter("password") == null || request.getParameter("password2") == null) {
            return getServletContext().getRequestDispatcher("/registrazione.jsp");
        } else {
            try {
                if (DB.controllaUtente(request.getParameter("username"))) {//se l'utente è già presente restituisce l'errore
                    request.setAttribute("errore", "err");
                    return getServletContext().getRequestDispatcher("/registrazione.jsp");
                } else {//registra il nuovo utente
                    DB.aggiungiUtente(request.getParameter("username"), request.getParameter("password"));
                    goLogin(request, response);
                    return getServletContext().getRequestDispatcher("/registrato.html");
                }
            } catch (SQLException ex) {
                throw new ServletException(ex.getMessage());
            }
        }
    }

    /**
     * Il metodo gestisce le richieste di prenotazioni
     *
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    private RequestDispatcher goPrenotazioni(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("prenotazioneCancellataOra") != null) {//controllo solo uno perchè non può essercene solo uno dei due
            //Logger.getGlobal().info("if");
            String data = (String) request.getParameter("prenotazioneCancellataData");
            String ora = (String) request.getParameter("prenotazioneCancellataOra");
            Utente target = (Utente) request.getSession().getAttribute("user");
            if(target.isAdmin()){
                target = new Utente(request.getParameter("prenotazioneCancellataName"));
            }
            try {
                DB.cancellaPrenotazione(target, data, ora);
            } catch (SQLException ex) {
                throw new ServletException(ex.getMessage());
            }
        }
        return getServletContext().getRequestDispatcher("/prenotazioni.jsp");
    }
    
    /**
     * Il metodo cambia lo stato della prenotazione a confermato o annulla la conferma su richiesta dell'admin.
     * @param request
     * @param response
     * @return
     * @throws ServletException prenotazioni.jsp
     * @throws IOException 
     */
    private RequestDispatcher goConfermaConsegna(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = (String) request.getParameter("usernamePrenotazione");
        String data = (String) request.getParameter("dataPrenotazione");
        String ora = (String) request.getParameter("oraPrenotazione");
        String c = (String) request.getParameter("confermaConsegna");//confermato o no
        int consegnato = Integer.parseInt(c);
        try {
            DB.confermaConsegna(username, data, ora, consegnato);
        }catch(SQLException e) {
            throw new ServletException(e.getMessage());    
        }
        
        return getServletContext().getRequestDispatcher("/prenotazioni.jsp");
    }

    /**
     * Il metodo gestisce le richieste di login
     *
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    private RequestDispatcher goLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        if (request.getParameter("username") == null || request.getParameter("password") == null) {
            Logger.getGlobal().warning("null");
        }

        Utente utente = caricaUtente(request.getParameter("username"), request.getParameter("password"));
        if (utente == null) {
            return getServletContext().getRequestDispatcher(request.getSession().getAttribute("back")!=null?"/?page=" + request.getSession().getAttribute("back")+"&error=e":"/index.jsp?error=e");
        } else {
            request.getSession().setAttribute("user", utente);
            if (utente.isAdmin()) {
                request.getSession().setAttribute("utenti", utenti);
            }
            /*CAMBIA A SECONDA DI PROVENIENZA*/
            return getServletContext().getRequestDispatcher(request.getSession().getAttribute("back")!=null?"/?page=" + request.getSession().getAttribute("back")+"":"/index.jsp");
        }
        

    }

    /**
     * Il metodo gestisce i logout.
     *
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    private RequestDispatcher goLogout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String back = (String)request.getSession().getAttribute("back");
        request.getSession().invalidate();
        
        request.getSession().setAttribute("user", new Utente("Guest"));
        return getServletContext().getRequestDispatcher(back!=null?"/?page=" + back+"":"/index.jsp");

    }

    /**
     * Il metodo gestisce le richieste relative al catalogo
     *
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    private RequestDispatcher goCatalogo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String pre = (String) request.getParameter("visPr");
        String confermaPr = (String) request.getParameter("confPr");
        
        //C'è una prenotazione pendente.
        if (pre != null) { //Ho scelto le pizze
            ArrayList<Pizza> prenotazione = new ArrayList<>();
            ArrayList<Integer> quantità = new ArrayList<>();
            String q;
            for (int i = 0; i < pizze.size(); i++) { //Crea la prenotazione e la aggiunge alla sessione
                q = request.getParameter("q" + i);
                if (q != null && !q.equals("") && Integer.parseInt(q) > 0) {
                    Pizza temp = pizze.get(i);
                    prenotazione.add(temp);
                    quantità.add(Integer.parseInt(q));
                }
            }
            request.getSession().setAttribute("prenotazione", prenotazione);
            request.getSession().setAttribute("quantità", quantità);
            request.getSession().setAttribute("data", request.getParameter("data"));
            request.getSession().setAttribute("recapito", request.getParameter("recapito"));
            request.getSession().setAttribute("telefono", request.getParameter("telefono"));
            request.getSession().setAttribute("nominativo", request.getParameter("nominativo"));
            request.getSession().setAttribute("ora", request.getParameter("ora"));
        } else if (confermaPr != null) {
            try {
                //Scelta definitiva
                HttpSession s = request.getSession();
                DB.aggiungiPrenotazione((ArrayList<Pizza>) s.getAttribute("prenotazione"), (ArrayList<Integer>) s.getAttribute("quantità"), ((Utente) s.getAttribute("user")).getUsername(),
                        /*continua*/ (String) s.getAttribute("data"), (String) s.getAttribute("recapito"), (String) s.getAttribute("telefono"), (String) s.getAttribute("nominativo"), (String) s.getAttribute("ora"), confermaPr);
                request.getSession().setAttribute("prenotazione", null);
                return getServletContext().getRequestDispatcher("/prenotazioni.jsp");
            } catch (SQLException ex) {
                throw new ServletException(ex.getMessage());
            }
        
        //non ci sono prenotazioni pendenti e viene visualizzato il catalogo delle pizze.
        } else {

            
            if(request.getParameter("actionFormAdd")!=null){
                if(request.getParameter("actionFormAdd").equals("new")){
                    try {
                        //inserisco nuova pizza
                        DB.editPizza(request.getParameter("nomeAddPizza"), request.getParameter("ingredientiAddPizza"), request.getParameter("prezzoAddPizza"), true);
                    } catch (SQLException ex) {
                        throw new ServletException(ex.getMessage());
                    }
                }
                else{
                    //modifico una pizza
                     try {
                        //inserisco nuova pizza
                        DB.editPizza(request.getParameter("nomeAddPizza"), request.getParameter("ingredientiAddPizza"), request.getParameter("prezzoAddPizza"), false);
                    } catch (SQLException ex) {
                        throw new ServletException(ex.getMessage());
                    }
                    
                }
                init();
            }
            request.setAttribute("pizze", pizze);
        }
        return getServletContext().getRequestDispatcher("/catalogo.jsp");
    }

    /**
     * Il metodo gestisce i casi di errore
     *
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    private RequestDispatcher goErrore(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        return getServletContext().getRequestDispatcher("/errore.html");
    }
    
    /**
     * Annulla la prenotazione pendente.
     * @param request
     * @param response
     * @return Dispatcher del catalogo.
     * @throws ServletException
     * @throws IOException 
     */
    private RequestDispatcher goElimina(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getSession().setAttribute("prenotazione", null);
        request.getSession().setAttribute("quantità", null);
        Logger.getGlobal().info((String) request.getSession().getAttribute("prenotazione"));
        Logger.getGlobal().info((String) request.getSession().getAttribute("quantità"));
        return goCatalogo(request, response);
    }
    
    /**
     * Elimina una pizza dal catalogo
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException 
     */
    private RequestDispatcher goEliminaPizza(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(((Utente)request.getSession().getAttribute("user")).isAdmin()){
            Pizza pizza = pizze.get(Integer.parseInt(request.getParameter("index")));
            try {
                DB.removePizza(pizza.getNome());
            } catch (SQLException ex) {
                Logger.getLogger(servlet.class.getName()).log(Level.SEVERE, null, ex);
                throw new ServletException(ex.getMessage());
            }
        }
        init();//aggiorna la lista delle pizze
        
        return goCatalogo(request, response);
    }

    /**
     * Il metodo Crea un oggeto utene caricandone i parametri dal database
     *
     * @param name
     * @param pass
     * @return Utente
     * @throws ServletException
     */
    private Utente caricaUtente(String name, String pass) throws ServletException {
        try {
            return DB.caricaUtente(name, pass);
        } catch (SQLException e) {
            throw new ServletException(e.getMessage());
        }
    }

    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    
}
