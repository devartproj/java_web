package step.learning.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.dao.AuthTokenDao;
import step.learning.dto.entities.AuthToken;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Контролер доступу до інформації з обмеженим доступом
 */
@Singleton
public class TemplatesServlet extends HttpServlet {
    private static final byte[] buffer = new byte[8192] ;
    private final Logger logger ;
    private final AuthTokenDao authTokenDao ;

    @Inject
    public TemplatesServlet(Logger logger, AuthTokenDao authTokenDao) {
        this.logger = logger;
        this.authTokenDao = authTokenDao;
    }
    private String checkAuthToken( HttpServletRequest req ) {
        String token = req.getHeader( "Authorization" ) ;
        if( token == null ) {
            return "Authorization header required" ;
        }
        if( ! token.startsWith( "Bearer " ) ) {
            return "Bearer Authorization scheme only" ;
        }
        token = token.replace( "Bearer ", "" ) ;
        AuthToken authToken = authTokenDao.getTokenByBearer( token ) ;
        if( authToken == null ) {
            return "Token rejected" ;
        }
        return null ;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*
        Сервлет для мульти-роутингу відповідає усім запитам tpl/{...}
        req.getServletPath() - постійна частина (/uk/tpl)
        req.getPathInfo() - варіативна частина (/{...})
        Для даного сервлету PathInfo - ім'я файлу (шаблону), що
        є предметом запиту. Відповідно, перевіряємо його існування
        та надсилаємо у відповідь.
         */
        String tokenCheckError = this.checkAuthToken( req ) ;
        if( tokenCheckError != null ) {
            sendResponse( resp, 401, tokenCheckError ) ;
            return ;
        }
        String tplName = req.getPathInfo() ;
        if( tplName == null || tplName.isEmpty() ) {
            sendResponse( resp, 400, "Resource name required" ) ;
            return ;
        }
        URL tplUrl = this.getClass().getClassLoader().getResource( "tpl" + tplName ) ;
        try {
            if( tplUrl == null ||
                    ! Files.isRegularFile( Paths.get( tplUrl.toURI() ) ) ) {
                sendResponse(resp, 404, "Resource not located");
                return;
            }
        }
        catch( URISyntaxException ignored ) {
            sendResponse( resp, 400, "Resource name invalid" ) ;
            return ;
        }
        try( InputStream tplStream = tplUrl.openStream() ) {
            int bytesRead ;
            resp.setContentType(
                    URLConnection.getFileNameMap().getContentTypeFor( tplName )
            ) ;
            // resp.get
            OutputStream respStream = resp.getOutputStream() ;
            while( ( bytesRead = tplStream.read( buffer ) ) > 0 ) {
                respStream.write( buffer, 0, bytesRead ) ;
            }
            respStream.close() ;
        }
        catch( IOException ex ) {
            logger.log( Level.SEVERE, ex.getMessage() + " -- " + tplName ) ;
            sendResponse( resp, 500, "Look at server logs" ) ;
        }
    }

    private void sendResponse( HttpServletResponse resp, int statusCode, String message ) throws IOException {
        resp.setStatus( statusCode ) ;
        resp.setContentType( "text/plain" ) ;
        resp.getWriter().print( message ) ;
    }
}
