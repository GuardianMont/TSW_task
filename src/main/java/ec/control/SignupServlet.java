package ec.control;

import ec.model.HashGenerator;
import ec.model.user.UserBean;
import ec.model.user.UserDaoDM;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import javax.management.BadAttributeValueExpException;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

@WebServlet("/Signup")
//si occupa solo del signup di un utente
public class SignupServlet extends HttpServlet {
    private UserDaoDM userDao;

    @Override
    public void init() throws ServletException {
        userDao = new UserDaoDM();
        super.init();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            if (!doSignup(req, resp)) {
                //se ci sono problemi nella funzione di registrazione allora reinderizza nuovamente
                //al form di registrazione
                req.setAttribute("errorMessage", "registrazione fallita");
                req.getRequestDispatcher("/login_signup.jsp").forward(req, resp);
            }
        } catch (SQLException | BadAttributeValueExpException e) {
            throw new ServletException(e);
        }
    }

    private boolean doSignup(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException, BadAttributeValueExpException {
        String username = req.getParameter("signup-username").trim();
        String name = req.getParameter("signup-name").trim();
        String surname = req.getParameter("signup-surname").trim();
        String email = req.getParameter("signup-email").trim();
        String phoneN = req.getParameter("signup-phone").trim();
        String password = req.getParameter("signup-password").trim();
        String repPassword = req.getParameter("signup-rep-password").trim();
        //username e email non possono contenere spazi
        // è check che viene fatto sia in lato js che backend
        if (username.contains(" ") || email.contains(" ")) {
            return false;
        }
        //se password e password ripetuta coincidono è tutto apposto e si crea l'istanza utente
        if (password.equals(repPassword)) {
            try {
                UserBean user = new UserBean();
                user.setUsername(username);
                user.setNome(name);
                user.setCognome(surname);
                user.setEmail(email);
                user.setPhoneNumber(phoneN);
                byte[] salt = HashGenerator.generateSalt();
                byte[] passwordHash = HashGenerator.generateHash(password, salt);
                user.setPassword(passwordHash);
                user.setSalt(salt);
                user.setAdmin(false);
                userDao.doSave(user);
                HttpSession session = req.getSession();
                session.setAttribute("userId", user.getUsername());
                session.setAttribute("signupSuccess", true);
                resp.sendRedirect(req.getContextPath() + "/ProductView.jsp");
                return true;
            } catch (SQLIntegrityConstraintViolationException e) {
                e.printStackTrace();
            } catch (BadAttributeValueExpException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //altrimenti se non vi è coincidenza non fa nulla
        return false;
    }
}
