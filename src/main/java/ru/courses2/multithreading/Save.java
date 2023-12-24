package ru.courses2.multithreading;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter @ToString
public class Save<V> {
    private final String object;
    private long timeLife;
    private V value;

    public Save(String object, long timeLife, V value) {
        this.object = object;
        this.timeLife = timeLife;
        this.value = value;
    }
}