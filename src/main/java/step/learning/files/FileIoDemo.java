package step.learning.files;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.Scanner;

/**
 * Демонстрація роботи з файлами для збереження даних
 */
public class FileIoDemo {
    public void run() {
        String filename = "test.txt" ;
        try( OutputStream writer = new FileOutputStream( filename, false ) ) {
            writer.write( "Hello, world!".getBytes() ) ;
        }
        catch( IOException ex ) {
            System.err.println( ex.getMessage() ) ;
        }

        try( FileWriter writer = new FileWriter( filename, true ) ) {
            writer.write( "\r\nNew Line" ) ;
        }
        catch( IOException ex ) {
            System.err.println( ex.getMessage() ) ;
        }

        StringBuilder sb = new StringBuilder() ;
        try( InputStream reader = new FileInputStream( filename ) ) {
            int c ;
            while( ( c = reader.read() ) != -1 ) {
                sb.append( (char) c ) ;
            }
            System.out.println( sb.toString() ) ;
        }
        catch( IOException ex ) {
            System.err.println( ex.getMessage() ) ;
        }

        System.out.println( "-----------------------------------------" ) ;
        ByteArrayOutputStream byteBuilder = new ByteArrayOutputStream() ;
        byte[] buf = new byte[1024] ;
        try( BufferedInputStream reader = new BufferedInputStream(
                                            new FileInputStream( filename ) ) ) {
            int cnt ;
            while( ( cnt = reader.read( buf ) ) > 0 ) {
                byteBuilder.write( buf, 0, cnt ) ;
            }
            String content = new String(
                    byteBuilder.toByteArray(),
                    StandardCharsets.UTF_8
            ) ;
            System.out.println( content ) ;
        }
        catch( IOException ex ) {
            System.err.println( ex.getMessage() ) ;
        }

        System.out.println( "-----------------------------------------" ) ;
        try( InputStream stream = new FileInputStream( filename ) ;
             Scanner scanner = new Scanner( stream ) ) {
            while( scanner.hasNext() ) {
                System.out.println( scanner.nextLine() ) ;
            }
        }
        catch( IOException ex ) {
            System.err.println( ex.getMessage() ) ;
        }

        // Задача: створити файл "lines.txt" з випадковою кількістю (від 20 до 100)
        // рядків, кожен з який має випадову кількість символів (від 10 до 100)
        // самі символи також випадкові (коди від 20 до 127)
        StringBuilder builder = new StringBuilder() ;
        Random random = new Random();
        int nLines = random.nextInt(81) + 20 ;
        for (int i = 0; i < nLines; i++) {
            int nChars = random.nextInt(91) + 10 ;
            for (int j = 0; j < nChars; j++) {
                builder.append( (char)(random.nextInt(108) + 20) ) ;
            }
            builder.append("\r\n");
        }
        System.out.println(builder);
        // Визначити у файлі "lines.txt" найдовший рядок (вивести його номер, довжину та контент)
    }
}
/*
Робота з вмістом файлів відбувається у формалізмі Stream
У базових можливостях:
 - читання/запис одного байту
 - читання/запис масиву байт
 - відкриття/закриття
Для розширення базових можливостей застосовуються різноманітні
"обгортки", які спрощують роботу з даними

 потоки - некеровані ресурси, замість окремого блоку (~using)
 використовується try-with-resource
 */