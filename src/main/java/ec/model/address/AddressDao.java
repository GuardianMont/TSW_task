package ec.model.address;

import java.sql.SQLException;
import java.util.Collection;

public interface AddressDao {
    public boolean doSave(AddressUs ad, String userID, int num) throws SQLException;

    public boolean doDelete(String userID, int num) throws SQLException;

    public AddressUs doRetrieveByKey(String userID, int num) throws SQLException;

    public Collection<AddressUs> doRetrieveAll(String userID) throws SQLException;
}
