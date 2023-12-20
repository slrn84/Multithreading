package ru.courses2.multithreading;

public class Pair {
    private Object obj;
    private long timeLife;

    public Pair(Object obj, long timeOut) {
        this.obj = obj;
        this.timeLife = System.currentTimeMillis() + timeOut;
    }

    public Object getObj() {
        return obj;
    }

    public long getTimeLife() {
        return timeLife;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public void setTimeLife(long timeLife) {
        this.timeLife = timeLife;
    }

    public boolean isLife(long curTime){
        return curTime > timeLife;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "obj=" + obj +
                ", timeLife=" + timeLife +
                '}';
    }
}
