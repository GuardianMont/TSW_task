package ec.util;

import ec.model.cart.CartItem;
import ec.model.cart.ShoppingCart;

import jakarta.servlet.http.HttpSession;

public class CartManager {

    public static void removeProductFromCart(HttpSession session, int productId) {
        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
        if (cart != null) {
            CartItem item = cart.getItem(productId);
            if (item != null) {
                item.cancelOrder();
                cart.deleteItem(productId);
                session.setAttribute("cart", cart);
            }
        }
    }

    public static boolean updateProductInCart(HttpSession session, int productId) {
        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
        if (cart != null) {
            if (cart.getItem(productId) != null) {
                return true;
            }
        }
        return false;
    }
}
