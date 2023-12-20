package ru.courses2.multithreading;

import java.util.Objects;

public class Fraction implements Fractionable {
    private int num;
    private int denum;

    public int testMethodIsCached = 0;

    public Fraction(int num, int denum) {
        this.num = num;
        this.denum = denum;
    }

    @Override
    @Mutator
    public void setNum(int num) {
        this.num = num;
    }

    @Override
    @Mutator
    public void setDenum(int denum) {
        this.denum = denum;
    }

    @Override
    @Cache(1000)
    public double doubleValue() {
        testMethodIsCached++;
        System.out.println("invoke double value");
        return (double) num / denum;
    }

    @Override
    public String toString() {
        return "Fraction {" + num + ";" + denum + '}';
    }

    @Override
    public boolean equals(Object o) {
        System.out.println("Сравнение: 1");
        if (this == o) return true;
        if (!(o instanceof Fraction fraction)) return false;
        System.out.println("Сравнение: "+(num == fraction.num && denum == fraction.denum));
        return num == fraction.num && denum == fraction.denum;
    }

    @Override
    public int hashCode() {
        return Objects.hash(num, denum);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new Fraction(this.num, this.denum);
    }
}
