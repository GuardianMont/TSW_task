package ec.control;

import static ec.util.ResponseUtils.*;
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

@WebServlet("/payMethodsManager")
public class Payment extends HttpServlet {
    private PaymentDaoDM model;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        model = new PaymentDaoDM();
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
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Sessione non valida o utente non loggato");
            return;
        }

        System.out.println("Parametri payment: " + opzione);
        if (opzione != null) {
            try {
                switch (opzione) {
                    case "add":
                        handleAddAction(request, response);
                        break;
                    case "show":
                        handleShowAction(request, response);
                        break;
                    case "delete":
                        handleDeleteAction(request, response);
                        break;
                    default:
                        sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Opzione non valida");
                        break;
                }
            } catch (SQLException | IOException e) {
                e.printStackTrace();
                sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            }
        } else {
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Opzione non fornita");
        }
    }

    private void handleAddAction(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        PayMethod pay = new PayMethod();

        try {
            String numCarta = request.getParameter("NumeroCarta");
            String mese = request.getParameter("meseScadenza");
            String anno = request.getParameter("annoScadenza");
            String circuito = request.getParameter("circuito");
            String titolareCarta = request.getParameter("titolareCarta");
            String cvv = request.getParameter("cvv");

            if (numCarta == null || mese == null || anno == null || circuito == null || titolareCarta == null || cvv == null) {
                throw new IllegalArgumentException("Tutti i campi sono obbligatori.");
            }

            pay.setNumCarta(numCarta);
            pay.setDataScadenza(mese + "/" + anno);
            pay.setCircuito(circuito);
            pay.setTitolareCarta(titolareCarta);

            byte[] salt = HashGenerator.generateSalt();
            byte[] cvvHash = HashGenerator.generateHash(cvv, salt);

            pay.setSalt(salt);
            pay.setCvv(cvvHash);

            int n = model.checkNum((String) request.getSession().getAttribute("userId"));
            pay.setNumId(n + 1);

            model.doSave(pay, (String) request.getSession().getAttribute("userId"), pay.getNumId());

            sendSuccessResponse(response, "Metodo di pagamento aggiunto con successo.");
        } catch (BadAttributeValueExpException | IllegalArgumentException e) {
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore durante l'aggiunta del metodo di pagamento.");
        }
    }

    private void handleShowAction(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        try {
            Collection<PayMethod> payMethods = model.doRetrieveAll((String) request.getSession().getAttribute("userId"));
            sendJsonResponse(response, true, payMethods);
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore durante il recupero dei metodi di pagamento.");
        }
    }

    private void handleDeleteAction(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        try {
            int numId = Integer.parseInt(request.getParameter("numId"));
            String userId = (String) request.getSession().getAttribute("userId");

            model.doDelete(userId, numId);

            sendSuccessResponse(response, "Metodo di pagamento eliminato con successo.");
        } catch (NumberFormatException e) {
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "ID metodo di pagamento non valido.");
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore durante l'eliminazione del metodo di pagamento.");
        }
    }
}
