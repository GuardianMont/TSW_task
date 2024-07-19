<%@ page import="ec.model.user.UserBean" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%  //check nel caso in cui l'utente cercasse di accedere alla pagina del profilo da url
    UserBean user = (UserBean) request.getAttribute("user");
    String sessionUser = (String) request.getSession().getAttribute("userId");

    if (user == null || sessionUser == null || !sessionUser.equals(user.getUsername())) {
        response.sendRedirect("login_signup.jsp");
        return;
    }
%>
<html>
<head>
    <title>Profilo</title>
    <link rel="stylesheet" type="text/css" href="css/Profile.css">
    <link rel="stylesheet" type="text/css" href="css/Ordini.css">
    <link rel="stylesheet" type="text/css" href="css/notifica.css">
    <link rel="stylesheet" type="text/css" href="css/IndirizziPagamentiInfo.css">
    <link rel="stylesheet" type ="text/css" href="css/FormAddressStyle.css">
    <script src="js/notifica.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <script src="js/profileSwitchingForms.js"></script>
    <script src="js/profileLoadingFunctions.js"></script>
    <script src="js/validationEditProfile.js"></script>
    <script src="js/validationAddress.js"></script>
    <script src="js/validationPayMathods.js"></script>
    <script src="js/validationFunctions.js"></script>
    <script src="js/loadOrdini.js"></script>
</head>
<body>
<jsp:include page="Header.jsp"/>

