package edu.gatech.cs2340.trydent.math.curve;

/**
 * Interface to manage appropriate wrapping, clamping, etc behavior for
 * indexing-based behavior. (Advanced functionality).
 */
public abstract class IndexWrapMode {

    /**
     * If 0 <= index <= size-1, this just returns the index. Otherwise, it
     * applies some strategy to convert index to a value between 0 and size-1
     * (inclusive), depending on the subclass.
     *
     * @param index
     *            - index (possibly out-of-bounds) to index.
     * @param size
     *            - (maximum value of index) + 1, eg the length of an array
     *            index is for.
     * @return
     */
    public abstract int handle(int index, int size);

    /**
     * Index wrapping mode which clamps the index to the range. Eg, -10 will be
     * converted to 0, size+10 will be converted to size-1.
     */
    public static final IndexWrapMode CLAMP = new IndexWrapMode() {
        @Override
        public int handle(int index, int size) {
            if (index < 0)
                return 0;
            if (index >= size)
                return size - 1;
            return index;
        }
    };

    /**
     * Index wrapping mode which wraps the index, such that going past (size-1)
     * wraps back around to 0, and going below 0 wraps back around to (size-1).
     */
    public static final IndexWrapMode WRAP = new IndexWrapMode() {
        @Override
        public int handle(int index, int size) {
            if (index < 0) {
                return size - ((-index - 1) % size) - 1;
            }
            return index % size;
        }
    };

    /**
     * Index wrapping mode which "reflects" the index, such that a value past
     * (size-1) starts going back down towards 0, and a value below 0 starts
     * moving back up towards (size-1).
     * <p>
     * For example, for size=5, the input indices
     * {@code (-5, -4, -3, -2, -1, 0, 1, 2,
     * 3, 4, 5, 6, 7)} would be transformed to
     * {@code (3, 4, 3, 2, 1, 0, 1, 2, 3, 4, 3, 2, 1)}.
     */
    public static final IndexWrapMode REFLECT = new IndexWrapMode() {
        @Override
        public int handle(int index, int size) {
            if (index < 0)
                index = -index;
            if ((index / size) % 2 == 0) {
                return index % size;
            }
            return size - (index % size) - 1;
        }
    };

}
