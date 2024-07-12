<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, ec.model.product.ProductBean, java.util.Base64"%>

<%
    Collection<?> products = (Collection<?>) request.getAttribute("preferiti");
    if (products == null) {
        response.sendRedirect("./product");
        return;
    }
    String userId = (String) session.getAttribute("userId");
    if (userId == null) {
        response.sendRedirect("./login_signup.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/ProductView.css">
    <link rel="stylesheet" href="css/notifica.css">
    <link rel="stylesheet" href="css/Sconto.css">
    <title>Articoli Preferiti</title>
    <script src="js/validationProduct.js"></script>
    <script src="js/notifica.js"></script>
    <script src="js/functionFavorites.js"></script>
    <script>
        window.onload = function() {
            if ('<%= session.getAttribute("userId") %>' !== 'null') {
                loadFavorites();
            }
        };
    </script>
</head>
<body>

<jsp:include page="Header.jsp" />

<div class="generale">
    <h1>La tua selezione di prodotti preferiti</h1>
    <div class="sorting-dropdown">
        <select onchange="window.location.href=this.value" class="select-sorting">
            <option value="#">Ordina per</option>
            <option value="product?sort=id">Nuove Aggiunte</option>
            <option value="product?sort=nome">Ordine alfabetico</option>
            <option value="product?sort=prezzo">Prezzo (Ascendente)</option>
            <option value="product?sort=prezzoDec">Prezzo (Decrescente)</option>
        </select>
    </div>
</div>

<%
    if (products == null || products.isEmpty()) { %>
<div class="no-product-wrapper">
    <div class="No-product">
        <h1>Nessun risultato</h1>
        <p>Non hai selezionato ancora nessun prodotto tra i preferiti</p>
        <p>Per farlo puoi dal catalogo interagire con la stellina posta su ogni prodotto</p>
        <p>e creare la tua selezione di prodotti personale</p>
    </div>
</div>
<% } %>

<div class="container">
    <%
        if (products != null && !products.isEmpty()) {
            for (Object obj : products) {
                ProductBean bean = (ProductBean) obj;
                boolean outOfStock = bean.getDisponibilita() == 0;
                double prezzoUnitario = bean.getPrezzo();
                double prezzoScontato = bean.getPrezzoScontato();
                boolean hasDiscount = prezzoScontato > 0;
    %>
    <div class="product <%= outOfStock ? "out-of-stock" : "" %>">
        <div class="product-content">
            <div class="immagini">
                <%
                    String stockImg = "";
                    byte[] imageData = bean.getImmagineUrl();
                    if (imageData != null) {
                        stockImg = Base64.getEncoder().encodeToString(imageData);
                    }
                %>
                <img src="data:image/jpeg;base64,<%= stockImg %>" class="img-product" alt="<%= bean.getNome() %>" onclick="window.location.href='product?opzione=read&id=<%= bean.getId() %>'">
                <%
                    if (hasDiscount) {
                %>
                <div class="sconto-visible">
                    <%= bean.getPercentualeSconto() %>%
                </div>
                <% } %>
            </div>
            <h2 onclick="window.location.href='product?opzione=read&id=<%= bean.getId() %>'"><%= bean.getNome() %></h2>
            <p>
                <% if (hasDiscount) { %>
                <span class="prezzo-unitario"><%= String.format("%.2f", prezzoUnitario) %> &euro;</span>
                <span class="prezzo-scontato"><%= String.format("%.2f", prezzoScontato) %> &euro;</span>
                <% } else { %>
                <span><%= prezzoUnitario %> &euro;</span>
                <% } %>
            </p>
            <div class="button-container">
                <p>
                        <% if (bean.getDisponibilita() > 0) { %>
                    <a href="carrello?opzione=add&id=<%= bean.getId() %>" class="add-button">Aggiungi al carrello</a> <br>
                        <% } else { %>
                <div class="out-of-stock-message">
                    <img src="uploadFile/erroreAttentionIcon.png" alt="Info Icon" width="15" height="15">
                    <span id="out-of-stock-message-text">Esaurito</span>
                </div>
                <br>
                <% } %>
                <% if (userId != null) { %>
                <img src="uploadFile/favorites_32.png" alt="Favorite Icon" id="favorite-icon-<%= bean.getId() %>"
                     class="favorite not-added" onclick="toggleFavorite(<%= bean.getId() %>)">
                <% } %>
                </p>
            </div>
        </div>
    </div>
    <%
            }
        }
    %>
</div>

<jsp:include page="Footer.jsp" />

<script>
    var dropdownBtn = document.querySelector(".dropdown-btn");
    if (dropdownBtn) {
        dropdownBtn.addEventListener("click", function() {
            this.parentElement.classList.toggle("active");
        });
    }
</script>

</body>
</html>
