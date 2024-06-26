package ec.control;

import ec.model.product.ProductBean;
import ec.model.product.ProductDaoDM;
import ec.util.CartManager;
import ec.util.FileUploadManager;
import ec.util.ProductBeanCreator;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.*;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

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
						if (handleUpdateAction(request)) {
							dis = "/carrello";
							ServletContext context = getServletContext();
							context.setAttribute("op", "update");
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
		int id = Integer.parseInt(request.getParameter("id"));
		model.doDelete(id);
		CartManager.removeProductFromCart(request.getSession(), id);
		//gestione esterna del carrello
	}

	private boolean handleUpdateAction(HttpServletRequest request) throws SQLException, ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("identificatore"));
		String appPath = request.getServletContext().getRealPath("");
		List<Part> fileParts = request.getParts().stream().filter(part -> "img".equals(part.getName()) && part.getSize() > 0).toList();
		String uploadedFilePath = FileUploadManager.saveUploadedFiles(appPath, SAVE_DIR, fileParts);
		//gestione esterna per la scrittura e salvataggio del file
		ProductBean bean = ProductBeanCreator.createProductBean(request, uploadedFilePath);
		//creazione esterna del bean
		bean.setId(id);
		model.doUpdate(bean);
		return CartManager.updateProductInCart(request.getSession(), id);
	}

	private void handleInsertAction(HttpServletRequest request) throws SQLException, ServletException, IOException {
		if (!request.getMethod().equalsIgnoreCase("POST")) {
			throw new ServletException("La richiesta deve essere di tipo POST");
		}
		String appPath = request.getServletContext().getRealPath("");
		List<Part> fileParts = request.getParts().stream().filter(part -> "img".equals(part.getName()) && part.getSize() > 0).toList();
		String uploadedFilePath = FileUploadManager.saveUploadedFiles(appPath, SAVE_DIR, fileParts);
		ProductBean bean = ProductBeanCreator.createProductBean(request, uploadedFilePath);
		model.doSave(bean);
		HttpSession session = request.getSession();
		session.setAttribute("inserted", true);
	}
}
