package edu.gatech.cs2340.trydent.internal;

/**
 * Generic adapter class for converting objects of type A to objects of type B.
 * @param <A>
 * @param <B>
 */
public interface Adapter<A, B> {

    /** Converts the instance of A to an instance of B. **/
    public B convert(A a);

}
