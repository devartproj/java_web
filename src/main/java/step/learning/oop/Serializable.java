package step.learning.oop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)  // анотація використовується під час запуску
@Target(ElementType.TYPE)  // анотація "помічає" тип (клас)
public @interface Serializable {
    // анотація-маркер, без тіла
}
/*
Анотації - різновид інтерфейсів, але на відміну від них можуть маркувати не тільки типи (класи),
а й їх частини - поля, методи, конструктори, тощо
Також (на відміну від інтерфейсів) анотації можуть містити внутрішні дані, зберігаючи
певні метадані про маркований елемент.
 */
