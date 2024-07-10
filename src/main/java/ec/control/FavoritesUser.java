package ec.control;

import ec.model.favorites.FavoritesDaoDM;
import ec.model.product.ProductBean;
import jakarta.servlet.RequestDispatcher;
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

@WebServlet("/favoritesUser")
public class FavoritesUser extends HttpServlet {
    private FavoritesDaoDM model;

    @Override
    public void init() throws ServletException {
        super.init();
        model = new FavoritesDaoDM();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/login_signup.jsp");
            dispatcher.forward(request, response);
            return;
        }

        try {
            String userId = (String) session.getAttribute("userId");
            Collection<ProductBean> result = model.retriveProductFavorites(userId);

            request.setAttribute("products", result);
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/ProductView.jsp");
            dispatcher.forward(request, response);

        } catch (SQLException e) {
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }
}
