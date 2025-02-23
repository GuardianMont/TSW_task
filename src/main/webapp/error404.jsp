<%@ page language="java" contentType="text/html; charset=UTF-8" isErrorPage="true" %>

<!DOCTYPE html>
<html>
<head>
    <title>Errore del server</title>
    <meta charset="UTF-8">
    <style>
        body {
            font-family:'Open sans', Arial, serif;
        }
        .error-container {
            border: 1px solid #ccc;
            padding: 40px;
            max-width: none;
            text-align: center;
            margin: 0 auto;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        h1 {
            color: lightseagreen;
        }
    </style>
</head>
<body>
<jsp:include page="./Header.jsp"/>
<div class="error-container">
    <h1>Si è verificato un errore</h1>
    <p>La pagina in cui si sta tentando di accedere non esiste</p>
    <p>Per favore, ricaricare la pagina o tornare indietro.</p>
    <p>Ci scusiamo per il disagio</p>
</div>
<jsp:include page="./Footer.jsp"/>
</body>
</html>