package step.learning.oop;

import com.google.gson.JsonObject;

import java.util.Arrays;
import java.util.stream.Stream;

@Serializable
public class Gun extends Weapon implements Classified {
    @Required
    private int cartridge ;

    public Gun( String name, int cartridge ) {
        super.setName( name ) ;
        this.setCartridge( cartridge ) ;
    }

    public int getCartridge() {
        return cartridge;
    }

    public void setCartridge( int cartridge ) {
        this.cartridge = cartridge ;
    }

    @Override
    public String getCard() {
        return String.format(
                "Gun: '%s' (cartridge %d)",
                super.getName(),
                this.getCartridge()
        ) ;
    }

    @Override
    public String getLevel() {
        return "For civil";
    }

    @JsonParseCheck
    public static boolean isParseableFromJson( JsonObject jsonObject ) {
        return
            Stream.concat(
                Arrays.stream( Gun.class.getDeclaredFields() ),
                Arrays.stream( Gun.class.getSuperclass().getDeclaredFields() ) )
                    .filter( field -> field.isAnnotationPresent( Required.class ) )
                    .allMatch( field -> jsonObject.has( field.getName() ) ) ;
        /*
        Д.З. Реалізувати методи isParseableFromJson та fromJson зі скануванням
        полів класу на предмет наявності анотації Required.
        Порада: при першому звернені будувати масив/колекцію необхідних полів,
        при наступних викликах використовувати цю колекцію.
        * Реалізувати сканування усіх суперкласів, не лише найближчого
         */
    }

    @JsonFactory
    public static Gun fromJson( JsonObject jsonObject ) throws IllegalArgumentException {
        String[] requiredFields = { "name", "cartridge" } ;
        for( String field : requiredFields ) {
            if( ! jsonObject.has( field ) ) {
                throw new IllegalArgumentException( "Gun construct error: Missing required field: " + field ) ;
            }
        }
        return new Gun(
                jsonObject.get( requiredFields[0] ).getAsString(),
                jsonObject.get( requiredFields[1] ).getAsInt()
        ) ;
    }
}
/*
Спадкування (розширення).
Weapon (super) [ getCard(), name, getName() ]
Gun (this)     [  <>         <>     <>      | cartridge  getCartridge() ]

?? getName() чи треба додавати super. ?
- Чи спадковується приватне поле name?
   = це поле є частиною об'єкта gun (окремо у кожного) - за цією ознакою так, спадкується.
   = це поле не є доступним з методів об'єкту gun, за цією ознакою - ні
- Що буде, якщо буде оголошене поле та геттер у класі Gun?
Weapon (super) [ getCard(), name, getName() ]
Gun (this)     [<getCard()>  <>     <>      | cartridge,  getCartridge(), name, getName() ]
- Чи буде getName() заміняти батьківський?
   = ні, буде розширення базового класу, поля будуть додані, будуть існувати як
   super.getName() так і this.getName() і це будуть два різні незалежні методи, які
   працюють з різними, незалежними полями name (super та this)
- Який метод буде викликано інструкцією getName() (без префіксів)?
   = залежить від того, чи є this.getName()
   !! getName() (без префіксів) автоматично перемкнеться на this.getName() після
       його оголошення, але буде викликати super.getName(), якщо немає this.getName()
   !! Оскільки анотація  @Override не є обов'язковою, оголошення this.getName()
       може статись випадково
   !! Залишати виклики методів, які спадкуютья, без префіксів - не радиться.

- А така ситуація:
Weapon w = new Gun(...)
w.getName() - який метод викличе? (за наявності Gun.getName() та Weapon.getName())
C++ - можливі варіанти, у залежності від того, чи є virtual в оголошенні методу getName
Java/C# - без варіантів, всі методи віртуальні - виклик за об'єктом (Gun)
 */
