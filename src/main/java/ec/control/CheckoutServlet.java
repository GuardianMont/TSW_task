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
import com.itextpdf.kernel.PdfException;
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


        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            sendErrorResponse(response, "ID utente non trovato");
            return;
        }

        if (opzione != null) {
            switch (opzione) {
                case "add":
                    try {
                        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
                        if (cart == null) {
                            sendErrorResponse(response, "Carrello non trovato");
                            return;
                        }
                        handleAddAction(request, response, session);
                    } catch (SQLException e) {
                        e.printStackTrace();
                        sendErrorResponse(response, "Errore durante il salvataggio dell'ordine:DB");
                    }catch (IOException e){
                        e.printStackTrace();
                        sendErrorResponse(response, "Errore durante il salvataggio dell'ordine: INPUT OUTPUT");
                    }
                    break;
                case "show":
                    try {
                        handleShowAction(request, response);
                    } catch (SQLException e) {
                        e.printStackTrace();
                        sendErrorResponse(response, "Errore durante l'estrazione degli ordini: DB Error");
                    }
                    break;
                case "order":
                    try{

                        handleOrderAction(request,response);
                    }catch (SQLException e){
                        e.printStackTrace();
                        sendErrorResponse(response, "Errore durante l'estrazione degli ordini secondo ordine: DB Error");

                    }
                    break;
                case "pdf":{
                    try {
                        handlePdfAction(request,response);
                    } catch (SQLException e) {
                        e.printStackTrace();
                        sendErrorResponse(response,"Errore nella generazione del documento pdf");
                    }
                }
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
            generatePdfInvoice(getOrderDetails(ordine), response);
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
    private void sendJsonResponse(HttpServletResponse response,boolean success, Object responseObject) throws IOException {
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("success", success);
        jsonResponse.add("data", new Gson().toJsonTree(responseObject));

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse.toString());

    }
    private void generatePdfInvoice(JsonObject jsonOrder, HttpServletResponse response) throws IOException, URISyntaxException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4);

        PdfFont font = PdfFontFactory.createFont("Times-Roman");
        String imagePath = getServletContext().getRealPath("/uploadFile/logoso.png");

        Image img = new Image(ImageDataFactory.create(imagePath));
        img.setWidth(UnitValue.createPercentValue(50)); // 50% of the page width
        img.setHeight(UnitValue.createPercentValue(50));
        img .setTextAlignment(TextAlignment.CENTER);
        document.add(img);

        LineSeparator ls = new LineSeparator(new SolidLine());
        ls.setMarginBottom(10);
        document.add(ls);

        document.add(new Paragraph("Codice Fattura: " + jsonOrder.get("ordineFattura").getAsString())
                .setFont(font)
                .setFontSize(12)
                .setTextAlignment(TextAlignment.CENTER));

        LineSeparator ls_2 = new LineSeparator(new SolidLine());
        ls_2.setMarginBottom(10);
        document.add(ls_2);

        if (jsonOrder.has("ordineId")) {
            document.add(new Paragraph(
                    "Esercente TavolandoSRL"+
                    "\nOrdine ID: " + jsonOrder.get("ordineId").getAsString() +
                    "\nIn data: " + jsonOrder.get("dataOrdine") +
                    "\nCodice Fattura: " + jsonOrder.get("ordineFattura").getAsString() )
                    .setFont(font)
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.RIGHT));
        }

        LineSeparator ls_3 = new LineSeparator(new SolidLine());
        ls_3.setMarginBottom(10);
        document.add(ls_3);

        float[] columnWidths = {1, 1};
        Table tableInfo = new Table(columnWidths).useAllAvailableWidth();


        if (jsonOrder.has("address")) {
            JsonObject address = jsonOrder.getAsJsonObject("address");
            Cell cell = new Cell().add(new Paragraph("Indirizzo di spedizione: "
            + address.get("via").getAsString() + ", " + address.get("numCiv").getAsString() +
                    "\n cita: " + address.get("citta").getAsString() + "(" + address.get("provincia").getAsString() + "),"
            + address.get("cap").getAsString())
                    .setFont(font)
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.LEFT));
            cell.setBorder(Border.NO_BORDER);
            tableInfo.addCell(cell);
        }

        if (jsonOrder.has("paymentMethod")) {
            JsonObject paymentMethod = jsonOrder.getAsJsonObject("paymentMethod");
            Cell cell = new Cell().add(new Paragraph("Numero carta: " + paymentMethod.get("numCarta").getAsString() +
                    "\ndata scedenza: " + paymentMethod.get("dataScadenza") +
            "\ntitolare carta: " + paymentMethod.get("titolareCarta") +
                    "\ncircuito di pagamento: " + paymentMethod.get("circuito"))
                    .setFont(font)
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.RIGHT));
            cell.setBorder(Border.NO_BORDER);
            tableInfo.addCell(cell);
        }

        document.add(tableInfo);

        if (jsonOrder.has("cartItems")) {
            JsonArray cartItems = jsonOrder.getAsJsonArray("cartItems");
            Table table = new Table(UnitValue.createPercentArray(new float[]{3, 1, 1}));
            table.setWidth(UnitValue.createPercentValue(100));

            table.addHeaderCell(new Cell().add(new Paragraph("Nome Prodotto").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Quantità").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Prezzo").setBold()));

            for (int i = 0; i < cartItems.size(); i++) {
                JsonObject item = cartItems.get(i).getAsJsonObject();
                table.addCell(new Cell().add(new Paragraph(item.get("nome").getAsString())));
                table.addCell(new Cell().add(new Paragraph(item.get("quantity").getAsString())));
                table.addCell(new Cell().add(new Paragraph(item.get("prezzoUnitario").getAsString())));
            }

            document.add(table);
        }

        if (jsonOrder.has("prezzototale")) {
            document.add(new Paragraph("Prezzo Totale: " + jsonOrder.get("prezzototale").getAsString()).setBold());
        }

        document.close();

        // Salva il PDF su file system o restituirlo come risposta
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=fattura.pdf");
        response.setContentLength(baos.size());
        ServletOutputStream out = response.getOutputStream();
        baos.writeTo(out);
        out.flush();
        out.close();
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
