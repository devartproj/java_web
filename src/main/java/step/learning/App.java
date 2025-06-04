package step.learning;

import com.google.inject.Guice;
import com.google.inject.Injector;
import step.learning.async.AsyncDemo;
import step.learning.basics.BasicsDemo;
import step.learning.files.DirDemo;
import step.learning.files.FileIoDemo;
import step.learning.ioc.ConfigModule;
import step.learning.ioc.IocDemo;
import step.learning.ioc.IocDemo2;
import step.learning.oop.OopDemo;

public class App
{
    public static void main( String[] args )
    {
        // new BasicsDemo().run() ;
        // new DirDemo().run() ;
        // new FileIoDemo().run() ;
        // new OopDemo().run() ;
        // Injector injector = Guice.createInjector( new ConfigModule() ) ;
        // IocDemo iocDemo = injector.getInstance( IocDemo.class ) ;   // замість new IocDemo()
        // iocDemo.run() ;
        // Guice.createInjector( new ConfigModule() ).getInstance( IocDemo2.class ).run() ;
        Guice.createInjector( new ConfigModule() ).getInstance( AsyncDemo.class ).run() ;
    }
}
/*
Встановлення
JRE/JVM - середовище запуску (може бути наявне)
JDK - засоби розроблення (компілятор, бібліотеки)
IDE - JetBrains Idea, Apache NetBeans, Eclipse, VS Code (+plugin)

Характеристики
Покоління: 4GL
Парадигма: ООП (класична)
Тип: транслятор (компіляція у проміжний код)
Вихідний код: текст.java
Виконавчий код: .class
Особливості: прив'язка до файлової системи -
 пакети відповідають за структуру папок (імена мають збігатись)
 класи відповідають за файли: один файл - один клас
 традиції:
  class: CapitalCamelCase
  var/method: lowerCamelCase
  package: small.case
  const: UPPER_CASE
 */
