import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SubsetGenerator {

    /**
     * Generates all subsets of a given set.
     *
     * @param <T> The type of elements in the set.
     * @param s The set for which subsets are to be generated.
     * @return An Iterable over lists, each list representing a subset.
     */
    public static <T> Iterable<List<T>> generate(Set<T> s) {
        List<T> list = new ArrayList<>(s); // Convert the set into a list
        List<Iterable<List<T>>> allCombinations = new ArrayList<>();

        // Generate combinations of all sizes (from 1 to the size of the set)
        for (int r = 1; r <= list.size(); r++) {
            allCombinations.add(new Combinations<>(list, r)); // Add combinations of size r
        }

        // Chain all combinations into a single iterable sequence
        return new ChainFromIterable<>(allCombinations);
    }
}