package com.unlikepaladin.pfm.utilities;

public class Tuple<T, T1> {
    T A;
    T1 B;

    public Tuple(T o, T1 o1) {
        this.A = o;
        this.B = o1;
    }

    public void setA(T a) {
        A = a;
    }

    public void setB(T1 b) {
        B = b;
    }

    public T getA() {
        return A;
    }

    public T1 getB() {
        return B;
    }
}
