package ec.control;


import ec.model.*;
import ec.model.cart.CartDao;
import ec.model.cart.CartDaoDM;
import ec.model.cart.CartItem;
import ec.model.cart.ShoppingCart;
import ec.model.product.ProductBean;
import ec.model.product.ProductDaoDM;
import jakarta.servlet.http.*;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



import javax.swing.*;

public class ProductControl extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String SAVE_DIR = "\\uploadFile";
	private ProductDaoDM model;

	@Override
	public void init() throws ServletException {
		super.init();
		model = new ProductDaoDM();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	//Metodo per gestire una richiesta generica (le immagini non vanno gestite in un get)
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("opzione");
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
					case "show":
						handleReadAction(request);
						dis = "/update.jsp";
						break;
					case "update":
						if(handleUpdateAction(request)){
							dis="/carrello";
							request.setAttribute("opzione","update");
						}
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

	private void handleDeleteAction(HttpServletRequest request) throws SQLException, ServletException, IOException {
		//azione di cancellazione
		int id = Integer.parseInt(request.getParameter("id"));
		model.doDelete(id);
	}

	private boolean handleUpdateAction(HttpServletRequest request) throws SQLException, ServletException, IOException {
		//azione di modifica
		boolean update= false;
		int id = Integer.parseInt(request.getParameter("identificatore"));
        ProductBean bean = handleBeanCreation(request);
        bean.setId(id);
		// Eseguo l'aggiornamento nel database
		model.doUpdate(bean);
		//mi assicuro di aggiornare anche i prodotti nel carrello
		//per farlo nel caso in cui l'oggetto che sto aggiornando fa parte del carrello di sessione
		//induco il salvataggio delle informazioni del carrello nel db dove la reference del prodotto
		// è in base al id e non alle specifiche, ricarico il carrello in modo che in automatico si aggiorna il tutto
		ShoppingCart cart = (ShoppingCart)request.getSession().getAttribute("cart");
		if (cart!=null){
			if(cart.getItem(id)!=null) {
				update = true;
			}
		}
		return update;
	  }


	private ProductBean handleBeanCreation(HttpServletRequest request) throws SQLException, ServletException, IOException{
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
						File existingFile = new File(savePath, fileName);
						if (!existingFile.exists()) {
							//se esiste già un file con lo stesso nome non aggiungerà l'immagine
							filePart.write(savePath + File.separator + fileName);
						}
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
		return bean;
	}
	private void handleInsertAction(HttpServletRequest request) throws SQLException, ServletException, IOException {
		//inserimento
//		if (!request.getMethod().equalsIgnoreCase("POST")) {
//			//mi devo accertare che la richiesta sia di tipo post
//			throw new ServletException("La richiesta deve essere di tipo POST");
//		}
		model.doSave(handleBeanCreation(request));
	}
}