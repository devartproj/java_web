package step.learning.basics;

import java.util.*;

public class BasicsDemo {
    public void run() {
        // масиви та колекції
        System.out.println( "Java arrays" ) ;
        int[] arr1 = /* new int[] */ { 5, 4, 3, 2, 1 } ;
        for( int i = 0; i < arr1.length; i++ ) {
            System.out.print( arr1[i] + " " ) ;
        }
        System.out.println();

        for( int x : arr1 ) {  // foreach (JS for-of)
            System.out.print( x + " " ) ;
        }
        System.out.println();
                                     //
        int[][] arr2 = {             //  |1  2  3|   |1  2  3|   |30  24  18|
                { 1, 2, 3 },         //  |4  5  6| x |4  5  6| = |84  69  54|
                { 4, 5, 6 },         //  |7  8  9|   |7  8  9|   |138 114 90|
                { 7, 8, 9 }          //
        } ;                          //
        for( int[] row : arr2 ) {
            for( int x : row ) {
                System.out.printf("%d ", x);
            }
            System.out.println();
        }

        // Collections
        List<Integer> list = new LinkedList<>();  // List - interface, ArrayList - impl
        list.add(10);                             // ArrayList - базується на масиві
        list.add(20);                             // LinkedList - на зв'язаному списку
        list.add(30);
        list.add(40);
        list.add(50);
        list.add(60);
        for( int x : list ) {
            System.out.printf("%d ", x);
        }
        System.out.println();

        // Associative collection (Dictionary, Map)
        Map<String, String> headers = new HashMap<>() ;
        headers.put( "Host", "localhost" ) ;             // Map - interface
        headers.put( "Connection", "close" ) ;           // HashMap - дерево з ключем-хешем (не
        headers.put( "Content-Type", "text/html" ) ;     //    зберігається порядок додавання)
        headers.put( "Accept", "application/json" ) ;    // LinkedHashMap - зберігає порядок
        for( String key : headers.keySet() ) {
            System.out.printf( "%s: %s%n", key, headers.get(key) ) ;
        }
        /*
        Д.З. На базі колекцій реалізувати "словник" - користувач вводить слово,
        видається його переклад або повідомлення про відсутність слова.
        Перелік слів закласти статично, не оновлювати з клавіатури.

        Scanner kbScanner = new Scanner( System.in ) ;
        String word = kbScanner.next();
        System.out.println( word ) ;
         */
        Scanner kbScanner = new Scanner( System.in ) ;
        String word = kbScanner.next();
        System.out.println( word ) ;
    }

    public void run1() {
        System.out.println( "Java basics" ) ;
        // типи даних та змінні - всі типи даних Reference, за винятком Primitives
        byte  b = 10;              // Дані цілого типу - лише знакові,
        short s = 1000;            // беззнакових варіацій - немає
        int   i = 100000 + b;      // b. - немає методів, це не нащадки Object
        long  l = 100000000000L;   //
        // існують Reference-аналоги для цих типів
        Byte    rb = 10;                // Ці типи є упаковками (boxing)
        Short   rs = 1000;              // працюють за Value-семантикою,
        Integer ri = 1000000;           // але є Reference, що робить сумісними
        Long    rl = 100000000000000L;  // з іншими Reference-типами

        float  f = Float.parseFloat( "1e-3f" ) ;
        Double d = (double) s / 10;
        Float  rf = f;
        Double rd = d;
        System.out.printf( "d = %f (%d), rd = %f (%d)\n", d, d.hashCode(), rd, rd.hashCode() ) ;
        d *= 2 ;  // new Double
        System.out.printf( "d = %f (%d), rd = %f (%d)\n", d, d.hashCode(), rd, rd.hashCode() ) ;
        rd += 2 ;  // new Double
        System.out.printf( "d = %f (%d), rd = %f (%d)\n", d, d.hashCode(), rd, rd.hashCode() ) ;

        char c = 'c';  // Внутрішнє кодування UTF-16, двобайтові символи
        Character rc = c;

        boolean bool = true;
        Boolean rBool = bool;

        String str1 = "Hello" ;  // String pooling - кешування рядків
        String str2 = "Hello" ;  // Всі рядки "Hello" - суть один об'єкт
        String str3 = new String( "Hello" ) ;
        System.out.println( "str1 " + ( str1==str2 ? "==" : "!=" ) + " str2" ) ;
        System.out.println( "str1 " + ( str1==str3 ? "==" : "!=" ) + " str3" ) ;
        System.out.printf( "str1 = %s (%d), str2 = %s (%d), str3 = %s (%d)\n",
                str1, str1.hashCode(), str2, str2.hashCode(), str3, str3.hashCode() );
        /*
            !!! Оператор == діє як reference (рівність якщо це посилання на один об'єкт)
            !!! Перевантаження операторів у Java відсутнє
            Рядки (String) також порінюються як посилання, для порівняння контенту
            використовується метод .equals()
         */
        System.out.println( "str1 " + ( str1.equals(str3) ? "eq" : "!eq" ) + " str3" ) ;
    }
}
