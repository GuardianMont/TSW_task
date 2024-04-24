<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="java.util.Base64,ec.model.ProductBean"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Dettaglio prodotto</title>
<link href="ProductStyle.css" rel="stylesheet" type="text/css">
</head>
<body>
	<%
            ProductBean product = (ProductBean) request.getAttribute("product");
            if (product != null) {
    %>
    <h1>Prodotto <%= product.getNome() %></h1>
    <%
   				String stockImg = "";
   				byte[] imageData = product.getImmagineUrl();
    			if (imageData != null) {
    		    stockImg = Base64.getEncoder().encodeToString(imageData);
    }
%>
    <img src="data:image/jpeg;base64,<%= stockImg %>" width=400px height=auto alt="no immagine" />
    <table border="1">
        <tr>
            <th>ID</th>
            <th>Descrizione</th>
            <th>Quantità</th>
            <th>Prezzo</th>
        </tr>
        
        <tr>
            <td><%= product.getId() %></td>
            <td><%= product.getDescrizione() %></td>
            <td><%= product.getDisponibilita() %></td>
            <td><%= product.getPrezzo() %></td>
        </tr>
        <%
            }
        %>
    </table>
    <a href="./product"> return</a>
</body>
</html>