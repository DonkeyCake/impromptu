package com.promptu.runnable;

/**
 * Created by Guy on 08/01/2017.
 */
@FunctionalInterface
public interface TriConsumer<T, U, V> {

    void apply(T t, U u, V v);

}
