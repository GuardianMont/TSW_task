package ec.model.user;

import ec.model.user.UserBean;

import java.sql.SQLException;
import java.util.Collection;

public interface UserDao {
    public void doSave(UserBean user) throws SQLException;

    public boolean doDelete(String id) throws SQLException;

    public UserBean doRetrieveByKey(String id) throws SQLException;

    public UserBean doRetrieveByEmail(String email) throws SQLException;

    public Collection<UserBean> doRetrieveAll(String order) throws SQLException;

    public UserBean getUserIfPasswordIsCorrect(String id, String password);


}
