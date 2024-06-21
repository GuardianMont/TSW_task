<%@ page import="ec.model.cart.ShoppingCart" %>
<%@ page import="ec.control.Profile" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <title>Header</title>
    <link rel="stylesheet" href="css/Header.css">
</head>
<body>
<div class="header-container">
    <header class="Tavolando">
        <img src="uploadFile/loghino.svg" alt="Tavolando" class="TavolandImg" id="TavilandoIcon">
    </header>
    <nav>
        <ul class="nav-menu">
            <li class="barra-ricerca">
                <form class="ricerca">
                    <input id="cerca" type="text" placeholder="Cerca nel sito" required>
                    <input id="submit" type="submit" value="Cerca">
                </form>
            </li>
            <li><a href="./Homepage.jsp">Home</a></li>
            <li><a href="product">Prodotti</a>
                <ul class="submenu">
                    <li><a href="#">Prodotto 1</a></li>
                    <li><a href="#">Prodotto 2</a></li>
                    <li><a href="#">Prodotto 3</a></li>
                </ul>
            </li>
            <li><a href="#">Servizi</a></li>
            <li><a href="#">Contatti</a></li>
            <li>
                <div class="carrello">
                    <a href="carrello">
                        <img src="uploadFile/cart.png" width="30" height="auto" alt="carrello" class="carrelloImg" id="carrelloIcon">
                        <% ShoppingCart cart = (ShoppingCart) request.getSession().getAttribute("cart");
                            if (cart != null && cart.getNumProdottiTot() > 0) { %>
                        <span class="numeroProdotti"><%= cart.getNumProdottiTot() %></span>
                        <% } %>
                    </a>
                </div>
            </li>
            <li>
                <div class="utente">
                    <a href="profileServlet">
                        <img src="uploadFile/profile-user-svgrepo-com.svg" width="30" height="auto" alt="utente" class="carrelloImg" id="userIcon">
                    </a>
                </div>
            </li>
        </ul>
    </nav>
</div>
</body>
</html>
