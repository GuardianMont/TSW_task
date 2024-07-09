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
                case "delete":
                    try{
                        handleDeleteAction(request,response);
                    }catch (SQLException e){
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
        String cap = request.getParameter("cap");
        String citta = request.getParameter("citta");
        String nCivico = request.getParameter("n_civico");
        String preferenze = request.getParameter("preferenze");
        String via = request.getParameter("via");
        String provincia = request.getParameter("provincia");

        if (cap == null || citta == null || nCivico == null || preferenze == null || via == null || provincia == null) {
            //check lato server
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Parametri mancanti o non validi");
            return;
        }

        AddressUs ad = new AddressUs();
        ad.setCap(cap);
        ad.setCitta(citta);
        ad.setNumCiv(Integer.parseInt(nCivico));
        ad.setPreferenze(preferenze);
        ad.setVia(via);
        ad.setProvincia(provincia);
        int n = model.checkNum((String) request.getSession().getAttribute("userId"));
        ad.setNum_ID(n + 1);
        model.doSave(ad, (String) request.getSession().getAttribute("userId"), ad.getNum_ID());

        sendSuccessResponse(response, request.getContextPath() + "/Payment.jsp");
    }


    private void handleShowAction(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        Collection<AddressUs> addresses = model.doRetrieveAll((String) request.getSession().getAttribute("userId"));
        sendJsonResponse(response,true, addresses);
    }

    private void handleDeleteAction(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        int numId = Integer.parseInt(request.getParameter("numId"));
        String userId = (String) request.getSession().getAttribute("userId");
        model.doDelete(userId, numId);
        sendSuccessResponse(response, request.getContextPath() + "/Profile.jsp");
    }
}
