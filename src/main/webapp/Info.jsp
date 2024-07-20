<%--
  Created by IntelliJ IDEA.
  User: mauri
  Date: 05/07/2024
  Time: 11:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Info - Tavolando</title>
    <link rel="stylesheet" href="css/Info.css">
</head>
<body>

<jsp:include page="Header.jsp" />

<div class="container">
    <div class="info-box">
        <h1>Benvenuti su Tavolando</h1>
        <p>
            La nostra azienda è specializzata nella vendita di tavoli da gioco e attrezzatura per sport da tavolo.
            Offriamo una vasta gamma di prodotti di alta qualità, tra cui tavoli da biliardo, air hockey, ping pong e
            molto altro ancora. La nostra missione è fornire ai nostri clienti i migliori prodotti per il divertimento
            e l'intrattenimento a casa o in qualsiasi ambiente.
        </p>
        <p>
            Siamo appassionati di giochi e ci dedichiamo a offrire un servizio clienti eccellente e prodotti che
            soddisfino le esigenze dei giocatori di ogni livello. Che siate principianti o esperti, abbiamo l'attrezzatura
            giusta per voi.
        </p>
        <p>
            Contattateci per maggiori informazioni o visitate il nostro showroom per vedere di persona la qualità dei
            nostri prodotti. Vi aspettiamo!
        </p>
    </div>

    <div class="founders-section">
        <div class="founders-title">I nostri founder:</div>
        <div class="founders">
            <div class="founder">
                <h2>Mario Zurolo</h2>
                <p>
                    Co-founder e CEO
                </p>
            </div>
            <div class="founder">
                <h2>Geraldine Montella</h2>
                <p>
                    Co-founder e CTO
                </p>
            </div>
            <div class="founder">
                <h2>Maurilio La Rocca</h2>
                <p>
                    Co-founder e CMO
                </p>
            </div>
        </div>
    </div>
</div>

<jsp:include page="Footer.jsp" />

</body>
</html>
