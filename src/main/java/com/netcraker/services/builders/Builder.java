package com.netcraker.services.builders;


@FunctionalInterface
public interface Builder<T, R> {
    R build(T t);
}
