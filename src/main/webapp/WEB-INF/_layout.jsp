<%@ page import="java.util.Date" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
  String pageBody = (String) request.getAttribute( "page-body" ) ;
  String context = request.getContextPath() ;
  String contextCulture = context + "/" + request.getAttribute( "culture" );   // Deploy - App Context
  long time = new Date().getTime();
%>
<!DOCTYPE html>
<html>
<head>
    <title>Java web</title>
    <!--Import Google Icon Font-->
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <!-- Compiled and minified CSS -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css">
    <!-- Site CSS -->
    <link rel="stylesheet" href="<%= context %>/css/site.css?<%=time%>"/>
</head>
<body>
<nav>
    <div class="nav-wrapper deep-orange">
        <!-- Modal Trigger -->
        <a class="auth-trigger modal-trigger right" href="#auth-modal"><i class="material-icons">door_front</i></a>

        <a href="<%= context %>/" class="site-logo right">Java 202</a>
        <ul id="nav-mobile">
            <li><a href="#">JSP</a></li>

            <li <%= "filters.jsp".equals(pageBody) ? "class='active'" : "" %>
                ><a href="<%= contextCulture %>/filters">Filters</a></li>

            <li <%= "ioc.jsp".equals(pageBody) ? "class='active'" : "" %>
                ><a href="<%= contextCulture %>/ioc">IoC</a></li>

            <li <%= "db.jsp".equals(pageBody) ? "class='active'" : "" %>
            ><a href="<%= contextCulture %>/db">DB</a></li>

            <li <%= "spa.jsp".equals(pageBody) ? "class='active'" : "" %>
            ><a href="<%= contextCulture %>/spa">SPA</a></li>

            <li <%= "ws.jsp".equals(pageBody) ? "class='active'" : "" %>
            ><a href="<%= contextCulture %>/ws">WS</a></li>
        </ul>
    </div>
</nav>
<div class="container">
<jsp:include page="<%= pageBody %>"/>
</div>

<footer class="page-footer deep-orange">
    <div class="container">
        <div class="row">
            <div class="col l6 s12">
                <h5 class="white-text">Step Learning</h5>
                <p class="grey-text text-lighten-4">Open your mind</p>
            </div>
            <div class="col l4 offset-l2 s12">
                <h5 class="white-text">Good to visit</h5>
                <ul>
                    <li><a class="grey-text text-lighten-3" href="https://materializecss.com/">Materialize CSS</a></li>
                    <li><a class="grey-text text-lighten-3" href="https://fonts.google.com/icons">Materialize Icons</a></li>
                    <li><a class="grey-text text-lighten-3" href="https://planetscale.com/">PlanetScale platform</a></li>
                </ul>
            </div>
        </div>
    </div>
    <div class="footer-copyright">
        <div class="container">
            © 2023 IT Step Academy
            <a class="grey-text text-lighten-4 right" href="https://itstep.org/">IT Step Home</a>
        </div>
    </div>
</footer>

<!-- Modal Structure -->
<div id="auth-modal" class="modal">
    <div class="modal-content">
        <h4>Автентифікація на сайті</h4>
        <div class="row">
            <div class="input-field col s6">
                <i class="material-icons prefix">person</i>
                <input id="auth-login" type="text" >
                <label for="auth-login">Введіть логін</label>
            </div>
            <div class="input-field col s6">
                <i class="material-icons prefix">lock</i>
                <input id="auth-password" type="password" >
                <label for="auth-password">Введіть пароль</label>
            </div>
        </div>
    </div>
    <div class="modal-footer">
        <b id="auth-message-container"></b>
        <a href="<%= context %>/signup" class="modal-close btn-flat deep-orange lighten-2">Реєстрація</a>
        <button id="auth-modal-sign-in-button" class="waves-effect waves-green btn-flat  deep-orange lighten-3">Вхід</button>
    </div>
</div>

<!-- Compiled and minified JavaScript -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>
<!-- Site script -->
<script src="<%= context %>/js/site.js"></script>
<script src="<%= context %>/js/auth.js?<%=time%>"></script>
</body>
</html>
