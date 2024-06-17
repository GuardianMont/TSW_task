package ec.model.discount;

import ec.model.ConnectionPool;
import ec.model.DriverManagerConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

public class DiscountDaoDM implements DiscountDao {

    private static final String TABLE_NAME = "Sconto";
    @Override
    public void doSave(DiscountBean discount) throws SQLException {
        String insertSQL = "INSERT INTO " + DiscountDaoDM.TABLE_NAME
                + " (prodotto, percentuale_sconto)"
                + " VALUES (?, ?) ";
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

            preparedStatement.setInt(1, discount.getProduct());
            preparedStatement.setInt(2, discount.getDiscountPercentage());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e);

            //TODO: vedere che cazzo fare se esce un'eccezione nell'inserimento di uno sconto
        }
    }

    @Override
    public boolean doDelete(int id) throws SQLException {
        String deleteSQL = "DELETE FROM " + DiscountDaoDM.TABLE_NAME + " WHERE prodotto = ?";
        int result = 0;
        try( Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);) {

            preparedStatement.setInt(1, id);
            result = preparedStatement.executeUpdate();


        }catch (SQLException e){
            e.printStackTrace();
        }
        return result != 1 ? false : true;
    }

    @Override
    public DiscountBean doRetrieveByKey(int id) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        DiscountBean bean = null;

        String selectSQL = "select * from " + DiscountDaoDM.TABLE_NAME + " where prodotto = ? ";

        try {
            connection = DriverManagerConnectionPool.getConnection();
            preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                bean.setProduct(rs.getInt("prodotto"));
                bean.setDiscountPercentage(rs.getInt("percentuale_sconto"));
            }

        } finally {
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
            } finally {
                DriverManagerConnectionPool.releaseConnection(connection);
            }
        }
        return bean;
    }



    @Override
    public Collection<DiscountBean> doRetrieveAll(String order) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        Collection<DiscountBean> discounts = new LinkedList<DiscountBean>();

        String selectSQL = "select * from " + DiscountDaoDM.TABLE_NAME;

        if(order!=null && (order.toUpperCase().equals("DESC") || order.toUpperCase().equals("ASC")))
            selectSQL = selectSQL + " ORDER BY percentuale_sconto " + order;

        try {
            connection = DriverManagerConnectionPool.getConnection();
            preparedStatement = connection.prepareStatement(selectSQL);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                DiscountBean bean = new DiscountBean();

                bean.setProduct(rs.getInt("prodotto"));
                bean.setDiscountPercentage(rs.getInt("percentuale_sconto"));
            }

        } finally {
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
            } finally {
                DriverManagerConnectionPool.releaseConnection(connection);
            }
        }
        return discounts;
    }
}
