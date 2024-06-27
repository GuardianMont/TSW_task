package ec.control;

import ec.model.product.ProductBean;
import ec.model.product.ProductDaoDM;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

import static ec.util.ResponseUtils.sendErrorResponse;
import static ec.util.ResponseUtils.sendJsonResponse;
@WebServlet("/searchForm")
public class searchForm extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");

        if (query != null && !query.isEmpty()) {
            try {
                Collection<ProductBean> searchResults = performSearch(query);
                request.setAttribute("products", searchResults);
                response.setContentType("text/plain");
                response.setCharacterEncoding("UTF-8");
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/ProductView.jsp");
                dispatcher.forward(request, response);

            } catch (SQLException e) {
                sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            }
        } else {
            sendErrorResponse(response,HttpServletResponse.SC_BAD_REQUEST," query di ricerca per il form non fornita");
        }
    }

    private Collection<ProductBean> performSearch(String query) throws SQLException, IOException {
        ProductDaoDM productDao = new ProductDaoDM();
        return productDao.searchBarProducts(query);
    }
}
