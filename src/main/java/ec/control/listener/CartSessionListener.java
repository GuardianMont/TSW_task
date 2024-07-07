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
    // HttpSessionListener ascolta gli eventi relativi alla sessione stessa come creazione di una sessione e distruzione
    //HttpSessionAttributeListener invece ascolta gli eventi relativi all'aggiunta, modifica e rimozione degli attributi di sessione
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

        if (userId != null && cart != null && !cart.isEmpty()) {
            try {
                saveCartToDatabase(userId, cart);
                LOGGER.log(Level.INFO, "Carrello salvato correttamente per l'utente: {0}", userId);
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Errore nel salvataggio degli articoli del carrello nel database per l'utente: " + userId, e);
            }

        }
    }
    //metodo chiamato quando un attributo della sessione viene aggiunto o modificato

    public void attributeAdded (HttpSessionBindingEvent event){
                handleCartAttributeChange(event);
    }

    public void attributeReplaced(HttpSessionBindingEvent event) {
        handleCartAttributeChange(event);
    }
    private void handleCartAttributeChange(HttpSessionBindingEvent event) {
        if ("cart".equals(event.getName())) {
            HttpSession session = event.getSession();
            String userId = (String) session.getAttribute("userId");
            ShoppingCart cart = (ShoppingCart) event.getValue();

            if (userId != null && cart != null && !cart.isEmpty()) {
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
    public void attributeRemoved(HttpSessionBindingEvent event) {
        // Non necessario perchè al momento del pagamento noi subito sostituiamo l'attuale carrello
        //con un nuovo carrello perciò alla fine sempre addAttribute viene invocata
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        // per ora non necessario perchè gestisce l'evento di creazione della sessione
        //e per ora la logica di estrazione carrello e merge con il carrello nella sessione pre
        // login è gestisto dal LoginSignup Servlet
    }
}