<%
    if (user != null && sessionUser != null && sessionUser.equals(user.getUsername())){
%>

<div class="container main-container" id="main-container">
    <form class="logout-form" action="${pageContext.request.contextPath}/Logout">
        <input type="hidden" name="option" value="logout">
        <input type="submit" value="Logout">
    </form>
    <div class="container button-container">
        <button class="edit-button" id="profilo" onclick="showProfile()">
            <i class="fas fa-user"></i> Informazioni utente
        </button>
        <button class="edit-button" id="viewOrdini" onclick="loadOrders()">
            <i class="fas fa-box"></i> Visualizza Ordini
        </button>
        <button class="edit-button" id="viewPayment" onclick="loadPaymentMethods()">
            <i class="fas fa-credit-card"></i> Metodi di Pagamento
        </button>
        <button class="edit-button" id="viewAddresses" onclick="loadAddresses()">
            <i class="fas fa-map-marker-alt"></i> Indirizzi di spedizione
        </button>
    </div>
    <div class="container profile-container" id="profile-container">
        <h3>Profilo <%= user.getUsername() %></h3>
        <p>Nome: <%= user.getNome() %></p>
        <p>Cognome: <%= user.getCognome() %></p>
        <p>Email: <%= user.getEmail() %></p>
        <p>Telefono: <%= user.getPhoneNumber() %></p>
        <button class="edit-button" onclick="editProfile()">
            <i class="fas fa-edit"></i> Modifica informazioni
        </button>
        <button class="edit-button" id="cambiaPassword" onclick="changePassword()">
            <i class="fas fa-key"></i> Cambia Password
        </button>
    </div>

    <div class="container edit-container" id="edit-container" style="display: none;">
        <p id="edit-error" class="edit-error"></p>
        <h3>Modifica</h3>
        <form id="edit-profile-form" action="updateUser" method="post">
            <div class="edit-form-hidden">
                <input type="hidden" name="option" value="update">
                <input type="hidden" name="username" value="<%= user.getUsername() %>">
            </div>
            <div class="edit-form-group">
                <label for="edit-nome">Nome:</label><br>
                <input id="edit-nome" name="nome" type="text" maxlength="20" required value="<%= user.getNome() %>"><br>
                <span id="edit-nome-error" class="error-message">Non ci possono essere numeri o simboli.</span>
            </div>
            <div class="edit-form-group">
                <label for="edit-cognome">Cognome:</label><br>
                <input id="edit-cognome" name="cognome" type="text" maxlength="20" required value="<%= user.getCognome() %>"><br>
                <span id="edit-cognome-error" class="error-message">Non ci possono essere numeri o simboli.</span>
            </div>
            <div class="edit-form-group">
                <label for="edit-email">Email:</label><br>
                <input id="edit-email" name="email" type="email" required value="<%= user.getEmail() %>"><br>
                <span id="edit-email-error" class="error-message">Email non valida.</span>
            </div>
            <div class="edit-form-group">
                <label for="edit-phonenumber">Telefono:</label><br>
                <input id="edit-phonenumber" name="phoneNumber" type="tel" required value="<%= user.getPhoneNumber() %>"><br>
                <span id="edit-phonenumber-error" class="error-message">Numero di telefono non valido.</span>
            </div>
            <div class="edit-form-group">
                <input type="submit" id="edit-submit" value="Salva modifiche">
                <input type="reset" value="Reset">
            </div>
        </form>
    </div>

    <div class="container change-password-container" id="change-password-container" style="display: none;">
        <p id="change-password-error" class="change-password-error"></p>
        <h3>Cambia Password</h3>
        <form id="change-password-form" action="updateUser" method="post">
            <div class="change-password-form-hidden">
                <input type="hidden" name="option" value="changePassword">
                <input type="hidden" name="username" required value="<%= user.getUsername() %>">
            </div>
            <div class="change-password-form-group">
                <label for="change-password-new">Inserisci Password:</label><br>
                <input id="change-password-new" name="newPassword" type="text" maxlength="20" required placeholder="Nuova Password"><br>
                <span id="change-password-new-error" class="error-message">Sono richiesti almeno 8 caratteri, almeno una lettera maiuscola e un numero.</span>
            </div>
            <div class="change-password-form-group">
                <label for="change-password-confirm">Ripeti Password:</label><br>
                <input id="change-password-confirm" name="confirmPassword" type="text" maxlength="20" required placeholder="Conferma Password"><br>
                <span id="change-password-confirm-error" class="error-message">La password deve essere la stessa.</span>
            </div>
            <div class="change-password-form-submit">
                <input type="submit" value="Cambia Password">
                <input type="reset" value="Reset">
            </div>
        </form>
    </div>

    <div class="ordiniEffettuati" id="ordiniEffettuati" style="display: none;">
        <div class="generale">
            <div class="sorting-dropdown">
                <select onchange="orderOrdini()" class="select-sorting" id="sortingDropdown">
                    <option value="#">Ordina per</option>
                    <option value="data">Dal meno recente</option>
                    <option value="dataDESC">Dal più recente</option>
                </select>
            </div>
        </div>
        <div id="checkOutOrdini">
            <!--li immetto con ajax-->
        </div>
    </div>


    <div class="orderDetail" id="orderDetail" style="display: none;">
        <div class="order-details">
            <h1>Dettagli Ordine</h1>
            <div class="order-info">
                <p><strong>Ordine ID:</strong> <span id="ordineId"></span></p>
                <p><strong>Data Ordine:</strong> <span id="dataOrdine"></span></p>
                <p><strong>Codice Fattura:</strong> <span id="ordineFattura"></span></p>
            </div>
            <div class="address-info">
                <h2>Indirizzo</h2>
                <p id="address"></p>
            </div>
            <div class="payment-method-info">
                <h2>Metodo di Pagamento</h2>
                <p id="paymentMethod"></p>
            </div>
            <div class="cart-items">
                <h2>Articoli nel Carrello</h2>
                <table>
                    <thead>
                    <tr>
                        <th>Prodotto</th>
                        <th>Nome</th>
                        <th>IVA</th>
                        <th>Quantità</th>
                        <th>Prezzo Unitario</th>
                        <th>Sconto</th>
                        <th>Prezzo Totale</th>
                        <th>Immagine</th>
                    </tr>
                    </thead>
                    <tbody id="cartItems"></tbody>
                </table>
            </div>
            <div class="total-price">
                <h3>Prezzo Totale: <span id="prezzoTotale"></span></h3>
            </div>
        </div>
    </div>

    <!--si da la possibilità di aggiungere indirizzi di spedizione dalla pagina utente-->
    <div class="indirizziSpedizione" id="indirizziSpedizione" style="display: none;">
        <button id="add-address" class="add-button add-methods-button" onclick="toggleAddressForm()">
            <i class="fas fa-plus"></i>
        </button>
        <div id="shipping-address">
            <!--li immetto con ajax-->
        </div>
        <div id="remove-button" class="hidden">
            <button class="remove-button" onclick="toggleAddressForm()">X</button>
        </div>
        <div id="new-address-form" class="hidden form-container">
            <form id="addressForm" action="AddressManagement" method="POST">
                <input type="hidden" name="opzione" value="add">

                <div class="form-group">
                    <label for="via">Via:</label>
                    <input id="via" name="via" type="text" maxlength="20" placeholder="Via Muni..." pattern="^[a-zA-Z0-9\s]+$" required>
                    <span id="via-error" class="error-message">Il campo 'Via' è obbligatorio.</span>
                </div>

                <div class="form-group">
                    <label for="n_civico">Numero civico:</label>
                    <input id="n_civico" name="n_civico" type="text" maxlength="4" required>
                    <span id="n_civico-error" class="error-message">Inserisci un numero civico valido.</span>
                </div>

                <div class="form-group">
                    <label for="preferenze">Preferenze spedizione:</label>
                    <textarea class="textarea" id="preferenze" name="preferenze" maxlength="30" placeholder="Inserire preferenze di spedizione" pattern="^[a-zA-Z0-9\s]+$"></textarea>
                </div>

                <div class="form-group">
                    <label for="cap">CAP:</label>
                    <input id="cap" name="cap" type="text" maxlength="5" placeholder="00000" required>
                    <span id="cap-error" class="error-message">Inserisci un CAP valido (5 cifre).</span>
                </div>

                <div class="form-group">
                    <label for="citta">Città:</label>
                    <input id="citta" name="citta" type="text" placeholder="Città" pattern="^[a-zA-Z0-9\s]+$" required>
                    <span id="citta-error" class="error-message">Il campo 'Città' è obbligatorio.</span>
                </div>

                <div class="form-group">
                    <label for="provincia">Provincia:</label>
                    <input id="provincia" name="provincia" type="text" maxlength="2" required>
                    <span id="provincia-error" class="error-message">Inserisci una provincia valida (2 lettere).</span>
                </div>

                <div class="button-container">
                    <input type="submit" value="Aggiungi Indirizzo" class="submit-button">
                    <input type="reset" value="Reset" class="reset-button">
                </div>
            </form>
        </div>
    </div>

    <!--si da la possibilità di aggiungere metodi di pagamento dalla pagina utente-->
    <div class="metodiPagamento" id="metodiPagamento" style="display: none;">
        <button id="add-pay-button" class="add-button add-methods-button" onclick="togglePayMethodForm()">
            <i class="fas fa-plus"></i>
        </button>
        <div id="shipping-payment">
            <!--li immetto con ajax-->
        </div>
        <div id="remove-pay-form" class="button-container hidden">
            <button class="remove-button" onclick="togglePayMethodForm()">X</button>
        </div>
        <div id="new-payMethod-form" class="hidden form-container">
            <!--form per l'aggiunta di un nuovo metodo di pagamento celato se l'utente non clicca il bottone-->
            <form id="payMethodsForm" action="payMethodsManager" method="post">
                <input type="hidden" name="opzione" value="add">
                <div class="form-group">
                    <label for="NumeroCarta">Numero Carta:</label>
                    <input id="NumeroCarta" name="NumeroCarta" type="text" pattern="\d{16}" maxlength="16" required placeholder="Numero Carta">
                    <span id="NumeroCartaError" class="error-message">Il campo Numero Carta è obbligatorio.</span>
                </div>
                <div class="form-group">
                    <div class="data-scadenza">
                       <label for="meseScadenza">Data Scadenza:</label>
                        <input id="meseScadenza" name="meseScadenza" type="text" maxlength="2" placeholder="MM" pattern="(0[1-9]|1[0-2])" required>
                       <label for="annoScadenza">/</label>
                       <input id="annoScadenza" name="annoScadenza" type="text" maxlength="2" placeholder="YY" pattern="([2-9][4-9]|[3-9][0-9])" required>
                    </div>
                    <span id="dataScadenzaError" class="error-message">Inserisci una data di scadenza valida (MM/YY).</span>
                </div>

                <div class="form-group">
                    <label for="cvv">CVV:</label>
                    <input id="cvv" name="cvv" type="text" maxlength="3" placeholder="000" required>
                    <span id="CvvError" class="error-message">Inserisci un CVV valido (3 cifre).</span>
                </div>

                <div class="form-group">
                    <label for="circuito">Circuito:</label>
                    <select id="circuito" name="circuito" required>
                        <option value="" disabled selected>Seleziona un circuito</option>
                        <option value="visa">Visa</option>
                        <option value="mastercard">Mastercard</option>
                        <option value="amex">American Express</option>
                        <option value="discover">Discover</option>
                        <option value="diners">Diners Club</option>
                        <option value="bancomat">Bancomat</option>
                    </select>
                    <span id="circuitoError" class="error-message">Il campo 'Circuito' è obbligatorio.</span>
                </div>

                <div class="form-group">
                    <label for="titolareCarta">Titolare Carta:</label>
                    <input id="titolareCarta" name="titolareCarta" type="text" placeholder="Nome Cognome" pattern="^[a-zA-Z0-9\s]+$" required>
                    <span id="titolareError" class="error-message">Il campo Titolare Carta è obbligatorio.</span>
                </div>

                <div class="button-container">
                    <input type="submit" value="Aggiungi Metodo" class="add-methods-button">
                    <input type="reset" value="Reset">
                </div>
            </form>
        </div>
    </div>


</div>
<%
} else {
%>
<div class="container" id="not-logged-message">
    <h3>Devi essere loggato per visualizzare il profilo, coglione!</h3>
    <p>
        Ma come cazzo hai fatto ad arrivare in sta pagina poi...<br>
        Ti ci sei messo proprio d'impegno, eh?
    </p>
</div>
<%
    }
%>

<jsp:include page="Footer.jsp"/>
</body>
</html>
