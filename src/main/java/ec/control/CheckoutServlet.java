package ec.control;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import ec.model.CheckOut.CheckOutDaoDM;
import ec.model.CheckOut.Ordine;
import ec.model.CheckOut.StoricoProdottiDaoDM;
import ec.model.PaymentMethod.PayMethod;
import ec.model.PaymentMethod.PaymentDaoDM;
import ec.model.PaymentMethod.StroricoPagamentiDaoDM;
import ec.model.address.AddressDaoDM;
import ec.model.address.AddressUs;
import ec.model.address.StoricoIndirizziDaoDM;
import ec.model.cart.CartDaoDM;
import ec.model.cart.CartItem;
import ec.model.cart.ShoppingCart;
import ec.model.product.ProductBean;
import ec.model.product.ProductDaoDM;
import ec.util.PdfGeneratorHelper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;

import static ec.util.ResponseUtils.sendErrorMessage;
import static ec.util.ResponseUtils.sendJsonResponse;
@WebServlet("/CheckoutServlet")
public class CheckoutServlet extends HttpServlet {
    private CheckOutDaoDM modelCheckOut;
    private StoricoProdottiDaoDM modelStoricoProdotti;

    private StoricoIndirizziDaoDM modelStoricoIndirizzi;
    private StroricoPagamentiDaoDM modelStoricoPagamenti;

    private AddressDaoDM modelAddress;
    private PaymentDaoDM modelPayment;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        modelCheckOut = new CheckOutDaoDM();
        modelStoricoProdotti = new StoricoProdottiDaoDM();
        modelStoricoIndirizzi = new StoricoIndirizziDaoDM();
        modelStoricoPagamenti = new StroricoPagamentiDaoDM();
        modelAddress = new AddressDaoDM();
        modelPayment = new PaymentDaoDM();
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

