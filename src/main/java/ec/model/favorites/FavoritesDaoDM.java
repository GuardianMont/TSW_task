package ec.model.favorites;

import ec.model.ConnectionPool;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FavoritesDaoDM implements FavoritesDao{
    private static  final String  TABLE_NAME="Preferiti";

    public void doSave (String userId, int ProductID) throws SQLException {
        String sqlInsert = "Insert into " + FavoritesDaoDM.TABLE_NAME +
                " (prodotto_id, utente_id) " +
                " VALUES (?, ?) ";

        try(Connection connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert)) {
            preparedStatement.setInt(1,ProductID);
            preparedStatement.setString(2,userId);

            preparedStatement.executeUpdate();
        }
    }

    public boolean doDelete (String userId, int ProductID) throws SQLException {
        String sqlDelete = "DELETE from " + FavoritesDaoDM.TABLE_NAME +
                " where utente_id=? AND prodotto_id = ?";

        int result=0;
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlDelete)){
            preparedStatement.setString(1,userId);
            preparedStatement.setInt(2, ProductID);

            result = preparedStatement.executeUpdate();
        }
        return (result!=0);
    }

    public ArrayList<Integer> retriveFavorites (String userId) throws SQLException{
        String sqlRetrive= "SELECT * from " + FavoritesDaoDM.TABLE_NAME +
                " where utente_id=? ";
    ArrayList<Integer> UserFavorites = new ArrayList<>();
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlRetrive)){
            preparedStatement.setString(1,userId);

            ResultSet res = preparedStatement.executeQuery();

            while (res.next()){
                UserFavorites.add(res.getInt("prodotto_id"));

            }

        }
        return UserFavorites;
    }
}
