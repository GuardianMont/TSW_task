function RemoveHiddenUsers() {
    document.getElementById('userView').classList.remove('hide'); // Mostra la sezione degli utenti
    document.getElementById('ordiniEffettuati').classList.add('hide'); // Nasconde la sezione degli ordini
}

function RenderUserOrder() {
    document.getElementById('userView').classList.add('hide'); // Nasconde la sezione degli utenti
    document.getElementById('ordiniEffettuati').classList.remove('hide'); // Mostra la sezione degli ordini
}


function viewUsers() {
    RemoveHiddenUsers();
    const xhr = new XMLHttpRequest();
    xhr.open("POST", "/TSW_task_war_exploded/UserAdminOption");
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.onload = function () {
        if (xhr.status === 200) {
            const response = JSON.parse(xhr.responseText);
            console.log(response);
            if (response.success) {
                displayUsers(response.data);
            } else {
                console.error('Error loading utenti:', response.error);
            }
        } else {
            console.error('Request failed. Status:', xhr.status);
        }
    };
    xhr.onerror = function () {
        console.error('Request failed. Network error.');
    };
    const data = "opzione=viewUsers";
    xhr.send(data);
}

function displayUsers(users) {
    const UserTable = document.getElementById('UtentiInfo');
    UserTable.innerHTML = ''; // Svuota la tabella prima di aggiungere nuove righe

    const tableHead = document.querySelector('.userView thead');
    tableHead.classList.remove('hide-elements'); // Mostra l'intestazione della tabella

    users.forEach(user => {
        const row = document.createElement('tr');
        const role = user.isAdmin ? 'admin' : 'utente';

        // Determina la funzione da chiamare in base al ruolo dell'utente
        const functionName = user.isAdmin ? "removeAdmin" : "setAdmin";
        // setta di conseguenza la label corretta per il button
        const buttonLabel = user.isAdmin ? "Remove Admin" : "Set Admin";

        //id per identificare la riga
        row.id = 'user-' + user.username; // Aggiunge un ID alla riga dell'utente
        row.innerHTML = `
            <td>${user.username}</td>
            <td>${user.email}</td>
            <td>${user.nome}</td>
            <td>${user.cognome}</td>
            <td>${user.phoneNumber}</td>
            <td class="ruolo">
                <span>${role}</span>
                <button class="admin-toggle" onclick="${functionName}('${user.username}')">${buttonLabel}</button>
            </td>
            <td>
                <button onclick="loadOrders('${user.username}')">View Orders</button>
            </td>
        `;

        UserTable.appendChild(row);
    });
}
function  setAdmin (userId){
    const xhr = new XMLHttpRequest();
    xhr.open('POST', '/TSW_task_war_exploded/SetAdminOption');
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.onload = function () {
        if (xhr.status === 200) {
            const response = JSON.parse(xhr.responseText);
            console.log(response);
            if (response.success) {
                showInfoNotifica("nuovo utente settato admin");
                //aggiorno anche visivamente il cambiamento del ruolo
                const userRow = document.getElementById('user-' + userId);
                if (userRow) {
                    const roleCell = userRow.querySelector('.ruolo');
                    roleCell.querySelector('span').textContent = 'admin';
                    const button = roleCell.querySelector('.admin-toggle');
                    button.textContent = 'Remove Admin';
                    button.setAttribute('onclick', `removeAdmin('${userId}')`);
                }
            } else {
                console.error('Error loading ordini:', response.error);
            }
        } else {
            console.error('Request failed. Status:', xhr.status);
        }
    };
    xhr.onerror = function () {
        console.error('Request failed. Network error.');
    };
    const data = 'opzione=roleAdmin&userAdmin=' + encodeURIComponent(userId) + '&role=true';
    xhr.send(data);
}

function  removeAdmin (userId){
    const xhr = new XMLHttpRequest();
    xhr.open('POST', '/TSW_task_war_exploded/SetAdminOption');
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.onload = function () {
        if (xhr.status === 200) {
            const response = JSON.parse(xhr.responseText);
            console.log(response);
            if (response.success) {
                showAttentionNotifica("rimossi permessi admin");
                //aggiorno anche visivamente il cambiamento del ruolo
                const userRow = document.getElementById('user-' + userId);
                if (userRow) {
                    const roleCell = userRow.querySelector('.ruolo');
                    roleCell.querySelector('span').textContent = 'user';
                    const button = roleCell.querySelector('.admin-toggle');
                    button.textContent = 'set Admin';
                    button.setAttribute('onclick', `setAdmin('${userId}')`);

                }
            } else {
                console.error('Error loading ordini:', response.error);
            }
        } else {
            console.error('Request failed. Status:', xhr.status);
        }
    };
    xhr.onerror = function () {
        console.error('Request failed. Network error.');
    };
    const data = 'opzione=roleAdmin&userAdmin=' + encodeURIComponent(userId) + '&role=false';
    xhr.send(data);
}
function loadOrders(userId) {
    RenderUserOrder();
    const xhr = new XMLHttpRequest();
    xhr.open('POST', '/TSW_task_war_exploded/AdminViewOrders');
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.onload = function () {
        if (xhr.status === 200) {
            const response = JSON.parse(xhr.responseText);
            console.log(response);
            if (response.success) {
                renderOrders(response.data);
            } else {
                console.error('Error loading ordini:', response.error);
            }
        } else {
            console.error('Request failed. Status:', xhr.status);
        }
    };
    xhr.onerror = function () {
        console.error('Request failed. Network error.');
    };
    const data = 'opzione=show&userAdmin=' + encodeURIComponent(userId);
    xhr.send(data);
}

