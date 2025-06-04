package step.learning.files;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Демонстрація роботи з файлами та папками як з об'єктами файлової системи
 */
public class DirDemo {
    public void run() {
        System.out.println( "Directories demo" ) ;
        String path = "./" ;
        File dir = new File( path ) ;  // File - основа роботи з файлами, створення
        // нового File не чинить ніякого впливу на файлову систему, лише створює Java об'єкт,
        // який відповідає за вказаний шлях
        if( dir.exists() ) {   // перевірка існування
            System.out.printf( "Object '%s' does exist @ real path '%s' and it is %s %n",
                    path,
                    dir.getAbsolutePath(),
                    dir.isFile() ? "file" : "directory"
            ) ;
            if( dir.isDirectory() ) {  // вивести вміст директорії
                try {
                    for( String name : dir.list() ) {  // лише імена
                        System.out.printf("%s ", name);
                    }
                    System.out.println();
                    for( File f : dir.listFiles() ) {  // перебір як файлів
                        System.out.printf( "%s ", f.getName() ) ;
                    }
                    System.out.println();
                }
                catch( NullPointerException ignored ) {
                    System.err.println( "Iteration Exception" ) ;
                }
/* Д.З. Реалізувати виведення вмісту директорії у стилі команди "dir"("ls")
Mode          LastWriteTime     Length   Name
----          -------------     ------   ----
d-----     21.09.2023  14:43             .idea
d-----     20.09.2023  12:32             src
d-----     20.09.2023  12:42             target
d-----     21.09.2023  14:12             upload
-a----     20.09.2023  12:32       490   .gitignore
-a----     20.09.2023  15:43      1093   pom.xml
 */
            }
        }
        else {
            System.out.printf( "Object '%s' does not exist%n", path ) ;
        }
        String subPath = "./upload";
        File subDir = new File( subPath ) ;
        if( subDir.isDirectory() ) {
            System.out.printf( "Dir '%s' already exists%n", subPath ) ;
        }
        else {
            boolean needCreate = false;
            if( subDir.exists() ) {
                System.out.printf( "Object '%s' does exist, BUT NOT A DIR %n", subPath ) ;
                // Задача: вивести повідомлення "Delete object? (y/n) __"
                // обробити відповідь - якщо Y, то видаляємо та створюємо, інакше - ні
                System.out.print( "Delete object? (y/...) " ) ;
                Scanner scanner = new Scanner( System.in ) ;
                String answer = scanner.nextLine() ;
                if( "y".equalsIgnoreCase( answer ) ) {
                    System.out.print( "Deleting... " ) ;
                    if( subDir.delete() ) {
                        System.out.println( "done" );
                        needCreate =  true ;
                    }
                    else {
                        System.out.println( "error" );
                    }
                }
            }
            else {
                System.out.printf( "Dir '%s' does not exist%n", subPath ) ;
                needCreate =  true ;
            }
            if( needCreate ) {
                System.out.print( "Creating... " ) ;
                if( subDir.mkdir() ) {
                    System.out.println( "Done" ) ;
                }
                else {
                    System.out.println( "Creation error" ) ;
                }
            }
        }

        String logPath = subPath + File.separator + "actions.log" ;
        File logFile = new File( logPath ) ;
        if( logFile.isFile() ) {
            System.out.printf( "File '%s' already exists%n", logPath ) ;
        }
        else {
            if( logFile.exists() ) {
                System.out.printf( "Object '%s' does exist, BUT NOT A FILE %n", logPath ) ;
            }
            else {
                try {
                    if( logFile.createNewFile() ) {
                        System.out.println( "File created" ) ;
                    }
                    else {
                        System.err.println( "File creation error" ) ;
                    }
                }
                catch( IOException ex ) {
                    System.err.println( ex.getMessage() ) ;
                }
            }
        }
    }
}
/*
Робота з файлами поділяється на дві групи задач:
- робота з файловою системою: пошук, навігація по папках, створення, знищення файлів/папок
- використання файлів для збереження/відновлення даних
 */