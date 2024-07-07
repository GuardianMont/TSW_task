package ec.model.address;

import ec.model.cart.CartItem;

import java.sql.SQLException;
import java.util.ArrayList;

public interface StoricoIndirizziDao {
    public void doSave(AddressUs indirizzo, int CodFattura, String UtenteId, int numId) throws SQLException;

}
