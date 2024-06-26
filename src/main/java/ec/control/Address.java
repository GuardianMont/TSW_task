package ec.control;
import ec.model.address.AddressDaoDM;
import ec.model.address.AddressUs;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

import static ec.util.ResponseUtils.*;

@WebServlet("/AddressManagement")
public class Address extends HttpServlet {
    private AddressDaoDM model;
    @Override
    public void init() throws ServletException {
        super.init();
        model = new AddressDaoDM();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String opzione = request.getParameter("opzione");
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Sessione non valida o utente non loggato");
            return;
        }

        if (opzione != null) {
            switch (opzione) {
                case "add":
                    try {
                        handleAddAction(request, response);
                    } catch (SQLException e) {
                        sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
                    }
                    break;
                case "show":
                    try {
                        handleShowAction(request, response);
                    } catch (SQLException e) {
                        sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
                    }
                    break;
                default:
                    sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Opzione non valida");
                    break;
            }
        } else {
           sendErrorResponse (response, HttpServletResponse.SC_BAD_REQUEST, "Opzione non fornita");
        }
    }

    private void handleAddAction(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        AddressUs ad = new AddressUs();
        ad.setCap(request.getParameter("cap"));
        ad.setCitta(request.getParameter("citta"));
        ad.setNumCiv(Integer.parseInt(request.getParameter("n_civico")));
        ad.setPreferenze(request.getParameter("preferenze"));
        ad.setVia(request.getParameter("via"));
        ad.setProvincia(request.getParameter("provincia"));
        int n = model.checkNum((String) request.getSession().getAttribute("userId"));
        ad.setNum_ID(n + 1);
        model.doSave(ad, (String) request.getSession().getAttribute("userId"), ad.getNum_ID());

        sendSuccessResponse(response,request.getContextPath() + "/Payment.jsp");
    }

    private void handleShowAction(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        Collection<AddressUs> addresses = model.doRetrieveAll((String) request.getSession().getAttribute("userId"));
        sendJsonResponse(response,true, addresses);
    }

}
