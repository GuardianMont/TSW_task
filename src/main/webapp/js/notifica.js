const icons = {
    attention: 'erroreAttentionIcon.png',
    info: 'IconInfo.png',
    favorites: 'stellinaNotifica.png'
};

function showNotification(type, text) {
    const notification = document.createElement('div');
    console.log("Showing notification");
    notification.classList.add('notification', type);

    const img = document.createElement('img');
    img.src = `/TSW_task_war_exploded/uploadFile/${icons[type]}`;
    img.alt = `${type.charAt(0).toUpperCase() + type.slice(1)} Icon`;
    img.width = 20;
    img.height = 20;

    const span = document.createElement('span');
    span.textContent = text;

    notification.appendChild(img);
    notification.appendChild(span);

    document.body.appendChild(notification);

    setTimeout(() => {
        notification.remove();
    }, 5000); // 5 seconds
}

// Function to show attention notification
function showAttentionNotifica(text) {
    showNotification('attention', text);
}

// Function to show info notification
function showInfoNotifica(text) {
    showNotification('info', text);
}

// Function to show favorites notification
function showFavoritesNotifica() {
    showNotification('favorites', 'Nuovo Prodotto aggiunto ai Preferiti!');
}