function viewOrders() {
    RenderUserOrder();
    const xhr = new XMLHttpRequest();
    xhr.open('POST', '/TSW_task_war_exploded/AdminViewOrders');
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.onload = function () {
        if (xhr.status === 200) {
            const response = JSON.parse(xhr.responseText);
            console.log(response);
            if (response.success) {
                renderOrders(response.data);
            } else {
                console.error('Error loading ordini:', response.error);
            }
        } else {
            console.error('Request failed. Status:', xhr.status);
        }
    };
    xhr.onerror = function () {
        console.error('Request failed. Network error.');
    };
    const data = 'opzione=viewAll';
    xhr.send(data);
}

function renderOrders(ordini) {
    const container = document.getElementById("checkOutOrdini");
    container.innerHTML = '';
    ordini.forEach(ordine => {
        console.log(ordine);
        const div = document.createElement('div');
        div.classList.add('ordine-container');

        const orderInfo = document.createElement('div');
        orderInfo.classList.add('ordine-info');
        orderInfo.innerHTML = `<div>Ordine effettuato il: ${ordine.dataOrdine}, Codice Fattura: ${ordine.ordineFattura}</div>`;

        const buttonFattura = document.createElement("button");
        const buttonDettagli = document.createElement("button");
        buttonFattura.textContent = 'Scarica Fattura';
        buttonFattura.addEventListener("click", function() {
            downloadInvoice(ordine.ordineId);
        });

        const address = ordine.address ? ordine.address.via : 'No info';
        const paymentMethod = ordine.paymentMethod ? ordine.paymentMethod.numCarta : 'No info';
        const prezzoTotale = ordine.prezzototale.toFixed(2);

        const orderDetails = document.createElement('div');
        orderDetails.innerHTML = `<div>Indirizzo: ${address}</div>
            <div>Metodo di Pagamento: ${paymentMethod}</div>
            <div>Prezzo Totale: ${prezzoTotale}€</div>
            <div>Prodotti:</div>`;

        const prodottiList = document.createElement('ul');
        if (ordine.cartItems) {
            ordine.cartItems.forEach(item => {
                const prezzo = item.prezzoUnitario.toFixed(2);
                const prodotto = document.createElement("li");
                prodotto.innerHTML = `Nome: ${item.nome}, Quantità: ${item.quantity}, Prezzo: ${prezzo}€`;
                prodottiList.appendChild(prodotto);
            });
        }
        orderDetails.appendChild(prodottiList);

        div.appendChild(orderInfo);
        div.appendChild(buttonFattura);
        div.appendChild(orderDetails);
        container.appendChild(div);
    });
}

function filterOrdersByDate() {
    const startDate = document.getElementById('startDate').value;
    const endDate = document.getElementById('endDate').value;

    const xhr = new XMLHttpRequest();
    xhr.open('POST', '/TSW_task_war_exploded/AdminViewOrders');
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.onload = function () {
        if (xhr.status === 200) {
            const response = JSON.parse(xhr.responseText);
            console.log(response);
            if (response.success) {
                renderOrders(response.data);
            } else {
                console.error('Error filtering orders:', response.error);
            }
        } else {
            console.error('Request failed. Status:', xhr.status);
        }
    };
    xhr.onerror = function () {
        console.error('Request failed. Network error.');
    };
    const data = 'opzione=filterByDate&startDate=' + startDate + '&endDate=' + endDate;
    xhr.send(data);
}

function downloadInvoice(orderId) {
    const link = document.createElement('a');
    link.href = '/TSW_task_war_exploded/CheckoutServlet?orderId=' + orderId + '&opzione=pdf';
    link.download = 'fattura_' + orderId + '.pdf';
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
}

function orderOrdini(){
    var dropdown = document.getElementById('sortingDropdown');
    var selectedValue = dropdown.value;
    console.log('Opzione selezionata:', selectedValue);
    var xhr = new XMLHttpRequest();
    xhr.open('POST', '/TSW_task_war_exploded/CheckoutServlet');
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.onload = function() {
        if (xhr.status === 200) {
            var response = JSON.parse(xhr.responseText);
            console.log(response);  // serve per visualizzare in conosole la stringa JSON che mi invia la servlet
            //in modo anche da vedere se le proprietà e le varie info sono state settate correttamente

            if (response.success) {
                renderOrders(response.data);
            } else {
                console.error('Error loading ordini:', response.error);
            }
        } else {
            console.error('Request failed. Status:', xhr.status);
        }
    };
    xhr.onerror = function() {
        console.error('Request failed. Network error.');
    };
    var data = 'opzione=order&order=' + encodeURIComponent(selectedValue);
    xhr.send(data);
}
