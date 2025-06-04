package step.learning.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Names;
import step.learning.ioc.services.hash.HashService;
import step.learning.ioc.services.hash.Md5HashService;
import step.learning.ioc.services.hash.Sha1HashService;
import step.learning.ioc.services.random.RandomService;
import step.learning.ioc.services.random.RandomServiceV1;

public class ConfigModule extends AbstractModule {
    @Override
    protected void configure() {
        // основний метод для налаштування інжектора
        // bind( HashService.class ).to( Md5HashService.class ) ;
        // bind( HashService.class ).to( Sha1HashService.class ) ;

        // іменовані залежності
        bind( HashService.class )
                .annotatedWith( Names.named( "Digest-hash" ) )
                .to( Md5HashService.class ) ;
        bind( HashService.class )
                .annotatedWith( Names.named( "Signature-hash" ) )
                .to( Sha1HashService.class ) ;
    }

    /*
    Провайдери - методи інжектора, які дають більш гнучкі можливості
    управління впровадженням залежностей.
    Методи зв'язуться за типами (повернення), їх назва - не грає ролі,
    вони викликаються автоматично для кожної точки інжекції, їх результат
    (повернення) встановлюється як запитана залежність.
     */
    private RandomService randomService ;
    @Provides
    private RandomService injectRandomService() {
        if( randomService == null ) {
            randomService = new RandomServiceV1() ;
            randomService.seed( "initial" ) ;
        }
        return randomService ;
    }
}
/*
Додати реалізацію RandomService, використати масив char[] замість
StringBuilder. Впровадити дві іменовані залежності через  @Provides.
Навести скриншоти їх роботи.
 */