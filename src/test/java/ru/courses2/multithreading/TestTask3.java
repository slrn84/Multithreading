package ru.courses2.multithreading;

import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.lang.reflect.*;

public class TestTask3 {
    @Test
    @DisplayName("Визуальная проверка работы функционала в целом")
    void visualTest () throws InterruptedException {
        Fraction fr= new Fraction(2,3);
        Fractionable num = Utils.cache((Fractionable)fr);
        num.doubleValue();// sout сработал
        num.doubleValue();// sout молчит
        num.setNum(5);
        num.doubleValue();// sout сработал
        num.doubleValue();// sout молчит
        num.setNum(2);
        num.doubleValue();// sout молчит
        num.doubleValue();// sout молчит
        System.out.println("ждем...");
        Thread.sleep(2500);
        num.doubleValue();// sout сработал
        num.doubleValue();// sout молчит
    }

    @Test
    @DisplayName("Проверка потокобезопасности")
    void threadSafetyTest() throws InterruptedException{
        Fraction fr= new Fraction(2,3);
        Fractionable num = Utils.cache((Fractionable)fr);

        for (int i = 1; i < 10000; i++) {
            num.setNum(i);
            num.doubleValue();
        }
        Thread.sleep(5000);

        try {
            InvocationHandler invocationHandler = Proxy.getInvocationHandler(num);
            Method method = invocationHandler.getClass().getDeclaredMethod("getCountTest");
            method.setAccessible(true);
            int x = (int) method.invoke(invocationHandler);
            Assert.assertTrue(x==0);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
