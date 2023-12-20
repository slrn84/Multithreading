package ru.courses2.multithreading;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class CacheHandler<T> implements InvocationHandler {
    private T object;
    private Map<Save, Pair> cacheValue = new HashMap<>();
    private long timeout = -1;

//    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
//        @Override
//        public Thread newThread(Runnable r) {
//            return new Thread(r);
//        }
//    });

    public CacheHandler(T object) {
        this.object = object;

//        scheduler.scheduleWithFixedDelay(new Runnable() {
//            @Override
//            public void run() {
//                clearCache();
//            }
//        }, 0, 3, TimeUnit.SECONDS);
    }

    public void clearCache() {
//        System.out.println("Зашли в очистку..");
        // Если аннтоция Cache еще не вызывалась или равна 0, значит чистить пока нечего или не надо - выходим
        if (timeout < 0) {
            return;
        }
        // скопируем мапу во временную переменную
        Map<Save, Pair> tmp = cacheValue
                .entrySet()
                .stream()
                .collect(Collectors.toMap(e -> new Save(e.getKey().getObject()), Map.Entry::getValue));

        // запомним текущее время
        long timeCurrent = System.currentTimeMillis();
        // очистим временную переменную от просроченных данных в кеше
        for (Save save : tmp.keySet()) {
            if (tmp.get(save).isLife(timeCurrent))
                System.out.println("Удалим объект: "+save.toString());
                tmp.remove(save);
        }

        // заменим cacheValue на временную мапу с синхронизацией блока
        synchronized (cacheValue) {
            cacheValue.clear();
            cacheValue = tmp;
        }
    }


    @Override
    public boolean equals(Object object1) {
        if (this == object1) return true;
        if (!(object1 instanceof CacheHandler that)) return false;
        return timeout == that.timeout && Objects.equals(object, that.object) && Objects.equals(cacheValue, that.cacheValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(object, cacheValue, timeout);
    }

    private void threadStart() {
        Thread thread = new Thread(() -> this.clearCache());
        thread.start();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object value = null;
        Method tmp = object.getClass().getDeclaredMethod(method.getName(), method.getParameterTypes());
//        if (tmp.isAnnotationPresent(Mutator.class)) {
//            value = method.invoke(object, args);
//            Save sKey = new Save(object);
//            if (!cacheValue.isEmpty() && cacheValue.containsKey(sKey)) {
//                System.out.println("Удалили объект из кеша: "+object);
//                synchronized (cacheValue) {
//                    cacheValue.remove(new Save(object));
//                }
//            }
//            this.threadStart();

//        } else
        if (tmp.isAnnotationPresent(Cache.class)) {
            this.threadStart();
            Save sKey = new Save(object);
            timeout = tmp.getAnnotation(Cache.class).value();
            if (!cacheValue.containsKey(sKey)) {
                value = method.invoke(object, args);
                System.out.println("Добавили объект в кеш: " + object);
                synchronized (cacheValue) {
                    cacheValue.put(sKey, new Pair(value, timeout));
                }
            } else {
                value = cacheValue.get(sKey).getObj();
                cacheValue.get(sKey).setTimeLife(System.currentTimeMillis() + timeout);
                System.out.println("Обновили время жизни объекта в кеше: " + object);
            }
        } else {
            value = method.invoke(object, args);
        }
        return value;
    }
}
