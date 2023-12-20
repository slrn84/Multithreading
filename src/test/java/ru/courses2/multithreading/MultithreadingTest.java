package ru.courses2.multithreading;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MultithreadingTest {
    @Test
    void stepTest () throws InterruptedException {

        Fraction fr= new Fraction(2,3);
        Fractionable num =Utils.cache(fr);
        num.doubleValue();// sout сработал
        num.doubleValue();// sout молчит
        num.setNum(5);
        num.doubleValue();// sout сработал
        num.doubleValue();// sout молчит
        num.setNum(2);
        num.doubleValue();// sout молчит
        num.doubleValue();// sout молчит

        System.out.println("ждем...");
        Thread.sleep(5000);
        num.doubleValue();// sout сработал
        num.doubleValue();// sout молчит

    }

}
