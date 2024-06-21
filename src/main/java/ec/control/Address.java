package ec.control;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Opzione non fornita");
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

        sendSuccessResponse(request, response);
    }

    private void handleShowAction(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        Collection<AddressUs> addresses = model.doRetrieveAll((String) request.getSession().getAttribute("userId"));
        sendJsonResponse(response,true, addresses);
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
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("success", true);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse.toString());
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
