package step.learning.oop;

import com.google.gson.*;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Armory {
    private final List<Weapon> weapons;

    public Armory() {
        weapons = new ArrayList<>();
    }

    /**
     * Відбір тих елементів, які анотовані @Serializable
     */
    private List<Weapon> getSerializableWeapons() {
        List<Weapon> result = new LinkedList<>() ;
        for(Weapon weapon : weapons ) {
            if( weapon.getClass().isAnnotationPresent( Serializable.class ) ) {
                result.add( weapon ) ;
            }
        }
        return result ;
    }

    /**
     * Зберігає усі наявні у колекції зразки до файлу armory.json
     */
    public void save() {
        // Визначаємо шлях до робочої папки
        String path = URLDecoder.decode(  // позбутись закодованих символів %20
            this.getClass()
                    .getClassLoader()
                    .getResource( "./" )
                    .getPath() ) ;
        // System.out.println( path ) ;  // /C:/Users/_dns_/source/repos/Java-KN-P-202/target/classes/
        // Відкриваємо файл (див. вправи "файли") і серіалізуємо у нього колекцію
        try( FileWriter writer = new FileWriter( path + "armory.json" ) ) {
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()  // додавання пробілів та розривів
                    .serializeNulls()     // включення полів із null
                    .setDateFormat("yyyy-MM-dd")
                    .create() ;
            writer.write( gson.toJson( this.getSerializableWeapons() ) ) ;
        }
        catch( IOException ex ) {
            throw new RuntimeException( ex ) ;
        }
    }

    private List<Class<?>> findSerializableClasses() {
        List<Class<?>> weaponClasses = new ArrayList<>() ;
        /* Ідея - у відповідності імен класів та імен файлів. Необхідно дістатись
        папки з класами (шлях до якої - назва пакету), перебрати всі файли,
        вилучити з них класи та перевірити анотації.
        Чи сканувати окрему папку, чи перебирати всі - за вимогами проєкту,
        будемо вважати, що усі класи знаходяться в одній папці - оор. */
        // Обираємо клас, який гарантовано знаходиться у потрібній папці
        String armoryName = Armory.class.getName() ;  // step.learning.oop.Armory
        // Визначаємо пакет - видаляємо все після останньої "."
        String packageName =   // step.learning.oop.
                armoryName.substring( 0, armoryName.lastIndexOf('.') + 1 ) ;
        // замінюємо точки на "/" - файлові роздільники
        String packagePath =   // step/learning/oop/
                packageName.replace( '.', '/' ) ;
        // звертаємось до завантажувача класів, передаємо даний шлях
        URL resourceUrl = Armory.class.getClassLoader().getResource( packagePath ) ;
        if( resourceUrl == null ) {
            throw new RuntimeException( String.format( "Package '%s' got no resource", packagePath ) ) ;
        }
        String resourcePath = resourceUrl.getPath() ;  // /C:/Users/_dns_/source/repos/Java-KN-P-202/target/classes/step/learning/oop/
        // позбавляємось URL-кодованих символів
        try { resourcePath = URLDecoder.decode( resourcePath, "UTF-8" ) ; }
        catch( UnsupportedEncodingException ignored ) { }
        // звертаємось до папки як до файлу (див. вправи "файли")
        File resourceDirectory = new File( resourcePath ) ;
        // та перебираємо її вміст
        File[] files = resourceDirectory.listFiles() ;
        if( files == null ) {
            throw new RuntimeException(
                    String.format( "Directory '%s' got no file list", resourceDirectory ) ) ;
        }
        for( File file : files ) {
            if( file.isDirectory() ) {
                // за потреби сканувати підкаталоги - переходимо у нього і повторюємо сканування
                continue ;
            }
            else if( file.isFile() ) {
                String filename = file.getName() ;
                // перевіряємо чи це файл класу (такі файли закінчуються на ".class")
                if (filename.endsWith(".class")) {   // Gun.class
                    // формуємо назву класа - назва пакету + назва файлу (без .class)
                    String className = packageName +   // step.learning.oop.Gun
                            filename.substring(0, filename.lastIndexOf('.'));
                    try {
                        // будуємо тип з наявного класу (за іменем)
                        Class<?> classType = Class.forName( className ) ;
                        // перевіряємо в ньому наявність анотації та сумісність з Weapon
                        if( classType.isAnnotationPresent( Serializable.class )
                                && Weapon.class.isAssignableFrom( classType ) ) {
                            // якщо це так, то додаємо тип до переліку серіалізованих
                            weaponClasses.add( classType ) ;
                        }
                    }
                    catch( ClassNotFoundException ignored ) {
                        System.err.printf( "Class '%s' not accessible %n", className ) ;
                    }
                }
            }
        }
        return weaponClasses ;
    }

    /**
     * Зчитує колекцію зброї зі збереженого файлу armory.json
     */
    public void load() throws RuntimeException {
        String resourceName = "armory.json" ;
        // Class<?>[] weaponClasses = { Gun.class, MachineGun.class, Rifle.class } ;
        try( InputStreamReader reader =
                     new InputStreamReader(
                             Objects.requireNonNull(
                                     this.getClass()
                                             .getClassLoader()
                                             .getResourceAsStream( resourceName )
         ) ) ) {
            JsonArray jsonArray = JsonParser.parseReader( reader ).getAsJsonArray() ;
            for( JsonElement jsonElement : jsonArray ) {
                JsonObject jsonObject = jsonElement.getAsJsonObject() ;
                Weapon weapon = null ;
                for( Class<?> weaponClass : findSerializableClasses() ) {
                    Method isParseableFromJson = null ;  // = weaponClass.getDeclaredMethod("isParseableFromJson", JsonObject.class ) ;
                    Method fromJson = null ;   // = weaponClass.getDeclaredMethod("fromJson", JsonObject.class ) ;
                    // скануємо всі методи класу, шукаємо той, що має анотацію @JsonParseCheck
                    for( Method method : weaponClass.getDeclaredMethods() ) {
                        if( method.isAnnotationPresent( JsonParseCheck.class ) ) {
                            if( isParseableFromJson != null ) {
                                // Знайдено другий метод з тією ж анотацією
                                throw new RuntimeException(  String.format(
                                        "Multiple methods with @%s annotation in %s class",
                                        JsonParseCheck.class.getName(),
                                        weaponClass.getName()
                                ) ) ;
                            }
                            isParseableFromJson = method ;
                        }
                        if( method.isAnnotationPresent( JsonFactory.class ) ) {
                            if( fromJson != null ) {
                                // Знайдено другий метод з тією ж анотацією
                                throw new RuntimeException(  String.format(
                                        "Multiple methods with @%s annotation in %s class",
                                        JsonFactory.class.getName(),
                                        weaponClass.getName()
                                ) ) ;
                            }
                            fromJson = method ;
                        }
                    }
                    if( isParseableFromJson == null || fromJson == null ) {   // якщо у класі немає якогось методу,
                        continue ;   // то ігноруємо його і переходимо до наступного
                    }
                    isParseableFromJson.setAccessible( true ) ;
                    boolean res = (boolean) isParseableFromJson.invoke( null, jsonObject ) ;
                    if( res ) {
                        fromJson.setAccessible( true ) ;
                        weapon = (Weapon) fromJson.invoke( null, jsonObject ) ;
                    }
                }
                if( weapon != null ) {
                    this.weapons.add( weapon ) ;
                }
                else {
                    System.err.println( "Weapon type unrecognized - skipped" ) ;
                }
            }
        }
        catch( IllegalAccessException | InvocationTargetException ex ) {
            throw new RuntimeException( "Reflection error: " + ex.getMessage() ) ;
        }
        catch( IOException ex ) {
            throw new RuntimeException( "IO error: " + ex.getMessage() ) ;
        }
        catch( NullPointerException ignored ) {
            throw new RuntimeException( String.format(
                        "Resource '%s' not found %n", resourceName ) ) ;
        }
        catch( IllegalArgumentException ex ) {
            throw new RuntimeException( "JSON parse error: " + ex.getMessage() ) ;
        }
    }
    public void add( Weapon weapon ) {
        weapons.add( weapon ) ;
    }

    public void remove( Weapon weapon ) {
        weapons.remove( weapon ) ;
    }

    public void printAll() {
        for( Weapon weapon : weapons ) {
            System.out.println( weapon.getCard() ) ;
            // System.out.println( weapon.getClass().getName() ) ;
        }
    }

    public void printAutomatic() {
        for( Weapon weapon : weapons ) {
            if( isAutomatic( weapon ) ) {
                System.out.println( weapon.getCard() ) ;
            }
        }
    }
    public void printNonAutomatic() {
        for( Weapon weapon : weapons ) {
            if( ! isAutomatic( weapon ) ) {
                System.out.println( weapon.getCard() ) ;
            }
        }
    }
    public boolean isAutomatic( Weapon weapon ) {
        return ( weapon instanceof Automatic ) ;
    }
    public void printClassified() {
        for( Weapon weapon : weapons ) {
            if( isClassified( weapon ) ) {
                Classified weaponAsClassified = (Classified) weapon ;
                System.out.println(
                        weaponAsClassified.getLevel() + " " +
                        weapon.getCard() ) ;
            }
        }
    }
    public boolean isClassified( Weapon weapon ) {
        return ( weapon instanceof Classified ) ;
    }
}