<%@ page import="ec.model.cart.ShoppingCart" %><%--
  Created by IntelliJ IDEA.
  User: mario
  Date: 06/05/2024
  Time: 14:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Header</title>
    <link rel="stylesheet" href="Header.css">
</head>
<body>

<div class="header-container">
    <header class="Tavolando">Tavolando</header>
    <nav>

        <!--per la search bar-->
        <div class="barra-ricerca">
            <form class="ricerca">
                <input id="cerca" type="text" placeholder="Cerca nel sito" required>
                <input id="submit" type="submit" value="Cerca">
            </form>
        </div>
        <ul>
            <li><a href="#">Home</a> </li>

            <li><a href="product">Prodotti</a>

                <ul>
                    <li><a href="#">Prodotto 1</a> </li>
                    <li><a href="#">Prodotto 2</a> </li>
                    <li><a href="#">Prodotto 3</a> </li>
                </ul>
            </li>

            <li><a href="#">Servizi</a> </li>
            <li><a href="#">Contatti</a> </li>
            <li>
                <div class="carrello">
                    <a href="carrello">
                        <img src="uploadFile/cart.png" width="30" height="auto" alt="carrello" class="carrello" id="carrelloIcon">
                    <% ShoppingCart cart = (ShoppingCart) request.getSession().getAttribute("cart");
                        if (cart != null && cart.getNumProdottiTot() > 0) {
                    %>
                    <span class="numeroProdotti"><%= cart.getNumProdottiTot() %></span>
                    <% }
                    %>
                    </a>
                </div>
            </li>
        </ul>


    </nav>
</div>


</body>
</html>
