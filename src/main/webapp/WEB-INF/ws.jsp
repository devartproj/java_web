<%@ page contentType="text/html;charset=UTF-8" %>
<h1>Websocket</h1>
<div class="row">
  <div class="col s3" id="chat-block">
    <b id="chat-nik">Wait...</b>
    <input id="chat-input" type="text" value="Hi All" />
    <button id="chat-send" disabled type="button" onclick="sendMessageClick()">Send</button>
    <ul class="collection" id="chat-container"></ul>
  </div>
  <div class="col s9">
    <h2>Ідея</h2>
    <p>
      Вебсокет являє собою протокол, який дозволяє створити
      постійний дуплексний зв'язок (сокет) між двома учасниками -
      клієнтом та сервером. Цей протокол діє за схемами "ws://"
      та "wss://"
    </p>
    <h2>Реалізація</h2>
    <p>
      Реалізація протоколу частіше за все здійснюється на подійній
      моделі - життєвий цикл даних супроводжується створенням програмних
      подій, на які можна підписати "слухачів" і обробляти дані.
      У Java реалізація входить javax (JavaEE), але потребує додавання
      <a href="https://mvnrepository.com/artifact/javax.websocket/javax.websocket-api/1.1">залежності</a>
    </p>
    <h2>Конфігурація</h2>
    <p>
      По-перше, інжекція. Оскільки сервер окремий, його запити не проходять
      фільтри та, відповідно, Guice інжектор
    </p>
  </div>
</div>
<script>
  document.addEventListener('DOMContentLoaded', initWebsocket);
  function sendMessageClick() {
    window.websocket.send(
        JSON.stringify({
          command: 'chat',
          data: document.getElementById("chat-input").value
        })
    );
  }
  function initWebsocket() {
    const host = window.location.host + getAppContext() ;
    const ws = new WebSocket(`ws://${host}/chat`) ;
    ws.onopen = onWsOpen;
    ws.onclose = onWsClose;
    ws.onmessage = onWsMessage;
    ws.onerror = onWsError;
    window.websocket = ws ;
  }
  function onWsOpen(e) {
    // console.log("onWsOpen", e);
    window.websocket.send(
            JSON.stringify({
              command: 'auth',
              data: window.localStorage.getItem("token202")
            })
    );
  }
  function onWsClose(e) {
    console.log("onWsClose", e);
  }
  function onWsMessage(e) {
    // console.log("onWsMessage", e);
    const chatMessage = JSON.parse(e.data) ;
    switch (chatMessage.status) {
        case 201: // broadcast
            appendChatMessage(chatMessage.data);
            break ;
        case 202: // token accepted, .data==nik
            enableChat(chatMessage.data);
            break ;
        case 403: // token rejected
            disableChat();
            break ;
        case 405: // Command unrecognized
            console.error(chatMessage);
            break ;
    }
  }
  function enableChat(nik) {
      document.getElementById("chat-nik").innerText = nik;
      for (let child of document.getElementById("chat-block").children) {
        child.disabled = false;
      }
      //document.getElementById("chat-input").disabled = false;
      //document.getElementById("chat-send").disabled = false;
      appendChatMessage(nik + ' join us');
  }
  function disableChat() {
      document.getElementById("chat-nik").innerText = "OFF";
      for (let child of document.getElementById("chat-block").children) {
        child.disabled = true;
      }
      //document.getElementById("chat-input").disabled = true;
      //document.getElementById("chat-send").disabled = false;

  }

  function appendChatMessage(message) {
      const li = document.createElement("li");
      li.className="collection-item";
      li.appendChild(document.createTextNode(message));
      document.getElementById("chat-container").appendChild(li);
  }
  function onWsError(e) {
    console.log("onWsError", e);
  }
  function getAppContext() {
    var isContextPreset = false ;
    return isContextPreset ? "" : '/' + window.location.pathname.split('/')[1] ;
  }
  /*
  Додати до повідомлень чату мітку часу та забезпечити її
  виведення у складі візуальної частини.
  Реалізувати "розумне" формування часу: якщо дата "сьогодні",
  то так і виводити (або лише час), також "вчора" і "2-3 дні тому".
  Для решти виводити повну дату і час до хвилин
   */
</script>