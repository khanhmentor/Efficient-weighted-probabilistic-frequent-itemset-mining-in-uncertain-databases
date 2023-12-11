import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * The Combinations class provides an Iterable for generating all combinations of a given size from a list.
 *
 * @param <T> The type of elements in the input list.
 */
public class Combinations<T> implements Iterable<List<T>> {
    private List<T> inputList; // The list from which combinations are generated
    private int r;             // The size of each combination

    /**
     * Constructs a Combinations object with the specified input list and combination size.
     *
     * @param inputList The list of elements to combine.
     * @param r The size of each combination.
     */
    public Combinations(List<T> inputList, int r) {
        this.inputList = inputList;
        this.r = r;
    }

    /**
     * Returns an iterator over combinations of elements.
     * 
     * @return An Iterator that iterates over combinations.
     */
    @Override
    public Iterator<List<T>> iterator() {
        return new Iterator<List<T>>() {
            private int n = inputList.size();    // The size of the input list
            private int[] indices = initIndices(); // Current indices for the combination
            private boolean hasNext = r <= n;    // Flag to check if more combinations are possible

            /**
             * Initializes the indices for the first combination.
             *
             * @return An array of initial indices.
             */
            private int[] initIndices() {
                int[] indices = new int[r];
                for (int i = 0; i < r; i++) {
                    indices[i] = i;  // Initialize indices to the first 'r' numbers
                }
                return indices;
            }

            /**
             * Checks if there are more combinations to generate.
             *
             * @return true if more combinations are available, false otherwise.
             */
            @Override
            public boolean hasNext() {
                return hasNext;
            }

            /**
             * Returns the next combination in the sequence.
             *
             * @return A list representing the next combination.
             * @throws NoSuchElementException if there are no more combinations.
             */
            @Override
            public List<T> next() {
                if (!hasNext) {
                    throw new NoSuchElementException();
                }

                List<T> combination = new ArrayList<>(r); // A list to store the current combination
                for (int i = 0; i < r; i++) {
                    combination.add(inputList.get(indices[i])); // Add elements to the combination based on current indices
                }

                // Generate the next combination
                hasNext = false;
                for (int i = r - 1; i >= 0; i--) {
                    if (indices[i] != i + n - r) {
                        indices[i]++;
                        for (int j = i + 1; j < r; j++) {
                            indices[j] = indices[j - 1] + 1;
                        }
                        hasNext = true;
                        break;
                    }
                }

                return combination; // Return the current combination
            }
        };
    }
}