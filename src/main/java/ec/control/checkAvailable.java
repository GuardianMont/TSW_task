package ec.control;

import ec.model.ConnectionPool;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet ("/checkAvailable")
public class checkAvailable extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
             String opzione = req.getParameter("opzione");
             if (opzione!=null){
                 switch (opzione){
                     case "checkUser":
                         try {
                             resp.setContentType("application/json");
                             resp.setCharacterEncoding("UTF-8");
                             boolean isAvailable = checkUser(req, resp) == 0;
                             resp.getWriter().write("{\"isAvailable\": " + isAvailable + "}");
                         } catch (SQLException e) {
                             resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                             resp.setContentType("application/json");
                             resp.setCharacterEncoding("UTF-8");
                             resp.getWriter().write("{\"isAvailable\": false, \"error\": \"" + e.getMessage() + "\"}");
                         }
                         return;
                 case "checkEmail":
                     try {
                         resp.setContentType("application/json");
                         resp.setCharacterEncoding("UTF-8");
                         boolean isAvailable = checkEmail(req, resp) == 0;
                         resp.getWriter().write("{\"isAvailable\": " + isAvailable + "}");
                     }
                     catch (SQLException e) {
                         resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                         resp.setContentType("application/json");
                         resp.setCharacterEncoding("UTF-8");
                         resp.getWriter().write("{\"isAvailable\": false, \"error\": \"" + e.getMessage() + "\"}");
                     }
                     return;
                }
             }
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write("{\"isAvailable\": false, \"error\": \"Opzione non fornita\"}");
    }


    protected int checkEmail(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        String checkEmail = "SELECT COUNT(*) as checkEmail from utente WHERE email=? ";
        String emailToCheck = request.getParameter("email");
        int n =0;
        try(Connection connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(checkEmail)){
            preparedStatement.setString(1,emailToCheck);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                n=rs.getInt("checkEmail");
            }
        }
        return n;
    }

    protected int checkUser(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        String checkUser = "SELECT COUNT(*) as checkUser from utente WHERE username=? ";
        String userToCheck = request.getParameter("username");
        int n =0;
        try(Connection connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(checkUser)){
            preparedStatement.setString(1,userToCheck);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                n=rs.getInt("checkUser");
            }
        }
        return n;
    }
}
