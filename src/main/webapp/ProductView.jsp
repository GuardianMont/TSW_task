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
	<link rel="stylesheet" href="ProductView.css">
	<title>Storage DS/BF</title>

</head>

<body>

<jsp:include page="Header.jsp"/>

<div class="generale">
	<a href="carrello"> <img src="uploadFile/cart.png" width="30" height="auto" alt="carrello" class="carrello"></a>
		<h2 class="prodotti">Products</h2>

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
		<%
			String stockImg = "";
			byte[] imageData = bean.getImmagineUrl();
			if (imageData != null) {
				stockImg = Base64.getEncoder().encodeToString(imageData);
			}
		%>
		<img  src="data:image/jpeg;base64,<%= stockImg %>" class="img-product" alt="<%=bean.getNome()%>">
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

<h2>Insert</h2>
<form action="product" enctype="multipart/form-data" method="post" style="" >
		<input type="hidden" name="opzione" value="insert">
		
		<label for="nome">Nome:</label><br> 
		<input id="nome" name="nome" type="text" maxlength="20" required placeholder="inserire nome"><br>
		
		<label for="descrizione">Descrizione:</label><br>
		<textarea class="textarea" id="descrizione" name="descrizione" maxlength="30" required placeholder="inserire descrizione"></textarea><br>
		
		<label for="prezzo">Prezzo:</label><br> 
		<input id="prezzo" name="prezzo" type="number" min="0" value="0" required><br>

		<label for="quantita">Disponibilità</label><br> 
		<input id="quantita" name="quantita" type="number" min="1" value="1" required><br>
		
		<label for="iva">Fascia_iva</label><br> 
		<input id="iva" name="iva" type="number" min="1" value="1" required><br>
		
		<label for="dimensioni">Dimensioni</label><br> 
		<input id="dimensioni" name="dimensioni" type="text" maxlength="40" required placeholder="inserire dimensioni"><br>
		
		<label for="categoria">Categoria</label><br> 
		<input id="categoria" name="categoria" type="text" maxlength="40" required placeholder="inserire categoria"><br>
		
		<label for="colore">Colore</label><br> 
		<input id="colore" name="colore" type="text" maxlength="40" required placeholder="inserire colore"><br>

		<label for="img">Image</label><br>
		<input id="img" name="img" type="file" accept="image/png, image/jpeg"><br>

		<input type="submit" value="Add"><input type="reset" value="Reset">
	</form>


<script>
	// JavaScript per gestire il click sul pulsante
	document.querySelector(".dropdown-btn").addEventListener("click", function() {
		this.parentElement.classList.toggle("active");
	});
</script>

<jsp:include page="Footer.jsp"/>
</body>
</html>