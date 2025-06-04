package step.learning.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import step.learning.dto.entities.CallMe;
import step.learning.services.db.DbProvider;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class CallMeDao {
    private final DbProvider dbProvider ;
    private final String dbPrefix ;
    private final Logger logger ;

    @Inject
    public CallMeDao(DbProvider dbProvider, @Named("db-prefix") String dbPrefix, Logger logger) {
        this.dbProvider = dbProvider;
        this.dbPrefix = dbPrefix;
        this.logger = logger;
    }

    public void add( CallMe item ) throws IllegalArgumentException {
        // Завдання: у методі CallMeDao::add( CallMe item )
        // 1) перевірити, чи є у item встановлене поле id, якщо є, то саме це
        //      значення підставляти до запиту, також врахувати всі інші дані
        //      (moment, callMoment) за тим же принципом: якщо є, то беремо дані, інакше - генеруємо
        // 2) у переданому item встановити ті поля, які було згенеровано (id, moment)
        // У HTML при прийманні замовлення видавати повідомлення "ваше замовлення №{id}"

        String sql = "INSERT INTO " + dbPrefix + "call_me (`name`, `phone`) VALUES(?,?)";
        try (PreparedStatement prep = dbProvider.getConnection().prepareStatement(sql)) {
            prep.setString(1, item.getName() );
            prep.setString(2, item.getPhone() );
            prep.execute();
        }
        catch( SQLException ex ) {
            logger.log( Level.WARNING, ex.getMessage() + " -- " + sql ) ;
            throw new IllegalArgumentException( ex ) ;
        }
    }

    public List<CallMe> getAll() {
        return getAll( false ) ;
    }
    public List<CallMe> getAll( boolean includeDeleted ) {
        List<CallMe> res = new ArrayList<>() ;
        String sql = "SELECT * FROM " + dbPrefix + "call_me " ;
        if( ! includeDeleted ) {
            sql += " WHERE delete_moment IS NULL";
        }
        try( Statement statement = dbProvider.getConnection().createStatement() ) {
            ResultSet resultSet = statement.executeQuery( sql ) ;
            while( resultSet.next() ) {
                res.add( new CallMe( resultSet ) ) ;
            }
        }
        catch( Exception ex ) {
            logger.log( Level.WARNING, ex.getMessage() + " -- " + sql ) ;
        }
        return res ;
    }

    public CallMe getById( String id ) {
        return getById( id, false ) ;
    }
    public CallMe getById( String id, boolean includeDeleted ) {
        String sql = "SELECT * FROM " + dbPrefix + "call_me WHERE id = ?" ;
        if( ! includeDeleted ) {
            sql += " AND delete_moment IS NULL" ;
        }
        try( PreparedStatement prep = dbProvider.getConnection().prepareStatement(sql) ) {
            prep.setString(1, id ) ;
            ResultSet resultSet = prep.executeQuery() ;
            if( resultSet.next() ) {
                return new CallMe( resultSet ) ;
            }
        }
        catch( Exception ex ) {
            logger.log( Level.WARNING, ex.getMessage() + " -- " + sql ) ;
        }
        return null ;
    }

    public boolean setCallMoment( CallMe item ) {
        String sql = "SELECT CURRENT_TIMESTAMP" ;
        Timestamp timestamp ;
        try( Statement statement = dbProvider.getConnection().createStatement() ) {
            ResultSet resultSet = statement.executeQuery( sql ) ;
            resultSet.next() ;
            timestamp = resultSet.getTimestamp(1) ;
            item.setCallMoment( new Date( timestamp.getTime() ) ) ;
        }
        catch( Exception ex ) {
            logger.log( Level.WARNING, ex.getMessage() + " -- " + sql ) ;
            return false ;
        }
        sql = "UPDATE " + dbPrefix + "call_me SET call_moment = ? WHERE id = ?";
        try( PreparedStatement prep = dbProvider.getConnection().prepareStatement(sql) ) {
            prep.setTimestamp(1, timestamp ) ;
            prep.setString(2, item.getId() ) ;
            prep.execute() ;
            return true ;
        }
        catch( Exception ex ) {
            logger.log( Level.WARNING, ex.getMessage() + " -- " + sql );
        }
        return false ;
    }

    public boolean delete( CallMe item ) {
        return delete( item, false ) ;
    }
    public boolean delete( CallMe item, boolean hardDelete ) {
        if( item == null || item.getId() == null ) {
            return false ;
        }
        String sql = hardDelete
            ? "DELETE FROM " + dbPrefix + "call_me WHERE id = ?"
            : "UPDATE " + dbPrefix + "call_me SET delete_moment = CURRENT_TIMESTAMP WHERE id = ?";

        try( PreparedStatement prep = dbProvider.getConnection().prepareStatement(sql) ) {
            prep.setString(1, item.getId() ) ;
            prep.executeUpdate();
            return true;
        }
        catch( Exception ex ) {
            logger.log(Level.WARNING, ex.getMessage() + " -- " + sql );
        }
        return false ;
    }
}
/*
DAO - Data Access Object - об'єкти ORM, які утворюють шар DAL (Layer)
і переводять операції з даними до операцій з об'єктами мови.
Сукупність таких об'єктів також називають контекстом даних
 */