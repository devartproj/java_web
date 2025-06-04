package step.learning.dto.models;

import org.apache.commons.fileupload.FileItem;
import step.learning.services.formparse.FormParseResult;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class SignupFormModel {
    private static final SimpleDateFormat formDateFormat =
            new SimpleDateFormat( "yyyy-MM-dd" ) ;

    public SignupFormModel( FormParseResult formParseResult ) throws ParseException {
        Map<String, String> fields = formParseResult.getFields() ;
        this.setLogin( fields.get( "reg-login" ) ) ;
        this.setName( fields.get( "reg-name" ) ) ;
        this.setPassword( fields.get( "reg-password" ) ) ;
        this.setRepeat( fields.get( "reg-repeat" ) ) ;
        this.setEmail( fields.get( "reg-email" ) ) ;
        this.setAgree( fields.get( "reg-agree" ) ) ;
        this.setBirthdate( fields.get( "reg-birthdate" ) ) ;

        this.setAvatar( formParseResult ) ;
    }

    /**
     * Валідація кожного з полів та формування повідомлень про помилки.
     * Порожня відповідь означає успішну валідацію
     * @return словник "ім'я поля" - "повідомлення про помилку"
     */
    public Map<String, String> getValidationErrorMessages() {
        Map<String, String> result = new HashMap<>() ;
        if( login == null || login.isEmpty() ) {
            result.put( "login", "signup_login_empty" ) ;
        }
        else if( login.length() < 2 ) {
            result.put( "login", "signup_login_too_short" ) ;
        }
        else if( ! Pattern.matches( "^[a-zA-Z0-9_-]+$", login ) ) {
            result.put( "login", "signup_login_pattern_mismatch" ) ;
        }

        return result ;
    }

    // region fields
    private String login ;
    private String name ;
    private String password ;
    private String repeat ;
    private String email ;
    private Date birthdate ;
    private Boolean isAgree ;
    private String avatar ;  // filename or URL
    // endregion

    // region accessors
    public void setAvatar( FormParseResult formParseResult ) {  // завантажує та встановлює ім'я
        Map<String, FileItem> files = formParseResult.getFiles() ;
        if( ! files.containsKey( "reg-avatar" ) ) {
            this.avatar = null ;
            return;
        }
        FileItem fileItem = files.get( "reg-avatar" ) ;
        String uploadedFilename = fileItem.getName() ;
        int dotIndex = uploadedFilename.lastIndexOf('.');
        String ext = uploadedFilename.substring( dotIndex );
        String[] extensions = {".jpg", ".jpeg", ".png", ".ico", ".gif"}; // valid extensions
        if( ! Arrays.asList(extensions).contains(ext) ) {
            // throw exception if extension is invalid
            throw new RuntimeException( "Invalid file extension" ) ;
        }
        // визначаємо директорію збереження файлу
        String uploadDir = formParseResult.getRequest()
                .getServletContext()  // контекст - оточення сервлету (з відомостями про реальне розміщення)
                .getRealPath("./upload/avatar/");

        // генеруємо випадкове ім'я для збереження файлу
        String savedFilename ;
        File savedFile ;
        do {
            savedFilename = UUID.randomUUID().toString().substring(0, 8) + ext ;
            savedFile = new File( uploadDir, savedFilename ) ;
        } while( savedFile.exists() ) ;

        try {
            fileItem.write( savedFile ) ;
        }
        catch( Exception ex ) {
            throw new RuntimeException( ex );
        }
        this.setAvatar( savedFilename ) ;
    }
    public void setBirthdate( String birthdate ) throws ParseException {
        if( birthdate == null || birthdate.isEmpty() ) {
            this.birthdate = null ;
        }
        else {
            this.setBirthdate( formDateFormat.parse( birthdate ) ) ;
        }
    }
    public void setAgree( String input ) {
        this.setAgree(
            "on".equalsIgnoreCase( input ) || "true".equalsIgnoreCase( input )
        ) ;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public Boolean getAgree() {
        return isAgree;
    }

    public void setAgree(Boolean agree) {
        isAgree = agree;
    }

    // endregion
}
