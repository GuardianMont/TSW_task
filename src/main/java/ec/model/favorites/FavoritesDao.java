package ec.model.favorites;

import ec.model.CheckOut.Ordine;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public interface FavoritesDao {
    public void doSave(String userId, int ProductID) throws SQLException;

    public boolean doDelete(String userId, int ProductID) throws SQLException;

    public ArrayList<Integer> retriveFavorites (String userId) throws SQLException;


}
