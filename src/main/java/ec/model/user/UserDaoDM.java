package ec.model.user;

import ec.model.ConnectionPool;
import ec.model.DriverManagerConnectionPool;
import ec.model.HashGenerator;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.management.BadAttributeValueExpException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

public class UserDaoDM implements UserDao {

    private static final String TABLE_NAME = "Utente";

    @Override
    public synchronized void doSave(UserBean user) throws SQLException {
        String insertSQL = "INSERT INTO " + UserDaoDM.TABLE_NAME
                + " (username, nome, cognome, email, n_telefono, pssw, salt)"
                + " VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = ConnectionPool.getInstance().getConnection();

             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getNome());
            preparedStatement.setString(3, user.getCognome());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setString(5, user.getPhoneNumber());

            //putting salt and hashing the password
            byte[] salt = HashGenerator.generateSalt();
            byte[] passwordHash = HashGenerator.generateHash(user.getPassword(), salt);


            preparedStatement.setBytes(6, );
            preparedStatement.setBytes(7, )

            preparedStatement.executeUpdate();
        }
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
                bean.setPassword(rs.getString("pssw"));
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
                bean.setPassword(rs.getString("pssw"));
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
}
