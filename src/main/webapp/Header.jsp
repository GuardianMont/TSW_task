<%@ page import="ec.model.cart.ShoppingCart" %>
<%@ page import="ec.control.Profile" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Header</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/Header.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/searchBar.css">
    <script src="${pageContext.request.contextPath}/js/searchBar.js"></script>
</head>
<body>
<header class="header-container">
    <div class="logo">
        <a href="${pageContext.request.contextPath}/Homepage.jsp">
            <img src="${pageContext.request.contextPath}/uploadFile/loghino.svg" alt="Tavolando">
        </a>
    </div>
    <ul class="nav-menu">
        <li><a href="${pageContext.request.contextPath}/Homepage.jsp">Home</a></li>
        <li>
            <a href="${pageContext.request.contextPath}/product">Prodotti</a>
            <ul class="submenu">
                <li><a href="#">Prodotto</a></li>
                <li><a href="#">Prodotto</a></li>
                <li><a href="#">Prodotto</a></li>
            </ul>
        </li>
    </ul>
    <div class="search-bar">
        <form id="searchForm" class="ricerca">
            <input id="cerca" type="text" placeholder="Cerca prodotto" aria-label="Cerca prodotto" autocomplete="off" required>
            <button id="submit" type="submit"><i class="fas fa-search" aria-hidden="true"></i><span class="sr-only">Cerca</span></button>
        </form>
    </div>
    <div class="icons">
        <a href="${pageContext.request.contextPath}/carrello" aria-label="Carrello">
            <i class="fas fa-shopping-cart" aria-hidden="true"></i>
            <% ShoppingCart cart = (ShoppingCart) request.getSession().getAttribute("cart");
                if (cart != null && cart.getNumProdottiTot() > 0) { %>
            <span class="numeroProdotti"><%= cart.getNumProdottiTot() %></span>
            <% } %>
        </a>
        <a href="${pageContext.request.contextPath}/profileServlet" aria-label="Profilo utente">
            <i class="fas fa-user" aria-hidden="true"></i>
        </a>
    </div>
</header>
</body>
</html>
