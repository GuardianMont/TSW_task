package ec.control;

import ec.model.PaymentMethod.PaymentDaoDM;
import ec.model.cart.ShoppingCart;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet ("/CheckoutServlet")
public class CheckoutServlet extends HttpServlet {
    private PaymentDaoDM model;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        model = new PaymentDaoDM();
    }

    public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    public void doPost (HttpServletRequest request, HttpServletResponse response){
        HttpSession session = request.getSession(false);
        ShoppingCart cart= (ShoppingCart) session.getAttribute("cart");
        if (cart==null){
            //tecnicemente impossibile arrivare qui senza cart
            throw new RuntimeException();
        }
        String userId = (String) session.getAttribute("userId");
        if (userId==null){
            //tecnicamente impossibile arrivare qua senza userID
            throw new RuntimeException();
        }

        String selectedAddress = request.getParameter("selectedAddress");
        String selectedPaymentMethod = request.getParameter("selectedPaymentMethod");
    }
}
