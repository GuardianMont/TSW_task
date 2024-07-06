package ec.control;

import ec.model.user.UserBean;
import ec.model.user.UserDaoDM;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

import static ec.util.ResponseUtils.sendErrorResponse;
import static ec.util.ResponseUtils.sendJsonResponse;

@WebServlet("/UserAdminOption")
public class UserAdminOption extends HttpServlet {
    private UserDaoDM model;
    public void init() throws ServletException {
        super.init();
        model = new UserDaoDM();
    }
    protected void doGet (HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPost(request,response);
    }

     protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String opzione = request.getParameter("opzione");
        HttpSession sessione= request.getSession(false);

        if(sessione ==null || sessione.getAttribute("userId") == null || sessione.getAttribute("isAdmin") == null|| !(Boolean) sessione.getAttribute("isAdmin")){
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Sessione non valida o  admin non loggato");
        }
        if (opzione!=null){
            switch (opzione){
                case "viewUsers" :
                    try {
                        handleShowAction(request, response);
                    } catch (SQLException e) {
                        sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
                    }
                    break;
                default:
                    sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Opzione non valida");
                    break;
            }
        } else {
            sendErrorResponse (response, HttpServletResponse.SC_BAD_REQUEST, "Opzione non fornita");
        }

     }

     private void handleShowAction (HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
         Collection<UserBean> users = model.doRetrieveAll(null);
         String sessionUserId = (String) request.getSession(false).getAttribute("userId");
         //elimina l'utente in sessione
         if (sessionUserId != null) {
             users.removeIf(user -> user.getUsername().equals(sessionUserId));
         }
         sendJsonResponse(response, true, users);
     }
}
