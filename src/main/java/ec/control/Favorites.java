package ec.control;

import ec.model.favorites.FavoritesDaoDM;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.ArrayList;

import static ec.util.ResponseUtils.*;

@WebServlet("/favorites")
public class Favorites extends HttpServlet {
    private FavoritesDaoDM favoritesDao = new FavoritesDaoDM();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "utente non loggato");
            return;
        }

        try {
            ArrayList<Integer> favorites = favoritesDao.retriveFavorites((String) session.getAttribute("userId"));
            sendJsonResponse(response,true,favorites);
        } catch (SQLException e) {
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore recupero preferiti");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "utente non loggato");
            return;
        }

        try {
            int productId = Integer.parseInt(request.getParameter("productId"));
            favoritesDao.doSave((String) session.getAttribute("userId"), productId);
            sendSuccessResponse(response,null);
        } catch (SQLException e) {
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Errore Salvataggio Preferiti");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "utente non loggato");
            return;
        }

        try {
            int productId = Integer.parseInt(request.getParameter("productId"));
            boolean success = favoritesDao.doDelete((String) session.getAttribute("userId"), productId);
            if (success) {
                sendSuccessResponse(response,null);
            } else {
                sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Errore cancellazione Preferiti");
            }
        } catch (SQLException e) {
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore cancellazione Preferiti");
        }
    }
}

