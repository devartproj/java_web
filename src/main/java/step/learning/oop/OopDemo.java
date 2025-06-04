package step.learning.oop;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public class OopDemo {
    public void run() {
        Armory armory = new Armory() ;
        armory.load() ;
        armory.printAll() ;
    }
    public void run3() {
        // доступ до ресурсів, без-типова робота з JSON
        String resourceName = "colt.json" ;
        try( InputStreamReader reader =              // "обгортка" для роботи з JsonParser
                new InputStreamReader(               // звернення до ресурсу з перевіркою
                    Objects.requireNonNull(          // його на null
                        this.getClass()
                                .getClassLoader()
                                .getResourceAsStream( resourceName )
        ) ) ) {
            // узагальнена робота з JSON - використання JsonElement, які схожі на Map<K,V>
            JsonObject jsonObject = JsonParser.parseReader( reader ).getAsJsonObject() ;
            // уявимо, що нам невідомо про те, який саме об'єкт є серіалізованим,
            // визначити це треба по наявності відмінних полей Cartridge/FireRate/Caliber
            Weapon weapon = null ;
            if( jsonObject.has( "cartridge" ) ) {  // Gun
                weapon = new Gun(
                        jsonObject.get( "name" ).getAsString(),
                        jsonObject.get( "cartridge" ).getAsInt()
                ) ;
            }
            else if( jsonObject.has( "fireRate" ) ) {
                weapon = new MachineGun(
                        jsonObject.get( "name" ).getAsString(),
                        jsonObject.get( "fireRate" ).getAsDouble()
                ) ;
            }
            else if( jsonObject.has( "caliber" ) ) {
                weapon = new Rifle(
                        jsonObject.get( "name" ).getAsString(),
                        jsonObject.get( "Caliber" ).getAsFloat()
                ) ;
            }
            else {
                System.err.println( "Weapon type unrecognized" ) ;
            }
            if( weapon != null ) {
                System.out.println( weapon.getCard() ) ;
            }
        }
        catch( IOException ex ) {
            System.err.println( "IO error: " + ex.getMessage() ) ;
        }
        catch( NullPointerException ignored ) {
            System.err.printf( "Resource '%s' not found %n", resourceName ) ;
        }

    }
    public void run2() {
        // Gson - бібліотека класів для роботи з JSON
        String jsonString = "{\"name\":\"Colt Defender\",\"cartridge\":8}";
        Gson gson = new Gson();
        Gun gun = gson.fromJson( jsonString, Gun.class ) ;  // Gun.class ~ typeof(Gun)
        System.out.println( gun.getCard() ) ;
        // за замовчанням .toJson - оптимізований рядок (з мін. кількістю символів)
        // та без полів, що мають значення null
        System.out.println( gson.toJson( gun ) ) ;
        // для налаштування серіалізатора використовується GsonBuilder()
        Gson gson2 = new GsonBuilder()
                        .setPrettyPrinting()  // додавання пробілів та розривів
                        .serializeNulls()     // включення полів із null
                        .setDateFormat("yyyy-MM-dd")
                        .create() ;
        System.out.println( gson2.toJson( gun ) ) ;
    }
    public void run1() {
        Armory armory = new Armory() ;
        armory.add( new Gun( "Colt Defender", 8 ) ) ;
        armory.add( new MachineGun( "M249 SAW", 8.5 ) ) ;
        armory.add( new Rifle( "Mauser 98k", 	7.92f ) ) ;
        armory.add( new Rifle("35M rifle", 7.92f ) ) ;
        armory.add( new MachineGun("Breda 30", 6.5 ) ) ;
        armory.add( new Gun("Glock 19", 17 ) ) ;
        armory.add( new MachineGun("ДШК", 600 ) ) ;
        armory.printAll() ;
        System.out.println("---------------AUTOMATIC------------------");
        armory.printAutomatic();
        System.out.println("---------------NON AUTOMATIC------------------");
        armory.printNonAutomatic();
        System.out.println("---------------CLASSIFIED------------------");
        armory.printClassified();

        armory.save();
    }
}
/*
Робота з файлами, необхідними у проєкті. Особливості:
Зкомпільований проєкт знаходиться у папці target, яка є кінцевим рез-том.
Для того щоб файли проєкту копіювались у збірку (у папку target)
необхідно розмістити у спеціальній папці - resources (src/main/resources)
Всі файли з цієї папки копіюються у target/classes, тобто у prod
Звернутись до таких файлів можна за допомогою classLoader, який визначає
місце, з якого беруться класи.
 */
/*
Бібліотеки класів - набір зкомпільованих класів, які здатні виконуватись
(платформою). У C# - це .DLL файли, у Java - .JAR (Java Archive)
Підключення бібліотек до проєкту
 а) завантаження .JAR файлу та включення його до команд компіляції/запуску
 б) використання системи управління пакетами (у складі систем збирання)
Наш проєкт - на системі Maven, відповідно, краще варіант б)
Додаткові бібліотеки зазначаються у файлі pom.xml у розділі <dependencies>
Самі бібліотеки беруться з репозиторіїв, наприклад, https://mvnrepository.com/
На сайті здійснюємо пошук (або за назвою, або за призначенням),
обираємо версію та копіюємо залежність, на зразок
    <!-- https://mvnrepository.com/artifact/com.google.code.gson/gson -->
    <dependency>
        <groupId>com.google.code.gson</groupId>
        <artifactId>gson</artifactId>
        <version>2.10.1</version>
    </dependency>
Вставляємо цю директиву у розділ <dependencies> у файлі pom.xml
!! оновлюємо залежності - натискаємо відповідну кнопку, що з'являється
   при змінах у цьому файлі.
 */
/*
Об'єктно-орієнтована парадигма програмування
Сутність: система подається у вигляді об'єктів та їх взаємодії

Поліморфізм: один інтерфейс, багато реалізацій, але із спільними ознаками
Gun { Name, Cartridge }              | Weapon - абстрагування
Machine Gun { Name, FireRate }       | Weapon
Rifle { Name, Caliber }              | Weapon
....
        Armory -----<> Weapon { Name }  *code, *num, *date, *price
                        / | \
      Gun { Cartridge }   |  MachineGun { FireRate }
                  Rifle { Caliber }

String getCard() - облікова картка: назва, тип, інші хар-ки
Д.З. Встановити склад класу Armory,
описати клас, реалізувати необхідні поля та методи
 */