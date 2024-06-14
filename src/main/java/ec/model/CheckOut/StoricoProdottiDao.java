package ec.model.CheckOut;

import ec.model.cart.CartItem;
import ec.model.cart.ShoppingCart;

import java.sql.SQLException;
import java.util.ArrayList;

interface  StoricoProdottiDao {
    public void doSave(ArrayList<CartItem> Item_ordinati, int CodFattura, String UtenteId) throws SQLException;
}
