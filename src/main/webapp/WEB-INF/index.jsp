<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String context = request.getContextPath() ;
%>
<img src="<%=context%>/img/Java_Logo.png" style="float:left; height: 50px"/>
<h1>Java web. Вступ.</h1>
<p>
    Створюємо проєкт з архетипом webapp.
    Перегенеровуємо індексний файл (index.jsp).
    Налаштовуємо конфігурацію запуску. Для цього потрібний локальний
    сервер: Tomcat, Glassfish, JBoss, WildFly, TomEE.
    Більшість цих серверів встановлюються простим розпаковуванням з
    архіву.
</p>
<%
    String str = "Hello" ;
    str += " World" ;
    int x = 10 ;
%>
<p>
    str = <%= str %>, x + 10 = <%= x + 10 %>
</p>
<ul>
<% for( int i = 1; i <= 10; ++i ) { %>
    <li>
        item No <%= i %>
    </li>
<% } %>
</ul>
<jsp:include page="fragment.jsp">
    <jsp:param name="str" value="<%= str %>"/>
    <jsp:param name="x" value="<%= x %>"/>
</jsp:include>

