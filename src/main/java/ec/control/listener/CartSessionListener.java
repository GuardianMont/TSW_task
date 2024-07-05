import ec.model.cart.CartItem;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

import java.util.List;

@WebListener
public class CartSessionListener implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        // Non necessario
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {

        HttpSession session = se.getSession();
        Integer userId = (Integer) session.getAttribute("user_id");
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

        if (userId != null && cart != null) {
            saveCartToDatabase(userId, cart);
        }
    }

}