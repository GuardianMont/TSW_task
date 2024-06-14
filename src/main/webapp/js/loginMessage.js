function showNotification(message) {
    var notification = document.getElementById("notification");
    var notificationText = document.getElementById('notification-text');
    notificationText.textContent = message;
    notification.style.display = "flex";
    setTimeout(function() {
        notification.style.display = "none";
    }, 5000);
}

