package ec.model.user;

import ec.model.ConnectionPool;
import ec.model.DriverManagerConnectionPool;
import ec.model.HashGenerator;


import javax.management.BadAttributeValueExpException;
import java.sql.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

public class UserDaoDM implements UserDao {

    private static final String TABLE_NAME = "Utente";

    @Override
    public synchronized void doSave(UserBean user) throws SQLException {
        String insertSQL = "INSERT INTO " + UserDaoDM.TABLE_NAME
                + " (username, nome, cognome, email, n_telefono, password_hash, salt, is_admin)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try( Connection connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)){

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getNome());
            preparedStatement.setString(3, user.getCognome());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setString(5, user.getPhoneNumber());
            preparedStatement.setBytes(6, user.getPassword());
            preparedStatement.setBytes(7, user.getSalt());
            preparedStatement.setBoolean(8, user.isAdmin());

            preparedStatement.executeUpdate();
        }
    }

    @Override
    public synchronized boolean doDelete(String id) throws SQLException {

        int result = 0;

        String deleteSQL = "DELETE FROM " + UserDaoDM.TABLE_NAME + " WHERE username = ?";

        try(Connection connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {

            preparedStatement.setString(1, id);

            result = preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return (result != 0);
    }

    @Override
    public synchronized UserBean doRetrieveByKey(String id) throws SQLException {

        UserBean bean = new UserBean();

        String selectSQL = "select * from " + UserDaoDM.TABLE_NAME + " where username = ? ";

        try(Connection connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setString(1, id);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                bean.setUsername(rs.getString("username"));
                bean.setNome(rs.getString("nome"));
                bean.setCognome(rs.getString("cognome"));
                bean.setEmail(rs.getString("email"));
                bean.setPassword(rs.getBytes("password_hash"));
                bean.setSalt(rs.getBytes("salt"));
                bean.setPhoneNumber(rs.getString("n_telefono"));
                bean.setAdmin(rs.getBoolean("is_admin"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return bean;
    }

    @Override
    public synchronized UserBean doRetrieveByEmail(String email) throws SQLException {

        UserBean bean = new UserBean();

        String selectSQL = "select * from " + UserDaoDM.TABLE_NAME + " where email = ?";

        try(Connection connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setString(1, email);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                bean.setUsername(rs.getString("username"));
                bean.setNome(rs.getString("nome"));
                bean.setCognome(rs.getString("cognome"));
                bean.setEmail(rs.getString("email"));
                bean.setPassword(rs.getBytes("password_hash"));
                bean.setSalt(rs.getBytes("salt"));
                bean.setPhoneNumber(rs.getString("n_telefono"));
                bean.setAdmin(rs.getBoolean("is_admin"));
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return bean;
    }

    @Override
    public Collection<UserBean> doRetrieveAll(String order) throws SQLException {

        Collection<UserBean> users = new LinkedList<>();

        String selectSQL = "SELECT * FROM " + UserDaoDM.TABLE_NAME;

        try(Connection connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                UserBean bean = new UserBean();

                bean.setUsername(rs.getString("username"));
                bean.setNome(rs.getString("nome"));
                bean.setCognome(rs.getString("cognome"));
                bean.setEmail(rs.getString("email"));
                bean.setPassword(rs.getBytes("password_hash"));
                bean.setSalt(rs.getBytes("salt"));
                bean.setPhoneNumber(rs.getString("n_telefono"));
                bean.setAdmin(rs.getBoolean("is_admin"));
                users.add(bean);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    @Override
    public synchronized UserBean getUserIfPasswordIsCorrect(String token, String pssw) {
        UserBean user = new UserBean();
        try {
            if (token.contains("@")) {
                user = doRetrieveByEmail(token);
            } else {
                user = doRetrieveByKey(token);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //else
        try {
            if (user.getSalt() != null) {
                // se l'hash calcolato sulla password passata è uguale a quello memorizzato sul database allora è true
                byte[] hash = HashGenerator.generateHash(pssw, user.getSalt());
                if (Arrays.equals(hash, user.getPassword())){
                    return user;
                }
            }
        } catch (BadAttributeValueExpException e) {
            e.printStackTrace();
        }
        return null;
    }

    //metodo per aggiornare i dati dell'utente
    @Override
    public boolean doUpdate(UserBean user) throws SQLException {
        String updateSQL = "UPDATE " + UserDaoDM.TABLE_NAME + " SET nome = ?, cognome = ?, email = ?, n_telefono = ?, password_hash = ?, salt = ?, is_admin = ? WHERE username = ?";
        int result = 0;
        try( Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);){

            preparedStatement.setString(1, user.getNome());
            preparedStatement.setString(2, user.getCognome());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getPhoneNumber());
            preparedStatement.setBytes(5, user.getPassword());
            preparedStatement.setBytes(6, user.getSalt());
            preparedStatement.setBoolean(7, user.isAdmin());

            preparedStatement.setString(8, user.getUsername());
            result = preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return result > 0;
    }

    public boolean UpdateRole(String userName, boolean role) throws SQLException {
        String updateSQL = "UPDATE " + UserDaoDM.TABLE_NAME + " SET is_admin = ? WHERE username = ?";
        int result = 0;
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);) {
            preparedStatement.setBoolean(1, role);
            preparedStatement.setString(2, userName);
            result = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Ritorna false in caso di errore
        }
        return result > 0; // Ritorna true se almeno una riga è stata aggiornata con successo
    }

    public int countByUsername(String username) throws SQLException {
        String query = "SELECT COUNT(*) AS count FROM  " +  UserDaoDM.TABLE_NAME + " WHERE username = ?";
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count");
                }
            }
        }
        return 0;
    }

    public int countByEmail(String email) throws SQLException {
        String query = "SELECT COUNT(*) AS count FROM " +  UserDaoDM.TABLE_NAME + " WHERE email = ?";
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count");
                }
            }
        }
        return 0;
    }

}
