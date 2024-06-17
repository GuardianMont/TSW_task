
document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('categoria').addEventListener('change', function() {
        var specificaTavolo = document.getElementById('specificaTavolo');
        var specificaAttrezzatura = document.getElementById('specificaAttrezzatura');
        if (this.value === 'tavolo') {
            specificaTavolo.style.display = 'block';
        } else {
            specificaTavolo.style.display = 'none';
        }

        if (this.value==='attrezzatura'){
            specificaAttrezzatura.style.display='block';
        }else{
            specificaAttrezzatura.style.display='none';
        }
    });
});

