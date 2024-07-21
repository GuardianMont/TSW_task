let currentUser = null;

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

        const functionName = user.isAdmin ? "removeAdmin" : "setAdmin";
        const buttonLabel = user.isAdmin ? "Remove Admin" : "Set Admin";

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

function setAdmin(userId) {
    const xhr = new XMLHttpRequest();
    xhr.open('POST', '/TSW_task_war_exploded/SetAdminOption');
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.onload = function () {
        if (xhr.status === 200) {
            const response = JSON.parse(xhr.responseText);
            if (response.success) {
                showInfoNotifica("Nuovo utente settato admin");
                const userRow = document.getElementById('user-' + userId);
                if (userRow) {
                    const roleCell = userRow.querySelector('.ruolo');
                    roleCell.querySelector('span').textContent = 'admin';
                    const button = roleCell.querySelector('.admin-toggle');
                    button.textContent = 'Remove Admin';
                    button.setAttribute('onclick', `removeAdmin('${userId}')`);
                }
            } else {
                console.error('Error setting admin:', response.error);
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

function removeAdmin(userId) {
    const xhr = new XMLHttpRequest();
    xhr.open('POST', '/TSW_task_war_exploded/SetAdminOption');
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.onload = function () {
        if (xhr.status === 200) {
            const response = JSON.parse(xhr.responseText);
            if (response.success) {
                showAttentionNotifica("Rimossi permessi admin");
                const userRow = document.getElementById('user-' + userId);
                if (userRow) {
                    const roleCell = userRow.querySelector('.ruolo');
                    roleCell.querySelector('span').textContent = 'user';
                    const button = roleCell.querySelector('.admin-toggle');
                    button.textContent = 'Set Admin';
                    button.setAttribute('onclick', `setAdmin('${userId}')`);
                }
            } else {
                console.error('Error removing admin:', response.error);
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
    currentUser = userId;  // setto la ricerca per l'utente specifico
    RenderUserOrder();
    const xhr = new XMLHttpRequest();
    xhr.open('POST', '/TSW_task_war_exploded/AdminViewOrders');
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.onload = function () {
        if (xhr.status === 200) {
            const response = JSON.parse(xhr.responseText);
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
    currentUser = null; // setto la ricerca a generale
    RenderUserOrder();
    const xhr = new XMLHttpRequest();
    xhr.open('POST', '/TSW_task_war_exploded/AdminViewOrders');
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.onload = function () {
        if (xhr.status === 200) {
            const response = JSON.parse(xhr.responseText);
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

    if (ordini.length === 0) {
        const noOrdersMessage = document.createElement('p');
        noOrdersMessage.textContent = 'Nessun ordine effettuato';
        container.appendChild(noOrdersMessage);
        return;
    }

    ordini.forEach(ordine => {
        const div = document.createElement('div');
        div.classList.add('ordine-container');

        const orderInfo = document.createElement('div');
        orderInfo.classList.add('ordine-info');
        orderInfo.innerHTML = `<div>Ordine effettuato il: ${ordine.dataOrdine}, Codice Fattura: ${ordine.ordineFattura}</div>`;

        const buttonFattura = document.createElement("button");
        buttonFattura.classList.add('btn', 'btn-down'); // Aggiungo la classe in modo da poterlo stilizzare
        buttonFattura.innerHTML = '<span>Scarica Fattura</span>';
        buttonFattura.addEventListener("click", function () {
            downloadInvoice(ordine.ordineId);
        });

        const address = ordine.address ? ordine.address.via : 'No info';
        let paymentMethod = ordine.paymentMethod ? censorCardNumber(ordine.paymentMethod.numCarta) : 'No info';
        const prezzoTotale = ordine.prezzototale.toFixed(2);

        const orderDetails = document.createElement('div');
        orderDetails.innerHTML = `
            <div>Indirizzo: ${address}</div>
            <div>Metodo di Pagamento: ${paymentMethod}</div>
            <div>Prezzo Totale: ${prezzoTotale}€</div>
            <div>Prodotti:</div>
        `;

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

    const data = currentUser ?
        'opzione=filterByDate&startDate=' + encodeURIComponent(startDate) + '&endDate=' + encodeURIComponent(endDate) + '&userAdmin=' + encodeURIComponent(currentUser) :
        'opzione=filterByDate&startDate=' + encodeURIComponent(startDate) + '&endDate=' + encodeURIComponent(endDate);

    xhr.send(data);
}

function censorCardNumber(cardNumber) {
    return '****-' + cardNumber.slice(-4);
    // prendo solo gli ultimi 4 numeri
}

function downloadInvoice(orderId) {
    const link = document.createElement('a');
    link.href = '/TSW_task_war_exploded/CheckoutServlet?orderId=' + encodeURIComponent(orderId) + '&opzione=pdf';
    link.download = 'fattura_' + encodeURIComponent(orderId) + '.pdf';
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
}

function orderOrdini(orderBy) {
    const xhr = new XMLHttpRequest();
    xhr.open('POST', '/TSW_task_war_exploded/AdminViewOrders');
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.onload = function () {
        if (xhr.status === 200) {
            const response = JSON.parse(xhr.responseText);
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
    const data = currentUser ?
        'opzione=orderBy&orderBy=' + encodeURIComponent(orderBy) + '&userAdmin=' + encodeURIComponent(currentUser) :
        'opzione=orderBy&orderBy=' + encodeURIComponent(orderBy);

    xhr.send(data);
}
