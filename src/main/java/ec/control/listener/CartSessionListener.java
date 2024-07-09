package ec.control.listener;

import ec.model.cart.CartDaoDM;
import ec.model.cart.CartItem;
import ec.model.cart.ShoppingCart;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.*;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebListener
public class CartSessionListener implements HttpSessionListener, HttpSessionAttributeListener {
    private static final Logger LOGGER = Logger.getLogger(CartSessionListener.class.getName());
    private CartDaoDM model;

    public CartSessionListener() {
        model = new CartDaoDM();
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        String userId = (String) session.getAttribute("userId");
        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");

        if (userId != null && cart != null) {
            try {
                saveCartToDatabase(userId, cart);
                LOGGER.log(Level.INFO, "Carrello salvato correttamente per l'utente: {0}", userId);
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Errore nel salvataggio degli articoli del carrello nel database per l'utente: " + userId, e);
            }
        }
    }

    @Override
    public void attributeAdded(HttpSessionBindingEvent event) {
        handleCartAttributeChange(event);
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent event) {
        handleCartAttributeChange(event);
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent event) {
        handleCartAttributeChange(event);
    }

    private void handleCartAttributeChange(HttpSessionBindingEvent event) {
        if ("cart".equals(event.getName())) {
            HttpSession session = event.getSession();
            String userId = (String) session.getAttribute("userId");
            ShoppingCart cart = (ShoppingCart) event.getValue();

            if (userId != null && cart != null) {
                try {
                    saveCartToDatabase(userId, cart);
                    LOGGER.log(Level.INFO, "Aggiornamento del carrello salvato nel database per l'utente: {0}", userId);
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Errore nel salvataggio del carrello nel database per l'utente: " + userId, e);
                }
            }
        }
    }

    private void saveCartToDatabase(String userId, ShoppingCart cart) throws SQLException {
        model.doDeleteCart(userId); // Elimina il carrello antecedente
        for (CartItem item : cart.getItem_ordinati()) {
            model.doSave(item, userId); // Salva ogni elemento del carrello nel database
        }
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        // per ora non necessario perchè gestisce l'evento di creazione della sessione
        //e per ora la logica di estrazione carrello e merge con il carrello nella sessione pre
        // login è gestisto dal LoginSignup Servlet
    }
}
