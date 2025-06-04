package step.learning.ws;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import step.learning.dao.AuthTokenDao;
import step.learning.dao.ChatDao;
import step.learning.dto.entities.AuthToken;
import step.learning.dto.entities.ChatMessage;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint(
        value = "/chat",   // адреса: ws://localhost.../chat
        configurator = WebsocketConfigurator.class   // див. коментарі у WebsocketConfigurator
)
public class WebsocketController {
    /*
    Одна з найпоширеніших задач WS - оповіщення усіх підключених
    клієнтів про певні події. Відповідно, центральною частиною
    сервера є колекція активних підключень.
    Оскільки кожне підключення утворює новий об'єкт, колекція має
    бути статичною.
     */
    private static final Set<Session> sessions =
            Collections.synchronizedSet( new HashSet<>() ) ;
    private final AuthTokenDao authTokenDao ;
    private final ChatDao chatDao ;

    @Inject
    public WebsocketController(AuthTokenDao authTokenDao, ChatDao chatDao) {
        this.authTokenDao = authTokenDao;
        this.chatDao = chatDao;
    }

    @OnOpen
    public void onOpen( Session session, EndpointConfig sec ) {
        chatDao.install();
        String culture = (String) sec.getUserProperties().get( "culture" ) ;
        if( culture == null ) {  // відсутність даних можна вважати спробою нелегального доступу
            try { session.close() ; }
            catch( IOException ignored ) { }
        }
        else {
            session.getUserProperties().put( "culture", culture ) ;
            sessions.add( session ) ;
        }
    }
    @OnClose
    public void onClose( Session session ) {
        sessions.remove( session ) ;
    }
    @OnMessage
    public void onMessage( String message, Session session ) {
        JsonObject request = JsonParser.parseString( message ).getAsJsonObject() ;
        String command = request.get("command").getAsString() ;
        String data = request.get("data").getAsString() ;
        switch (command) {
            case "auth": {
                AuthToken token = authTokenDao.getTokenByBearer(data);
                if (token == null) {
                    sendToSession(session, 403, "Token rejected");
                    return;
                }
                session.getUserProperties().put("token", token);
                sendToSession(session, 202, token.getNik());
                break;
            }
            case "chat": {
                AuthToken token = (AuthToken) session.getUserProperties().get("token");
                ChatMessage chatMessage = new ChatMessage( token.getSub(), data ) ;
                chatDao.add( chatMessage ) ;
                broadcast( token.getNik() + ": " + data ) ;
                break;
            }
            case "load": {
                AuthToken token = (AuthToken) session.getUserProperties().get("token");
                if (token!=null){
                    JsonObject response = new JsonObject();
                    response.addProperty("status", 200);
                    JsonArray array = new JsonArray();

                }
            }
            default:
                sendToSession( session, 405, "Command unrecognized" ) ;
        }
    }
    @OnError
    public void onError( Throwable ex, Session session ) {
        System.err.println( "onError" + ex.getMessage() ) ;
    }

    public static void sendToSession( Session session, int status, String message ) {
        JsonObject response = new JsonObject() ;
        /*
        Д.З. Реорганізувати роботу методів sendToSession/broadcast
        у WebsocketController - при масовій розсилці створюється
        багато повністю однакових об'єктів JsonObject response.
        Змінити алгоритм для зменшення навантаження на пам'ять
         */
        response.addProperty( "status", status ) ;
        response.addProperty( "data", message ) ;
        try {
            session.getBasicRemote().sendText( response.toString() ) ;
        }
        catch( Exception ex ) {
            System.err.println( "sendToSession" + ex.getMessage() ) ;
        }
    }

    public static void sendToSession(Session session, JsonObject jsonObject) {
        try {
            session.getBasicRemote().sendText(jsonObject.toString());
        } catch (Exception ex) {
            System.err.println("sendToSession: " + ex.getMessage());
    }}

    public static void broadcast( String message ) {
        sessions.forEach( session ->
                sendToSession( session, 201, message ) ) ;
    }
}
