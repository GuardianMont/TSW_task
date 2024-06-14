package ec.model.discount;


import java.sql.SQLException;
import java.util.Collection;

public interface DiscountDao {
    public void doSave(DiscountBean discount) throws SQLException;

    public boolean doDelete(int id) throws SQLException;

    public DiscountBean doRetrieveByKey(int id) throws SQLException;

    public Collection<DiscountBean> doRetrieveAll(String order) throws SQLException;

}
