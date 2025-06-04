package step.learning.ioc;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import step.learning.ioc.services.hash.HashService;
import step.learning.ioc.services.random.RandomService;

public class IocDemo2 {
    // Іменовані залежності дозволяють інжектувати декілька реалізацій одного інтерфейсу (типу)
    // @Inject @Named("Digest-hash")    // інжекція через поля - не найкращий варіант
    // private HashService digestHashService ;   // оскільки впроваджується як змінна,
    // @Inject @Named("Signature-hash")    // яку можна змінити (навмисно чи випадково)
    // private HashService signatureHashService ;

    private final HashService digestHashService ;
    private final HashService signatureHashService ;
    private final RandomService randomService ;

    @Inject  // інжекційний конструктор
    public IocDemo2(
            @Named("Digest-hash") HashService digestHashService,
            @Named("Signature-hash") HashService signatureHashService, RandomService randomService) {
        this.digestHashService = digestHashService;
        this.signatureHashService = signatureHashService;
        this.randomService = randomService;
    }
    // Чи можливий змішаний варіант? Як конструктор, так і поля?
    @Inject @Named("Digest-hash")  // Можливий, кожен @Inject - це нова точка інжекції і для
    private HashService digestHashService2 ;  // неї знову "запускається" інжектор
    // за замовчанням - це transient (новий об'єкт у кожній точці)
    // для зміни - зазначити @Singleton на реалізації служби (не на інтерфейсі)

    public void run() {
        System.out.println( "IoC Demo" ) ;
        System.out.println( digestHashService.hash( "IoC Demo" ) ) ;
        System.out.println( signatureHashService.hash( "IoC Demo" ) ) ;
        System.out.println( digestHashService2.hash( "IoC Demo" ) ) ;
        System.out.println( digestHashService.hashCode() + " " + digestHashService2.hashCode() ) ;

        System.out.println( randomService.randomHex( 6 ) ) ;
    }
}
