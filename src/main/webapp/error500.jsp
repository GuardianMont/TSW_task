<%@ page language="java" contentType="text/html; charset=UTF-8" isErrorPage="true" %>
<!DOCTYPE html>
<html>
<head>
    <title>Errore del server</title>
    <meta charset="UTF-8">
    <style>
        body {
            font-family:'Open sans', Arial, serif;
            text-align: center;
            margin-top: 50px;
        }
        .error-container {
            border: 1px solid #ccc;
            padding: 20px;
            max-width: 600px;
            margin: 0 auto;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        h1 {
            color: lightseagreen;
        }
    </style>
</head>
<body>
<jsp:include page="Header.jsp"/>
<div class="error-container">
    <h1>Si è verificato un errore</h1>
    <p>Ci scusiamo per l'inconveniente. Si è verificato un errore imprevisto.</p>
    <p>Il nostro team sta lavorando per risolvere il problema.</p>
    <p>Per favore, riprova più tardi.</p>
</div>
<jsp:include page="Footer.jsp"/>
</body>
</html>