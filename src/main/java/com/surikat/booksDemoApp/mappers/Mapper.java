package com.surikat.booksDemoApp.mappers;

public interface Mapper<T, V> {
    V mapTo(T a);
    T mapFrom(V b);
}
