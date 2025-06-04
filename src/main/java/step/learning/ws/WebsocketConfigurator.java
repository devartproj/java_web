package step.learning.ws;

import com.google.inject.Inject;
import com.google.inject.Injector;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import java.lang.reflect.Field;

public class WebsocketConfigurator
        extends ServerEndpointConfig.Configurator {

    @Inject
    private static Injector injector ;

    @Override
    public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
        return injector.getInstance( endpointClass ) ;
    }

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        super.modifyHandshake(sec, request, response);
        /*
        Доступ до HTTP-даних (сесії, параметрів/атрибутів) можна
        одержати у цьому методі з поля request, але це поле приватне
        і доступ до нього можливий засобами рефлексії
         */
        HttpServletRequest httpRequest = null ;
        for( Field field : request.getClass().getDeclaredFields() ) {
            if( HttpServletRequest.class.isAssignableFrom( field.getType() ) ) {
                field.setAccessible( true ) ;
                try {
                    httpRequest = (HttpServletRequest) field.get( request ) ;
                    break ;
                }
                catch( IllegalAccessException ignored ) { }
            }
        }
        if( httpRequest != null ) {  // ілюстрація того як з HTTP беремо атрибут
            String culture = (String) httpRequest.getAttribute( "culture" ) ;
            // "перекладаємо" дані в атрибути вебсокета
            sec.getUserProperties().put( "culture", culture ) ;
        }
    }
}
/*
Конфігуратор - клас, який відповідає за
- створення "контролерів" (об'єктів WebsocketController) - getEndpointInstance
- утворення з'єднання - modifyHandshake, зокрема ініціалізувати певні дані,
   які існують одноразово при підключенні вебсокета
Підключається конфігуратор в анотації для WebsocketController

Авторизація та Вебсокет
У залежності від способу підтримки авторизації рішення будуть відрізнятись
- у випадку серверної підтримки (HTTP-сесії) необхідно
    одержати доступ до HTTP-запиту, через який утворюється вебсокет
- у випадку клієнтської підтримки (токенів), їх слід дублювати
    у повідомленнях вебсокета
 */
