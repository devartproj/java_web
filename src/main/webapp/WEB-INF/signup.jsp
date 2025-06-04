<%@ page import="step.learning.dto.models.SignupFormModel" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String regData = (String) request.getAttribute( "reg-data" ) ;
    SignupFormModel formModel = (SignupFormModel) request.getAttribute( "reg-model" ) ;
    Map<String, String> validationErrors =
            request.getAttribute( "validationErrors" ) == null
            ? new HashMap<String, String>()
            : (Map<String, String>) request.getAttribute( "validationErrors" ) ;

    String loginClass = regData == null ? "validate" : (
            validationErrors.containsKey("login") ? "invalid" : "valid" ) ;
%>
<h2>Реєстрація користувача</h2>
<p>
<%= request.getAttribute("culture")%>
</p>
<div class="row">
    <form class="col s12" action="" method="post" enctype="multipart/form-data">
        <div class="row">
            <div class="input-field col s6">
                <i class="material-icons prefix">person</i>
                <input id="reg-login" name="reg-login" type="text"
                       class="<%= loginClass %>"
                       value="<%= formModel == null ? "" : formModel.getLogin() %>" >
                <label for="reg-login">Логін на сайті</label>
                <% if( validationErrors.containsKey("login") ) { %>
                    <span class="helper-text" data-error="<%= validationErrors.get("login") %>" ></span>
                <% } %>
            </div>
            <div class="input-field col s6">
                <i class="material-icons prefix">badge</i>
                <input id="reg-name" name="reg-name" type="text" class="validate"
                       value="Користувач" >
                <label for="reg-name">Реальне ім'я</label>
            </div>
        </div>
        <div class="row">
            <div class="input-field col s6">
                <i class="material-icons prefix">alternate_email</i>
                <input id="reg-email" name="reg-email" type="email" class="validate"
                       value="user@mail.net" >
                <label for="reg-email">Електронна пошта</label>
            </div>
            <div class="input-field col s6">
                <i class="material-icons prefix">child_friendly</i>
                <input id="reg-birthdate" name="reg-birthdate" type="date" class="validate"
                       value="2000-10-20" >
                <label for="reg-birthdate">Дата народження</label>
            </div>
        </div>
        <div class="row">
            <div class="input-field col s6">
                <i class="material-icons prefix">lock</i>
                <input id="reg-password" name="reg-password" type="password" class="validate"
                       value="123" >
                <label for="reg-password">Введіть пароль</label>
            </div>
            <div class="input-field col s6">
                <i class="material-icons prefix">lock_open</i>
                <input id="reg-repeat" name="reg-repeat" type="password" class="validate"
                       value="123" >
                <label for="reg-repeat">Повторіть пароль</label>
            </div>
        </div>
        <div class="row">
            <div class="input-field col s6">
                <i class="material-icons prefix">fact_check</i>
                <label>
                    <input name="reg-agree" type="checkbox" class="filled-in" />
                    <span>Не буду нічого порушувати</span>
                </label>
            </div>
            <div class="col s6">
                <div class="file-field input-field">
                    <div class="btn deep-orange">
                        <span><i class="material-icons">portrait</i></span>
                        <input type="file" name="reg-avatar"/>
                    </div>
                    <div class="file-path-wrapper">
                        <input class="file-path validate" type="text"
                               placeholder="Картинка-аватарка">
                    </div>
                </div>
            </div>
        </div>
        <div class="input-field row right-align">
            <button class="waves-effect waves-light btn deep-orange">
                <i class="material-icons right">how_to_reg</i>Реєстрація</button>
        </div>
    </form>
</div>
<p>
    Надсилання форм.<br/>
    У зразках форм на сайті CSS-фреймворка у полів не зазначається атрибут
    <code>name</code>, необхідно встановити його для тих полів, передача
    яких передбачається. Також змінюємо метод, яким надсилається форма, та
    переконуємось, що кнопка надсилання є елементом <code>submit</code>.
</p>
Д.З. Реалізувати перевірку (валідацію) необхідних та переданих даних
(якщо поле не є необхідним але є переданим, то перевіряємо, інакше ні)
Вивести усі повідомлення валідації на формі реєстрації.