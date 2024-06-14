package ec.control;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import ec.model.CheckOut.CheckOutDaoDM;
import ec.model.CheckOut.Ordine;
import ec.model.CheckOut.StoricoProdottiDaoDM;
import ec.model.PaymentMethod.PayMethod;
import ec.model.PaymentMethod.PaymentDaoDM;
import ec.model.address.AddressDaoDM;
import ec.model.address.AddressUs;
import ec.model.cart.ShoppingCart;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/CheckoutServlet")
public class CheckoutServlet extends HttpServlet {
    private CheckOutDaoDM modelCheckOut;
    private StoricoProdottiDaoDM modelStoricoProdotti;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        modelCheckOut = new CheckOutDaoDM();
        modelStoricoProdotti = new StoricoProdottiDaoDM();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String opzione = request.getParameter("opzione");
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            sendErrorResponse(response, "Sessione non valida o utente non autenticato");
            return;
        }

        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
        if (cart == null) {
            sendErrorResponse(response, "Carrello non trovato");
            return;
        }

        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            sendErrorResponse(response, "ID utente non trovato");
            return;
        }

        if (opzione != null) {
            switch (opzione) {
                case "add":
                    try {
                        handleAddAction(request, response, session);
                    } catch (SQLException e) {
                        e.printStackTrace();
                        sendErrorResponse(response, "Errore durante il salvataggio dell'ordine:DB");
                    }catch (IOException e){
                        e.printStackTrace();
                        sendErrorResponse(response, "Errore durante il salvataggio dell'ordine: INPUT OUTPUT");
                    }
                    break;
                default:
                    sendErrorResponse(response, "Azione non riconosciuta");
            }
        } else {
            sendErrorResponse(response, "Nessuna opzione fornita");
        }
    }

    private void handleAddAction(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws SQLException, IOException {
        Ordine ordine = new Ordine();
        ordine.setUtenteId((String) session.getAttribute("userId"));
        ordine.setCodAdress(Integer.parseInt(request.getParameter("address")));
        ordine.setCodMethod(Integer.parseInt(request.getParameter("paymentMethod")));
        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");

        modelCheckOut.doSave(ordine);
        modelStoricoProdotti.doSave(cart.getItem_ordinati(), ordine.getNumId(), (String) session.getAttribute("userId"));
        request.setAttribute("acquistoCompletato", true);
        session.setAttribute("cart", new ShoppingCart());
        sendSuccessResponse(request, response,ordine, cart);
    }

    private void sendSuccessResponse(HttpServletRequest request, HttpServletResponse response, Ordine ordine, ShoppingCart cart) throws IOException, SQLException {
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("success", true);

        AddressUs indirizzo = new AddressDaoDM().doRetrieveByKey(request.getParameter("userId"),ordine.getCodAdress() );
        jsonResponse.add("address", indirizzo.toJson());
        PayMethod metodo= new PaymentDaoDM().doRetrieveByKey(request.getParameter("userId"),ordine.getCodMethod() );
        jsonResponse.add("paymentMethod", metodo.toJson() );
        jsonResponse.addProperty("spesa", cart.getPrezzoTot());
        JsonArray cartItems = new JsonArray();
        cart.getItem_ordinati().forEach(item -> {
            JsonObject jsonItem = new JsonObject();
            jsonItem.addProperty("nome", item.getItem().getNome());
            jsonItem.addProperty("quantity", item.getNumItem());
            jsonItem.addProperty("prezzo", item.getItem().getPrezzo());
            cartItems.add(jsonItem);
        });
        jsonResponse.add("cartItems", cartItems);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse.toString());
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
