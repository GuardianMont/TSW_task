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
import ec.model.product.ProductBean;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Base64;

import static ec.util.ResponseUtils.sendErrorMessage;

@WebServlet("/DetailOrder")
public class DetailOrder extends HttpServlet {
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
            sendErrorMessage(response, "Sessione non valida o utente non autenticato");
            return;
        }

        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            sendErrorMessage(response, "ID utente non trovato");
            return;
        }

        try {
            if (opzione == null) {
                sendErrorMessage(response, "Nessuna opzione fornita");
                return;
            }
            switch (opzione) {
                case "detail":
                    handleDetailAction(request, response);
                    break;
                default:
                    sendErrorMessage(response, "Azione non riconosciuta");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            sendErrorMessage(response, "Errore nel DB durante l'esecuzione dell'opzione: " + opzione);
        }
    }

    private void handleDetailAction(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        try {
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            Ordine ordine = modelCheckOut.retriveOrdineFattura(orderId);
            if (ordine == null) {
                sendErrorMessage(response, "Ordine non trovato");
                return;
            }
            getOrderDetails(request, response, ordine);
        } catch (NumberFormatException e) {
            sendErrorMessage(response, "ID ordine non valido");
        }
    }

    private void getOrderDetails(HttpServletRequest request, HttpServletResponse response, Ordine ordine) throws SQLException, IOException {
        JsonObject jsonOrder = new JsonObject();
        try {
            AddressUs indirizzo = new AddressDaoDM().doRetrieveByKey(ordine.getUtenteId(), ordine.getCodAdress());
            PayMethod metodo = new PaymentDaoDM().doRetrieveByKey(ordine.getUtenteId(), ordine.getCodMethod());

            jsonOrder.add("address", indirizzo.toJson());
            jsonOrder.add("paymentMethod", metodo.toJson());
            jsonOrder.addProperty("ordineFattura", ordine.getCodiceFattura());
            if (ordine.getData() != null) {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                jsonOrder.addProperty("dataOrdine", sdf.format(ordine.getData().getTime()));
            }

            double sconto;
            double prezzoTot = 0;
            JsonArray cartItems = new JsonArray();
            for (ProductBean item : modelStoricoProdotti.retriveOrdineitem(ordine.getNumId(), ordine.getUtenteId())) {
                JsonObject jsonItem = new JsonObject();
                jsonItem.addProperty("idProdotto", item.getId());
                jsonItem.addProperty("nome", item.getNome());
                jsonItem.addProperty("iva", item.getFasciaIva());
                jsonItem.addProperty("quantity", item.getDisponibilita());
                jsonItem.addProperty("prezzoUnitario", item.getPrezzo());
                jsonItem.addProperty("sconto", item.getPercentualeSconto());

                byte[] imageData = item.getImmagineUrl();
                if (imageData != null) {
                    String base64Image = Base64.getEncoder().encodeToString(imageData);
                    jsonItem.addProperty("immagine", "data:image/jpeg;base64," + base64Image);
                }
                sconto = (item.getPrezzo() * item.getPercentualeSconto()) / 100;
                prezzoTot += item.getDisponibilita() * (item.getPrezzo() - sconto);
                cartItems.add(jsonItem);
            }

            jsonOrder.add("cartItems", cartItems);
            jsonOrder.addProperty("prezzototale", prezzoTot);
            jsonOrder.addProperty("success", true);

        } catch (SQLException e) {
            e.printStackTrace();
            sendErrorMessage(response, "Errore nel recupero dei dettagli dell'ordine");
            return;
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonOrder.toString());
    }
}
