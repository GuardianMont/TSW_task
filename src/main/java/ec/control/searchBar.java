package ec.control;

import com.google.gson.Gson;
import ec.model.product.ProductBean;
import ec.model.product.ProductDaoDM;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import ec.util.ResponseUtils.*;

import static ec.util.ResponseUtils.*;

@WebServlet("/search")
public class searchBar extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");

        if (query != null && !query.isEmpty()) {
            try {
                Collection<ProductBean> searchResults = performSearch(query);
                sendJsonResponse(response,true, searchResults);

            } catch (SQLException e) {
                sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            }
        } else {
            sendErrorResponse(response,HttpServletResponse.SC_BAD_REQUEST," query di ricerca non fornita");
        }
    }

    private Collection<ProductBean> performSearch(String query) throws SQLException, IOException {
        ProductDaoDM productDao = new ProductDaoDM();
        return productDao.searchBarProducts(query);
    }
}
