package ec.control;

import ec.model.user.UserDao;
import ec.model.user.UserDaoDM;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
@WebServlet("/profileServlet")
public class Profile extends HttpServlet {

    UserDao userDao;
    @Override
    public void init() throws ServletException {
        userDao = new UserDaoDM();
        super.init();
    }

    /*
    * il metodo doGet prende le informazioni di profilo
    * dell'utente loggato e le mostra nella pagina Profile.jsp
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String dis = "/Profile.jsp";

        try {
            //Prende lo userId dalla sessione
            HttpSession session = req.getSession();
            String userId = (String) session.getAttribute("userId");
            if (userId == null) {
                resp.sendRedirect("login_signup.jsp");
                return;
            }
            //Prende le informazioni dell'utente dal database e le passa alla pagina Profile.jsp
            req.setAttribute("user", userDao.doRetrieveByKey(userId));
            req.getRequestDispatcher(dis).forward(req, resp);

        }catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write("{\"success\": false, \"error\": \"" + e.getMessage() + "\"}");
        }

    }
}
