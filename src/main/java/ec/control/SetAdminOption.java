package ec.control;

import ec.model.user.UserDaoDM;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

import static ec.util.ResponseUtils.sendErrorMessage;
import static ec.util.ResponseUtils.sendJsonResponse;

@WebServlet("/SetAdminOption")
//è una servlet per la gestione del setting dei ruoli
//ovvero da utente normale un admin può decidere di trasformarlo in un altro admin
public class SetAdminOption extends HttpServlet {
    private UserDaoDM modelUser;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        modelUser = new UserDaoDM();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String opzione = request.getParameter("opzione");
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            sendErrorMessage(response, "Sessione non valida o utente non autenticato");
            return;
        }

        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            sendErrorMessage(response, "ID utente non trovato");
            return;
        }

        try{
            if (opzione == null) {
                sendErrorMessage(response, "Nessuna opzione fornita");
                return;
            }
            switch (opzione) {

                case "roleAdmin":
                    modelUser.UpdateRole(request.getParameter("userAdmin"), Boolean.parseBoolean(request.getParameter("role")));
                    break;
                default:
                    sendErrorMessage(response, "Azione non riconosciuta");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            sendErrorMessage(response, "Errore nel DB durante opzione: "+ opzione);
        }
        //se va tutto bene l'utente diventa admin
        sendJsonResponse(response,true,null);
    }

}
