package ec.control;

import ec.model.HashGenerator;
import ec.model.cart.CartDaoDM;
import ec.model.cart.CartItem;
import ec.model.cart.ShoppingCart;
import ec.model.user.UserBean;
import ec.model.user.UserDaoDM;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import javax.management.BadAttributeValueExpException;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

@WebServlet("/LoginSignup")
public class LoginSignupControl extends HttpServlet {
    private UserDaoDM userDao;
    private CartDaoDM cartDaoDM;

    @Override
    public void init() throws ServletException {
        userDao = new UserDaoDM();
        cartDaoDM = new CartDaoDM();
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String option = req.getParameter("option");
        if (option != null && option.equals("logout")) {
            HttpSession session = req.getSession();
            session.invalidate(); // Usare invalidate per rimuovere tutte le attributi di sessione
            resp.sendRedirect(req.getContextPath() + "/Homepage.jsp");
        } else if (req.getSession().getAttribute("userId") != null) {
            resp.sendRedirect(req.getContextPath() + "/profileServlet");
        } else {
            resp.sendRedirect(req.getContextPath() + "/Homepage.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String option = req.getParameter("option");
        String dis = "/Profile.jsp";
        String errorMessage = "";
        boolean redirectFlag = false;

        if (option != null) {
            switch (option) {
                case "login":
                    try {
                        if (!doLogin(req, resp)) {
                            dis = "/login_signup.jsp";
                            errorMessage = "Login fallito. username o password errati";
                        } else {
                            redirectFlag = true;
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;

                case "signup":
                    if (!doSignup(req, resp)) {
                        dis = "/login_signup.jsp";
                        errorMessage = "registrazione fallita";
                    } else {
                        redirectFlag = true;
                    }
                    break;
            }
        }
        if (!redirectFlag) {
            req.setAttribute("errorMessage", errorMessage);
            resp.setContentType("text/plain");
            resp.setCharacterEncoding("UTF-8");
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(dis);
            dispatcher.forward(req, resp);
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
            //logica per mergiare i carrelli
            ShoppingCart sessionCart = (ShoppingCart) session.getAttribute("cart");
            ShoppingCart dbCart = cartDaoDM.retriveItem(user.getUsername());
            if (sessionCart == null) {
                //cioè se in sessione non vi è alcun carrello
                sessionCart = new ShoppingCart();
            }
            //se invece nel Db non si ha il salvataggio di altri carrelli antecedenti
            if (dbCart != null) {
                for (CartItem item : dbCart.getItem_ordinati()) {
                    sessionCart.addCartItem(item.getItem(), item.getNumItem());
                }
            }
            session.setAttribute("cart", sessionCart);
            //aggiorno il carrello in sessione
            if (user.isAdmin()) {
                System.out.println("Setting isAdmin to true for user: " + user.getUsername());
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

    protected boolean doSignup(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("signup-username").trim();
        String name = req.getParameter("signup-name").trim();
        String surname = req.getParameter("signup-surname").trim();
        String email = req.getParameter("signup-email").trim();
        String phoneN = req.getParameter("signup-phone").trim();
        String password = req.getParameter("signup-password").trim();
        String repPassword = req.getParameter("signup-rep-password").trim();

        if (username.contains(" ") || email.contains(" ")) {
            return false;
        }

        if (password.equals(repPassword)) {
            try {
                UserBean user = new UserBean();
                user.setUsername(username);
                user.setNome(name);
                user.setCognome(surname);
                user.setEmail(email);
                user.setPhoneNumber(phoneN);
                byte[] salt = HashGenerator.generateSalt();
                byte[] passwordHash = HashGenerator.generateHash(password, salt);
                user.setPassword(passwordHash);
                user.setSalt(salt);
                user.setAdmin(false);
                userDao.doSave(user);
                HttpSession session = req.getSession();
                session.setAttribute("userId", user.getUsername());
                session.setAttribute("signupSuccess", true);
                resp.sendRedirect(req.getContextPath() + "/ProductView.jsp");
                return true;
            } catch (SQLIntegrityConstraintViolationException e) {
                e.printStackTrace();
            } catch (BadAttributeValueExpException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
