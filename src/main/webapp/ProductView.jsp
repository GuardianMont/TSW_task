<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
	Collection<?> products = (Collection<?>) request.getAttribute("products");
	if(products == null) {
		response.sendRedirect("./product");	
		return;
	}
%>

<!DOCTYPE html>
<html>
<%@ page contentType="text/html; charset=UTF-8" import="java.util.*,ec.model.product.ProductBean"%>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" href="css/ProductView.css">
	<title>Storage DS/BF</title>
	<script src="js/validationProduct.js"></script>
</head>

<body>

<jsp:include page="Header.jsp"/>

<div class="generale">
	<div class="sorting-dropdown">
		<select onchange="window.location.href=this.value" class="select-sorting">
			<option value="#">Ordina per</option>
			<option value="product?sort=id">ID</option>
			<option value="product?sort=nome">Nome</option>
			<option value="product?sort=prezzo">Prezzo (Ascendente)</option>
			<option value="product?sort=prezzoDec">Prezzo (Decrescente)</option>
		</select>
	</div>
</div>

<div class="container">
		<%
			if (products != null && products.size() != 0) {
				Iterator<?> it = products.iterator();
				while (it.hasNext()) {
					ProductBean bean = (ProductBean) it.next();
		%>

	<div class="product">
		<div class="product-content">
			<div class="immagini">
		<%
			String stockImg = "";
			byte[] imageData = bean.getImmagineUrl();
			if (imageData != null) {
				stockImg = Base64.getEncoder().encodeToString(imageData);
			}
		%>

				<img  src="data:image/jpeg;base64,<%= stockImg %>" class="img-product" alt="<%=bean.getNome()%>">
			</div>
		<h2><%=bean.getNome()%></h2>
		<p><%=bean.getPrezzo()%></p>
		<p><a  href="product?opzione=delete&id=<%=bean.getId()%>">Delete</a> <br>
			<a href="product?opzione=read&id=<%=bean.getId()%>">Details</a><br>
			<a href="carrello?opzione=add&id=<%=bean.getId()%>">Add to Cart</a><br>
			<a href="product?opzione=show&id=<%=bean.getId()%>">Modifica</a>
		</p>
		</div>
	</div>

		<%
				}
			 %>
</div>
<% } else {
		%>
		<tr>
			<td colspan="6">No products available</td>
		</tr>
		<%
			}
		%>

<a href="insert.jsp" class="inserimento">Inserire prodotto</a>


<script>
	// JavaScript per gestire il click sul pulsante
	document.querySelector(".dropdown-btn").addEventListener("click", function() {
		this.parentElement.classList.toggle("active");
	});
</script>

<jsp:include page="Footer.jsp"/>
</body>
</html>