package ec.control;

import ec.model.HashGenerator;
import ec.model.user.UserBean;
import ec.model.user.UserDaoDM;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.*;


import javax.management.BadAttributeValueExpException;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

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
        if(option.equals("login"))
            doLogin(req, resp);
        else if(option.equals("signup"))
            doSignup(req, resp);
    }

    protected void doLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String dis ="/ProductView.jsp";
        String  name = req.getParameter("login-token");
        String password = req.getParameter("login-password");


        if(userDao.checkPassword(name, password)){
            session.setAttribute("userId", name);
        }
        else{
            dis = "login_signup.jsp";
        }

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(dis);
        dispatcher.forward(req, resp);

    }

    protected void doSignup(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String dis = "/ProductView.jsp";

        if(req.getParameter("signup-password").equals(req.getParameter("signup-rep-password"))){

            try {

                UserBean user = new UserBean();
                user.setUsername((String) req.getAttribute("signup-username"));
                user.setNome((String) req.getAttribute("signup-name"));
                user.setCognome((String) req.getAttribute("signup-surname"));
                user.setEmail((String) req.getAttribute("signup-email"));
                byte[] salt = HashGenerator.generateSalt();
                byte[] passwordHash = HashGenerator.generateHash((String) req.getAttribute("signup-password"), salt);
                user.setPassword(passwordHash);
                user.setSalt(salt);

                userDao.doSave(user);
            }catch (SQLIntegrityConstraintViolationException e){
                dis = "/login_signup.jsp";
            }catch (BadAttributeValueExpException e){
                e.printStackTrace();
            }catch (SQLException e){
                e.printStackTrace();//potremmo voler dare un errore diverso, per ora lo lascio così
            }
        }else{
            dis = "/login_signup.jsp";
        }
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(dis);
        dispatcher.forward(req, resp);

    }
}
