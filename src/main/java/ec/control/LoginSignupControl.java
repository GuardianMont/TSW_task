package ec.control;

import ec.model.HashGenerator;
import ec.model.user.UserBean;
import ec.model.user.UserDaoDM;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.*;


import javax.management.BadAttributeValueExpException;
import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

@WebServlet ("/LoginSignup")
public class LoginSignupControl extends HttpServlet {
    private UserDaoDM userDao;

    @Override
    public void init() throws ServletException {
        userDao = new UserDaoDM();
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String option = req.getParameter("option");
        //caso tutto apposto
        String dis = "/ProductView.jsp";
        String errorMessage="";
        if (option!=null){
            switch (option){
                case "login":
                    if(!doLogin(req,resp)){
                        dis="/login_signup.jsp";
                        errorMessage="Login fallito. username o password errati";
                    }
                    return;
                case "signup":
                    if (!doSignup(req,resp)){
                        dis="/login_signup.jsp";
                        errorMessage="registrazione fallita";
                    }
            }
        }
        req.setAttribute("errorMessage", errorMessage);
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(dis);
        dispatcher.forward(req, resp);
    }

    protected boolean doLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String  token = req.getParameter("login-token");
        String password = req.getParameter("login-password");

        if(password.length()>=100) return false;

        UserBean user = userDao.getUserIfPasswordIsCorrect(token, password);
        if(user != null){
            session.setAttribute("userId", user.getUsername());
            String referer = req.getHeader("referer");
            resp.sendRedirect(referer);
            return true;
        }
        else{
            return false;
        }
    }

    protected boolean doSignup(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String username = req.getParameter("signup-username").trim();
        if(!username.replaceAll(" ", "").equals(username))
            return false;

        String name = req.getParameter("signup-name").trim();

        String surname = req.getParameter("signup-surname").trim();

        String email = req.getParameter("signup-email").trim();

        String phoneN = req.getParameter("signup-phone").trim();

        String password = req.getParameter("signup-password").trim();

        String repPassword = req.getParameter("signup-rep-password").trim();

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
                userDao.doSave(user);

                HttpSession session = req.getSession();
                session.setAttribute("userId", user.getUsername());

                return true;
            } catch (SQLIntegrityConstraintViolationException e) {
                e.printStackTrace();
            } catch (BadAttributeValueExpException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();//potremmo voler dare un errore diverso, per ora lo lascio così
            } finally {
                return false;
            }
        }
        return false;
    }

}
