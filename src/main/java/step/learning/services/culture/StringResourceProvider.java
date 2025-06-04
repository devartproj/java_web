package step.learning.services.culture;

import com.google.inject.Singleton;

@Singleton
public class StringResourceProvider implements ResourceProvider {
    private String defaultCulture ;

    @Override
    public String getString(String name, String culture) {
        switch( name ) {
            case "signup_login_too_short" :
                switch (culture) {
                    default: return "Логін занадто короткий, треба 2 чи більше";
                }
            case "signup_login_empty" :
                switch (culture) {
                    default: return "Логін не може бути порожнім";
                }
            case "signup_login_pattern_mismatch" :
                switch (culture) {
                    default: return "Логін містить недозволені символи (дозволяються лише літери та цифри)";
                }
        }
        return null ;
    }

    @Override
    public String getString(String name) {
        return getString(name, defaultCulture);
    }

    @Override
    public void setCulture(String culture) {
        defaultCulture = culture ;
    }

    @Override
    public String getCulture() {
        return defaultCulture;
    }
}
