package ec.model.CheckOut;

import ec.model.ConnectionPool;

import java.sql.*;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.LinkedList;

public class CheckOutDaoDM implements CheckOutDao {
    private static final String TABLE_NAME = "Ordine";
    public void doSave(Ordine ordine) throws SQLException{
        String insertSQL = "INSERT INTO " + CheckOutDaoDM.TABLE_NAME
                + " (utente_id, cod_address, cod_method, data)"
                + " VALUES ( ?, ?, ?, CURDATE()) ";
        try (Connection connection= ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setString(1,ordine.getUtenteId());
            preparedStatement.setInt(2,ordine.getCodAdress());
            preparedStatement.setInt(3,ordine.getCodMethod());
            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();

            if (rs.next()) {
                int generatedId = rs.getInt(1);
                ordine.setNumId(generatedId);

            }

        }
    }

    public boolean doDelete(Ordine ordine) throws SQLException {
        String sqlDelete = "DELETE FROM " + CheckOutDaoDM.TABLE_NAME + " WHERE num = ?";
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlDelete)) {
            preparedStatement.setInt(1, ordine.getNumId());
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0; // Restituisce true se almeno una riga è stata eliminata con successo
        }
    }

    public Ordine retriveOrdineFattura (int numId) throws SQLException{
        String sqlSelectFattura = "SELECT * FROM "+ CheckOutDaoDM.TABLE_NAME + " where num= ?";
        Ordine ordine = new Ordine(numId);
        try (Connection connection =ConnectionPool.getInstance().getConnection();
        PreparedStatement preparedStatement= connection.prepareStatement(sqlSelectFattura)){
            preparedStatement.setInt(1, numId);
            GregorianCalendar calendar =null;
            ResultSet res = preparedStatement.executeQuery();
            while (res.next()){
                ordine.setUtenteId(res.getString("utente_id"));
                ordine.setCodAdress(res.getInt("cod_address"));
                ordine.setCodMethod(res.getInt("cod_method"));
                Timestamp timestamp = res.getTimestamp("data");
                if (timestamp != null) {
                    calendar = new GregorianCalendar();
                    calendar.setTimeInMillis(timestamp.getTime());
                    ordine.setData((GregorianCalendar) calendar);
                }
            }
        }
        return ordine;
    }


    public Collection<Ordine> retriveAllOrdineUtente (String utenteID, String order) throws SQLException{
        String sqlSelectFattura = "SELECT * FROM "+ CheckOutDaoDM.TABLE_NAME + " where utente_id= ?";
        if ("data".equals(order)) {
            // Order by data crescente
            sqlSelectFattura += " ORDER BY data";
        } else if ("dataDESC".equals(order)) {
            // Order by data decrescente
            sqlSelectFattura += " ORDER BY data DESC ";
        }
        Collection<Ordine> ordini = new LinkedList<>();

        try (Connection connection =ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement= connection.prepareStatement(sqlSelectFattura)){
            preparedStatement.setString(1, utenteID);

            ResultSet res = preparedStatement.executeQuery();

            while (res.next()){
                Ordine ordine = new Ordine(res.getInt("num"));
                ordine.setUtenteId(res.getString("utente_id"));
                ordine.setCodAdress(res.getInt("cod_address"));
                ordine.setCodMethod(res.getInt("cod_method"));

                Timestamp timestamp = res.getTimestamp("data");
                if (timestamp != null) {
                    Calendar calendar = GregorianCalendar.getInstance();
                    calendar.setTimeInMillis(timestamp.getTime());
                    ordine.setData((GregorianCalendar) calendar);
                }
                ordini.add(ordine);
            }
        }
        return ordini;
    }

    public Collection<Ordine> retriveAllOrdineFilter (String order) throws SQLException{
        String sqlSelectFattura = "SELECT * FROM "+ CheckOutDaoDM.TABLE_NAME;
        if ("data".equals(order)) {
            // Order by data crescente
            sqlSelectFattura += " ORDER BY data";
        } else if ("dataDESC".equals(order)) {
            // Order by data decrescente
            sqlSelectFattura += " ORDER BY data DESC ";
        }
        Collection<Ordine> ordini = new LinkedList<>();

        try (Connection connection =ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement= connection.prepareStatement(sqlSelectFattura)){

            ResultSet res = preparedStatement.executeQuery();

            while (res.next()){
                Ordine ordine = new Ordine(res.getInt("num"));
                ordine.setUtenteId(res.getString("utente_id"));
                ordine.setCodAdress(res.getInt("cod_address"));
                ordine.setCodMethod(res.getInt("cod_method"));

                Timestamp timestamp = res.getTimestamp("data");
                if (timestamp != null) {
                    Calendar calendar = GregorianCalendar.getInstance();
                    calendar.setTimeInMillis(timestamp.getTime());
                    ordine.setData((GregorianCalendar) calendar);
                }
                ordini.add(ordine);
            }
        }
        return ordini;
    }

    public Collection<Ordine> retriveAllOrders () throws SQLException{
        String sqlSelectFattura = "SELECT * FROM "+ CheckOutDaoDM.TABLE_NAME ;

        Collection<Ordine> ordini = new LinkedList<>();

        try (Connection connection =ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement= connection.prepareStatement(sqlSelectFattura)){

            ResultSet res = preparedStatement.executeQuery();

            while (res.next()){
                Ordine ordine = new Ordine(res.getInt("num"));
                ordine.setUtenteId(res.getString("utente_id"));
                ordine.setCodAdress(res.getInt("cod_address"));
                ordine.setCodMethod(res.getInt("cod_method"));

                Timestamp timestamp = res.getTimestamp("data");
                if (timestamp != null) {
                    Calendar calendar = GregorianCalendar.getInstance();
                    calendar.setTimeInMillis(timestamp.getTime());
                    ordine.setData((GregorianCalendar) calendar);
                }
                ordini.add(ordine);
            }
        }
        return ordini;
    }

    public Collection<Ordine> filterAllOrderByDate (java.util.Date startDate, java.util.Date endDate ) throws SQLException{
        String sqlSelectFattura = "SELECT * FROM "+ CheckOutDaoDM.TABLE_NAME +
                " WHERE data>= ? AND data<= ? ";

        Collection<Ordine> ordini = new LinkedList<>();

        try (Connection connection =ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement= connection.prepareStatement(sqlSelectFattura)){
            // Converto i GregorianCalendar in java.sql.Date in modo che sia compatibile

            java.sql.Date sqlStartDate = new java.sql.Date(startDate.getTime());
            java.sql.Date sqlEndDate = new java.sql.Date(endDate.getTime());

            preparedStatement.setDate(1, sqlStartDate);
            preparedStatement.setDate(2, sqlEndDate);

            ResultSet res = preparedStatement.executeQuery();

            while (res.next()){
                Ordine ordine = new Ordine(res.getInt("num"));
                ordine.setUtenteId(res.getString("utente_id"));
                ordine.setCodAdress(res.getInt("cod_address"));
                ordine.setCodMethod(res.getInt("cod_method"));

                Timestamp timestamp = res.getTimestamp("data");
                if (timestamp != null) {
                    Calendar calendar = GregorianCalendar.getInstance();
                    calendar.setTimeInMillis(timestamp.getTime());
                    ordine.setData((GregorianCalendar) calendar);
                }
                ordini.add(ordine);
            }
        }
        return ordini;
    }

    public Collection<Ordine> filterAllOrderByDateByUser (java.util.Date startDate, java.util.Date endDate, String userId ) throws SQLException{
        String sqlSelectFattura = "SELECT * FROM "+ CheckOutDaoDM.TABLE_NAME +
                " WHERE data>= ? AND data<= ? AND utente_id= ?";

        Collection<Ordine> ordini = new LinkedList<>();

        try (Connection connection =ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement= connection.prepareStatement(sqlSelectFattura)){
            // Converto i GregorianCalendar in java.sql.Date in modo che sia compatibile

            java.sql.Date sqlStartDate = new java.sql.Date(startDate.getTime());
            java.sql.Date sqlEndDate = new java.sql.Date(endDate.getTime());

            preparedStatement.setDate(1, sqlStartDate);
            preparedStatement.setDate(2, sqlEndDate);
            preparedStatement.setString(3,userId);

            ResultSet res = preparedStatement.executeQuery();

            while (res.next()){
                Ordine ordine = new Ordine(res.getInt("num"));
                ordine.setUtenteId(res.getString("utente_id"));
                ordine.setCodAdress(res.getInt("cod_address"));
                ordine.setCodMethod(res.getInt("cod_method"));

                Timestamp timestamp = res.getTimestamp("data");
                if (timestamp != null) {
                    Calendar calendar = GregorianCalendar.getInstance();
                    calendar.setTimeInMillis(timestamp.getTime());
                    ordine.setData((GregorianCalendar) calendar);
                }
                ordini.add(ordine);
            }
        }
        return ordini;
    }



}
