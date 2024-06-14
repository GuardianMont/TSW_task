package ec.control;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import ec.model.HashGenerator;
import ec.model.PaymentMethod.PayMethod;
import ec.model.PaymentMethod.PaymentDaoDM;

import jakarta.servlet.ServletConfig;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import javax.management.BadAttributeValueExpException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

@WebServlet ("/payMethodsManager")
public class Payment extends HttpServlet {
    private PaymentDaoDM model;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        model = new PaymentDaoDM();
    }

    public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
    public void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String opzione = request.getParameter("opzione");
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Sessione non valida o utente non loggato");
            return;
        }
        if (opzione != null) {
            switch (opzione){
                case "add":
                    if(request.getSession().getAttribute("userId")!=null) {
                        try {
                            handleAddAction(request,response);
                        } catch (SQLException | IOException e) {
                            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
                        }
                    }
                    break;
                case "show":
                    try {
                        request.setAttribute("payMethods", model.doRetrieveAll((String) request.getSession().getAttribute("userId")));
                        handleShowAction(request,response);
                    } catch (SQLException e) {
                        sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
                    }
                    break;
                default:
                    sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Opzione non valida");
                    break;
            }
        }else {
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Opzione non fornita");
        }
    }

    private void handleAddAction(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        PayMethod pay = new PayMethod();
        //hash cvv e numero carta e scadenza ??
        pay.setNumCarta(request.getParameter("NumeroCarta"));
        String mese = request.getParameter("meseScadenza");
        String anno = request.getParameter("annoScadenza");
        pay.setDataScadenza(mese + "/" + anno);
        pay.setCircuito(request.getParameter("circuito"));
        pay.setTitolareCarta(request.getParameter("titolareCarta"));
        //fai cvv hash
        byte[] salt = HashGenerator.generateSalt();

        try {
            byte[] cvvHash = HashGenerator.generateHash(request.getParameter("cvv"),salt);
            pay.setSalt(salt);
            pay.setCvv(cvvHash);
        } catch (BadAttributeValueExpException e) {
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore generazione hash");
        }
        int n = model.checkNum((String) request.getSession().getAttribute("userId"));
        pay.setNumId(n+1);
        model.doSave(pay, (String) request.getSession().getAttribute("userId"), pay.getNumId());
        sendSuccessResponse(request, response);
    }

    private void handleShowAction(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        Collection<PayMethod> payMethods = model.doRetrieveAll((String) request.getSession().getAttribute("userId"));
        sendJsonResponse(response,true, payMethods);
    }

    private void sendJsonResponse(HttpServletResponse response,boolean success, Object responseObject) throws IOException {
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("success", success);
        jsonResponse.add("data", new Gson().toJsonTree(responseObject));

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse.toString());

    }
    private void sendSuccessResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String redirectUrl = "/Payment.jsp";
        response.sendRedirect(request.getContextPath() + redirectUrl);
    }
    private void sendErrorResponse(HttpServletResponse response, int statusCode, String errorMessage) throws IOException {
        response.setStatus(statusCode);
        response.getWriter().write(new Gson().toJson(new ErrorResponse(false, errorMessage)));
    }
    private static class ErrorResponse {
        private final boolean success;
        private final String error;

        public ErrorResponse(boolean success, String error) {
            this.success = success;
            this.error = error;
        }
    }
}

