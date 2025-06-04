<%@ page contentType="text/html;charset=UTF-8" %>
<h1>Інверсія управління у веб-застосунку</h1>
<ul class="collection">
    <li class="collection-item">
        Додаємо Maven залежності для
        <a href="https://mvnrepository.com/artifact/com.google.inject/guice/6.0.0">Guice</a>
        та розширення
        <a href="https://mvnrepository.com/artifact/com.google.inject.extensions/guice-servlet/6.0.0">Guice Servlet</a>
    </li>
    <li class="collection-item">
        Створюємо пакет ioc, у ньому конфігураційні класи
        IocContextListener - обробник події створення контексту (
        певний аналог стартового методу main)
        RouterModule - клас з налаштуваннями фільтрів та сервлетів,
        ServicesModule - клас з налаштуваннями служб (сервісів)
    </li>
    <li class="collection-item">
        Змінюємо web.xml - залишаємо фільтр від Guice та наш
        слухач події створення контексту (див. web.xml)
    </li>
    <li class="collection-item">
        !!! для всіх фільтрів та сервлетів, заявлених у RouterModule
        необхідно додати анотацію @Singleton
    </li>
    <li class="collection-item">
        Інжекція служб здійснюється так само як і у консольному проєкті.
        Перевірка: <%= request.getAttribute("hash") %>
    </li>
</ul>