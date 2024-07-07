package ec.model.PaymentMethod;

import ec.model.address.AddressUs;

import java.sql.SQLException;

public interface StoricoMetodiDiPagamentoDao {
    public void doSave(PayMethod pay, int CodFattura, String UtenteId, int numId) throws SQLException;

}
