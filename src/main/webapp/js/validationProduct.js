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
        const prezzo = parseFloat(document.getElementById('prezzo').value);
        const quantita = parseInt(document.getElementById('quantita').value);
        const iva = parseInt(document.getElementById('iva').value);
        const dimensioni = document.getElementById('dimensioni').value;
        const sconto = parseInt(document.getElementById('sconto').value) || 0;
        const categoria = document.getElementById('categoria').value;
        const colore = document.getElementById('colore').value;
        const img = document.getElementById('img').files[0];
        clearErrors();
        var patterrNome = /^[a-zA-Z0-9\s]+$/
        let isValid = true;

        if (!nome){
            showError("nome", "nome-error", "Il campo nome è obbligatori");
            isValid=false;
        } else if (nome.length<3){
            showError("nome", "nome-error", "Il campo nome deve avere più di 3 caratteri");
            isValid=false;
        }else if(!patterrNome.test(nome)){
            showError("nome", "nome-error", "Il campo nome può contenere solo alfanumerici");
            isValid=false;
        }

        if (!descrizione || descrizione.length < 5) {
            showError('descrizione', 'descrizione-error', "Il campo deve avere almeno 5 caratteri");
            isValid = false;
        }

        if (!prezzo || prezzo <= 0) {
            showError("prezzo", "prezzo-error",'Il prezzo deve essere un numero positivo.');
            return false;
        }

        if (!quantita || quantita <= 0) {
            showError("quantita", "quantita-error",'La quantità deve essere almeno 1.');
            return false;
        }

        if (!iva || iva <= 0) {
            showError("iva", "iva-error",'La fascia IVA deve essere almeno 1.');
            return false;
        }

        if (!dimensioni|| dimensioni.length < 2) {
            showError("dimensioni", "dimensioni-error",'Le dimensioni devono contenere almeno 2 caratteri.');
            return false;
        }

        if(sconto<0 || sconto>99){
            showError("sconto", "sconto-error",'lo sconto deve essere compreso tra 0 e 99.');

        }
        if (!colore|| colore.length < 2) {
            showError("colore", "colore-error",'Il colore deve contenere almeno 2 caratteri.');
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

function showError(elementId, errorElementId, errorText) {
    const element = document.getElementById(elementId);
    if (element) {
        element.classList.add("error");
        const errorElement = document.getElementById(errorElementId);
        if (errorElement) {
            errorElement.textContent = errorText;
            errorElement.style.display = "block";
        } else {
            console.error(`Element with id ${errorElementId} not found.`);
        }
    } else {
        console.error(`Element with id ${elementId} not found.`);
    }
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

