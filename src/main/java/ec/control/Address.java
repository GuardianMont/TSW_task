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
        doPost(request,response);
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
                    } catch (SQLException | IOException e) {
                        e.printStackTrace();
                        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        response.getWriter().write("{\"success\": false, \"error\": \"" + e.getMessage() + "\"}");
                    }
                    break;
                case "show":
                    try {
                        handleShowAction(request, response);
                    } catch (SQLException e) {
                        e.printStackTrace();
                        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        response.getWriter().write("{\"success\": false, \"error\": \"" + e.getMessage() + "\"}");
                    }
                    break;
                default:
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write("{\"success\": false, \"error\": \"Opzione non valida\"}");
                    break;
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"success\": false, \"error\": \"Opzione non fornita\"}");
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

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"success\": true}");
    }

    private void handleShowAction(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        Collection<AddressUs> addresses = model.doRetrieveAll((String) request.getSession().getAttribute("userId"));

        sendJsonResponse(response, addresses);
    }

    private void sendJsonResponse(HttpServletResponse response, Object responseObject) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new Gson().toJson(responseObject));
    }

    private void sendErrorResponse(HttpServletResponse response, int statusCode, String errorMessage) throws IOException {
        JsonObject errorResponse = new JsonObject();
        errorResponse.addProperty("success", false);
        errorResponse.addProperty("error", errorMessage);

        response.setStatus(statusCode);
        sendJsonResponse(response, errorResponse);
    }
}
