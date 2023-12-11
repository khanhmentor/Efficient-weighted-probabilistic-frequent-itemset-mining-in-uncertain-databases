import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ProbabilityCalculator {

    /**
     * Calculates the probability of a set of items (X) being frequent.
     *
     * @param X The set of items.
     * @param minSup The minimum support threshold.
     * @param pXi The probability of each item in the set.
     * @return The probability that the itemset X is frequent.
     */
    public static double cal(Set<String> X, double minSup, Map<String, Double> pXi) {
        double mu = 0.0; // Mean value
        double sigmaSquared = 0.0; // Variance

        // Calculate the mean and variance based on item probabilities
        for (String item : X) {
            double probability = pXi.getOrDefault(item, 0.0);
            mu += probability;
            sigmaSquared += probability * (1 - probability);
        }

        double frequentnessProbability;
        // Calculate the probability of the itemset being frequent
        if (sigmaSquared > 0) {
            frequentnessProbability = 1 - PoissonProbability.poissonCdf(minSup - 1, mu);
        } else {
            frequentnessProbability = mu >= minSup ? 1 : 0;
        }

        return frequentnessProbability;
    }

    /**
     * Calculates the probability of each item in the database.
     *
     * @param db The database of transactions.
     * @param density The density threshold for an item to be considered.
     * @return A map of items to their probabilities.
     */
    public static Map<String, Double> calPXI(List<List<Pair<String, Double>>> db, double density) {
        Map<String, Integer> itemFrequencies = new HashMap<>();
        int totalTransactions = db.size();

        // Calculate frequencies of each item in the database
        for (List<Pair<String, Double>> transaction : db) {
            for (Pair<String, Double> itemProbabilityPair : transaction) {
                String item = itemProbabilityPair.getKey();
                double probability = itemProbabilityPair.getValue();

                // Increment the frequency count if the probability exceeds the density threshold
                if (probability > density) {
                    itemFrequencies.put(item, itemFrequencies.getOrDefault(item, 0) + 1);
                }
            }
        }

        Map<String, Double> pXi = new HashMap<>();
        // Calculate the probability for each item
        for (Map.Entry<String, Integer> entry : itemFrequencies.entrySet()) {
            pXi.put(entry.getKey(), (double) entry.getValue() / totalTransactions);
        }

        return pXi;
    }
}