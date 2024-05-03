package ec.model.cart;

import java.sql.SQLException;

public interface CartDao {
    public void doSave(CartItem Item, String codeUser) throws SQLException;
    public boolean doDeleteItem(int codeItem, String codeUser) throws SQLException;
    public boolean doDeleteCart(String codeUser)throws SQLException;
    public ShoppingCart retriveItem(String codeUser) throws SQLException;

}
