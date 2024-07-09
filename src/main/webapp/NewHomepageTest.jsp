<%@ page import="ec.model.cart.ShoppingCart" %>
<%@ page import="ec.control.Profile" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Title</title>
    <meta charset="UTF-8"  >
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Home Page</title>
    <link rel="stylesheet" href="./css/NewHomepage.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="./css/LogoSVG.css">
    <link rel="stylesheet" href="./css/searchBar.css">

    <script src="js/newHeaderScript.js"></script>
</head>
<body>

    <!-- Header -->
    <header id="header" class="transparent">
        <div class="header-container">
            <!-- Link utili -->
            <div id="link-container" class="link-container">
                <a id="catalogo-link" href="${pageContext.request.contextPath}/product">Catalogo</a>
                <a id="info-link" href="${pageContext.request.contextPath}/Info.jsp">Info</a>
            </div>

            <!-- Logo -->
            <a href="${pageContext.request.contextPath}/Homepage.jsp" class="logo-link">
                <!-- Div in cui viene caricato il logo -->
                <div id="logo-container" class="logo-container"></div>
            </a>
        <!--
            <div class="search-bar">
                <form id="searchForm" class="ricerca">
                    <input id="cerca" type="text" placeholder="Cerca prodotto" aria-label="Cerca prodotto" autocomplete="off" required>
                    <button id="submit" type="submit"><i class="fas fa-search" aria-hidden="true"></i><span class="sr-only">Cerca</span></button>
                </form>
            </div>
        -->
            <!-- Icone -->
            <div id="icons" class="icons">
                <a href="${pageContext.request.contextPath}/carrello" aria-label="Carrello">
                    <i class="fas fa-shopping-cart icon" aria-hidden="true"></i>
                    <% ShoppingCart cart = (ShoppingCart) request.getSession().getAttribute("cart");
                        if (cart != null && cart.getNumProdottiTot() > 0) { %>
                    <span class="numeroProdotti" id="numeroProdotti"><%= cart.getNumProdottiTot() %></span>
                    <% } %>
                </a>
                <a class="last-icon" href="${pageContext.request.contextPath}/profileServlet" aria-label="Profilo utente">
                    <i class="fas fa-user icon" aria-hidden="true"></i>
                </a>
            </div>
        </div>
    </header>

    <div class="first-image-container" id="fist-image-container">
        <img src="uploadFile/altrepalle.jpg" alt="immagine home">
        <div class="text-over-image transparent text-to-animate" id="text-over-image">"Il problema non è il numero di palle che hai,<br> ma come le giochi."</div>
    </div>

    <div class="double-section" id="first-section">
        <div class="double-image-text-container">
            <img src="uploadFile/airochei.jpg" alt="Immagine 1" class="double-section-image">
            <div class="double-section-text left">Prodotti scontati</div>
        </div>

        <div class="double-image-text-container">
            <img src="uploadFile/pallecoloratissime.jpg" alt="Immagine 2" class="double-section-image">
            <div class="double-section-text right">Mia sorella morta</div>
        </div>
    </div>

    <!-- TODO: aggiungere testo a scorrimento simile a first-image -->
    <div class="single-section" id="second-section">
        <div class="single-image-text-container">
            <img src="uploadFile/tabletennisroba.jpg" alt="Immagine 3" class="single-section-image">
            <div class="single-section-text">Palle giganterrime</div>
        </div>
    </div>

    <div class="scrollable-section" id="third-section">
        <div class="scrollable-container">

            <div class="scrollable-object">
                <img src="uploadFile/pallanera.jpg" alt="Immagine 3" class="scrollable-image">
                <div class="scrollable-text">Palle giganterrime</div>
            </div>
            <div class="scrollable-object">
                <img src="uploadFile/pallecolorate.jpg" alt="Immagine 3" class="scrollable-image">
                <div class="scrollable-text">Palle giganterrime</div>
            </div>
            <div class="scrollable-object">
                <img src="uploadFile/racchettepallina.jpg" alt="Immagine 3" class="scrollable-image">
                <div class="scrollable-text">Palle giganterrime</div>
            </div>
            <div class="scrollable-object">
                <img src="uploadFile/pallecoloratissime.jpg" alt="Immagine 3" class="scrollable-image">
                <div class="scrollable-text">Palle giganterrime</div>
            </div>

        </div>
    </div>




    <jsp:include page="Footer.jsp"/>
</body>
</html>
