package ec.model.address;

import ec.model.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

public class AddressDaoDM implements AddressDao{
    private static  final String  TABLE_NAME="indirizzo";

    public boolean doSave (AddressUs ad, String userID, int num) throws SQLException {
        String sqlInsert = "Insert into " + AddressDaoDM.TABLE_NAME +
                " (utente_id, num, cap, citta, provincia, via, n_civico, preferenze) " +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?) ";

        try(Connection connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert)) {
            preparedStatement.setString(1,userID);
            preparedStatement.setInt(2,num);
            preparedStatement.setString(3,ad.getCap());
            preparedStatement.setString(4,ad.getCitta());
            preparedStatement.setString(5,ad.getProvincia());
            preparedStatement.setString(6,ad.getVia());
            preparedStatement.setInt(7,ad.getNumCiv());
            preparedStatement.setString(8,ad.getPreferenze());

            preparedStatement.executeUpdate();
            return true;
        }
    }

    public boolean doDelete (String userID, int num) throws  SQLException{
        String sqlDelete = "DELETE from " + AddressDaoDM.TABLE_NAME +
                " where utente_id=? AND num = ?";

        int result=0;

        try (Connection connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlDelete)){
            preparedStatement.setString(1,userID);
            preparedStatement.setInt(2, num);

            result = preparedStatement.executeUpdate();
        }
        return (result!=0);
    }

    public Collection<AddressUs> doRetrieveAll (String userID) throws SQLException{
        String sqlRetrive= "SELECT * from " + AddressDaoDM.TABLE_NAME +
                " where utente_id=? ";

        Collection<AddressUs> indirizzi = new LinkedList<>();
        try (Connection connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlRetrive)){
            preparedStatement.setString(1,userID);

            ResultSet res = preparedStatement.executeQuery();

            while (res.next()){
                AddressUs ad = new AddressUs();
                ad.setNum_ID(res.getInt("num"));
                ad.setCap(res.getString("cap"));
                ad.setCitta(res.getString("citta"));
                ad.setPreferenze(res.getString("preferenze"));
                ad.setNumCiv(res.getInt("n_civico"));
                ad.setProvincia(res.getString("provincia"));
                ad.setVia(res.getString("via"));

                indirizzi.add(ad);
            }

        }
        return indirizzi;
    }

    public AddressUs doRetrieveByKey(String userID, int num) throws SQLException{
        String sqlRetrive= "SELECT * from " + AddressDaoDM.TABLE_NAME +
                " where utente_id=? and num = ?";
        AddressUs ad = new AddressUs();
        try(Connection connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlRetrive)){
            preparedStatement.setString(1,userID);
            preparedStatement.setInt(2,num);

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

    public int checkNum (String userId) throws SQLException{
        String sqlCount = "SELECT COUNT(*) AS numero_indirizzi " +
                "FROM Indirizzo " +
                "WHERE utente_id = ? " +
                "GROUP BY utente_id ";
        int n = 0;
        try(Connection connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlCount)){
            preparedStatement.setString(1,userId);

            ResultSet ret = preparedStatement.executeQuery();

            while (ret.next()){
                 n  = ret.getInt("numero_indirizzi");
            }
        }
        return n;
    }
}

