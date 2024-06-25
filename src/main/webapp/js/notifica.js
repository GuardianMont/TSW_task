function showNotification(type, text) {
    const notification = document.createElement('div');
    notification.classList.add('notification', type);
    notification.innerHTML = `
        <img src="uploadFile/${type === 'attention' ? 'erroreAttentionIcon.png' : 'IconInfo.png'}" alt="Info Icon" width="20" height="20">
        <span>${text}</span>
    `;
    document.body.appendChild(notification);
    setTimeout(() => {
        notification.remove();
    }, 4000); // 5 seconds
}

// Function to show attention notification
function showAttentionNotifica(text) {
    showNotification('attention', text);
}

// Function to show info notification
function showInfoNotifica(text) {
    showNotification('info', text);
}
