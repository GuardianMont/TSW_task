<%@ page import="ec.model.cart.ShoppingCart" %>
<%@ page import="ec.control.Profile" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <title>Header</title>
    <link rel="stylesheet" href="css/Header.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
    <link rel="stylesheet" href="css/searchBar.css">
    <script src = "js/searchBar.js"></script>
</head>
<body>
<div class="header-container">
    <header class="Tavolando">
        <img src="uploadFile/loghino.svg" alt="Tavolando" class="TavolandImg" id="TavolandoIcon">
    </header>
    <nav>
        <ul class="nav-menu">
            <li class="barra-ricerca">
                <form id="searchForm" class="ricerca">
                    <input id="cerca" type="text" placeholder="Cerca prodotto" autocomplete="off" required>
                    <button id="submit" type="submit"><i class="fas fa-search"></i></button>
                </form>
            </li>
            <li><a href="./Homepage.jsp">Home</a></li>
            <li><a href="product">Prodotti</a>
                <ul class="submenu">
                    <li><a href="#">Prodotto</a></li>
                    <li><a href="#">Prodotto</a></li>
                    <li><a href="#">Prodotto</a></li>
                </ul>
            </li>
            <li><a href="#">Servizi</a></li>
            <li><a href="#">Contatti</a></li>
            <li>
                <div class="carrello">
                    <a href="carrello">
                        <i class="fas fa-shopping-cart"></i>
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
                        <i class="fas fa-user"></i>
                    </a>
                </div>
            </li>
        </ul>
    </nav>
</div>
</body>
</html>
