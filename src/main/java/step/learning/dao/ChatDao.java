package step.learning.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import step.learning.dto.entities.ChatMessage;
import step.learning.services.db.DbProvider;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class ChatDao extends DaoBase {
    private final DbProvider dbProvider;
    private final String dbPrefix;
    private final Logger logger;

    @Inject
    public ChatDao(DbProvider dbProvider, @Named("db-prefix") String dbPrefix, Logger logger) {
        super(logger, dbProvider);
        this.dbProvider = dbProvider;
        this.dbPrefix = dbPrefix;
        this.logger = logger;
    }

    public List<ChatMessage> getLastMessages () {
        return getLastMessages(10);
    }

    public List<ChatMessage> getLastMessages (int count) {
        LinkedList<ChatMessage> lastMessages = new LinkedList<>();
        String sql = "SELECT sender_id, message FROM " + dbPrefix + "chat_messages ORDER BY moment DESC LIMIT ?";
        try (PreparedStatement prep = dbProvider.getConnection().prepareStatement(sql))
        {
            prep.setInt(1, count);
            try (Statement statement = dbProvider.getConnection().createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)
            )
                {
                    while (resultSet.next())
                {
                    //String sender_id = result_set.getString("sender_id");
                    //String message = result_set.getString("message");
                    lastMessages.addFirst(new ChatMessage(resultSet));
                }
        }
        }
        catch (SQLException ex)
        {
            logger.log(Level.WARNING, ex.getMessage() + "---" + sql);
        }
        Collections.reverse(lastMessages);
        return lastMessages;

    }

    public boolean add( ChatMessage chatMessage ) {
        chatMessage.setMoment(new Date(super.getDbTimestamp().getTime()));
        chatMessage.setId(super.getDbIdentity());
        String sql = "INSERT INTO " + dbPrefix +
                "chat_messages (`id`,`sender_id`,`message`,`moment`) VALUES(?,?,?,?)";
        try (PreparedStatement prep = dbProvider.getConnection().prepareStatement(sql)) {
            prep.setString(1, chatMessage.getId());
            prep.setString(2, chatMessage.getSenderId());
            prep.setString(3, chatMessage.getMessage());
            prep.setTimestamp(4, new Timestamp(chatMessage.getMoment().getTime()));
            prep.executeUpdate();
            return true;
        }
        catch (SQLException ex) {
            logger.log(Level.WARNING, ex.getMessage() + "---" + sql);
            return false;
        }
    }
    public void install() {
        String sql = "CREATE TABLE IF NOT EXISTS " + dbPrefix + "chat_messages (" +
                "id        BIGINT UNSIGNED  PRIMARY KEY DEFAULT( UUID_SHORT() )," +
                "sender_id BIGINT UNSIGNED  NOT NULL," +
                "message   TEXT             NOT NULL," +
                "moment    DATETIME         NOT NULL  DEFAULT CURRENT_TIMESTAMP" +
                ") ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE utf8mb4_unicode_ci";
        try( Statement statement = dbProvider.getConnection().createStatement() ) {
            statement.executeUpdate(sql);
        }
        catch( SQLException ex ) {
            logger.log( Level.WARNING, ex.getMessage() + " -- " + sql ) ;
        }
    }


}
