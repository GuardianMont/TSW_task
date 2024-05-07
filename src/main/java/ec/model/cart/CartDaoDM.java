package ec.model.cart;


import ec.model.ConnectionPool;
import ec.model.DriverManagerConnectionPool;
import ec.model.product.ProductBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class CartDaoDM implements CartDao {

    private static final String TABLE_NAME= "Carrello";

    @Override
    public void doSave(CartItem Item, String codeUser ) throws SQLException {
        String insertSQL = "INSERT INTO " + CartDaoDM.TABLE_NAME
                + " (prodotto_id, utente_id, quantita) "
                + " VALUES (?, ?, ?) ";

        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

            preparedStatement.setInt(1,Item.getItem().getId());
            //ho fatto con un utente finto inserito manualmente
            preparedStatement.setString(2, codeUser);
            preparedStatement.setDouble(3, Item.getNumItem());

            preparedStatement.executeUpdate();
        }
    }

    @Override
    public boolean doDeleteItem(int codeItem, String codeUser) throws SQLException {
        int result = 0;

        String deleteItemSQL = "DELETE FROM " + CartDaoDM.TABLE_NAME
                + " WHERE prodotto_id =? AND utente_id=?";

        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteItemSQL)){

            preparedStatement.setInt(1, codeItem);
            preparedStatement.setString(2,codeUser);
            result = preparedStatement.executeUpdate();

        }
        return (result != 0);
    }

    @Override
    public boolean doDeleteCart(String codeUser) throws SQLException {
        int result = 0;

        String deleteItemSQL = "DELETE FROM " + CartDaoDM.TABLE_NAME
                + " WHERE utente_id=? ";

        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteItemSQL)){

            preparedStatement.setString(1,codeUser);
            result = preparedStatement.executeUpdate();
        }
        return (result != 0);
    }

    @Override
    public ShoppingCart retriveItem(String codeUser) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        ShoppingCart cart = new ShoppingCart();

        String selectSQL = "SELECT p.id, p.nome, p.descrizione, p.prezzo, p.fascia_iva, p.dimensioni, p.disponibilita, p.categoria, p.immagine, p.colore, c.quantita "
       + " FROM " + CartDaoDM.TABLE_NAME + " AS c "
       + " LEFT JOIN Prodotto AS p ON c.prodotto_id = p.id "
       +" WHERE c.utente_id = ? ";

        try {
            connection = DriverManagerConnectionPool.getConnection();
            preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1,codeUser);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                ProductBean item = new ProductBean();

                item.setId(rs.getInt("id"));
                item.setNome(rs.getString("nome"));
                item.setDescrizione(rs.getString("descrizione"));
                item.setPrezzo(rs.getDouble("prezzo"));
                item.setFasciaIva(rs.getDouble("fascia_iva"));
                item.setDimensioni(rs.getString("dimensioni"));
                item.setDisponibilita(rs.getInt("disponibilita"));
                item.setCategoria(rs.getString("categoria"));
                item.setColore(rs.getString("colore"));
                item.setImmagineUrl(rs.getBytes("immagine"));

                CartItem carrello = new CartItem(item, rs.getInt("quantita"));
                cart.CartItem(carrello);
            }

        } finally {
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
            } finally {
                DriverManagerConnectionPool.releaseConnection(connection);
            }
        }
        return cart;
    }
}

