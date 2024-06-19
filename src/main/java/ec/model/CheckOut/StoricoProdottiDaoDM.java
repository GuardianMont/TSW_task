package ec.model.CheckOut;

import ec.model.ConnectionPool;
import ec.model.cart.CartDaoDM;
import ec.model.cart.CartItem;
import ec.model.cart.ShoppingCart;
import ec.model.product.ProductBean;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

public class StoricoProdottiDaoDM implements  StoricoProdottiDao{

    private static final String TABLE_NAME = "StoricoProdotti";
    public void doSave(ArrayList<CartItem> Item_ordinati, int CodFattura, String UtenteId) throws SQLException{
        String insertSQL = "INSERT INTO " + StoricoProdottiDaoDM.TABLE_NAME
                + " (codice_fattura, prodotto_id, nome_prodotto, iva, prezzo_unitario, quantita, sconto, immagine, utente_id) "
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) ";
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

            for (CartItem item : Item_ordinati) {
                ProductBean prodotto = item.getItem();
                preparedStatement.setInt(1, CodFattura);
                preparedStatement.setInt(2, prodotto.getId());
                preparedStatement.setString(3,prodotto.getNome());
                preparedStatement.setDouble(4, prodotto.getFasciaIva());
                preparedStatement.setDouble(5, prodotto.getPrezzo());
                preparedStatement.setInt(6, item.getNumItem());
                preparedStatement.setDouble(7,0);
                preparedStatement.setBytes(8, prodotto.getImmagineUrl());
                preparedStatement.setString(9, UtenteId);
                preparedStatement.executeUpdate();
            }
        }

    }
    public Collection<ProductBean> retriveOrdineitem (int CodFattura, String UtenteId ) throws SQLException{
        String retriveSql  = "Select * from " + StoricoProdottiDaoDM.TABLE_NAME + " where codice_fattura= ? and utente_id= ?";
        Collection<ProductBean> Storico = new LinkedList<>();
        try(Connection connection = ConnectionPool.getInstance().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(retriveSql)){
            preparedStatement.setInt(1, CodFattura);
            preparedStatement.setString(2,UtenteId);
            ResultSet res = preparedStatement.executeQuery();

            while (res.next()){
                ProductBean item = new ProductBean();
                item.setId(res.getInt("prodotto_id"));
                item.setFasciaIva(res.getDouble("iva"));
                item.setPrezzo(res.getDouble("prezzo_unitario"));
                item.setDisponibilita(res.getInt("quantita"));
                item.setImmagineUrl(res.getBytes("immagine"));
                item.setNome(res.getString("nome_prodotto"));
                //aggiungi sconto
                Storico.add(item);
            }
        }
        return Storico;
    }
}
