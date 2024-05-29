package ec.model.PaymentMethod;

import java.sql.SQLException;
import java.util.Collection;

public interface PaymentDao {
    public boolean doSave(PayMethod pay, String codeUser, int num) throws SQLException;
    public boolean doDelete (String userID, int num) throws  SQLException;
    public Collection<PayMethod> doRetrieveAll (String userID) throws SQLException;
    public PayMethod doRetrieveByKey(String userID, int num) throws SQLException;
}
