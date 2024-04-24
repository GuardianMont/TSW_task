package ec.control;


import jakarta.servlet.http.*;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import ec.model.ProductModel;
import ec.model.ProductModelDM;
import ec.model.ProductModelDS;
import ec.model.ProductBean;
import ec.model.FileManager;

/**
 * Servlet implementation class ProductControl
 */
public class ProductControl extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String SAVE_DIR = "/uploadTemp";
	private ProductModel model;
	private ProductModel connten;
	public ProductControl() {
		super();
		model = new ProductModelDS(); //  DataSource model andrebbe
		connten = new ProductModelDM();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		String dis = "/ProductView.jsp";

		if (action != null) {
			try {
				switch (action.toLowerCase()) {
					case "read":
						handleReadAction(request);
						dis = "/ProductDetail.jsp";
						break;
					case "delete":
						handleDeleteAction(request);
						break;
					case "insert":
						handleInsertAction(request);
						break;
				}
			} catch (SQLException e) {
				throw new ServletException("Database error", e);
			}
		}

		try {
			String sort = request.getParameter("sort");
			request.setAttribute("products", model.doRetrieveAll(sort));
		} catch (SQLException e) {
			throw new ServletException("Database error", e);
		}

		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(dis);
		dispatcher.forward(request, response);
	}

	private void handleReadAction(HttpServletRequest request) throws SQLException {
		int id = Integer.parseInt(request.getParameter("id"));
		request.setAttribute("product", model.doRetrieveByKey(id));
	}

	private void handleDeleteAction(HttpServletRequest request) throws SQLException {
		//azione di cancellazione
		int id = Integer.parseInt(request.getParameter("id"));
		model.doDelete(id);
	}

	private void handleInsertAction(HttpServletRequest request) throws SQLException, IOException, ServletException {
		//inserimento
		if (!request.getMethod().equalsIgnoreCase("POST")) {
			//mi devo accertare che la richiesta sia di tipo post
			throw new ServletException("La richiesta deve essere di tipo POST");
		}
		String name = request.getParameter("nome");
		String description = request.getParameter("descrizione");
		double price = Double.parseDouble(request.getParameter("prezzo"));
		int quantity = Integer.parseInt(request.getParameter("quantita"));
		double iva = Double.parseDouble(request.getParameter("iva"));
		String colore = request.getParameter("colore");
		String category = request.getParameter("categoria");
		String dimension = request.getParameter("dimensioni");

		String appPath = request.getServletContext().getRealPath("");
		String savePath = appPath + File.separator + SAVE_DIR;

		File fileSaveDir = new File(savePath);
		if (!fileSaveDir.exists()) {
			fileSaveDir.mkdir();
		}

		String ablPath = null;
		for (Part part : request.getParts()) {
			if (part.getName().equals("img")) { // il nome campo input è img !!
				String fileName = FileManager.extractFileName(part);
				if (fileName != null && !fileName.equals("")) {
					part.write(savePath + File.separator + fileName);
					ablPath = savePath + File.separator + fileName;
				}
			}
		}
		ProductBean bean = new ProductBean();
		bean.setNome(name);
		bean.setDescrizione(description);
		bean.setPrezzo(price);
		bean.setDisponibilita(quantity);
		bean.setFasciaIva(iva);
		bean.setColore(colore);
		bean.setCategoria(category);
		bean.setDimensioni(dimension);
		bean.setTemp_Url(ablPath);
		connten.doSave(bean);
	}
}