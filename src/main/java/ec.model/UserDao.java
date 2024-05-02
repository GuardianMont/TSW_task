package ec.model;

import java.sql.SQLException;
import java.util.Collection;

public interface UserDao {
    public void doSave(UserBean user) throws SQLException;

    public boolean doDelete(String id) throws SQLException;

    public UserBean doRetrieveByKey(String id) throws SQLException;

    public Collection<UserBean> doRetrieveAll(String order) throws SQLException;
}
