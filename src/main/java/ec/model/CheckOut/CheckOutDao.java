package ec.model.CheckOut;

import java.sql.SQLException;
import java.util.Collection;

public interface CheckOutDao {
    public void doSave(Ordine ordine) throws SQLException;

    public boolean doDelete(Ordine ordine) throws SQLException;

    public Ordine retriveOrdineFattura (int numId) throws SQLException;

    public Collection<Ordine> retriveAllOrdineUtente (int numId) throws SQLException;
}
