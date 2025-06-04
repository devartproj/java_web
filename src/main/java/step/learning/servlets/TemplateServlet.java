package step.learning.servlets;


import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.dao.AuthTokenDao;
import step.learning.dto.entities.AuthToken;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class TemplateServlet extends HttpServlet {
    private static final byte[] buffer = new byte[8192];
    private final Logger logger;
    private final AuthTokenDao authTokenDao;
    @Inject
    public TemplateServlet(Logger logger, AuthTokenDao authTokenDao) {
        this.logger = logger;
        this.authTokenDao = authTokenDao;
    }
    private String checkAuthToken (HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        if (token == null) {
            return "Auth head req";
        }
        if (!token.startsWith("Bearer")) {
            return "bearer auth scheme only";
        }
        token = token.replace("Bearer", "");
        AuthToken authToken = authTokenDao.getTokenByBearer(token);
        if (authToken == null) {
            return "Token rejected";
        }
        return null;
                //authTokenDao.getTokenByBearer(token) != null;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        /* Сервлет для мульти-роутинга отвечает всем запросам
        req.getServletPath() - потстоянная часть
        req.getPathInfo() - вариативная часть (/{...})
        Для данного сервлета PathInfo - имя файла (шаблона), что
        есть предметом запроса.
        */
        String tokenCheckError = this.checkAuthToken(req);
        if (tokenCheckError != null) {
            sendResponse(resp, 401, tokenCheckError);
            return;
        }
        String tplName = req.getPathInfo();
        if (tplName == null || tplName.isEmpty()) {
            sendResponse(resp, 400, "Resource name is require");
            return;
        }
        URL tplUrl = this.getClass().getClassLoader().getResource("tpl" + tplName);
        //Path tplPath;
        // tplFile;
        try {
            if (tplUrl == null ||
                    !Files.isRegularFile(Paths.get(tplUrl.toURI()))) {
                //(tplFile = new File( tplUrl.getFile() ) ).isFile() ) {
                sendResponse(resp, 404, "Resource name required");
                return;
            }
        }catch (URISyntaxException ignored) {
            sendResponse(resp, 400, "Resource name is require");
            return;
        }
        try (InputStream tplStream = tplUrl.openStream()) {
            int bytesRead;
            resp.setContentType(
                    //Files.probeContentType(tplPath)
                    URLConnection.getFileNameMap().getContentTypeFor(tplName)
            );
            OutputStream respStream = resp.getOutputStream();
            while ((bytesRead = tplStream.read(buffer)) > 0 ) {
                respStream.write(buffer, 0, bytesRead);
            }
            respStream.close();
//            resp.setContentType(
//                URLConnection.getFileNameMap().getContentTypeFor(tplName)
//            );
        }catch(Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage() + " -- " + tplName );
            sendResponse(resp, 500, "Look at server logs");
        }
//        resp.getWriter().print(
//                req.getServletPath() + " -- " + req.getPathInfo() );

    }
    private void sendResponse( HttpServletResponse resp, int statusCode, String message ) throws IOException {
        resp.setStatus( statusCode ) ;
        resp.setContentType( "text/plain" ) ;
        resp.getWriter().print(message);
    }
}