        try{
            if (opzione == null) {
                sendErrorMessage(response, "Nessuna opzione fornita");
                return;
            }
            switch (opzione) {
                case "add":
                    ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
                    if (cart == null) {
                            sendErrorMessage(response, "Carrello non trovato");
                            return;
                    }
                    handleAddAction(request, response, session);
                    break;
                case "show":
                    handleShowAction(request, response);
                    break;
                case "order":
                    handleOrderAction(request,response);
                    break;
                case "pdf":
                    handlePdfAction(request,response);
                    break;
                case "detail":
                    handleDetailAction(request,response);
                    break;
                default:
                    sendErrorMessage(response, "Azione non riconosciuta");
            }
        } catch (SQLException e) {
           e.printStackTrace();
           sendErrorMessage(response, "Errore nel DB durante opzione: "+ opzione);
        }
    }

    private void handleAddAction(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws SQLException, IOException {
        Ordine ordine = new Ordine();
        String userID = (String) session.getAttribute("userId");
        try {
            int codAddress = Integer.parseInt(request.getParameter("address"));
            int codMethod = Integer.parseInt(request.getParameter("paymentMethod"));

            ordine.setUtenteId(userID);
            ordine.setCodAdress(codAddress);
            ordine.setCodMethod(codMethod);

            ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");

            modelCheckOut.doSave(ordine);
            modelStoricoProdotti.doSave(cart.getItem_ordinati(), ordine.getNumId(), userID);
            modelStoricoIndirizzi.doSave(modelAddress.doRetrieveByKey(userID, codAddress), ordine.getNumId(), userID, codAddress);
            modelStoricoPagamenti.doSave(modelPayment.doRetrieveByKey(userID,codMethod), ordine.getNumId(), userID, codMethod);

            request.setAttribute("acquistoCompletato", true);
            new CartDaoDM().doDeleteCart("userId");
            session.setAttribute("cart", new ShoppingCart());

            sendSuccessResponse(request, response, ordine, cart);
        } catch (NumberFormatException e) {
            sendErrorMessage(response, "Indirizzo o metodo di pagamento non valido");
        }
    }
    private void handleShowAction(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        Collection<Ordine> ordini = modelCheckOut.retriveAllOrdineUtente((String) request.getSession().getAttribute("userId"), null);
        JsonArray jsonOrdini = new JsonArray();

        for (Ordine ord : ordini){
            jsonOrdini.add(getOrderDetails(ord));
        }
        sendJsonResponse(response,true, jsonOrdini);
    }
   private void handleDetailAction (HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        //prende un unico ordine e ne restituisce il json
        Ordine ord = modelCheckOut.retriveOrdineFattura(Integer.parseInt(request.getParameter("orderId")));
        sendJsonResponse(response, true, getOrderDetails(ord));
   }
    private void handlePdfAction (HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        Ordine ordine = modelCheckOut.retriveOrdineFattura(Integer.parseInt(request.getParameter("orderId")));
        try {
            PdfGeneratorHelper.generatePdfInvoice(getOrderDetails(ordine), response, getServletContext());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleOrderAction(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException{
        Collection<Ordine> ordini = modelCheckOut.retriveAllOrdineUtente((String) request.getSession().getAttribute("userId"), request.getParameter("order"));
        JsonArray jsonOrdini = new JsonArray();

        for (Ordine ord : ordini){
            jsonOrdini.add(getOrderDetails(ord));
        }
        sendJsonResponse(response,true, jsonOrdini);
    }

    private JsonObject getOrderDetails(Ordine ordine) throws SQLException {
        JsonObject jsonOrder = new JsonObject();
        AddressUs indirizzo = modelStoricoIndirizzi.doRetrieveByKey(ordine.getNumId(), ordine.getUtenteId(), ordine.getCodAdress());
        PayMethod metodo = modelStoricoPagamenti.doRetrieveByKey(ordine.getNumId(), ordine.getUtenteId(), ordine.getCodAdress());

        jsonOrder.addProperty("ordineId", ordine.getNumId());
        jsonOrder.add("address", indirizzo.toJson());
        jsonOrder.add("paymentMethod", metodo.toJson());
        jsonOrder.addProperty("ordineFattura", ordine.getCodiceFattura());
        if (ordine.getData() != null) {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            jsonOrder.addProperty("dataOrdine", sdf.format(ordine.getData().getTime()));
        }
        double sconto=0;
        double prezzoTot =0;
        JsonArray cartItems = new JsonArray();
        for (ProductBean item : modelStoricoProdotti.retriveOrdineitem(ordine.getNumId(), ordine.getUtenteId()) ){
            JsonObject jsonItem = new JsonObject();
            jsonItem.addProperty("idProdotto", item.getId());
            jsonItem.addProperty("nome", item.getNome());
            jsonItem.addProperty("iva", item.getFasciaIva());
            jsonItem.addProperty("quantity", item.getDisponibilita());
            jsonItem.addProperty("prezzoUnitario", item.getPrezzo());
            jsonItem.addProperty("sconto", item.getPercentualeSconto());
            //trasformo lo stream di byte in un
            //jsonItem.addProperty("immagine", Arrays.toString(item.getImmagineUrl()));
            sconto = (item.getPrezzo()*item.getPercentualeSconto())/100;
            prezzoTot += item.getDisponibilita()* (item.getPrezzo() -sconto);
            cartItems.add(jsonItem);
        }

        jsonOrder.add("cartItems", cartItems);
        jsonOrder.addProperty("prezzototale", prezzoTot);
        return jsonOrder;
    }
    private void sendSuccessResponse(HttpServletRequest request, HttpServletResponse response, Ordine ordine, ShoppingCart cart) throws IOException, SQLException {
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("success", true);

        AddressUs indirizzo = modelStoricoIndirizzi.doRetrieveByKey(ordine.getNumId(), ordine.getUtenteId(), ordine.getCodAdress());
        jsonResponse.add("address", indirizzo.toJson());

        PayMethod metodo = modelStoricoPagamenti.doRetrieveByKey(ordine.getNumId(), ordine.getUtenteId(), ordine.getCodAdress());
        jsonResponse.add("paymentMethod", metodo.toJson());

        double prezzoTot = 0; // Inizializza il prezzo totale a 0

        ProductDaoDM modelItem = new ProductDaoDM();
        JsonArray cartItems = new JsonArray();
        for (CartItem item : cart.getItem_ordinati()) {
            item.getItem().updateDisponibilita(item.getNumItem());
            try {
                modelItem.doUpdateQuantity(item.getItem());
            } catch (SQLException | IOException e) {
                // Gestisci l'errore
                throw new RuntimeException(e);
            }

            double prezzoUnitario = item.getItem().getPrezzo();
            double sconto = (prezzoUnitario * item.getItem().getPercentualeSconto()) / 100;
            double prezzoTotaleElemento = item.getNumItem() * (prezzoUnitario - sconto);
            prezzoTot += prezzoTotaleElemento; // Aggiorna il prezzo totale

            JsonObject jsonItem = new JsonObject();
            jsonItem.addProperty("idProdotto", item.getItem().getId());
            jsonItem.addProperty("iva", item.getItem().getFasciaIva());
            jsonItem.addProperty("nome", item.getItem().getNome());
            jsonItem.addProperty("quantity", item.getNumItem());
            jsonItem.addProperty("prezzoUnitario", prezzoUnitario);
            jsonItem.addProperty("sconto", item.getItem().getPercentualeSconto());
            jsonItem.addProperty("immagine", Arrays.toString(item.getItem().getImmagineUrl()));
            cartItems.add(jsonItem);
        }

        jsonResponse.add("cartItems", cartItems);
        jsonResponse.addProperty("prezzototale", prezzoTot); // Aggiungi il prezzo totale alla risposta

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse.toString());
    }

}
