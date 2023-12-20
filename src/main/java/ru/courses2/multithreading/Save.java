package ru.courses2.multithreading;


import java.util.Objects;

public class Save<T> {
    private final T object;
    public Save(T object) {
        this.object = object;
    }
    public Object getObject() {
        return object;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Save save)) return false;
        return Objects.equals(getObject(), save.getObject());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getObject());
    }

    @Override
    public String toString() {
        return "Save{" +
                "object=" + object +
                '}';
    }
}