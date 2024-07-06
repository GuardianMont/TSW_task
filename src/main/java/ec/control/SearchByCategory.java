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
import java.util.ArrayList;

import static ec.util.ResponseUtils.sendErrorResponse;
import static ec.util.ResponseUtils.sendJsonResponse;

@WebServlet("/SearchByCategory")
public class SearchByCategory extends HttpServlet {
    private ProductDaoDM model;

    @Override
    public void init() throws ServletException {
        super.init();
        model = new ProductDaoDM();
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
        String categoria = request.getParameter("categoria");

        if (categoria != null && !categoria.isEmpty()) {
            try {
                Collection<ProductBean> result = new ArrayList<>();
                switch (categoria) {
                    case "tavolo":
                        result = handleTavoloSearch();
                        break;
                    case "attrezzatura":
                        result = handleAttrezzaturaSearch();
                        break;
                    case "kit":
                    case "pingPong":
                    case "biliardo":
                    case "AirHokey":
                    case "multifunzione":
                    case "racchette":
                    case "stecche":
                    case "bilie":
                    case "palline":
                    case "AttrezzaturaAir":
                        result = handleSpecificSearch(categoria);
                        break;
                    default:
                        sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Categoria non valida fornita");
                        return;
                }

                request.setAttribute("products", result);
                response.setContentType("text/plain");
                response.setCharacterEncoding("UTF-8");
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/ProductView.jsp");
                dispatcher.forward(request, response);

            } catch (SQLException e) {
                sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            }
        } else {
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Query di ricerca non fornita");
        }
    }

    private Collection<ProductBean> handleTavoloSearch() throws SQLException {
        Collection<ProductBean> result = new ArrayList<>();
        result.addAll(model.SearchByCategory("biliardo"));
        result.addAll(model.SearchByCategory("pingPong"));
        result.addAll(model.SearchByCategory("AirHokey"));
        result.addAll(model.SearchByCategory("multifunzione"));
        return result;
    }

    private Collection<ProductBean> handleAttrezzaturaSearch() throws SQLException {
        Collection<ProductBean> result = new ArrayList<>();
        result.addAll(model.SearchByCategory("racchette"));
        result.addAll(model.SearchByCategory("stecche"));
        result.addAll(model.SearchByCategory("bilie"));
        result.addAll(model.SearchByCategory("palline"));
        result.addAll(model.SearchByCategory("AttrezzaturaAir"));
        return result;
    }

    private Collection<ProductBean> handleSpecificSearch(String categoria) throws SQLException {
        return model.SearchByCategory(categoria);
    }
}
