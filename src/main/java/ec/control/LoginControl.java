package ec.control;

import ec.model.user.UserDaoDM;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.*;


import java.io.IOException;

public class LoginControl extends HttpServlet {

    private UserDaoDM userDao;
    @Override
    public void init() throws ServletException {
        userDao = new UserDaoDM();
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
}
