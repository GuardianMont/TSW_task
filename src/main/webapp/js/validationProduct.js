function getMagicNumber(file, callback) {
    const reader = new FileReader();
    reader.onloadend = function(e) {
        if (e.target.readyState === FileReader.DONE) {
            const arr = (new Uint8Array(e.target.result)).subarray(0, 4);
            let header = "";
            for(let i = 0; i < arr.length; i++) {
                header += arr[i].toString(16);
            }
            callback(header);
        }
    };
    reader.readAsArrayBuffer(file.slice(0, 4));
}

function validateForm() {
    const nome = document.getElementById('nome').value;
    const descrizione = document.getElementById('descrizione').value;
    const prezzo = document.getElementById('prezzo').value;
    const quantita = document.getElementById('quantita').value;
    const iva = document.getElementById('iva').value;
    const dimensioni = document.getElementById('dimensioni').value;
    const categoria = document.getElementById('categoria').value;
    const colore = document.getElementById('colore').value;
    const img = document.getElementById('img').files[0];
    clearErrors();

    if(nome.trim()===""){
        showError(nome, document.getElementById("nome-error"))
    }
    if (nome.length < 3) {
        alert('Il nome deve contenere almeno 3 caratteri.');
        return false;
    }

    if (descrizione.length < 5) {
        alert('La descrizione deve contenere almeno 5 caratteri.');
        return false;
    }

    if (prezzo <= 0) {
        alert('Il prezzo deve essere un numero positivo.');
        return false;
    }

    if (quantita <= 0) {
        alert('La quantità deve essere almeno 1.');
        return false;
    }

    if (iva <= 0) {
        alert('La fascia IVA deve essere almeno 1.');
        return false;
    }

    if (dimensioni.length < 2) {
        alert('Le dimensioni devono contenere almeno 2 caratteri.');
        return false;
    }

    if (categoria.length < 2) {
        alert('La categoria deve contenere almeno 2 caratteri.');
        return false;
    }

    if (colore.length < 2) {
        alert('Il colore deve contenere almeno 2 caratteri.');
        return false;
    }

    if (img) {
        const allowedTypes = ['image/jpeg', 'image/png'];

        return new Promise((resolve, reject) => {
            getMagicNumber(img, (header) => {
                let isValid = false;

                if (header.startsWith('ffd8')) {
                    // JPEG magic number
                    isValid = true;
                } else if (header.startsWith('89504e47')) {
                    // PNG magic number
                    isValid = true;
                }

                if (!isValid) {
                    alert('Il file immagine deve essere in formato JPEG o PNG.');
                    resolve(false);
                } else {
                    resolve(true);
                }
            });
        });
    }

    return true;
}

async function handleSubmit(event) {
    event.preventDefault();
    const isValid = await validateForm();
    if (isValid) {
        event.target.submit();
    }
}

function showError(element, errorElementId) {
    element.classList.add("error");
    document.getElementById(errorElementId).style.display = "block";
}

function clearErrors() {
    var errors = document.querySelectorAll(".error");
    errors.forEach(function(element) {
        element.classList.remove("error");
    });

    var errorMessages = document.querySelectorAll(".error-message");
    errorMessages.forEach(function(element) {
        element.style.display = "none";
    });
}