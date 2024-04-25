package ec.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ProductModelDS implements ProductDao {

	private static DataSource ds;

	static {
		try {
			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");

			ds = (DataSource) envCtx.lookup("jdbc/tavolando");

		} catch (NamingException e) {
			System.out.println("Error:" + e.getMessage());
		}
	}

	private static final String TABLE_NAME = "Prodotto";

	@Override
	public synchronized void doSave(ProductBean product) throws SQLException {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		String insertSQL = "INSERT INTO " + ProductModelDS.TABLE_NAME
				+ " (nome, descrizione, prezzo, fascia_iva, dimensioni, disponibilita, categoria, colore, immagine)"
				+ " VALUES (?, ?, ?, ? ,? ,? ,? ,? , ?)";

		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(insertSQL);
			preparedStatement.setString(1, product.getNome());
			preparedStatement.setString(2, product.getDescrizione());
			preparedStatement.setDouble(3, product.getPrezzo());
			preparedStatement.setDouble(4, product.getFasciaIva());
			preparedStatement.setString (5, product.getDimensioni());
			preparedStatement.setInt(6, product.getDisponibilita());
			preparedStatement.setString(7, product.getCategoria());
			preparedStatement.setString (8, product.getColore());
			String tempUrl = product.getTemp_url() != null ? product.getTemp_url() : "C:\\Users\\user\\Desktop\\no_img.jpg";
			File imageFile = new File(tempUrl);
			try (FileInputStream fis = new FileInputStream(imageFile)) {
				preparedStatement.setBinaryStream(9, fis, (int) imageFile.length());
				preparedStatement.executeUpdate();
				//connection.commit();
			} catch (IOException e) {
				throw new SQLException("Error reading image file", e);
			}
			//preparedStatement.setBytes(9, product.getImmagineUrl());
			preparedStatement.executeUpdate();
			//connection.commit();
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} finally {
				if (connection != null)
					connection.close();
			}
		}
	}

	@Override
	public synchronized ProductBean doRetrieveByKey(int code) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		ProductBean bean = new ProductBean();

		String selectSQL = "SELECT * FROM " + ProductModelDS.TABLE_NAME + " WHERE id = ?";

		try {
			connection = ds.getConnection();
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
				if (connection != null)
					connection.close();
			}
		}
		return bean;
	}

	@Override
	public synchronized boolean doDelete(int code) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		int result = 0;

		String deleteSQL = "DELETE FROM " + ProductModelDS.TABLE_NAME + " WHERE id = ?";

		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(deleteSQL);
			preparedStatement.setInt(1, code);

			result = preparedStatement.executeUpdate();

		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} finally {
				if (connection != null)
					connection.close();
			}
		}
		return (result != 0);
	}

	@Override
	public synchronized Collection<ProductBean> doRetrieveAll(String order) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		Collection<ProductBean> products = new LinkedList<ProductBean>();

		String selectSQL = "SELECT * FROM " + ProductModelDS.TABLE_NAME;

		if (order != null && !order.equals("")) {
			if (order.equals("prezzoDec")) {
				//prezzo decrescente
				selectSQL += " Order by prezzo desc";
			}else {
			//prezzo crescente e tutti gli altri
			selectSQL += " ORDER BY " + order;
			}
		}

		try {
			connection = ds.getConnection();
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
				if (connection != null)
					connection.close();
			}
		}
		return products;
	}

}