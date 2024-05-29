package ec.model.product;

import ec.model.ConnectionPool;
import ec.model.DriverManagerConnectionPool;
import ec.model.cart.CartDaoDM;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

public class ProductDaoDM implements ProductDao {

	private static final String TABLE_NAME = "Prodotto";

	@Override
	public synchronized int doSave(ProductBean product) throws SQLException {
		int generatedId = -1;
		String insertSQL = "INSERT INTO " + ProductDaoDM.TABLE_NAME
				+ " (nome, descrizione, prezzo, fascia_iva, dimensioni, disponibilita, categoria, colore, immagine)"
				+ " VALUES (?, ?, ?, ? ,? ,? ,? ,? , ?) ";
		try (Connection connection = ConnectionPool.getInstance().getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

			preparedStatement.setString(1, product.getNome());
			preparedStatement.setString(2, product.getDescrizione());
			preparedStatement.setDouble(3, product.getPrezzo());
			preparedStatement.setDouble(4, product.getFasciaIva());
			preparedStatement.setString(5, product.getDimensioni());
			preparedStatement.setInt(6, product.getDisponibilita());
			preparedStatement.setString(7, product.getCategoria());
			preparedStatement.setString(8, product.getColore());
            if (product.getTemp_url()!=null) {
				File file = new File(product.getTemp_url());
				FileInputStream fis = new FileInputStream(file);
				preparedStatement.setBinaryStream(9, fis, fis.available());
			}else preparedStatement.setBinaryStream(9,null);

			int row = preparedStatement.executeUpdate();
			if (row == 1) {
				try (ResultSet res = preparedStatement.getGeneratedKeys()) {
					if (res.next()) {
						generatedId = res.getInt(1);
					}
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		} finally {
			return generatedId;
		}
	}

	@Override
	public synchronized ProductBean doRetrieveByKey(int code) throws SQLException {

		PreparedStatement preparedStatement = null;

		ProductBean bean = new ProductBean();

		String selectSQL = "select * from " + ProductDaoDM.TABLE_NAME + " where id = ?";

		try (Connection connection = ConnectionPool.getInstance().getConnection()) {
			preparedStatement = connection.prepareStatement(selectSQL);
			preparedStatement.setInt(1, code);

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				bean.setId(rs.getInt("id"));
				bean.setNome(rs.getString("nome"));
				bean.setDescrizione(rs.getString("descrizione"));
				bean.setPrezzo(rs.getInt("prezzo"));
				bean.setDisponibilita(rs.getInt("disponibilita"));
				bean.setDimensioni(rs.getString("dimensioni"));
				bean.setCategoria(rs.getString("categoria"));
				bean.setFasciaIva(rs.getDouble("fascia_iva"));
				bean.setImmagineUrl(rs.getBytes("immagine"));
				bean.setColore(rs.getString("colore"));
			}

		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		return bean;
	}

	@Override
	public synchronized boolean doDelete(int code) throws SQLException {
		PreparedStatement preparedStatement = null;

		int result = 0;

		String deleteSQL = "DELETE FROM " + ProductDaoDM.TABLE_NAME + " WHERE id = ?";
        String deleteCartItem ="DELETE FROM Carrello WHERE prodotto_id= ?";
		try (Connection connection = ConnectionPool.getInstance().getConnection()) {
            preparedStatement = connection.prepareStatement(deleteCartItem);
			preparedStatement.setInt(1,code);
			result = preparedStatement.executeUpdate();

			preparedStatement = connection.prepareStatement(deleteSQL);
			preparedStatement.setInt(1, code);

			result = preparedStatement.executeUpdate();

		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		return (result != 0);
	}

	@Override
	public synchronized Collection<ProductBean> doRetrieveAll(String order) throws SQLException {
		PreparedStatement preparedStatement = null;

		Collection<ProductBean> products = new LinkedList<>();
		String selectSQL = "SELECT * FROM " + ProductDaoDM.TABLE_NAME;
		try (Connection connection = ConnectionPool.getInstance().getConnection()) {
			if (order != null && !order.equals("")) {
				selectSQL += " ORDER BY ";
				if (order.equals("prezzoDec")) {
					// prezzo decrescente
					selectSQL += "prezzo DESC";
				} else {
					// prezzo crescente e tutti gli altri
					selectSQL += order;
				}
			}
			preparedStatement = connection.prepareStatement(selectSQL);
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				ProductBean bean = new ProductBean();

				bean.setId(rs.getInt("id"));
				bean.setNome(rs.getString("nome"));
				bean.setDescrizione(rs.getString("descrizione"));
				bean.setPrezzo(rs.getInt("prezzo"));
				bean.setDisponibilita(rs.getInt("disponibilita"));
				bean.setDimensioni(rs.getString("dimensioni"));
				bean.setCategoria(rs.getString("categoria"));
				bean.setFasciaIva(rs.getDouble("fascia_iva"));
				bean.setImmagineUrl(rs.getBytes("immagine"));
				bean.setColore(rs.getString("colore"));

				products.add(bean);
			}
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		return products;
	}

	public void doUpdate(ProductBean product) throws SQLException, IOException {
		String img_part= "";
		int state =9;
		if (product.getTemp_url()!=null){
			img_part=", immagine = ? ";
		}
		try (Connection connection = ConnectionPool.getInstance().getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement("UPDATE " + ProductDaoDM.TABLE_NAME + " SET nome = ?, descrizione = ?, prezzo = ?, fascia_iva = ?, dimensioni = ?, disponibilita = ?, categoria = ?, colore = ? "+ img_part +" WHERE id = ?")) {
			preparedStatement.setString(1, product.getNome());
			preparedStatement.setString(2, product.getDescrizione());
			preparedStatement.setDouble(3, product.getPrezzo());
			preparedStatement.setDouble(4, product.getFasciaIva());
			preparedStatement.setString(5, product.getDimensioni());
			preparedStatement.setInt(6, product.getDisponibilita());
			preparedStatement.setString(7, product.getCategoria());
			preparedStatement.setString(8, product.getColore());
			if (!img_part.equals("")) {
				File file = new File(product.getTemp_url());
				FileInputStream fis = new FileInputStream(file);
				preparedStatement.setBinaryStream(9, fis, fis.available());
				state++;
			}
			preparedStatement.setInt(state, product.getId());

			preparedStatement.executeUpdate();
		}
	}

}

