import java.util.Set;
import java.util.Map;
import java.util.HashSet;
import java.util.List;

public class FrequentItemsetChecker {

    /**
     * Checks if the candidate set has any infrequent subset.
     *
     * @param candidate The candidate itemset to check.
     * @param WPFIprev The set of previous weighted frequent itemsets.
     * @param w The map containing weights for each item.
     * @param t The threshold for determining whether a subset is frequent.
     * @return true if there is an infrequent subset, false otherwise.
     */
    public static boolean hasInfrequentSubset(Set<String> candidate, Set<Set<String>> WPFIprev, Map<String, Double> w, double t) {
        // Generate all possible subsets of the candidate set
        Iterable<List<String>> subsets = SubsetGenerator.generate(candidate);

        for (List<String> subsetList : subsets) {
            Set<String> subset = new HashSet<>(subsetList);  // Convert list to set for comparison
            // Calculate the weighted support for the subset
            double subsetWeight = WeightedSupportCalculator.cal(subset, w);

            // Check if the subset's weight is below the threshold and not in previous frequent itemsets
            if (subsetWeight < t && !WPFIprev.contains(subset)) {
                return true;  // Infrequent subset found
            }
        }

        return false;  // No infrequent subset found
    }
}