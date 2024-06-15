package ec.control;

import com.google.gson.JsonObject;
import ec.model.cart.ShoppingCart;
import ec.model.product.ProductDaoDM;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/updateProduct")
public class UpdateProduct extends HttpServlet{
    ProductDaoDM model;
    @Override
    public void init() throws ServletException {
        super.init();
        model = new ProductDaoDM();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String opzione = req.getParameter("opzione");
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            sendErrorResponse(resp, "Sessione non valida o utente non autenticato");
            return;
        }
        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");

        if (cart == null) {
            sendErrorResponse(resp, "Il carrello risulta inesistente");
            return;
        }

        if (opzione!= null) {
            switch (opzione) {
                case "upadateItemCart":

            }
        }
    }
    private void handleUpdateItemCart (HttpServletResponse response, HttpServletRequest request) {

    }

    private void sendErrorResponse(HttpServletResponse response, String errorMessage) throws IOException {
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("success", false);
        jsonResponse.addProperty("error", errorMessage);
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse.toString());
    }
}

