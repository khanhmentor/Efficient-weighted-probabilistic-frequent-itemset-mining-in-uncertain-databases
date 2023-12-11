import java.util.Map;
import java.util.Set;

public class WeightedSupportCalculator {
    
    /**
     * Calculates the weighted support for a given subset of items.
     *
     * @param subset The subset of items for which the weighted support is to be calculated.
     * @param w A map containing weights for each item.
     * @return The calculated weighted support.
     */
    public static double cal(Set<String> subset, Map<String, Double> w) {
        // Calculate the sum of weights for the items in the subset
        double totalWeight = subset.stream()
                                   .mapToDouble(item -> w.getOrDefault(item, 0.0))
                                   .sum();
        // Return the average weight (total weight divided by the number of items in the subset)
        return totalWeight / subset.size();
    }
}