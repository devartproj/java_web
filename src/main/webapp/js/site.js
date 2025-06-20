document.addEventListener('DOMContentLoaded', function() {
    // db.jsp
    const createButton = document.getElementById("db-create-button");
    if(createButton) createButton.addEventListener('click', createButtonClick);
    const callMeButton = document.getElementById("db-call-me-button");
    if(callMeButton) callMeButton.addEventListener('click', callMeButtonClick);
    const getAllButton = document.getElementById("db-get-all-button");
    if(getAllButton) getAllButton.addEventListener('click', getAllButtonClick);

});
function getAllButtonClick() {
    fetch(window.location.href, {
        method: 'LINK'
    }).then(r => r.json()).then(showCalls);
}
function showCalls( json ) {
    const container = document.getElementById("db-get-all-container");
    if( ! container ) throw "#db-get-all-container not found" ;
    const table = document.createElement('table');
    table.classList.add('striped');
    const thead = document.createElement('thead');
    const tr = document.createElement('tr');
    const th1 = document.createElement('th');
    th1.innerText = 'id';
    const th2 = document.createElement('th');
    th2.innerText = 'name';
    const th3 = document.createElement('th');
    th3.innerText = 'phone';
    const th4 = document.createElement('th');
    th4.innerText = 'call';
    const th5 = document.createElement('th');
    th5.innerText = 'Del';
    th5.classList.add('center-align');
    tr.appendChild(th1);
    tr.appendChild(th2);
    tr.appendChild(th3);
    tr.appendChild(th4);
    tr.appendChild(th5);
    thead.appendChild(tr);
    table.appendChild(thead);
    const tbody = document.createElement('tbody');
    json.forEach(call => {
        const tr = document.createElement('tr');
        const td1 = document.createElement('td');
        td1.innerText = call.id;
        const td2 = document.createElement('td');
        td2.innerText = call.name;
        const td3 = document.createElement('td');
        td3.innerText = call.phone;
        tr.appendChild(td1);
        tr.appendChild(td2);
        tr.appendChild(td3);
        // callMoment
        const td4 = document.createElement('td');
        if( typeof call.callMoment == 'undefined' || call.callMoment == null ) {
            // кнопка "подзвонити"
            const btn = document.createElement('button');
            btn.appendChild(document.createTextNode("call"));
            btn.classList.add('btn');
            btn.classList.add('waves-effect');
            btn.classList.add('waves-light');
            btn.classList.add('deep-orange');
            btn.addEventListener('click', makeCallClick);
            btn.setAttribute( 'data-call-id', call.id ) ;
            td4.appendChild(btn);
        }
        else {
            // показати дату
            td4.appendChild(document.createTextNode(call.callMoment));
        }
        tr.appendChild(td4);
        // Delete button
        const td5 = document.createElement('td');
        const btn5 = document.createElement('button');
        btn5.appendChild(document.createTextNode("X"));
        btn5.classList.add('btn');
        btn5.classList.add('lime-text');
        btn5.classList.add('deep-orange');
        btn5.addEventListener('click', deleteClick);
        btn5.setAttribute( 'data-call-id', call.id ) ;
        td5.classList.add('center-align');
        td5.appendChild(btn5);
        tr.appendChild(td5);

        tbody.appendChild(tr);
    });
    table.appendChild(tbody);
    container.innerHTML = "";
    container.appendChild( table ) ;
}

function deleteClick(e) {
    const callId = e.target.getAttribute('data-call-id');
    if( confirm( "Delete order " + callId ) ) {
        fetch(window.location.href + "?call-id=" + callId, {
            method: 'DELETE'
        }).then(r => {
            if( r.status === 204 ) {
                const tr = e.target  // button
                    .parentNode   // td
                    .parentNode;  // tr
                tr.parentNode.removeChild(tr);
            }
            else {
                r.json().then(alert);
            }
        });
    }
}
function makeCallClick(e) {
    const callId = e.target.getAttribute('data-call-id');
    if( confirm( "Make call to order " + callId ) ) {
        fetch(window.location.href + "?call-id=" + callId, {
            method: 'CALL'
        }).then(r => r.json()).then( j => {
           if( typeof j.callMoment == 'undefined' ) {
               alert( j ) ;
           }
           else {
               e.target.parentNode.innerHTML = j.callMoment ;
           }
        });
    }
}
function callMeButtonClick() {
    const nameInput = document.getElementById("db-call-me-name");
    if( ! nameInput ) throw "nameInput (#db-call-me-name) not found" ;
    if( ! nameInput.value ) {
        M.toast({html: 'Name is required'});
        return;
    }
    const phoneInput = document.getElementById("db-call-me-phone");
    if( ! phoneInput ) throw "phoneInput (#db-call-me-phone) not found" ;
    if( ! phoneInput.value ) {
        M.toast({html: 'Phone is required'});
        return;
    }
    fetch(window.location.href, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            name: nameInput.value,
            phone: phoneInput.value
        })
    }).then(r => r.json()).then(j => {
        console.log(j);
    });
}
function createButtonClick() {
    fetch(window.location.href, {
        method: 'POST'
    }).then(r => r.json()).then(j => {
        console.log(j);
    });
}
/*
Д.З. Реалізувати відображення повідомлень від бекенду на сторінці клієнта:
у випадку успішної операції виводити "створено успішно" (або символічний
 результат іконкою)
у випадку помилки виводити текст помилки
** виділити саму суть помилки (table already exists)
** додати інші типи виключень в обробку (окрім наявного SQLException)
 */