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
<%@ page contentType="text/html; charset=UTF-8" import="java.util.*,ec.model.ProductBean"%>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link href="ProductStyle.css" rel="stylesheet" type="text/css">
	<title>Storage DS/BF</title>
</head>

<body>
<a href="carrello"> <img src="src/main/webapp/uploadFile/cart.png" alt="carrello"></a>
	<h2>Products</h2>
	<a href="product">List</a>
	<table border="1">
		<tr>
			<th>ID <a href="product?sort=id">Sort</a></th>
			<th>Nome <a href="product?sort=nome">Sort</a></th>
			<th>Prezzo <a href="product?sort=prezzo">asc</a>/<a href="product?sort=prezzoDec">dec</a></th>
			<th>Img </th>
			<th>Action</th>
		</tr>
		<%
			if (products != null && products.size() != 0) {
				Iterator<?> it = products.iterator();
				while (it.hasNext()) {
					ProductBean bean = (ProductBean) it.next();
		%>
		<tr>
			<td><%=bean.getId()%></td>
			<td><%=bean.getNome()%></td>
			<td><%=bean.getPrezzo()%></td>
			<%
   				String stockImg = "";
   				byte[] imageData = bean.getImmagineUrl();
    			if (imageData != null) {
    		    stockImg = Base64.getEncoder().encodeToString(imageData);
    }
%>
			<td><img  src="data:image/jpeg;base64,<%= stockImg %>"  width=400px height=auto alt="no immagine" ></td>
			<td><a href="product?opzione=delete&id=<%=bean.getId()%>">Delete</a><br>
			<%//id del prodotto che vogliamo andare a cancellare %>
				<a href="product?opzione=read&id=<%=bean.getId()%>">Details</a><br>
				<a href="carrello?opzione=add&id=<%=bean.getId()%>">Add Cart</a>
				</td>
		</tr>
		<%
				}
			} else {
		%>
		<tr>
			<td colspan="6">No products available</td>
		</tr>
		<%
			}
		%>
	</table>
	<h2>Insert</h2>
	<form action="product" enctype="multipart/form-data" method="post" >
		<input type="hidden" name="opzione" value="insert">
		
		<label for="nome">Nome:</label><br> 
		<input id="nome" name="nome" type="text" maxlength="20" required placeholder="inserire nome"><br>
		
		<label for="descrizione">Descrizione:</label><br>
		<textarea id="descrizione" name="descrizione" maxlength="100" rows="3" required placeholder="inserire descrizione"></textarea><br>
		
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
</body>
</html>