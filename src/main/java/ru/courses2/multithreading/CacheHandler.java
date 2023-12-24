package ru.courses2.multithreading;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@EqualsAndHashCode
@ToString
public class CacheHandler<T> implements InvocationHandler {
    private final T object;
    private CopyOnWriteArrayList<Save> cache = new CopyOnWriteArrayList();
    private long timeout = -1;

    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(4);

    private int countTest = 0;

    public CacheHandler(T object) {
        this.object = object;
        scheduledExecutorService.scheduleWithFixedDelay(this::clearCache, 0, 1, TimeUnit.SECONDS);
    }

    public int getCountTest() {
        return countTest;
    }

    //   Очистить кеш от просроченных данных
    public void clearCache() {
        System.out.println("Зашли в очистку...");
        // Если аннотация Cache еще не вызывалась или равна 0, значит чистить пока нечего или не надо - выходим
        if (timeout < 0) {
            return;
        }

        long timeCurrent = System.currentTimeMillis();
        // Все что прочрочено удаляем
        for (Iterator<Save> it = cache.stream().iterator(); it.hasNext(); ) {
            Save save = it.next();
            if (save.getTimeLife() < timeCurrent) {
                System.out.println("Удалили объект из кеша: " + save);
                cache.remove(save);
                synchronized (this) {
                    countTest--;
                }
            }
        }
    }

    //  Возьмем объект Save из кеша
    public Save getObjectCache(String obj) {
        if (cache.isEmpty()) return null;
        for (Save sv : cache)
            if (sv.getObject().equals(obj)) return sv;
        return null;
    }

    //    Добавим обект Save в кеш
    public void addObjectCache(Save save) {
        if (getObjectCache(save.getObject()) == null) {
            cache.add(save);
            synchronized (this) {
                countTest++;
            }
        }
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object value = null;
        Method meth = object.getClass().getDeclaredMethod(method.getName(), method.getParameterTypes());
        String key = meth.getName() + " : " + object;

        if (meth.isAnnotationPresent(Cache.class)) {
            Save tsave = getObjectCache(key);
            timeout = meth.getAnnotation(Cache.class).value();
            if (tsave == null) {
                value = method.invoke(object, args);
                Save newSave = new Save<>(key, System.currentTimeMillis() + timeout, value);
                addObjectCache(newSave);
                System.out.println("Добавили объект в кеш: " + newSave);
            } else {
                value = tsave.getValue();
                tsave.setTimeLife(System.currentTimeMillis() + timeout);
                System.out.println("Обновили время жизни объекта в кеше: " + tsave);
            }
        } else {
            value = method.invoke(object, args);
        }
        return value;
    }
}