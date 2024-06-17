<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" import="java.util.Base64,ec.model.product.ProductBean"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Dettaglio prodotto</title>
    <link href="css/ProductStyle.css" rel="stylesheet" type="text/css">
</head>
<body>
<jsp:include page="Header.jsp"/>

<div class="product-container">
    <a href="./product" class="return-button">Return</a>
    <%
        ProductBean product = (ProductBean) request.getAttribute("product");
        if (product != null) {
    %>
    <div class="product-content">
        <div class="product-image">
            <br>
            <%
                String stockImg = "";
                byte[] imageData = product.getImmagineUrl();
                if (imageData != null) {
                    stockImg = Base64.getEncoder().encodeToString(imageData);
                }
            %>
            <img src="data:image/jpeg;base64,<%= stockImg %>" alt="no immagine" />
        </div>
        <div class="product-details">
            <h1><%= product.getNome() %></h1>
            <p><%= product.getDescrizione() %></p>
        </div>
        <div class="product-price">
            <h2>$<%= product.getPrezzo() %></h2>
            <a href="carrello?opzione=add&id=<%=product.getId()%>" class="add-to-cart-button">Add to Cart</a>
        </div>
    </div>

    <%
        }
    %>
</div>
<jsp:include page="Footer.jsp"/>
</body>
</html>
