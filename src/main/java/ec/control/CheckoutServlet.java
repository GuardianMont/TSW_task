package ec.control;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.ILineDrawer;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

import ec.model.CheckOut.CheckOutDaoDM;
import ec.model.CheckOut.Ordine;
import ec.model.CheckOut.StoricoProdottiDaoDM;
import ec.model.PaymentMethod.PayMethod;
import ec.model.PaymentMethod.PaymentDaoDM;
import ec.model.address.AddressDaoDM;
import ec.model.address.AddressUs;
import ec.model.cart.ShoppingCart;
import ec.model.product.ProductBean;
import ec.model.product.ProductDaoDM;
import ec.util.PdfGeneratorHelper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Collection;
import static ec.util.ResponseUtils.*;
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
    private void handleShowAction(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        Collection<Ordine> ordini = modelCheckOut.retriveAllOrdineUtente((String) request.getSession().getAttribute("userId"), null);
        JsonArray jsonOrdini = new JsonArray();

        for (Ordine ord : ordini){
            jsonOrdini.add(getOrderDetails(ord));
        }
        sendJsonResponse(response,true, jsonOrdini);
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

        double prezzoTot =0;
        JsonArray cartItems = new JsonArray();
        for (ProductBean item : modelStoricoProdotti.retriveOrdineitem(ordine.getNumId(), ordine.getUtenteId()) ){
            JsonObject jsonItem = new JsonObject();
            jsonItem.addProperty("nome", item.getNome());
            jsonItem.addProperty("iva", item.getFasciaIva());
            jsonItem.addProperty("quantity", item.getDisponibilita());
            jsonItem.addProperty("prezzoUnitario", item.getPrezzo());
            jsonItem.addProperty("sconto", item.getPercentualeSconto());
            prezzoTot += item.getPrezzo();
            cartItems.add(jsonItem);
        }

        jsonOrder.add("cartItems", cartItems);
        jsonOrder.addProperty("prezzototale", prezzoTot);
        return jsonOrder;
    }
    private void sendSuccessResponse(HttpServletRequest request, HttpServletResponse response, Ordine ordine, ShoppingCart cart) throws IOException, SQLException {
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("success", true);

        AddressUs indirizzo = new AddressDaoDM().doRetrieveByKey(ordine.getUtenteId(), ordine.getCodAdress());
        jsonResponse.add("address", indirizzo.toJson());

        PayMethod metodo = new PaymentDaoDM().doRetrieveByKey(ordine.getUtenteId(), ordine.getCodMethod());
        jsonResponse.add("paymentMethod", metodo.toJson());
        jsonResponse.addProperty("spesa", cart.getPrezzoTot());

        ProductDaoDM modelItem = new ProductDaoDM();
        JsonArray cartItems = new JsonArray();
        cart.getItem_ordinati().forEach(item -> {
            item.getItem().updateDisponibilita(item.getNumItem());
            try {
                modelItem.doUpdateQuantity(item.getItem());
            } catch (SQLException |IOException e) {
                //scrittura di un errore
                throw new RuntimeException(e);
            }
            JsonObject jsonItem = new JsonObject();
            jsonItem.addProperty("iva", item.getItem().getFasciaIva());
            jsonItem.addProperty("nome", item.getItem().getNome());
            jsonItem.addProperty("quantity", item.getNumItem());
            jsonItem.addProperty("prezzoUnitario", item.getItem().getPrezzo());
            jsonItem.addProperty("sconto", item.getItem().getPercentualeSconto());
            cartItems.add(jsonItem);
        });
        jsonResponse.add("cartItems", cartItems);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse.toString());
    }

}
