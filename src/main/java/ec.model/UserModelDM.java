package ec.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

public class UserModelDM implements UserDao{

    private static final String TABLE_NAME = "Utente";
    @Override
    public synchronized void doSave(UserBean user) throws SQLException {
        String insertSQL = "INSERT INTO " + UserModelDM.TABLE_NAME
                + " (username, nome, cognome, email, n_telefono, pssw)"
                + " VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getNome());
            preparedStatement.setString(3, user.getCognome());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setString(5, user.getPhoneNumber());
            preparedStatement.setString(6, user.getPassword());

            preparedStatement.executeUpdate();
        }
    }

    @Override
    public boolean doDelete(String id) throws SQLException {
        return false;
    }

    @Override
    public UserBean doRetrieveByKey(String id) throws SQLException {
        return null;
    }

    @Override
    public Collection<UserBean> doRetrieveAll(String order) throws SQLException {
        return null;
    }
}
