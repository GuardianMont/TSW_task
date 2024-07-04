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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;

import static ec.util.ResponseUtils.sendErrorMessage;
import static ec.util.ResponseUtils.sendJsonResponse;

@WebServlet("/AdminViewOrders")
public class AdminViewOrders extends HttpServlet {
    private CheckOutDaoDM modelCheckOut;
    private StoricoProdottiDaoDM modelStoricoProdotti;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        modelCheckOut = new CheckOutDaoDM();
        modelStoricoProdotti = new StoricoProdottiDaoDM();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

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

                case "show":
                    handleShowAction(request, response);
                    break;
                case "viewAll":
                    handleViewAllAction(request,response);
                    break;
                case "filterByDate":
                    handleFilterByDate(request,response);
                    break;
                default:
                    sendErrorMessage(response, "Azione non riconosciuta");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            sendErrorMessage(response, "Errore nel DB durante opzione: "+ opzione);
        }
    }

    private void handleShowAction(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        Collection<Ordine> ordini = modelCheckOut.retriveAllOrdineUtente(request.getParameter("userAdmin"), null);
        JsonArray jsonOrdini = new JsonArray();

        for (Ordine ord : ordini){
            jsonOrdini.add(getOrderDetails(ord));
        }
        sendJsonResponse(response,true, jsonOrdini);
    }

    private  void handleViewAllAction (HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        Collection<Ordine> ordini = modelCheckOut.retriveAllOrders();
        JsonArray jsonOrdini = new JsonArray();

        for (Ordine ord : ordini){
            jsonOrdini.add(getOrderDetails(ord));
        }
        sendJsonResponse(response,true, jsonOrdini);
    }

    private  void handleFilterByDate (HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        String startDate=  request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date startDateUtil, endDateUtil ;
        try {
            startDateUtil = dateFormat.parse(startDate);
            endDateUtil = dateFormat.parse(endDate);
        } catch (ParseException e) {
            sendErrorMessage(response, "Errore nel parsing della data ");
            return;
        }
        Collection<Ordine> ordini = modelCheckOut.filterAllOrderByDate(startDateUtil, endDateUtil);
        JsonArray jsonOrdini = new JsonArray();

        for (Ordine ord : ordini){
            jsonOrdini.add(getOrderDetails(ord));
        }
        sendJsonResponse(response,true, jsonOrdini);
    }


    private JsonObject getOrderDetails(Ordine ordine) throws SQLException {
        JsonObject jsonOrder = new JsonObject();

        AddressUs indirizzo = new AddressDaoDM().doRetrieveByKey(ordine.getUtenteId(), ordine.getCodAdress());
        PayMethod metodo = new PaymentDaoDM().doRetrieveByKey(ordine.getUtenteId(), ordine.getCodMethod());

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
}
