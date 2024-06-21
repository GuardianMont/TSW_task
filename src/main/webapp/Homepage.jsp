<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Home Page</title>
    <link rel="stylesheet" href="styles.css">
    <!-- Includi Font Awesome per l'icona dell'hamburger menu -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
</head>
<body>

<header>
    <div class="menu-toggle">
        <i class="fas fa-bars"></i>
    </div>
    <!-- Includi il resto del tuo header -->
    <jsp:include page="Header.jsp"/>
</header>

<div class="header-background"></div>

<div class="main-content">
    <div class="section" id="new-arrivals">
        <h2>Nuovi Arrivi</h2>
        <div class="products-container">
            <%-- Esempio di prodotto. Sostituisci con i dati reali --%>
            <div class="product">
                <img src="./uploadFile/download.jpg" alt="Nuovo Arrivo 1">
                <p>Prodotto 1</p>
            </div>
            <div class="product">
                <img src="./uploadFile/download2.jpg" alt="Nuovo Arrivo 2">
                <p>Prodotto 2</p>
            </div>
            <div class="product">
                <img src="./uploadFile/fsvf.png" alt="Nuovo Arrivo 3">
                <p>Prodotto 3</p>
            </div>
            <%-- Aggiungi più prodotti qui --%>
        </div>
    </div>

    <div class="section" id="discounted-products">
        <h2>Prodotti Scontati</h2>
        <div class="products-container">
            <%-- Esempio di prodotto. Sostituisci con i dati reali --%>
            <div class="product">
                <img src="images/discounted1.jpg" alt="Prodotto Scontato 1">
                <p>Prodotto 1</p>
            </div>
            <div class="product">
                <img src="images/discounted2.jpg" alt="Prodotto Scontato 2">
                <p>Prodotto 2</p>
            </div>
            <div class="product">
                <img src="images/discounted3.jpg" alt="Prodotto Scontato 3">
                <p>Prodotto 3</p>
            </div>
            <%-- Aggiungi più prodotti qui --%>
        </div>
    </div>

    <div class="section" id="best-sellers">
        <h2>I Più Venduti</h2>
        <div class="products-container">
            <%-- Esempio di prodotto. Sostituisci con i dati reali --%>
            <div class="product">
                <img src="images/best-seller1.jpg" alt="Best Seller 1">
                <p>Prodotto 1</p>
            </div>
            <div class="product">
                <img src="images/best-seller2.jpg" alt="Best Seller 2">
                <p>Prodotto 2</p>
            </div>
            <div class="product">
                <img src="images/best-seller3.jpg" alt="Best Seller 3">
                <p>Prodotto 3</p>
            </div>
            <%-- Aggiungi più prodotti qui --%>
        </div>
    </div>
</div>

<jsp:include page="Footer.jsp"/>

<script src="./js/ProvaHeader.js"></script>

</body>
</html>
