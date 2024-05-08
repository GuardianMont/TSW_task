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
        //todo
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String option = req.getParameter("option");
        //caso tutto apposto
        String dis = "/ProductView.jsp";
//        if(option.equals("login"))
//            doLogin(req, resp);
//        else if(option.equals("signup"))
//            doSignup(req, resp);
        if (option!=null){
            switch (option){
                case "login":
                    if(!doLogin(req,resp)){
                        dis="/login_signup.jsp";
                    }
                    break;
                case "signup":
                    if (!doSignup(req,resp)){
                        dis="/login_signup.jsp";
                    }
            }
        }
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(dis);
        dispatcher.forward(req, resp);
    }

    protected boolean doLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String  token = req.getParameter("login-token");
        String password = req.getParameter("login-password");

        UserBean user = userDao.getUserIfPasswordIsCorrect(token, password);
        if(user != null){
            session.setAttribute("userId", user.getUsername());
            return true;
        }
        else{
            return false;
        }
    }

    protected boolean doSignup(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String password = req.getParameter("signup-password");
        if (password != null) {
            password = password.trim();
        } else {
            return false;
        }
        String repPassword = req.getParameter("signup-rep-password");
        if (repPassword != null) {
            repPassword = repPassword.trim();
        } else {
            return false;
        }
        if (password.equals(repPassword)) {
            try {
                UserBean user = new UserBean();

                user.setUsername(req.getParameter("signup-username"));
                user.setNome(req.getParameter("signup-name"));
                user.setCognome( req.getParameter("signup-surname"));
                user.setEmail( req.getParameter("signup-email"));
                user.setPhoneNumber(req.getParameter("signup-phone"));
                byte[] salt = HashGenerator.generateSalt();
                byte[] passwordHash = HashGenerator.generateHash(req.getParameter("signup-password"), salt);
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
