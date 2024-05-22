package ec.model.PaymentMethod;

import ec.model.ConnectionPool;
import ec.model.address.AddressDaoDM;
import ec.model.address.AddressUs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

public class PaymentDaoDM implements PaymentDao {
    private static  final String  TABLE_NAME="MetodoPagamento";

    public void doSave (PayMethod pay, String userID, int num) throws SQLException {
        String sqlInsert = "Insert into " + PaymentDaoDM.TABLE_NAME +
                " (utente_id, num, circuito, num_carta, data_scadenza, titolare_carta) " +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?) ";
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert)) {
            preparedStatement.setString(1,userID);
            preparedStatement.setInt(2,num);
            preparedStatement.setString(3,pay.getCircuito());
            preparedStatement.setString(4,pay.getNumCarta());
            preparedStatement.setString(5,pay.getDataScadenza());
            preparedStatement.setString(6,pay.getTitolareCarta());

            preparedStatement.executeUpdate();
        }
    }

    public boolean doDelete (String userID, int num) throws  SQLException{
        String sqlDelete = "DELETE from " + PaymentDaoDM.TABLE_NAME +
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

    public Collection<PayMethod> doRetrieveAll (String userID) throws SQLException{
        String sqlRetrive= "SELECT * from " + PaymentDaoDM.TABLE_NAME +
                " where utente_id=? ";

        Collection<PayMethod> payMeds = new LinkedList<>();
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlRetrive)){
            preparedStatement.setString(1,userID);
            ResultSet res = preparedStatement.executeQuery();

            while (res.next()){
                PayMethod pay = new PayMethod();

                pay.setCircuito(res.getString("circuito"));
                pay.setDataScadenza(res.getString("data_scadenza"));
                pay.setTitolareCarta(res.getString("titolare_carta"));
                pay.setCvv(res.getString("cvv"));
                pay.setNumCarta(res.getString("num_carta"));

                payMeds.add(pay);
            }

        }
        return payMeds;
    }

    public PayMethod doRetrieveByKey(String userID, int num) throws SQLException{
        String sqlRetrive= "SELECT * from " + PaymentDaoDM.TABLE_NAME +
                " where utente_id=? and num = ?";
        PayMethod pay  = new PayMethod();
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlRetrive)){
            preparedStatement.setString(1,userID);
            preparedStatement.setInt(2,num);
            ResultSet res = preparedStatement.executeQuery();

            while (res.next()){
                pay.setCircuito(res.getString("circuito"));
                pay.setDataScadenza(res.getString("data_scadenza"));
                pay.setTitolareCarta(res.getString("titolare_carta"));
                pay.setCvv(res.getString("cvv"));
                pay.setNumCarta(res.getString("num_carta"));
            }

        }
        return pay;
    }

    public int checkNum (String userId) throws SQLException{
        String sqlCount = "SELECT COUNT(*) AS numero_metodi " +
                "FROM " + PaymentDaoDM.TABLE_NAME +
                " WHERE utente_id = ? " +
                "GROUP BY utente_id ";
        int n = 0;
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlCount)){
            preparedStatement.setString(1,userId);
            ResultSet ret = preparedStatement.executeQuery();
            while (ret.next()){

                n  = ret.getInt("numero_metodi");
            }
        }
        return n;
    }
}

