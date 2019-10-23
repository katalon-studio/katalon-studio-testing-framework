package com.kms.katalon.core.trymonad;

/**
 * This is similar to the Java {@link java.util.function.Consumer Consumer} function type.
 * It has a checked exception on it to allow it to be used in lambda expressions on the Try monad.
 * 
 * @param <T>
 * @param <E> the type of throwable thrown by {@link #accept(Object)}
 * 
 * @author https://github.com/jasongoodwin/better-java-monads
 */
public interface TryConsumer<T, E extends Throwable> {

    /**
     * Performs this operation on the given argument.
     *
     * @param t the input argument
     */
    void accept(T t) throws E;

}
