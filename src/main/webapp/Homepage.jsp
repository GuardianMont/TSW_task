<%@ page import="ec.model.cart.ShoppingCart" %>
<%@ page import="ec.control.Profile" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Home Page</title>
    <link rel="stylesheet" href="./css/Homepage.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>
<body>
<jsp:include page="Header.jsp"/>

<div class="header-background"></div>

<div class="main-content">
    <div class="section" id="new-arrivals">
        <h2>Nuovi Arrivi</h2>
        <div class="products-container">
            <div class="product">
                <img src="./uploadFile/download.jpg" alt="Nuovo Arrivo 1">
                <p><a href="#">Prodotto 1</a></p>
            </div>
            <div class="product">
                <img src="./uploadFile/download2.jpg" alt="Nuovo Arrivo 2">
                <p><a href="#">Prodotto 2</a></p>
            </div>
            <div class="product">
                <img src="./uploadFile/fsvf.png" alt="Nuovo Arrivo 3">
                <p><a href="#">Prodotto 3</a></p>
            </div>
        </div>
    </div>

    <div class="section" id="discounted-products">
        <h2>Prodotti Scontati</h2>
        <div class="products-container">
            <div class="product">
                <img src="https://via.placeholder.com/300" alt="Prodotto Scontato 1">
                <p><a href="#">Prodotto 1</a></p>
            </div>
            <div class="product">
                <img src="https://via.placeholder.com/300" alt="Prodotto Scontato 2">
                <p><a href="#">Prodotto 2</a></p>
            </div>
            <div class="product">
                <img src="https://via.placeholder.com/300" alt="Prodotto Scontato 3">
                <p><a href="#">Prodotto 3</a></p>
            </div>
        </div>
    </div>

    <div class="section" id="best-sellers">
        <h2>I Più Venduti</h2>
        <div class="products-container">
            <div class="product">
                <img src="https://via.placeholder.com/300" alt="Best Seller 1">
                <p><a href="#">Prodotto 1</a></p>
            </div>
            <div class="product">
                <img src="https://via.placeholder.com/300" alt="Best Seller 2">
                <p><a href="#">Prodotto 2</a></p>
            </div>
            <div class="product">
                <img src="https://via.placeholder.com/300" alt="Best Seller 3">
                <p><a href="#">Prodotto 3</a></p>
            </div>
        </div>
    </div>
</div>

<jsp:include page="Footer.jsp"/>
</body>
</html>
