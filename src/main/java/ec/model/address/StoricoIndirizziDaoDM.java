package ec.model.address;

import ec.model.ConnectionPool;
import ec.model.cart.CartItem;
import ec.model.product.ProductBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class StoricoIndirizziDaoDM implements StoricoIndirizziDao {

    private static final String TABLE_NAME = "StoricoIndirizzi";

    public void doSave(AddressUs ad, int CodFattura, String UtenteId, int numID) throws SQLException {
        String sqlInsert = "Insert into " + StoricoIndirizziDaoDM.TABLE_NAME +
                " (codice_fattura, utente_id, num, cap, citta, provincia, via, n_civico, preferenze) " +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) ";
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert)) {
            preparedStatement.setInt(1, CodFattura);
            preparedStatement.setString(2, UtenteId);
            preparedStatement.setInt(3, numID);
            preparedStatement.setString(4,ad.getCap());
            preparedStatement.setString(5,ad.getCitta());
            preparedStatement.setString(6,ad.getProvincia());
            preparedStatement.setString(7,ad.getVia());
            preparedStatement.setInt(8,ad.getNumCiv());
            preparedStatement.setString(9,ad.getPreferenze());

            preparedStatement.executeUpdate();
        }

    }

    public AddressUs doRetrieveByKey(int CodFattura, String userID, int num) throws SQLException{
        String sqlRetrive= "SELECT * from " + StoricoIndirizziDaoDM.TABLE_NAME +
                " where codice_fattura=? and utente_id=? and num = ?";
        AddressUs ad = new AddressUs();
        try(Connection connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlRetrive)){
            preparedStatement.setInt(1,CodFattura);
            preparedStatement.setString(2,userID);
            preparedStatement.setInt(3,num);

            ResultSet res = preparedStatement.executeQuery();

            while (res.next()){
                ad.setNum_ID(res.getInt("num"));
                ad.setCap(res.getString("cap"));
                ad.setCitta(res.getString("citta"));
                ad.setPreferenze(res.getString("preferenze"));
                ad.setNumCiv(res.getInt("n_civico"));
                ad.setProvincia(res.getString("provincia"));
                ad.setVia(res.getString("via"));
            }

        }
        return ad;
    }
}
