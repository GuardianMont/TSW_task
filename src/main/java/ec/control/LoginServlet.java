package ec.control;

import ec.model.cart.CartDaoDM;
import ec.model.cart.CartItem;
import ec.model.cart.ShoppingCart;
import ec.model.user.UserBean;
import ec.model.user.UserDaoDM;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/Login")
//si occupa solo del login
public class LoginServlet extends HttpServlet {
    private UserDaoDM userDao;
    private CartDaoDM cartDaoDM;

    @Override
    public void init() throws ServletException {
        userDao = new UserDaoDM();
        cartDaoDM = new CartDaoDM();
        super.init();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            if (!doLogin(req, resp)) {
                //se il login fallisce l'utente viene renderizzato al form per il loginIn
                req.setAttribute("errorMessage", "Login fallito. username o password errati");
                req.getRequestDispatcher("/login_signup.jsp").forward(req, resp);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected boolean doLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        HttpSession session = req.getSession();
        String token = req.getParameter("login-token");
        String password = req.getParameter("login-password");

        if (password.length() >= 100) return false;

        UserBean user = userDao.getUserIfPasswordIsCorrect(token, password);
        if (user != null) {
            session.setAttribute("userId", user.getUsername());
            //mi occupo del merge tra il carrello attualmente in sessione ed il carrello
            //presente nel db ralativo all'utente registrato.
            ShoppingCart sessionCart = (ShoppingCart) session.getAttribute("cart");
            ShoppingCart dbCart = cartDaoDM.retriveItem(user.getUsername());
            if (sessionCart == null) {
                sessionCart = new ShoppingCart();
            }
            if (dbCart != null) {
                for (CartItem item : dbCart.getItem_ordinati()) {
                    //inoltre mi occupo di mergiare i due carrelli in modo da ottenerne uno
                    //unico e che mantiene le informazioni sia del carello in sessione
                     // che quello mantenuto nel db
                    sessionCart.addCartItem(item.getItem(), item.getNumItem());
                }
            }
            session.setAttribute("cart", sessionCart);

            if (user.isAdmin()) {
                session.setAttribute("isAdmin", true);
                resp.sendRedirect(req.getContextPath() + "/admin/AdminOptions.jsp");
                return true;
            } else {
                String referer = req.getHeader("referer");
                if (referer == null || referer.equals(req.getContextPath() + "/login_signup.jsp")) {
                    resp.sendRedirect(req.getContextPath() + "/ProductView.jsp");
                } else {
                    resp.sendRedirect(referer);
                }
                session.setAttribute("signupSuccess", true);
                return true;
            }
        }
        return false;
    }
}
