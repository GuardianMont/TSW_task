package ec.control;

import ec.model.HashGenerator;
import ec.model.user.UserBean;
import ec.model.user.UserDao;
import ec.model.user.UserDaoDM;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/updateUser")
public class UpdateUser extends HttpServlet {
    UserDao userDao;

    @Override
    public void init() throws ServletException {
        userDao = new UserDaoDM();
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    /* il metodo doPost prende il parametro option dalla richiesta
    *  e smista il tipo di richiesta ai metodi giusti
    *
    * option può avere parametri { update, changePassword }
    */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String option = req.getParameter("option");

        //
        if(option != null) {
            switch (option) {
                case "update":
                   doUpdate(req, resp);
                   break;
                case "changePassword":
                    doChangePassword(req, resp);
                    break;
            }
        }
        //se option non è presente reindirizza alla pagina di profilo
        resp.sendRedirect("Profile.jsp");
    }


    // il metodo doUpdate prende i parametri dalla richiesta e aggiorna i dati dell'utente
    private void doUpdate(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException{
        String dis = "/Profile.jsp";
        //TODO: aggiungere controlli sui parametri
        try {
            // Prende le informazioni vecchie dell'utente dal database
            UserBean oldUser = userDao.doRetrieveByKey(req.getParameter("username"));
            UserBean user = new UserBean();

            // Aggiorna i dati dell'utente
            user.setUsername(oldUser.getUsername());
            user.setNome(req.getParameter("nome"));
            user.setCognome(req.getParameter("cognome"));
            user.setEmail(req.getParameter("email"));
            user.setPhoneNumber(req.getParameter("phoneNumber"));
            user.setPassword(oldUser.getPassword());
            user.setSalt(oldUser.getSalt());

            // Se l'aggiornamento non va a buon fine, setta un errore
            if(!userDao.doUpdate(user)){
                req.setAttribute("error", "Errore nell'aggiornamento dei dati");
            }

            // Passa i dati dell'utente alla pagina Profile.jsp
            req.setAttribute("user", user);
            req.getRequestDispatcher(dis).forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write("{\"success\": false, \"error\": \"" + e.getMessage() + "\"}");
        }
    }
    // il metodo doChangePassword prende i parametri dalla richiesta e cambia la password dell'utente
    private void doChangePassword(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException{
        String dis = "/Profile.jsp";
        //TODO: aggiungere controlli sui parametri
        try {
            // Prende le informazioni vecchie dell'utente dal database
            UserBean oldUser = userDao.doRetrieveByKey(req.getParameter("username"));
            UserBean user = new UserBean();

            // Controlla che le nuove password siano uguali
            String newPassword = req.getParameter("newPassword");
            if(!newPassword.equals(req.getParameter("confirmPassword"))){
                // Se le password non coincidono, setta un errore e reindirizza alla pagina di profilo
                req.setAttribute("error", "Le password non coincidono");
                req.setAttribute("user", oldUser);
                req.getRequestDispatcher(dis).forward(req, resp);
                return;
            }

            // Aggiorna la password dell'utente
            user.setUsername(oldUser.getUsername());
            user.setNome(oldUser.getNome());
            user.setCognome(oldUser.getCognome());
            user.setEmail(oldUser.getEmail());
            user.setPhoneNumber(oldUser.getPhoneNumber());

            //utilizza lo stesso salt per la nuova password
            byte[] salt = oldUser.getSalt();
            user.setPassword(HashGenerator.generateHash(newPassword, salt));
            user.setSalt(salt);

            // Se l'aggiornamento non va a buon fine, setta un errore
            if(!userDao.doUpdate(user)){
                req.setAttribute("error", "Errore nell'aggiornamento dei dati");
                req.setAttribute("user", oldUser);
            }else {
                req.setAttribute("user", user);
            }
            req.getRequestDispatcher(dis).forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write("{\"success\": false, \"error\": \"" + e.getMessage() + "\"}");
        }
    }
}
