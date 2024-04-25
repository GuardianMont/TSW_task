package ec.control;


import ec.model.*;
import jakarta.servlet.http.*;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



import javax.swing.*;

/**
 * Servlet implementation class ProductControl
 */
public class ProductControl extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String SAVE_DIR = "\\uploadFile";
	private ProductModelDS model;

	public ProductControl() {
		super();
		model = new ProductModelDS(); //  DataSource model andrebbe
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		JOptionPane.showMessageDialog(null, "sono get");
		processRequest(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
		String action = request.getParameter("nome");
		JOptionPane.showMessageDialog(null, "devo fare " + action);
	}

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("opzione");
		JOptionPane.showMessageDialog(null, "devo fare " + action);
		String dis = "/ProductView.jsp";

		if (action != null) {
			try {
				switch (action.toLowerCase()) {
					case "read":
						JOptionPane.showMessageDialog(null, "sono qua");
						handleReadAction(request);
						dis = "/ProductDetail.jsp";
						break;
					case "delete":
						handleDeleteAction(request);
						break;
					case "insert":
						JOptionPane.showMessageDialog(null, "sono qui");
						handleInsertAction(request);
						JOptionPane.showMessageDialog(null, "sto dopo!");
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
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
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

	private void handleInsertAction(HttpServletRequest request) throws SQLException, ServletException, IOException {
		//inserimento
//		if (!request.getMethod().equalsIgnoreCase("POST")) {
//			//mi devo accertare che la richiesta sia di tipo post
//			throw new ServletException("La richiesta deve essere di tipo POST");
//		}
		String name = request.getParameter("nome");
		String description = request.getParameter("descrizione");
		double price = Double.parseDouble(request.getParameter("prezzo"));
		int quantity = Integer.parseInt(request.getParameter("quantita"));
		double iva = Double.parseDouble(request.getParameter("iva"));
		String colore = request.getParameter("colore");
		String category = request.getParameter("categoria");
		String dimension = request.getParameter("dimensioni");

		String appPath = request.getServletContext().getRealPath("");
		//String savePath = "C:\\Users\\user\\Desktop\\TSW_guardian_ver\\TSW_task\\src\\main\\webapp";
		//path usato diretto ma credo sia meglio dinamico
		//dinamicamente il save path arriva in task\\TSW_task-1.0-SNAPSHOT\\SAVE_DIR
		String savePath= appPath+ File.separator + SAVE_DIR;
		String ablPath = null;
		List<Part> fileParts = request.getParts().stream().filter(part -> "img".equals(part.getName()) && part.getSize() > 0).toList();
		if (fileParts.isEmpty()) {
			JOptionPane.showMessageDialog(null, "no files");
		}else {
			for (Part filePart : fileParts) {
				if (filePart.getName().equals("img")) { // il nome campo input è img !!
					String fileName = FileManager.extractFileName(filePart);
					if (fileName != null && !fileName.equals("")) {
						filePart.write(savePath + File.separator + fileName);
						ablPath = savePath + File.separator + fileName;
					}
				}
			}
			File fileSaveDir = new File(savePath);
			if (!fileSaveDir.exists()) {
				fileSaveDir.mkdir();
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
		model.doSave(bean);
	}
}