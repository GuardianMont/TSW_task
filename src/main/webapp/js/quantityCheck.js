function checkValidityQuantity(input) {
    input.value = input.value.replace(/\D/g, ''); //rimuove tutti i caratteri non numerici

    var value = parseInt(input.value, 10);
    //converte il valore di un numero in un intero

    //verifica se il valore è minore del minimo consentito
    if (value < parseInt(input.min)) {
        input.value = input.min; // Imposta il valore al minimo ovvero 1
    }
    //vicecerso verifica se il valore è maggiore di un massimo consentito
    if (value > parseInt(input.max)) {
        input.value = input.max; // Imposta il valore al massimo (in base al prodotto)
    }
}

function incrementQuantity(button) {
    var form = button.closest('.quantity-form');
    if (!form) return;

    var input = form.querySelector('input[name="quantity"]');
    if (!input) return;

    var max = parseInt(input.max, 10);
    var currentValue = parseInt(input.value, 10);
    if (currentValue < max) {
        input.value = currentValue + 1;
        updateQuantity(form);
        updateTotalPrice();
    }
}

function decrementQuantity(button) {
    var form = button.closest('.quantity-form');
    if (!form) return;

    var input = form.querySelector('input[name="quantity"]');
    if (!input) return;

    var min = parseInt(input.min, 10);
    var currentValue = parseInt(input.value, 10);
    if (currentValue > min) {
        input.value = currentValue - 1;
        updateQuantity(form);
        updateTotalPrice();
    }
}

function updateQuantity(form) {
    clearErrors();
    var quantityInput = form.querySelector('input[name="quantity"]');
    var errorElement = form.querySelector('.quantity-error');

    if (quantityInput && quantityInput.value) {
        var quantity = parseInt(quantityInput.value, 10);
        if (isNaN(quantity) || quantity <= 0) {
            showAttentionNotifica("La quantità è necessaria e deve essere maggiore di zero.") ;
            errorElement.style.display = "block";
        } else {
            form.submit();
        }
    } else {
        showAttentionNotifica("La quantità è necessaria e deve essere maggiore di zero.") ;
        errorElement.style.display = "block";
    }
}

function updateTotalPrice() {
    var totalPrice = 0;
    var quantityForms = document.querySelectorAll('.quantity-form');

    quantityForms.forEach(function(form) {
        var input = form.querySelector('input[name="quantity"]');
        var itemPriceText =form.querySelector('input[name="prezzo"]').value;
        //  '.prezzo' è la classe dell'elemento che cotiene il prezzo
        var itemPrice = parseFloat(itemPriceText);
        var quantity = parseInt(input.value, 10);

        if (!isNaN(itemPrice) && !isNaN(quantity)) {
            totalPrice += itemPrice * quantity;
        }
    });
    var totalPriceElement = document.getElementById('spesa_totale');
    if (totalPriceElement) {
        totalPriceElement.textContent = "Spesa totale: " + totalPrice.toFixed(2) + "€";
    } else {
        console.error("Element with class 'spesa_totale' not found in the document.");
    }
}
document.addEventListener('DOMContentLoaded', function() {
    var quantityForms = document.querySelectorAll('.quantity-form');
    quantityForms.forEach(function(form) {


        var quantityInput = form.querySelector('input[name="quantity"]');
        if (quantityInput) {
            quantityInput.addEventListener('blur', function() {
                checkValidityQuantity(this);
                updateQuantity(form);
                updateTotalPrice();
            });

            quantityInput.addEventListener('change', function() {
                checkValidityQuantity(this);
                updateQuantity(form);
                updateTotalPrice();
            });
        }
    });

    updateTotalPrice(); // Calcola e aggiorna la spesa totale iniziale
});