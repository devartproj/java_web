package step.learning.files.models;

import org.apache.commons.fileupload.FileItem;
import step.learning.services.formparse.FormParseResult;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SignupFormModel {
    private static final SimpleDateFormat formDateFormat =
            new SimpleDateFormat( "yyyy-MM-dd" ) ;

    public SignupFormModel( HttpServletRequest request ) throws ParseException {
        this.setLogin( request.getParameter( "reg-login" ) ) ;
        this.setName( request.getParameter( "reg-name" ) ) ;
        this.setPassword( request.getParameter( "reg-password" ) ) ;
        this.setRepeat( request.getParameter( "reg-repeat" ) ) ;
        this.setEmail( request.getParameter( "reg-email" ) ) ;
        this.setAgree( request.getParameter( "reg-agree" ) ) ;
        this.setBirthdate( request.getParameter( "reg-birthdate" ) ) ;
    }

    public SignupFormModel( FormParseResult formParseResult ) throws ParseException {
        Map<String, String> fields = formParseResult.getFields() ;
        this.setLogin( fields.get( "reg-login" ) ) ;
        this.setName( fields.get( "reg-name" ) ) ;
        this.setPassword( fields.get( "reg-password" ) ) ;
        this.setRepeat( fields.get( "reg-repeat" ) ) ;
        this.setEmail( fields.get( "reg-email" ) ) ;
        this.setAgree( fields.get( "reg-agree" ) ) ;
        this.setBirthdate( fields.get( "reg-birthdate" ) ) ;

        Map<String, FileItem> files = formParseResult.getFiles() ;
        if( files.containsKey( "reg-avatar" ) ) {
            this.setAvatar( files.get( "reg-avatar" ) ) ;
        }
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
    public void setAvatar( FileItem fileItem ) {  // завантажує та встановлює ім'я
        String uploadedFilename = fileItem.getName() ;
        /*
        Д.З. З імені файлу вилучити розширення (тип) та перевірити його
        на перелік дозволених (зображення). У разі невідповідності
        викидати виключення.
         */
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
