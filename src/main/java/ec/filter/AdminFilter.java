package ec.filter;


import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

// Filtra tutte le richieste alla cartella /admin
@WebFilter("/admin/*")
public class AdminFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // Cast dei parametri della richiesta e della risposta
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Ottieni la sessione se esiste, altrimenti restituisci null
        HttpSession session = httpRequest.getSession(false); // Ottieni la sessione se esiste, altrimenti restituisci null

        // Se la sessione non esiste o non è un amministratore, reindirizza alla homepage
        if (session == null || session.getAttribute("isAdmin") == null || !(Boolean) session.getAttribute("isAdmin")) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/Homepage.jsp"); // Reindirizza alla pagina di login o di errore
        } else {
            // Passa la richiesta al prossimo filtro o servlet nella catena
            chain.doFilter(request, response); // Passa la richiesta al prossimo filtro o servlet nella catena
        }
    }
}