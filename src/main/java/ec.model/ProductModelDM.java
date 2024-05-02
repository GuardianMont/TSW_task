package ec.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;



public class ProductModelDM implements ProductDao {

	private static final String TABLE_NAME = "Prodotto";

	@Override
	public synchronized void doSave(ProductBean product) throws SQLException {
		String insertSQL = "INSERT INTO " + ProductModelDM.TABLE_NAME
		+ " (nome, descrizione, prezzo, fascia_iva, dimensioni, disponibilita, categoria, colore, immagine)"
				+ " VALUES (?, ?, ?, ? ,? ,? ,? ,? , ?)";
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
			preparedStatement.setBytes(9, product.getImmagineUrl());

			preparedStatement.executeUpdate();
		}

	}

	@Override
	public synchronized ProductBean doRetrieveByKey(int code) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		ProductBean bean = new ProductBean();

		String selectSQL = "select * from " + ProductModelDM.TABLE_NAME + " where id = ?";

		try {
			connection = DriverManagerConnectionPool.getConnection();
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
			}

		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} finally {
				DriverManagerConnectionPool.releaseConnection(connection);
			}
		}
		return bean;
	}

	@Override
	public synchronized boolean doDelete(int code) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		int result = 0;

		String deleteSQL = "DELETE FROM " + ProductModelDM.TABLE_NAME + " WHERE CODE = ?";

		try {
			connection = DriverManagerConnectionPool.getConnection();
			preparedStatement = connection.prepareStatement(deleteSQL);
			preparedStatement.setInt(1, code);

			result = preparedStatement.executeUpdate();

		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} finally {
				DriverManagerConnectionPool.releaseConnection(connection);
			}
		}
		return (result != 0);
	}

	@Override
	public synchronized Collection<ProductBean> doRetrieveAll(String order) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		Collection<ProductBean> products = new LinkedList<>();

		String selectSQL = "SELECT * FROM " + ProductModelDM.TABLE_NAME;

		try {

			connection = DriverManagerConnectionPool.getConnection();


			if (order != null && !order.equals("")) {

				preparedStatement = connection.prepareStatement(selectSQL + "ORDER BY ?");

				if (order.equals("prezzoDec")) {
					//prezzo decrescente
					preparedStatement.setString(1, "prezzo desc");
				}else {
				//prezzo crescente e tutti gli altri
					preparedStatement.setString(1, order);
				}
			}else
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
				products.add(bean);
			}

		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} finally {
				DriverManagerConnectionPool.releaseConnection(connection);
			}
		}

		return products;
	}

}