<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="ec.model.address.AddressUs" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Collection<?> addresses = (Collection<?>) request.getAttribute("addresses");

    if (request.getSession().getAttribute("userID")!=null){
        RequestDispatcher dispatcher = request.getRequestDispatcher("/login_signup.jsp");
        dispatcher.forward(request, response);
    }

%>
<html>
<head>
    <title>Indirizzo di Spedizione</title>
    <!-- questo va messo nel css relativo alla spedizione-->
    <style>
        .hidden {
            display: none;
        }
        .error {
            border-color: red;
        }
        .error-message {
            color: red;
            display: none;
        }
    </style>
    <script src="js/validationAddress.js"></script>
</head>
<body>
<a href="carrello">cart</a>
<div class="section">
    <h2>Indirizzo di Spedizione</h2>
    <div id="shipping-addresses">
        <%
            if (addresses != null && addresses.size() != 0) {
                Iterator<?> it = addresses.iterator();
                while (it.hasNext()) {
                    AddressUs ad = (AddressUs) it.next();
        %>
        <div>
            <input type="checkbox" name="selectedAddress" value="<%= ad.getNum_ID() %>">
            <%= ad.toString()%>
        </div>
        <%      } //chiudo while
             } //chiuso if
        %>
    </div>
    <button class="add-button" onclick="aggiungiIndirizzo()">Aggiungi Nuovo Indirizzo</button>
    <div id="new-address-form" class="hidden">
        <!--form per l'aggiunta di un nuovo indirizzo celato se l'utente non clicca il bottone-->
        <form action="AddressManagement" method="post" onsubmit="return validateForm()">
            <input type="hidden" name="opzione" value="add">

            <label for="via">Via:</label><br>
            <input id="via" name="via" type="text" maxlength="20" required placeholder="Via Muni..."><br>
            <span id="via-error" class="error-message">Il campo 'Via' è obbligatorio.</span><br>

            <label for="n_civico">Numero civico </label><br>
            <input id="n_civico" name="n_civico" type="text" maxlength="4" required ><br>
            <span id="n_civico-error" class="error-message">Inserisci un numero civico valido.</span><br>

            <label for="preferenze">Descrizione:</label><br>
            <textarea class="textarea" id="preferenze" name="preferenze" maxlength="30"  placeholder="inserire preferenze di spedizione"></textarea><br>

            <label for="cap">Cap:</label><br>
            <input id="cap" name="cap" type="text" maxlength="5" placeholder="00000" required><br>
            <span id="cap-error" class="error-message">Inserisci un CAP valido (5 cifre).</span><br>

            <label for="citta">Città</label><br>
            <input id="citta" name="citta" type="text" placeholder="città" required> <br>
            <span id="citta-error" class="error-message">Il campo 'Città' è obbligatorio.</span><br>

            <label for="provincia">Provincia</label><br>
            <input id="provincia" name="provincia" type="text" maxlength="2" required><br>
            <span id="provincia-error" class="error-message">Inserisci una provincia valida (2 lettere).</span><br>

            <input type="submit" value="Add Adress"><input type="reset" value="Reset">
        </form>
    </div>

</div>



<script>
    function aggiungiIndirizzo() {
        //toglie dall'oggetto della classe "new-adress-form" il tag "hidden" in modo che sia visibile
        //fatto ciò una volta compilato il form è la servlet che gestisce l'inserimento nel db e il reinderizzamento
        //di nuovo a questa pagina
        document.getElementById("new-address-form").classList.remove("hidden");
    }
</script>

</body>
</html>
