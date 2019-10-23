package com.kms.katalon.core.trymonad;

/**
 * @author https://github.com/jasongoodwin/better-java-monads
 */
public interface TryMapFunction<T, R> {
    R apply(T t) throws Throwable;
}
