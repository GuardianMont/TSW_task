<%--
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
            <li><a href="carrello"> <img src="uploadFile/cart.png" width="30" height="auto" alt="carrello" class="carrello"></a></li>
        </ul>
        <div class="search-box">
            <input class="search-txt" placeholder="Search...">
            <button class="search-btn">
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none"
                     stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-search">
                    <circle cx="11" cy="11"
                            r="8">
                    </circle>
                    <line x1="21" y1="21" x2="16.65" y2="16.65"></line>
                </svg>
            </button>
        </div>

    </nav>
</div>


<!--per il search bar -->

</body>
</html>
