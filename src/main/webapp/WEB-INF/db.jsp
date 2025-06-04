<%@ page contentType="text/html;charset=UTF-8" %>
<h1>Робота з базами даних</h1>
<h2>JDBC</h2>
<p>
    <strong>JDBC</strong> - Java DataBase Connectivity - технологія
    доступу до даних, аналогічна ADO, PDO та іншим, мета якої є
    надання універсального інтерфейсу для різних типів СУБД та інших
    постачальників даних.
</p>
<p>
    Технологія базується на підключенні, передаванні команд до
    СУБД та отримання відповіді (або помилки або даних). Технічно
    взаємодію реалізують драйвери (або конектори). Програмно
    бажано реалізовувати у вигляді сервісу, оскільки доступ до
    даних може знадобитись у різних частинах проєкту.
</p>
<h2>Префікс</h2>
<p>
    У випадках коли доводиться поєднувати в одній БД декілька
    проєктів або самостійних рішень використовується або поділ
    таблиць за схемами (якщо СУБД підтримує схеми), або правило
    іменування таблиць за префіксною схемою: всі таблиці одного
    рішення (проєкту) починаються з однакового рядка, відомого
    як префікс. Досить часто цей префікс можна задати конфігурацією
    або при запуску проєкту.
</p>
<h2>Випробування</h2>
<div class="row">
    Створити таблицю БД
    <button id="db-create-button" class="waves-effect waves-light btn deep-orange">
        <i class="material-icons right">create_new_folder</i>
        Запит
    </button>
</div>
<div class="row">
    <form class="col s12">
        <div class="row">
            <div class="input-field col s4">
                <i class="material-icons prefix">badge</i>
                <input placeholder="Input your full name" id="db-call-me-name" type="text" class="validate">
                <label for="db-call-me-name">Name</label>
            </div>
            <div class="input-field col s5">
                <i class="material-icons prefix">phone_iphone</i>
                <input placeholder="+380 XX XXX XX XX" id="db-call-me-phone" type="tel" class="validate">
                <label for="db-call-me-phone">Phone number</label>
            </div>
            <div class="input-field col s3">
                <button type="button" id="db-call-me-button" class="waves-effect waves-light btn deep-orange">
                    <i class="material-icons right">call</i>
                    замовити
                </button>
            </div>
        </div>
    </form>
</div>
<h2>Logging</h2>
<p>
    З метою забезпечення можливості відпрацювання помилок у роботі
    програми, повідомлення про них мають зберігатись (на постійній
    основі, не лише у консолі). Ця задача покладається на систему
    логування (логер), який засобами Guice постачається "з коробки".
    Для зміни стандартних налаштувань можна скористатись або
    програмними інструкціями, або файлом конфігурації. Деталі - у
    класі LoggingModule. При роботі саме з БД рекомендовано
    зберігати тексти SQL запитів (разом з повідомленнями про
    помилку), оскільки це спростить виправлення їх синтаксичних
    помилок.
</p>
<div class="row">
    <button type="button" id="db-get-all-button" class="waves-effect waves-light btn deep-orange">
        <i class="material-icons right">table_chart</i>
        переглянути
    </button>
</div>
<div id="db-get-all-container"></div>
