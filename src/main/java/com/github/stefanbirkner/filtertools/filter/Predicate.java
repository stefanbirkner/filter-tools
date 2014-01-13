package com.github.stefanbirkner.filtertools.filter;

/**
 * A predicate that is used for enabling the {@link OptionalFilter}. Could be used as functional
 * interface in Java 8.
 *
 * @param <T> the type of the input to the predicate.
 */
public interface Predicate<T> {
    /**
     * Evaluates the predicate on the given object.
     *
     * @param object the input object
     * @return {@code true} if the object matches the predicate, otherwise {@code false}
     */
    boolean test(T object);
}
