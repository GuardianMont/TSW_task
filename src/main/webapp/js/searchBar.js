document.addEventListener('DOMContentLoaded', function() {
    const searchInput = document.getElementById('cerca');
    const formSearch= document.getElementById("searchForm");

    searchInput.addEventListener('input', function() {
        const query = searchInput.value;

        if (query.length > 0) {
            searchProduct(query);
        } else {
            clearResults();
        }
    });

    searchInput.addEventListener("blur", function(){
        clearResults();
    })

    formSearch.addEventListener("submit", function (){
        event.preventDefault();
        const query = searchInput.value.trim();
        if (query.length>0){
            window.location.href = `searchForm?query=${encodeURIComponent(query)}`;
        }else{
            console.log("La barra di ricerca è vuota")
        }
    })
    function searchProduct(query) {
        var xhr = new XMLHttpRequest();
        var url = "search?query=" + encodeURIComponent(query);
        xhr.open("GET", url, true);
        xhr.responseType = 'json'; // Imposta il tipo di risposta come JSON

        xhr.onload = function() {
            if (xhr.status === 200) {
                var response = xhr.response;
                console.log(response);

                if (response.success) {
                    displayResults(response.data);
                } else {
                    console.error("Error loading search results:", response.error);
                    clearResults();
                }
            } else {
                console.error("Request failed. Status:", xhr.status);
            clearResults();
            }
        };

        xhr.onerror = function() {
            console.error("Request failed. Network error");
        clearResults();
        };

        xhr.send();
    }

    function displayResults(data) {
        clearResults(); // Clear previous results

        const maxResults = 4;
        const resultsToShow = data.slice(0, maxResults); // Limit to 4 results

        if (resultsToShow.length > 0) {
            const resultsContainer = document.createElement('div');
            resultsContainer.classList.add('container-search');
            resultsContainer.id = 'dynamic-results';

            const ulElement = document.createElement('ul');
            resultsToShow.forEach(item => {
                const liElement = document.createElement('li');
                liElement.textContent = item.nome;
                liElement.addEventListener('click', function() {
                    window.location.href = `product?opzione=read&id=${item.id}`;
                });
                ulElement.appendChild(liElement);
            });
            resultsContainer.appendChild(ulElement);

            document.body.appendChild(resultsContainer);

            // Position the results container below the search input
            const searchInputRect = searchInput.getBoundingClientRect();
            resultsContainer.style.left = `${searchInputRect.left}px`;
            resultsContainer.style.top = `${searchInputRect.bottom + window.scrollY}px`;
            resultsContainer.style.width = `${searchInputRect.width}px`;
        } else {
            clearResults();
        }
    }

    function clearResults() {
        const existingResults = document.getElementById('dynamic-results');
        if (existingResults) {
            existingResults.remove();
        }
    }
});
