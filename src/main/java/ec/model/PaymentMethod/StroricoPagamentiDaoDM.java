package ec.model.PaymentMethod;

import ec.model.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StroricoPagamentiDaoDM implements StoricoMetodiDiPagamentoDao {
    private static final String TABLE_NAME = "StoricoMetodiDiPagamento";

    public void doSave(PayMethod pay, int CodFattura, String UtenteId, int numID) throws SQLException {
        String sqlInsert = "Insert into " + StroricoPagamentiDaoDM.TABLE_NAME +
                " (codice_fattura, utente_id, num, circuito, num_carta, data_scadenza, titolare_carta) " +
                " VALUES (?, ?, ?, ?, ?, ?, ?) ";
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert)) {
            preparedStatement.setInt(1, CodFattura);
            preparedStatement.setString(2, UtenteId);
            preparedStatement.setInt(3, numID);
            preparedStatement.setString(4, pay.getCircuito());
            preparedStatement.setString(5, pay.getNumCarta());
            preparedStatement.setString(6, pay.getDataScadenza());
            preparedStatement.setString(7, pay.getTitolareCarta());

            preparedStatement.executeUpdate();
        }

    }

    public PayMethod doRetrieveByKey(int CodFattura, String userID, int num) throws SQLException{
        String sqlRetrive= "SELECT * from " + StroricoPagamentiDaoDM.TABLE_NAME +
                " where codice_fattura=? and utente_id=? and num = ?";
        PayMethod pay = new PayMethod();
        try(Connection connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlRetrive)){
            preparedStatement.setInt(1,CodFattura);
            preparedStatement.setString(2,userID);
            preparedStatement.setInt(3,num);

            ResultSet res = preparedStatement.executeQuery();

            while (res.next()){
                pay.setCircuito(res.getString("circuito"));
                pay.setNumCarta(res.getString("num_carta"));
                pay.setDataScadenza(res.getString("data_scadenza"));
                pay.setTitolareCarta(res.getString("titolare_carta"));
            }

        }
        return pay;
    }
}
