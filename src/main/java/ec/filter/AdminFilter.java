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
        if (session == null) {
            System.out.println("Session is null");
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/NewHomepageTest.jsp");
        } else {
            Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
            if (isAdmin == null || !isAdmin) {
                System.out.println("User is not admin: " + isAdmin);
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/NewHomepageTest.jsp");
            } else {
                System.out.println("User is admin: " + isAdmin);
                chain.doFilter(request, response);
            }
        }
    }
}