package step.learning.ioc;

import com.google.inject.Inject;
import step.learning.ioc.services.hash.HashService;
import step.learning.ioc.services.hash.Md5HashService;
import step.learning.ioc.services.hash.Md5OldHashService;

public class IocDemo {
    @Inject  // залежність від інтерфейсу потребує конфігурації (зв'язування)
    private HashService hashService ;  // інтерфейсу та реалізації
    @Inject   // Якщо зазначається залежність від реалізації, то конфігурація не потрібна
    private Md5HashService md5HashService ;   // інжектор сам знаходить потрібний тип
    @Inject
    private Md5OldHashService md5OldHashService ;

    public void run() {
        System.out.println( "IoC Demo" ) ;
        long t1, t2;
        String hash ;

        System.out.println( hashService.hash( "IoC Demo" ) ) ;
        hash =  md5HashService.hash( "IoC Demo" ) ;
        hash =  md5OldHashService.hash( "IoC Demo" ) ;

        t1 = System.nanoTime();
        hash =  md5HashService.hash( "IoC Demo" ) ;
        t2 = System.nanoTime() ;
        System.out.println( hash + " " + (t2 - t1) ) ;

        t1 = System.nanoTime();
        hash =  md5OldHashService.hash( "IoC Demo" ) ;
        t2 = System.nanoTime() ;
        System.out.println( hash + " " + (t2 - t1) ) ;
    }
}
/*
Inversion of Control - Інверсія управління
Архітектурний патерн - шаблон організації коду, згідно з яким відокремлюється
частина (модуль), що відповідає за задачі створення об'єктів, точніше, за
управління їх життєвим циклом.
Цей модуль часто називають інжектором або контейнером залежностей, за що принцип
також називають DI (Dependency Injection) [не плутати з DIP - Dependency
Inversion Principle]

Архітектура програми змінюється, процедура створення об'єктів поділяється на
декілька етапів:
 - створення та налаштування інжектора (контейнера)
 - резолюція (Resolve) - вирішення задачі порядку створення об'єктів та
    впровадження у них залежностей

Class1 { Hash = new() }                      Class1 { @Inject Hash }
Class2 { Hash = new() }              =====>  Class2 { @Inject Hash }
Class3 { Class2 = new() }                    Class3 { @Inject Class2 }
Class4 { Class3 = new(), Hash = new() }      Class4 { @Inject Class3, @Inject Hash }

Серед поширених систем ІоС для Java - Spring, Guice
На прикладі Guice
 - встановлюємо - через залежність Maven або через JAR
 - створюємо клас налаштувань інжектора - нащадок AbstractModule - ConfigModule
 - створюємо інжектор з даними налаштуваннями та через нього - об'єкт (див. Арр)
 - описуємо залежності (служби), додаємо їх до конфігуратора, та інжектуємо у класи
 */
