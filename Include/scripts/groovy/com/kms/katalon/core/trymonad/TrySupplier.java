package com.kms.katalon.core.trymonad;

/**
 * This is similar to the Java Supplier function type.
 * It has a checked exception on it to allow it to be used in lambda expressions on the Try monad.
 * 
 * @param <T>
 * 
 * @author https://github.com/jasongoodwin/better-java-monads
 */

public interface TrySupplier<T> {
    T get() throws Throwable;
}
