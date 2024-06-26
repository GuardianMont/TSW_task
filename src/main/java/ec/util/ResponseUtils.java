package ec.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ResponseUtils {
    public static void sendJsonResponse(HttpServletResponse response, boolean success, Object responseObject) throws IOException, IOException {
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("success", success);
        jsonResponse.add("data", new Gson().toJsonTree(responseObject));

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse.toString());
    }

    public static void sendSuccessResponse(HttpServletResponse response, String redirectUrl) throws IOException {
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("success", true);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse.toString());
        response.sendRedirect(redirectUrl);
    }

    public static void sendErrorResponse(HttpServletResponse response, int statusCode, String errorMessage) throws IOException {
        response.setStatus(statusCode);
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("success", false);
        jsonResponse.addProperty("error", errorMessage);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse.toString());
    }

    public static void sendErrorMessage(HttpServletResponse response, String errorMessage) throws IOException {
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("success", false);
        jsonResponse.addProperty("error", errorMessage);
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse.toString());
    }
}
