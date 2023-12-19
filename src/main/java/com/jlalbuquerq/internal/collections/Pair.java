package com.jlalbuquerq.internal.collections;

import java.io.Serializable;

public class Pair<T, E> implements Serializable {
    public T first;
    public E second;
    public Pair(T first, E second) {
        this.first = first;
        this.second = second;
    }
}
