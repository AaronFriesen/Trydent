package edu.gatech.cs2340.trydent.internal;

/**
 * Generic adapter class for converting objects of type A to objects of type B.
 *
 * @param <A>
 *            the type to convert from
 * @param <B>
 *            the type to convert to
 */
public interface Adapter<A, B> {

    /**
     * Converts the instance of A to an instance of B.
     *
     * @param a
     *            the instance of A to convert to B.
     * @return the converted instance of type B.
     * */
    B convert(A a);

}
