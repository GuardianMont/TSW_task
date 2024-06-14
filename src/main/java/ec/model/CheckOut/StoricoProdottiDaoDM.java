package ec.model.CheckOut;

import ec.model.ConnectionPool;
import ec.model.cart.CartDaoDM;
import ec.model.cart.CartItem;
import ec.model.cart.ShoppingCart;
import ec.model.product.ProductBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class StoricoProdottiDaoDM implements  StoricoProdottiDao{

    private static final String TABLE_NAME = "StoricoProdotti";
    public void doSave(ArrayList<CartItem> Item_ordinati, int CodFattura, String UtenteId) throws SQLException{
        String insertSQL = "INSERT INTO " + StoricoProdottiDaoDM.TABLE_NAME
                + " (codice_fattura, prodotto_id, iva, prezzo_unitario, quantita, sconto, utente_id) "
                + " VALUES (?, ?, ?, ?, ?, ?, ?) ";
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

            for (CartItem item : Item_ordinati) {
                ProductBean prodotto = item.getItem();
                preparedStatement.setInt(1, CodFattura);
                preparedStatement.setInt(2, prodotto.getId());
                preparedStatement.setDouble(3, prodotto.getFasciaIva());
                preparedStatement.setDouble(4, prodotto.getPrezzo());
                preparedStatement.setInt(5, item.getNumItem());
                preparedStatement.setDouble(6,0);
                preparedStatement.setString(7, UtenteId);
                preparedStatement.executeUpdate();
            }
        }

    }
}
