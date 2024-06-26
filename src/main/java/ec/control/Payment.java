package ec.control;
import static ec.util.ResponseUtils.*;

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
                        request.setAttribute("payMethods", model.doRetrieveAll((String)request.getSession().getAttribute("userId")));
                        handleShowAction(request,response);
                    } catch (SQLException e) {
                        sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
                    }
                    break;
                case "delete":
                    try {
                        handleDeleteAction(request,response);
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
        sendSuccessResponse(response, request.getContextPath() + "/Payment.jsp");
    }

    private void handleShowAction(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        Collection<PayMethod> payMethods = model.doRetrieveAll((String) request.getSession().getAttribute("userId"));
        sendJsonResponse(response,true, payMethods);
    }

    //TODO QUESTA È UNA RICHIESTA D'AIUTO
    private void handleDeleteAction(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        int numId = Integer.parseInt(request.getParameter("numId"));
        String userId = (String) request.getSession().getAttribute("userId");
        model.doDelete(userId, numId);
        sendSuccessResponse(response, request.getContextPath() + "/Profile.jsp");
    }
}

