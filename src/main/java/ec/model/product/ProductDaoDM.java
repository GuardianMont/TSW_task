package ec.model.product;

import ec.model.ConnectionPool;


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
				+ " (nome, descrizione, prezzo, fascia_iva, dimensioni, disponibilita, categoria, colore, immagine, percentuale_sconto, is_visible)"
				+ " VALUES (?, ?, ?, ? ,? ,? ,? ,? , ?, ?, ?) ";
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
			preparedStatement.setInt(10, product.getPercentualeSconto());
			preparedStatement.setBoolean(11, product.isVisible());
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
			System.out.println("inserito il prodotto:" + generatedId);
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
				bean.setPrezzo(rs.getDouble("prezzo"));
				bean.setDisponibilita(rs.getInt("disponibilita"));
				bean.setDimensioni(rs.getString("dimensioni"));
				bean.setCategoria(rs.getString("categoria"));
				bean.setFasciaIva(rs.getDouble("fascia_iva"));
				bean.setImmagineUrl(rs.getBytes("immagine"));
				bean.setColore(rs.getString("colore"));
				bean.setPercentualeSconto(rs.getInt("percentuale_sconto"));
				bean.setVisible(rs.getBoolean("is_visible"));
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
		try (Connection connection = ConnectionPool.getInstance().getConnection()) {
            preparedStatement = connection.prepareStatement(deleteSQL);
			preparedStatement.setInt(1,code);
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
			if (order != null && !order.isEmpty()) {
				// non concateno direttamente la stringa perché lo renderebbe vulnerabile a SQL-injection
                switch (order) {
                    case "filtraScontati" -> selectSQL += " WHERE percentuale_sconto IS NOT NULL AND percentuale_sconto > 0";
                    case "id" -> selectSQL += " ORDER BY id ASC";
                    case "nome" -> selectSQL += " ORDER BY nome ASC";
                    case "prezzo" -> selectSQL += " ORDER BY prezzo ASC";
                    case "prezzoDec" -> selectSQL += " ORDER BY prezzo DESC";
                }
			}
			preparedStatement = connection.prepareStatement(selectSQL);
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				ProductBean bean = new ProductBean();

				bean.setId(rs.getInt("id"));
				bean.setNome(rs.getString("nome"));
				bean.setDescrizione(rs.getString("descrizione"));
				bean.setPrezzo(rs.getDouble("prezzo"));
				bean.setDisponibilita(rs.getInt("disponibilita"));
				bean.setDimensioni(rs.getString("dimensioni"));
				bean.setCategoria(rs.getString("categoria"));
				bean.setFasciaIva(rs.getDouble("fascia_iva"));
				bean.setImmagineUrl(rs.getBytes("immagine"));
				bean.setColore(rs.getString("colore"));
				bean.setPercentualeSconto(rs.getInt("percentuale_sconto"));
				bean.setVisible(rs.getBoolean("is_visible"));

				products.add(bean);
			}
		}catch (SQLException e){
			e.printStackTrace();
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
		int state =11;
		if (product.getTemp_url()!=null){
			img_part=", immagine = ? ";
		}
		try (Connection connection = ConnectionPool.getInstance().getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement("UPDATE " + ProductDaoDM.TABLE_NAME + " SET nome = ?, descrizione = ?, prezzo = ?, fascia_iva = ?, dimensioni = ?, disponibilita = ?, categoria = ?, colore = ?, percentuale_sconto = ?, is_visible = ? "+ img_part +" WHERE id = ?")) {
			preparedStatement.setString(1, product.getNome());
			preparedStatement.setString(2, product.getDescrizione());
			preparedStatement.setDouble(3, product.getPrezzo());
			preparedStatement.setDouble(4, product.getFasciaIva());
			preparedStatement.setString(5, product.getDimensioni());
			preparedStatement.setInt(6, product.getDisponibilita());
			preparedStatement.setString(7, product.getCategoria());
			preparedStatement.setString(8, product.getColore());
			preparedStatement.setInt(9, product.getPercentualeSconto());
			preparedStatement.setBoolean(10, product.isVisible());
			if (!img_part.isEmpty()) {
				File file = new File(product.getTemp_url());
				FileInputStream fis = new FileInputStream(file);
				preparedStatement.setBinaryStream(state, fis, fis.available());
				state++;
			}
			preparedStatement.setInt(state, product.getId());

			preparedStatement.executeUpdate();
		}
	}

	public void doUpdateQuantity(ProductBean product) throws SQLException, IOException {
		String sqlUpadate ="UPDATE " + ProductDaoDM.TABLE_NAME + " SET  disponibilita = ?  WHERE id = ? ";
		try (Connection connection = ConnectionPool.getInstance().getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(sqlUpadate)) {
			preparedStatement.setInt(1, product.getDisponibilita());
			preparedStatement.setInt(2, product.getId());

			preparedStatement.executeUpdate();
		}
	}

	public Collection<ProductBean> searchBarProducts(String query) throws SQLException, IOException{
        String sqlSearch = "(SELECT * FROM "+ ProductDaoDM.TABLE_NAME +" WHERE nome LIKE ?) " +
							"UNION " +
				"(SELECT * FROM " + ProductDaoDM.TABLE_NAME +" WHERE nome LIKE ? AND nome NOT LIKE ?); ";
		//creo due tabelle e poi ne posso dare la union dato che sono sicuro della loro compatibilità
		//in questo modo posso assicurarmi che i primi risultati siano quelli più precisi
		//dopodichè si ha una granularità maggiore

		Collection<ProductBean> products = new LinkedList<>();
		String search_1, search_2;
		if (query == null || query.trim().isEmpty()) {
			System.out.println("La stringa di query non può essere nulla o vuota.");
			return products;
		} else {
			search_1 =query + "%";
			search_2 = "%" + query + "%";
		}
		try ( Connection connection = ConnectionPool.getInstance().getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement(sqlSearch)){
			preparedStatement.setString(1, search_1);
			preparedStatement.setString(2, search_2);
			preparedStatement.setString(3,search_1);
			ResultSet res = preparedStatement.executeQuery();
			while (res.next()) {
				ProductBean bean = new ProductBean();

				bean.setId(res.getInt("id"));
				bean.setNome(res.getString("nome"));
				bean.setDescrizione(res.getString("descrizione"));
				bean.setPrezzo(res.getDouble("prezzo"));
				bean.setDisponibilita(res.getInt("disponibilita"));
				bean.setDimensioni(res.getString("dimensioni"));
				bean.setCategoria(res.getString("categoria"));
				bean.setFasciaIva(res.getDouble("fascia_iva"));
				bean.setImmagineUrl(res.getBytes("immagine"));
				bean.setColore(res.getString("colore"));
				bean.setPercentualeSconto(res.getInt("percentuale_sconto"));
				bean.setVisible(res.getBoolean("is_visible"));

				products.add(bean);
			}
		}
		return products;
	}

	public synchronized Collection<ProductBean> SearchByCategory(String categoria) throws SQLException {


		Collection<ProductBean> products = new LinkedList<>();
		String selectSQL = "SELECT * FROM " + ProductDaoDM.TABLE_NAME + " WHERE categoria=? ";
		try (Connection connection = ConnectionPool.getInstance().getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
			preparedStatement.setString(1, categoria);

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				ProductBean bean = new ProductBean();

				bean.setId(rs.getInt("id"));
				bean.setNome(rs.getString("nome"));
				bean.setDescrizione(rs.getString("descrizione"));
				bean.setPrezzo(rs.getDouble("prezzo"));
				bean.setDisponibilita(rs.getInt("disponibilita"));
				bean.setDimensioni(rs.getString("dimensioni"));
				bean.setCategoria(rs.getString("categoria"));
				bean.setFasciaIva(rs.getDouble("fascia_iva"));
				bean.setImmagineUrl(rs.getBytes("immagine"));
				bean.setColore(rs.getString("colore"));
				bean.setPercentualeSconto(rs.getInt("percentuale_sconto"));
				bean.setVisible(rs.getBoolean("is_visible"));

				products.add(bean);
			}
		}
		return products;
	}



}

