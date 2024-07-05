document.addEventListener('DOMContentLoaded', function() {
    const searchInput = document.getElementById('cerca');
    const formSearch= document.getElementById("searchForm");
    let resultContainer;

    searchInput.addEventListener('input', function() {
        const query = searchInput.value;

        if (query.length > 0) {
            searchProduct(query);
        } else {
            clearResults();
        }
    });

    searchInput.addEventListener("blur", function(){
        //clearResults();
    })

    formSearch.addEventListener("submit", function (){
        event.preventDefault();
        const query = searchInput.value.trim();
        if (query.length>0){
            window.location.href = `/searchForm?query=${encodeURIComponent(query)}`;
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
        clearResults(); // cancella i risultati precenti

        const maxResults = 4;
        const resultsToShow = data.slice(0, maxResults);
        //limito la rappresentazione a 4 risultati

        if (resultsToShow.length > 0) {
            resultContainer = document.createElement('div');
            resultContainer.classList.add('container-search');
            resultContainer.id = 'dynamic-results';

            const ulElement = document.createElement('ul');
            resultsToShow.forEach(item => {
                const liElement = document.createElement('li');
                const linkElement = document.createElement('a');
                linkElement.textContent = item.nome;
                linkElement.href =  `product?opzione=read&id=${item.id}`;
            //rendo i nomi che si visualizzano come risposta dalla ricerca come
                //dei link alle loro pagine dettaglio
                liElement.appendChild(linkElement);
                ulElement.appendChild(liElement);
            });
            resultContainer.appendChild(ulElement);

            document.body.appendChild(resultContainer);


            positionResultsContainer();

            document.addEventListener('click', closeResultsIfClickedOutside);
            //riaggiorna la posizione dei risultati nel caso in cui la finestra cambia di dimensione
            window.addEventListener('resize', positionResultsContainer);
        } else {
            clearResults();
        }
    }
    function positionResultsContainer() {
        //è una funzione che cerca di posizionare in base alla barra di ricerca
        //dove mostrare i risultati
        if (resultContainer && searchInput) {
            const searchInputRect = searchInput.getBoundingClientRect();
            resultContainer.style.left = `${searchInputRect.left}px`;
            resultContainer.style.top = `${searchInputRect.bottom + window.scrollY}px`;
            resultContainer.style.width = `${searchInputRect.width}px`;

            // Assicura che il contenitore non superi la larghezza dello schermo
            const windowWidth = window.innerWidth;
            const containerRightEdge = searchInputRect.left + searchInputRect.width;
            if (containerRightEdge > windowWidth) {
                resultContainer.style.width = `${windowWidth - searchInputRect.left - 10}px`; // 10px di margine
            }
        }
    }

    function closeResultsIfClickedOutside(event) {
        if (resultContainer && !resultContainer.contains(event.target) && event.target !== searchInput) {
            clearResults();
            document.removeEventListener('click', closeResultsIfClickedOutside);
        }
    }
    function clearResults() {
        const existingResults = document.getElementById('dynamic-results');
        if (existingResults) {
            existingResults.remove();
        }
    }
});
