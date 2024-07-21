function toggleFavorite(productId) {
    const xhr = new XMLHttpRequest();
    const icon = document.getElementById('favorite-icon-' + productId);
    const isAdded = icon.classList.contains('added');

    if (isAdded) {
        xhr.open("DELETE", "/TSW_task_war_exploded/favorites?productId=" + productId);
    } else {
        xhr.open("POST", "/TSW_task_war_exploded/favorites");
        xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    }

    xhr.onload = function () {
        if (xhr.status === 200) {
            const response = JSON.parse(xhr.responseText);
            if (response.success) {
                if (isAdded) {
                    icon.classList.remove('added');
                    icon.classList.add('not-added');
                    locatio.reload();
                } else {
                    showFavoritesNotifica();
                    icon.classList.remove('not-added');
                    icon.classList.add('added');
                }
            } else {
                console.error('Error updating favorites:', response.error);
            }
        } else {
            console.error('Request failed. Status:', xhr.status);
        }
    };

    xhr.onerror = function () {
        console.error('Request failed. Network error.');
    };

    if (!isAdded) {
        const data = "productId=" + productId;
        xhr.send(data);
    } else {
        xhr.send();
    }
}

function loadFavorites() {
    const xhr = new XMLHttpRequest();
    xhr.open("GET", "/TSW_task_war_exploded/favorites");
    xhr.onload = function () {
        if (xhr.status === 200) {
            const response = JSON.parse(xhr.responseText);
            if (response.success) {
                response.data.forEach(productId => {
                    var icon = document.getElementById('favorite-icon-' + productId);
                    if (icon) {
                        icon.classList.add('added');
                        icon.classList.remove('not-added');
                    }
                });
            } else {
                console.error('Error loading favorites:', response.error);
            }
        } else {
            console.error('Request failed. Status:', xhr.status);
        }
    };
    xhr.onerror = function () {
        console.error('Request failed. Network error.');
    };
    xhr.send();
}