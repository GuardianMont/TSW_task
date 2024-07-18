package ec.control;

import ec.model.user.UserDaoDM;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import static ec.util.ResponseUtils.sendErrorMessage;

@WebServlet ("/checkAvailable")
//si occupa di controllare se un username o un email siano già stati utilizzati
//in caso affermativo non consente la registrazione all'utente forzandolo a scegliere tra altre opzioni
public class checkAvailable extends HttpServlet {
    private UserDaoDM model;

    public void init() throws ServletException {
        super.init();
        model = new UserDaoDM();
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String opzione = req.getParameter("opzione");
        if (opzione != null) {
            boolean isAvailable;
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            try {
                switch (opzione) {
                    case "checkUser":
                        //caso in cui si controlla se un username è disponibile (non è già stato utilizzato)
                        isAvailable = model.countByUsername(req.getParameter("username")) == 0;
                        // se è disponibile si invia un json in cui lo si specifica
                        // resp.getWriter().write("{\"isAvailable\": " + isAvailable + "}");
                        return;
                    case "checkEmail":
                        //stesso check effettuato sull'username lo si attua sull'email
                        isAvailable = model.countByEmail(req.getParameter("email")) == 0;
                        return;
                    default:
                        sendErrorMessage(resp, "Opzione non riconosciuta");
                }
            } catch (SQLException e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("{\"isAvailable\": false, \"error\": \"" + e.getMessage() + "\"}");
            }
        } else {
            sendErrorMessage(resp, "Opzione non fornita");
        }

    }
}
