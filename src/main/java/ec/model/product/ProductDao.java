package ec.model.product;

import java.sql.SQLException;

import java.util.Collection;

public interface ProductDao {
	public int doSave(ProductBean product) throws SQLException;

	public boolean doDelete(int code) throws SQLException;

	public ProductBean doRetrieveByKey(int code) throws SQLException;
	
	public Collection<ProductBean> doRetrieveAll(String order) throws SQLException;

}
