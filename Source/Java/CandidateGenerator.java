import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CandidateGenerator {

    /**
     * Generates candidate itemsets using pruning based on weighted support.
     * @param WPFIprev - Previous weighted frequent itemsets.
     * @param I - A set of items.
     * @param w - Weights associated with each item.
     * @param t - A threshold value.
     * @return A set of candidate itemsets.
     */

    private static Set<Set<String>> wPFICandidateGenPruning(Set<Set<String>> WPFIprev, Set<String> I, Map<String, Double> w, double t) {
        Set<Set<String>> Ck = new HashSet<>();  // Initialize an empty set for storing candidates

        for (Set<String> itemset : WPFIprev) {  // Iterate over each itemset from the previous frequent itemsets
            for (String item : I) {  // Iterate over each item
                if (!itemset.contains(item)) {  // Check if the item is not already in the itemset
                    Set<String> candidate = new HashSet<>(itemset);  // Create a new candidate itemset
                    candidate.add(item);  // Add the new item to the candidate itemset

                    double candidateWeight = WeightedSupportCalculator.cal(candidate, w);  // Calculate weighted support for the candidate

                    // Find the item with the minimum weight in the current itemset
                    String Im = itemset.stream()
                                       .min((i1, i2) -> Double.compare(w.getOrDefault(i1, Double.MAX_VALUE), w.getOrDefault(i2, Double.MAX_VALUE)))
                                       .orElse(null);

                    // Check if the candidate's weight is above the threshold and if it's valid based on the minimum weight item
                    if (candidateWeight >= t && (Im == null || w.getOrDefault(item, 0.0) < w.getOrDefault(Im, Double.MAX_VALUE))) {
                        // Check if the candidate does not have any infrequent subset
                        if (!FrequentItemsetChecker.hasInfrequentSubset(candidate, WPFIprev, w, t)) {
                            Ck.add(candidate);  // Add the valid candidate to the set
                        }
                    }
                }
            }
        }

        return Ck;  // Return the set of candidate itemsets
    }

    /** 
     * Generates candidate itemsets using a probability model based approach.
     * @param WPFIprev - Previous weighted frequent itemsets.
     * @param I - A set of items.
     * @param mu - A probability model.
     * @param w - Weights associated with each item.
     * @param alpha, n, msup - Additional parameters for the probability model.
     * @param t - A threshold value.
     * @return A set of candidate itemsets.
     */
    
    private static Set<Set<String>> wPFICandidateGenPruningProbabilityModelBase(
            Set<Set<String>> WPFIprev, Set<String> I, Map<Set<String>, Double> mu,
            Map<String, Double> w, double alpha, int n, double msup, double t) {

        Set<Set<String>> Ck = new HashSet<>();  // Initialize an empty set for storing candidates
        double m = w.values().stream().mapToDouble(Double::doubleValue).max().orElse(0.0);  // Find the maximum weight

        // Calculate a threshold based on Poisson probability and minimum support
        double muWedge = 1 - PoissonProbability.poissonCdf(msup - 1, WPFIprev.stream().mapToDouble(itemset -> mu.getOrDefault(itemset, 0.0)).sum());
        muWedge = MathCalculator.min(muWedge, t / m);  // Adjust the threshold

        for (Set<String> itemset : WPFIprev) {  // Iterate over each itemset from the previous frequent itemsets
            for (String item : I) {  // Iterate over each item
                if (!itemset.contains(item)) {  // Check if the item is not already in the itemset
                    Set<String> newCandidate = new HashSet<>(itemset);  // Create a new candidate itemset
                    newCandidate.add(item);  // Add the new item to the candidate itemset

                    double candidateWeightedSupport = WeightedSupportCalculator.cal(newCandidate, w);  // Calculate weighted support for the candidate

                    // Find the item with the minimum weight in the current itemset
                    String Im = itemset.stream()
                            .min((i1, i2) -> Double.compare(w.getOrDefault(i1, Double.MAX_VALUE), w.getOrDefault(i2, Double.MAX_VALUE)))
                            .orElse(null);

                    // Check if the candidate's weight is above the threshold and if it's valid based on the minimum weight item
                    if (candidateWeightedSupport >= t && (Im == null || w.getOrDefault(item, 0.0) < w.getOrDefault(Im, Double.MAX_VALUE))) {
                        // Calculate the average and individual probabilities
                        double muX = itemset.stream().mapToDouble(x -> mu.getOrDefault(Set.of(x), 0.0)).sum() / itemset.size();
                        double muIi = mu.getOrDefault(Set.of(item), 0.0);

                        // Check if the candidate meets the probability model criteria
                        if (MathCalculator.min(muX, muIi) >= muWedge && muX * muIi >= alpha * n * muWedge) {
                            Ck.add(newCandidate);  // Add the valid candidate to the set
                        }
                    }
                }
            }
        }

        return Ck;  // Return the set of candidate itemsets
    }

    /** 
     * Main method to generate weighted frequent itemsets based on the specified model.
     * @param WPFIprev - Previous weighted frequent itemsets.
     * @param I - A set of items.
     * @param mu - A probability model.
     * @param w - Weights associated with each item.
     * @param alpha, n, msup, t - Additional parameters for the probability model.
     * @param isProbabilityModelBase - Flag to choose the model.
     * @return A set of candidate itemsets based on the chosen model.
     */

    public static Set<Set<String>> wPFIAprioriGen(Set<Set<String>> WPFIprev, Set<String> I,
                                                  Map<Set<String>, Double> mu, Map<String, Double> w,
                                                  double alpha, int n, double msup, double t,
                                                  boolean isProbabilityModelBase) {
        // Decide which candidate generation method to use based on the probability model flag
        if (isProbabilityModelBase) {
            return wPFICandidateGenPruningProbabilityModelBase(WPFIprev, I, mu, w, alpha, n, msup, t);
        } else {
            return wPFICandidateGenPruning(WPFIprev, I, w, t);
        }
    }
}