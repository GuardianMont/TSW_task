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
    <script src="${pageContext.request.contextPath}/js/burgerMenu.js"></script>

</head>
<body>
<header class="header-container">
    <div class="logo">
        <a href="${pageContext.request.contextPath}/Homepage.jsp">
            <img src="${pageContext.request.contextPath}/uploadFile/loghino.svg" alt="Tavolando">
        </a>
    </div>
    <nav class="nav-menu">
        <ul>
            <li>
                <a href="${pageContext.request.contextPath}/NewHomepageTest.jsp">Home</a>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/product">Catalogo</a>
                <ul class="submenu">
                    <li>
                        <a href="${pageContext.request.contextPath}/SearchByCategory?categoria=tavolo">Tavoli</a>
                        <ul class="submenu hide">
                            <li><a href="${pageContext.request.contextPath}/SearchByCategory?categoria=biliardo">Tavoli da biliardo</a></li>
                            <li><a href="${pageContext.request.contextPath}/SearchByCategory?categoria=AirHokey">Tavoli Air hockey</a></li>
                            <li><a href="${pageContext.request.contextPath}/SearchByCategory?categoria=pingPong">Tavoli ping pong</a></li>
                            <li><a href="${pageContext.request.contextPath}/SearchByCategory?categoria=multifunzione">Tavoli multifunzione</a></li>
                        </ul>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/SearchByCategory?categoria=attrezzatura">Attrezzature</a>
                        <ul class="submenu hide">
                            <li><a href="${pageContext.request.contextPath}/SearchByCategory?categoria=stecche">Stecche</a></li>
                            <li><a href="${pageContext.request.contextPath}/SearchByCategory?categoria=bilie">bilie biliardo</a></li>
                            <li><a href="${pageContext.request.contextPath}/SearchByCategory?categoria=racchette">Racchette ping pong</a></li>
                            <li><a href="${pageContext.request.contextPath}/SearchByCategory?categoria=palline">Palline ping pong</a></li>
                            <li><a href="${pageContext.request.contextPath}/SearchByCategory?categoria=AttrezzaturaAir">Attrezzatura air hockey</a></li>
                        </ul>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/SearchByCategory?categoria=kit">Kit assortiti</a>
                    </li>
                </ul>
            </li>
        </ul>
    </nav>

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
        <a href="${pageContext.request.contextPath}/favoritesUser" aria-label="Favourites-User">
            <i class="fas fa-star" aria-hidden="true"></i>
        </a>
    </div>
    <div class="burger-menu">
        <span class="burger-icon">&#9776;</span>
    </div>
    <div class="burger-menu-container">
        <a href="${pageContext.request.contextPath}/NewHomepageTest.jsp">Home</a>
        <a href="${pageContext.request.contextPath}/product">Catalogo</a>
        <a href="${pageContext.request.contextPath}/carrello"><i class="fas fa-shopping-cart" aria-hidden="true"></i> Carrello
            <% if (cart != null && cart.getNumProdottiTot() > 0) { %>(<%= cart.getNumProdottiTot() %>)<% } %></a>
        <a href="${pageContext.request.contextPath}/profileServlet"><i class="fas fa-user" aria-hidden="true"></i> Profilo utente</a>
        <a href="${pageContext.request.contextPath}/favoritesUser" ><i class="fas fa-star" aria-hidden="true"></i> Preferiti</a>
    </div>
</header>
</body>
</html>
