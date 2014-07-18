/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var controllo = false;

function paginaPrecedente() {
    window.location.href = document.referrer;
}
function redirectRegistrazione() {
//    var stateObj = { foo: "bar" };
//    history.pushState(null, "", history.go(-2));
//  DA RIVEDERE!!!!!!!!!!!!!
    //window.location.href = document.referrer;
    window.location.href = ".";
}
function sostituzione(index) {
    document.getElementById("add" + index).style.display = "none";
    document.getElementById("q" + index).style.display = "inline";
    document.getElementById("q" + index).focus();
    document.getElementById("q" + index).value = 1;
    controllo = true;
}
function isLogged(ut) {
    if (ut === "Guest") {
        alert("Per prenotarti devi fare il login. Se sei già registrato, puoi farlo a lato.");
    }
    else {
        document.forms["vis"].submit();

        //window.location.href = "?page=catalogo";
    }
}

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

//Funzione che serviva a capire se la tabella del catalogo delle pizze conteneva o meno valori e in tal caso visualizzare l'input number invece del +
//function haveValues(n){
//    for(i=0; i<n; i++){
//        if(document.getElementById("q"+i).value !== null)
//            sostituzione(i);
//    }
//}
function cancellaPrenotazione(data, ora, name) {
    document.getElementById("prenotazioneCancellataData").value = data;
    document.getElementById("prenotazioneCancellataOra").value = ora;
    document.getElementById("prenotazioneCancellataName").value = name;
    document.forms["vis"].submit();
}

function visualizzaForm() {
    document.getElementById("divFormAddPizza").style.display = "inline";
}
function nascondiForm() {
    document.getElementById("actionFormAdd").value = "new";
    document.getElementById("nomeAddPizza").readOnly = false;
    document.getElementById("nomeAddPizza").value = "";
    document.getElementById("ingredientiAddPizza").value = "";
    document.getElementById("prezzoAddPizza").value = "";
    document.getElementById("divFormAddPizza").style.display = "none";
}

function removePizza(index) {
    document.location.href = "?page=cancellazionePizza&index=" + index;
}
function modificaPizza(index) {
    document.getElementById("nomeAddPizza").value = document.getElementById("nomeP" + index).innerHTML;
    document.getElementById("nomeAddPizza").readOnly = true;
    document.getElementById("ingredientiAddPizza").value = document.getElementById("ingredientiP" + index).innerHTML;
    var p = document.getElementById("prezzoP" + index).innerHTML;
    document.getElementById("prezzoAddPizza").value = (p.split(" ", p.indexOf(" ")))[0];
    document.getElementById("actionFormAdd").value = "edit";
    visualizzaForm();
}

function controlloPrezzo() {
    var prezzo = document.getElementById("prezzoAddPizza");
    if (isNaN(prezzo.value) || parseInt(prezzo.value) < 0 || parseInt(prezzo.value) > 9999) {
        alert('Il prezzo non è in un formato corretto. Utilizzare il punto per i numeri decimali.');
        prezzo.value = "";
        prezzo.focus();
    }
}

function resettaCampiLogin() {
    document.getElementById("username").value = "";
    document.getElementById("password").value = "";
}

function confermaConsegna() {
//    alert(c);
//    document.getElementById("confermaConsegna").value = c;
//    alert(document.getElementById("confermaConsegna").value);
//    document.getElementsByName("confermaConsegna").value = c;
    document.forms["formConfermaConsegna"].submit();
}



