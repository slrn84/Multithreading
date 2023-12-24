package ru.courses2.multithreading;

import java.lang.reflect.Proxy;

public class Utils<T> {
    public static <T> T cache(T t) {
        T t1 = (T) Proxy.newProxyInstance(t.getClass().getClassLoader(),
                t.getClass().getInterfaces(),
                new CacheHandler(t));
        return t1;
    }

}