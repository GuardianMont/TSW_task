package ec.model.user;

import ec.model.ConnectionPool;
import ec.model.DriverManagerConnectionPool;
import ec.model.HashGenerator;


import java.sql.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

public class UserDaoDM implements UserDao {

    private static final String TABLE_NAME = "Utente";

    @Override
    public synchronized void doSave(UserBean user) throws SQLException {
        String insertSQL = "INSERT INTO " + UserDaoDM.TABLE_NAME
                + " (username, nome, cognome, email, n_telefono, pssw, salt)"
                + " VALUES (?, ?, ?, ?, ?, ?)";

        Connection connection = ConnectionPool.getInstance().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);

        preparedStatement.setString(1, user.getUsername());
        preparedStatement.setString(2, user.getNome());
        preparedStatement.setString(3, user.getCognome());
        preparedStatement.setString(4, user.getEmail());
        preparedStatement.setString(5, user.getPhoneNumber());
        preparedStatement.setBytes(6, user.getPassword());
        preparedStatement.setBytes(7, user.getSalt());

        preparedStatement.executeUpdate();

        if(connection != null)
            connection.close();

    }

    @Override
    public synchronized boolean doDelete(String id) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        int result = 0;

        String deleteSQL = "DELETE FROM " + UserDaoDM.TABLE_NAME + " WHERE username = ?";

        try {
            connection = DriverManagerConnectionPool.getConnection();
            preparedStatement = connection.prepareStatement(deleteSQL);
            preparedStatement.setString(1, id);

            result = preparedStatement.executeUpdate();

        } finally {
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
            } finally {
                DriverManagerConnectionPool.releaseConnection(connection);
            }
        }
        return (result != 0);
    
    }

    @Override
    public synchronized UserBean doRetrieveByKey(String id) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        UserBean bean = new UserBean();

        String selectSQL = "select * from " + UserDaoDM.TABLE_NAME + " where username = ?";

        try {
            connection = DriverManagerConnectionPool.getConnection();
            preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, id);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                bean.setUsername(rs.getString("username"));
                bean.setNome(rs.getString("nome"));
                bean.setCognome(rs.getString("cognome"));
                bean.setEmail(rs.getString("email"));
                bean.setPassword(rs.getBytes("pssw"));
                bean.setSalt(rs.getBytes("salt"));
                bean.setPhoneNumber(rs.getString("n_telefono"));
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
    public synchronized UserBean doRetrieveByEmail(String email) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        UserBean bean = new UserBean();

        String selectSQL = "select * from " + UserDaoDM.TABLE_NAME + " where email = ?";

        try {
            connection = DriverManagerConnectionPool.getConnection();
            preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, email);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                bean.setUsername(rs.getString("username"));
                bean.setNome(rs.getString("nome"));
                bean.setCognome(rs.getString("cognome"));
                bean.setEmail(rs.getString("email"));
                bean.setPassword(rs.getBytes("pssw"));
                bean.setSalt(rs.getBytes("salt"));
                bean.setPhoneNumber(rs.getString("n_telefono"));
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
    public Collection<UserBean> doRetrieveAll(String order) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        Collection<UserBean> users = new LinkedList<>();

        String selectSQL = "SELECT * FROM " + UserDaoDM.TABLE_NAME;

        try {

            connection = DriverManagerConnectionPool.getConnection();

            preparedStatement = connection.prepareStatement(selectSQL);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                UserBean bean = new UserBean();

                bean.setUsername(rs.getString("username"));
                bean.setNome(rs.getString("nome"));
                bean.setCognome(rs.getString("cognome"));
                bean.setEmail(rs.getString("email"));
                bean.setPassword(rs.getBytes("pssw"));
                bean.setSalt(rs.getBytes("salt"));
                bean.setPhoneNumber(rs.getString("n_telefono"));
                users.add(bean);
            }

        } finally {
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
            } finally {
                DriverManagerConnectionPool.releaseConnection(connection);
            }
        }

        return users;
    }

    @Override
    public synchronized boolean checkPassword(String token, String pssw) {

        UserBean user = null;
        try {
            if (token.contains("@")) {
                user = doRetrieveByEmail(token);
            } else {
                user = doRetrieveByKey(token);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        if(user == null) {
            return false;
        }
        //else
        try{
            // se l'hash calcolato sulla password passata è uguale a quello memorizzato sul database allora è true
            if (Arrays.equals(HashGenerator.generateHash(pssw, user.getSalt()), user.getPassword()))
                return true;
        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }
}
