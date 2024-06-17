package ec.control;

import ec.model.product.ProductBean;
import ec.model.cart.CartDaoDM;
import ec.model.cart.CartItem;
import ec.model.cart.ShoppingCart;
import ec.model.product.ProductDaoDM;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class Cart extends HttpServlet {
    private CartDaoDM model;
    @Override
    public void init() throws ServletException {
        super.init();
        model = new CartDaoDM();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String dis="/Cart.jsp";
        HttpSession session = request.getSession();
        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
        if (cart == null) {
            cart = new ShoppingCart();
            session.setAttribute("cart", cart);
        }
        String action = request.getParameter("opzione");
            if (action != null) {
                String par_id = request.getParameter("id");
                int id = 0;
                if (par_id != null) {
                    try {
                        id = Integer.parseInt(par_id);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid item ID format.");
                        return;
                    }
                }
                switch (action.toLowerCase()) {
                    case "decrement":
                        cart.deleteItem(id);
                        session.setAttribute("cart", cart);
                        break;
                    case "delete":

                        CartItem do_delete;
                        if ((do_delete = cart.getItem(id)) != null) {
                            do_delete.cancelOrder();
                            cart.deleteItem(id);
                            session.setAttribute("cart", cart);
                        }
                        break;
                    case "increment":
                        cart.incrementItem(id);
                        session.setAttribute("cart", cart);
                        break;
                    case "add":
                        try {
                            handleAddAction(request, response);
                            return;
                            //mi assicuro che il redirect avvenga rispetto ad
                            // una pagina che cambia in base al contesto
                            //e non una pagina settata a priori
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    case "aggiornaquantita":
                        String quantityStr = request.getParameter("quantity");
                        if (quantityStr != null && !quantityStr.isEmpty()) {
                            try {
                                int quantita = Integer.parseInt(quantityStr);
                                CartItem item = cart.getItem(id);
                                if (item != null) {
                                    if (quantita == 0) {
                                        item.cancelOrder();
                                        cart.deleteItem(id);
                                    } else {
                                        item.setNumItem(quantita);
                                    }
                                    session.setAttribute("cart", cart);
                                }
                            } catch (NumberFormatException e) {
                                // Gestisci l'errore di formato non valido
                                e.printStackTrace();
                                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid quantity format.");
                            }
                        } else {
                            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Quantity is required.");
                        }
                        break;
                    case "acquisto":
                        if (request.getSession().getAttribute("userId") != null) {
                            //mi assicuro che l'utente sia loggato prima di effettuare un acquisto
                            try {
                                handleAcquistoAction(request);
                                //è la servelet cart che si occupa di reindirizzare
                                //verso la servlet address per fare il fetch degli indirizzi
                                //e proseguire verso la finalizzazione dell'acquisto
                                //ServletContext context = getServletContext();
                                //context.setAttribute("op", "show");
                                dis = "/Payment.jsp";
                            } catch (SQLException e) {
                                e.printStackTrace();
                                request.setAttribute("errorMessage", "Errore durante l'acquisto: " + e.getMessage());
                                dis = "/error500.jsp";
                            }
                        } else {
                            dis = "/login_signup.jsp";
                        }
                        break;

                }
            }
        ServletContext context = getServletContext();
        String value = (String) context.getAttribute("op");
        if (value!=null){
            switch (value){
                case "update":
                    if (request.getSession().getAttribute("userId") != null) {
                        //mi assicuro che l'utente sia loggato prima di effettuare un aggiornamento del cart
                        try {
                            handleUpdateAction(request);
                            dis = "/ProductView.jsp";
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    break;
            }
        }
        if (!response.isCommitted()){
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(dis);
            dispatcher.forward(request, response);
            return;
        }
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    private void handleAddAction(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        HttpSession session = request.getSession();
        String idStr = request.getParameter("id");
        if (idStr != null && !idStr.isEmpty()) {
            try {
                int id_item = Integer.parseInt(idStr);
                ProductDaoDM model = new ProductDaoDM();
                ProductBean item = model.doRetrieveByKey(id_item);
                ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
                cart.addItem(item);
                session.setAttribute("cart", cart);
                String referer = request.getHeader("referer");
                response.sendRedirect(referer);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid item ID format.");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Item ID is required.");
        }
    }

    private void handleAcquistoAction(HttpServletRequest request) throws SQLException {
        HttpSession session = request.getSession();
        ShoppingCart carrello = (ShoppingCart) session.getAttribute("cart");
        ArrayList<CartItem> Item_ordinati = carrello.getItem_ordinati();

        model.doDeleteCart((String) request.getSession().getAttribute("userId"));
        //mi assicuro che ci sia solo un carrello per utente
        for (var e : Item_ordinati) {
                model.doSave(e, (String)request.getSession().getAttribute("userId"));
            }
        //session.setAttribute("cart", new ShoppingCart());
        //request.setAttribute("acquistoCompletato", true);

        // Redirect alla stessa pagina per visualizzare l'aggiornamento
        //response.sendRedirect(request.getContextPath() + forward);
    }

    private void handleUpdateAction(HttpServletRequest request) throws SQLException, ServletException, IOException {
        HttpSession session = request.getSession();
        CartDaoDM model = new CartDaoDM();
        ShoppingCart carrello = (ShoppingCart) session.getAttribute("cart");
        ArrayList<CartItem> Item_ordinati = carrello.getItem_ordinati();
        String root= (String) request.getSession().getAttribute("userId");
        model.doDeleteCart(root);
        //mi assicuro che ci sia solo un carrello per utente
        for (var e : Item_ordinati) {
            model.doSave(e, root);
        }
        session.setAttribute("cart", model.retriveItem(root));
    }
}
