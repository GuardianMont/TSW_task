package ec.control;

import ec.model.ProductBean;
import ec.model.cart.CartDaoDM;
import ec.model.cart.CartItem;
import ec.model.cart.ShoppingCart;
import ec.model.product.ProductDaoDM;
import jakarta.servlet.RequestDispatcher;
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
                int id=0;
                if (par_id!=null){
                    id = Integer.parseInt(request.getParameter("id"));
                }
                    switch (action.toLowerCase()) {
                        case "decrement":
                            cart.deleteItem(id);
                            session.setAttribute("cart", cart);
                            break;
                        case "delete":
                            CartItem do_delete;
                            if ((do_delete= cart.getItem(id))!=null){
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
                            dis = "/ProductView.jsp";
                            try {
                                handleAddAction(request);
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        case "acquisto":
                            try{
                                handleAcquistoAction(request, response, dis);
                            }catch (SQLException e){
                                e.printStackTrace();
                                request.setAttribute("errorMessage", "Errore durante l'acquisto: " + e.getMessage());
                                dis="/error.jsp";
                            }
                            break;

            }
        }
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(dis);
        dispatcher.forward(request, response);
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    private void handleAddAction(HttpServletRequest request) throws SQLException, ServletException, IOException {
        HttpSession session = request.getSession();
        int id_item = Integer.parseInt(request.getParameter("id"));
        ProductDaoDM model = new ProductDaoDM();
        ProductBean item= model.doRetrieveByKey(id_item);
        ShoppingCart cart = (ShoppingCart)session.getAttribute("cart");
        cart.addItem(item);
        session.setAttribute("cart", cart);
    }

    private void handleAcquistoAction(HttpServletRequest request, HttpServletResponse response, String forward) throws SQLException, ServletException, IOException {
        JOptionPane.showMessageDialog(null, "sto qua");
        HttpSession session = request.getSession();
        CartDaoDM model = new CartDaoDM();
        ShoppingCart carrello = (ShoppingCart) session.getAttribute("cart");
        ArrayList<CartItem> Item_ordinati = carrello.getItem_ordinati();

        model.doDeleteCart("root");
        //mi assicuro che ci sia solo un carrello per utente
        for (var e : Item_ordinati) {
                model.doSave(e, "root");
            }
        JOptionPane.showMessageDialog(null, "Acquisto completato");
        session.setAttribute("cart", new ShoppingCart());
        request.setAttribute("acquistoCompletato", true);
        // Redirect alla stessa pagina per visualizzare l'aggiornamento
        //response.sendRedirect(request.getContextPath() + forward);
    }
}
