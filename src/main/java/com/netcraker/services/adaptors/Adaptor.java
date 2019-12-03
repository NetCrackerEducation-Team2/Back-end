package com.netcraker.services.adaptors;


@FunctionalInterface
public interface Adaptor<T, R> {
    R adapt(T t);
}
