import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Class ChainFromIterable provides an Iterable over a sequence of Iterable objects.
 * It allows iterating over a sequence of Iterable<T> as if they were a single iterable sequence.
 * 
 * @param <T> the type of elements returned by the iterator
 */
public class ChainFromIterable<T> implements Iterable<T> {
    private Iterable<Iterable<T>> iterables; // Collection of Iterable objects to be chained

    /**
     * Constructs a ChainFromIterable with the specified collection of iterables.
     *
     * @param iterables the collection of Iterable<T> to be chained
     */
    public ChainFromIterable(Iterable<Iterable<T>> iterables) {
        this.iterables = iterables;
    }

    /**
     * Returns an iterator over elements of type T.
     * 
     * @return an Iterator.
     */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Iterator<Iterable<T>> outerIterator = iterables.iterator(); // Iterator over the collection of Iterable<T>
            private Iterator<T> innerIterator = null; // Current iterator over an Iterable<T>

            /**
             * Checks if the next element is available.
             *
             * @return true if there is a next element, false otherwise
             */
            @Override
            public boolean hasNext() {
                // Loop to shift to the next non-empty inner iterable
                while ((innerIterator == null || !innerIterator.hasNext()) && outerIterator.hasNext()) {
                    innerIterator = outerIterator.next().iterator();
                }
                return innerIterator != null && innerIterator.hasNext();
            }

            /**
             * Returns the next element in the iteration.
             *
             * @return the next element of type T
             * @throws NoSuchElementException if no more elements are present
             */
            @Override
            public T next() {
                // Throw exception if no elements are left to iterate
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return innerIterator.next(); // Return next element from the current iterator
            }
        };
    }
}