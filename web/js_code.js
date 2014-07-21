/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 * Torna alla pagina precedentemente visitata.
 */
function paginaPrecedente() {
    window.location.href = document.referrer;
}

/**
 * Redirige alla home dopo la registrazione.
 */
function redirectRegistrazione() {
    window.location.href = ".";
}

/**
 * Sostituisce l'immagine nella colonna "quantità" con il numero di pizze scelto.
 * @param {type} index posizione della pizza nel catalogo.
 */
function sostituzione(index) {
    document.getElementById("add" + index).style.display = "none";
    document.getElementById("q" + index).style.display = "inline";
    document.getElementById("q" + index).focus();
    document.getElementById("q" + index).value = 1;
}

/**
 * Controlla che ut sia un utente autenticato. 
 * Se l'utente è autenticato può effettuare la prenotazione, altrimenti il sistema richiede il login.
 * @param {type} ut utente
 */
function isLogged(ut) {
    if (ut === "Guest") {
        alert("Per prenotarti devi fare il login. Se sei già registrato, puoi farlo a lato.");
    }
    else {
        document.forms["vis"].submit();
    }
}
/**
 * Controlla che vengano inseriti tutti i dati necessari per effettuare una prenotazione.
 * @param {type} n lunghezza elenco pizze
 */
function controlloDati(n) {
    if (document.getElementById("nominativo").value === "" || document.getElementById("recapito").value === "" || document.getElementById("telefono").value === "" || document.getElementById("data").value === "" || document.getElementById("ora").value === "") {
        alert("Non hai inserito tutti i dati necessari.");
        return;
    }
    for (var i = 0; i < n; i++) {
        if (document.getElementById("q" + i).value > 0) {
            document.forms["vis"].submit();
            return;
        }
    }
    alert("Non ci sono pizze inserite. Ricontrolla i dati inseriti.");
}

/**
 * Viene cancellata la prenotazione identificata con data, ora e name.
 * @param {type} data
 * @param {type} ora
 * @param {type} name
 */
function cancellaPrenotazione(data, ora, name) {
    document.getElementById("prenotazioneCancellataData").value = data;
    document.getElementById("prenotazioneCancellataOra").value = ora;
    document.getElementById("prenotazioneCancellataName").value = name;
    document.forms["vis"].submit();
}

/**
 * Visualizza il form per aggiungere nuove pizze al catalogo.
 */
function visualizzaForm() {
    document.getElementById("divFormAddPizza").style.display = "inline";
}

/**
 * Nasconde il form per l'inserimento di nuove pizze nel catalogo.
 */
function nascondiForm() {
    document.getElementById("actionFormAdd").value = "new";
    document.getElementById("nomeAddPizza").readOnly = false;
    document.getElementById("nomeAddPizza").value = "";
    document.getElementById("ingredientiAddPizza").value = "";
    document.getElementById("prezzoAddPizza").value = "";
    document.getElementById("divFormAddPizza").style.display = "none";
}

/**
 * Rimuove la pizza in posizione 'index'
 * @param index posizione della pizza nel catalogo.
 */
function removePizza(index) {
    document.location.href = "?page=cancellazionePizza&index=" + index;
}

/**
 * Permette di modificare le proprietà della pizza in posizione 'index'.
 * @param index posizione della pizza nel catalogo.
 */
function modificaPizza(index) {
    document.getElementById("nomeAddPizza").value = document.getElementById("nomeP" + index).innerHTML;
    document.getElementById("nomeAddPizza").readOnly = true;
    document.getElementById("ingredientiAddPizza").value = document.getElementById("ingredientiP" + index).innerHTML;
    var p = document.getElementById("prezzoP" + index).innerHTML;
    document.getElementById("prezzoAddPizza").value = (p.split(" ", p.indexOf(" ")))[0];
    document.getElementById("actionFormAdd").value = "edit";
    visualizzaForm();
}

/**
 * Viene controllato che il prezzo sia scritto nel formato corretto.
 */
function controlloPrezzo() {
    var prezzo = document.getElementById("prezzoAddPizza");
    if (isNaN(prezzo.value) || parseInt(prezzo.value) < 0 || parseInt(prezzo.value) > 9999) {
        alert('Il prezzo non è in un formato corretto. Utilizzare il punto per i numeri decimali.');
        prezzo.value = "";
        prezzo.focus();
    }
}

/**
 * Resetta i campi di login.
 */
function resettaCampiLogin() {
    document.getElementById("username").value = "";
    document.getElementById("password").value = "";
    document.getElementById("password2").value = "";
}

/**
 * L'amministratore segna come consegnata una prenotazione o annulla la conferma.
 */
function confermaConsegna() {
    document.forms["formConfermaConsegna"].submit();
}



