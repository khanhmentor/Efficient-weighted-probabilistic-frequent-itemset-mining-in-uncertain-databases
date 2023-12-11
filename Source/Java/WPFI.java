import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WPFI {

    /**
     * Scans the database to find all single-item itemsets that meet the weighted probabilistic frequent itemset criteria.
     * 
     * @param I Set of all items.
     * @param DB Database of transactions.
     * @param w Weights of each item.
     * @param density Density threshold.
     * @param msup Minimum support threshold.
     * @param t Threshold for determining frequent itemsets.
     * @return Pair of sets of frequent items and their corresponding support values.
     */

    private static Pair<Set<String>, Map<Set<String>, Double>> scanFindSize1WPFI(Set<String> I, List<List<Pair<String, Double>>> DB, Map<String, Double> w, double density, double msup, double t) {
        Set<String> WPFI1 = new HashSet<>(); // Set to store single-item WPFI
        Map<Set<String>, Double> mu1 = new HashMap<>(); // Map to store the weighted support of single-item WPFI
    
        for (String item : I) {
            Set<String> itemset = new HashSet<>();
            itemset.add(item); // Create a single-item itemset
    
            // Calculate the weighted support for the itemset
            double weightedSupport = w.getOrDefault(item, 0.0) / itemset.size();
    
            // Calculate the probability of the itemset being frequent
            double probability = ProbabilityCalculator.cal(itemset, msup, ProbabilityCalculator.calPXI(DB, density));
    
            // Check if the itemset meets the criteria for being a weighted probabilistic frequent itemset
            if (weightedSupport * probability >= t) {
                WPFI1.add(item); // Add the item to the WPFI set
                mu1.put(itemset, weightedSupport); // Map the itemset to its weighted support
            }
        }
    
        return new Pair<>(WPFI1, mu1); // Return the single-item WPFI and their weighted supports
    }

    /**
     * Scans the candidate itemsets of size k to find those that meet the minimum support criteria.
     * 
     * @param Ck Candidate itemsets of size k.
     * @param DB Database of transactions.
     * @param w Weights of each item.
     * @param density Density threshold.
     * @param msup Minimum support threshold.
     * @param t Threshold for determining frequent itemsets.
     * @return Pair of sets of frequent itemsets of size k and their corresponding support values.
     */

    private static Pair<Set<Set<String>>, Map<Set<String>, Double>> scanFindSizeKWpfi(Set<Set<String>> Ck, List<List<Pair<String, Double>>> DB, Map<String, Double> w, double density, double msup, double t) {
        Set<Set<String>> WPFIk = new HashSet<>(); // Set to store size-k WPFI
        Map<Set<String>, Double> muk = new HashMap<>(); // Map to store the weighted support of size-k WPFI
    
        for (Set<String> itemset : Ck) {
            // Calculate the weighted support for the itemset
            double weightedSupport = itemset.stream()
                                            .mapToDouble(i -> w.getOrDefault(i, 0.0))
                                            .sum() / itemset.size();
    
            // Calculate the probability of the itemset being frequent
            double probability = ProbabilityCalculator.cal(itemset, msup, ProbabilityCalculator.calPXI(DB, density));
    
            // Check if the itemset meets the criteria for being a weighted probabilistic frequent itemset
            if (weightedSupport * probability >= t) {
                WPFIk.add(itemset); // Add the itemset to the WPFI set
                muk.put(itemset, weightedSupport); // Map the itemset to its weighted support
            }
        }
    
        return new Pair<>(WPFIk, muk); // Return the size-k WPFI and their weighted supports
    }

    /**
     * Orchestrates the process of finding frequent itemsets of increasing sizes until no more can be found.
     * 
     * @param DB Database of transactions.
     * @param n Total number of transactions.
     * @param w Weights of each item.
     * @param density Density threshold.
     * @param msup Minimum support threshold.
     * @param t Threshold for determining frequent itemsets.
     * @param alpha Additional parameter for the mining process.
     * @param isProbabilityModelBase Flag to indicate exact or approximate mode.
     * @return List of pairs of itemsets and their support values at each level.
     */

    public static List<Pair<Set<Set<String>>, Map<Set<String>, Double>>> wPFIApriori(List<List<Pair<String, Double>>> DB, 
                                                                                     int n, Map<String, Double> w, 
                                                                                     double density, double msup, 
                                                                                     double t, double alpha, 
                                                                                     boolean isProbabilityModelBase) {
        List<Pair<Set<Set<String>>, Map<Set<String>, Double>>> WPFI = new ArrayList<>();
        Set<String> I = new HashSet<>();
        DB.forEach(transaction -> transaction.forEach(itemProbPair -> I.add(itemProbPair.getKey())));

        // Step 2: Find size-1 weighted probabilistic frequent itemsets
        Pair<Set<String>, Map<Set<String>, Double>> WPFI1 = scanFindSize1WPFI(I, DB, w, density, msup, t);
        WPFI.add(new Pair<>(Set.of(WPFI1.getKey()), WPFI1.getValue()));  // Step 3: Add WPFI1 to WPFI

        // Step 4: Initialize k to 2
        int k = 2;

        // Step 5-10: Main loop to find WPFI of size k
        while (!WPFI.get(k - 2).getKey().isEmpty()) {  // while the k-1 th element of WPFI is not empty

            // Step 6: Generate candidates of size k
            Set<Set<String>> Ck = CandidateGenerator.wPFIAprioriGen(WPFI.get(k - 2).getKey(), I, WPFI.get(k - 2).getValue(), w, alpha, n, msup, t, isProbabilityModelBase);

            // Step 7: Find WPFI of size k
            Pair<Set<Set<String>>, Map<Set<String>, Double>> WPFIk = scanFindSizeKWpfi(Ck, DB, w, density, msup, t);

            // Step 8: Add WPFIk to WPFI
            WPFI.add(WPFIk);

            k++;  // Step 9: Increment k
        }

        return WPFI;  // WPFI now contains the weighted probabilistic frequent itemsets
    }

    // Demonstrates the functionality of the WPFI mining algorithm with a sample database
    public static void Example() {
        System.out.println("This is an example!");
        List<Pair<String, Double>> transaction1 = List.of(new Pair<>("Item1", 0.5), new Pair<>("Item2", 0.7));
        List<Pair<String, Double>> transaction2 = List.of(new Pair<>("Item2", 0.6), new Pair<>("Item3", 0.8));
        List<List<Pair<String, Double>>> DB = List.of(transaction1, transaction2);

        Map<String, Double> w = Map.of("Item1", 0.5, "Item2", 0.6, "Item3", 0.4);

        int n = DB.size();
        double density = 0.5;
        double msup = 0.3;
        double t = 0.4;
        double alpha = 0.2;

        Test(DB, w, n, density, msup, t, alpha);
    }

    // Implementation for testing the WPFI algorithm with specific parameters
    public static void Test(List<List<Pair<String, Double>>> DB, Map<String, Double> w, int n, double density, double msup, double t, double alpha) {

        GaussianProbability.printDB(DB);
        
        WeightAssigner.printW(w);
        System.out.println("n = " + n + ", density = " + density + ", msup = " + msup + ", t = " + t + ", alpha = " + alpha);

        System.out.println();
        System.out.println("Exact weighted probabilistic frequent itemset mining");
        List<Pair<Set<Set<String>>, Map<Set<String>, Double>>> result = wPFIApriori(DB, n, w, density, msup, t, alpha, false);
        printResult(result);

        System.out.println();
        System.out.println("Approximate weighted probabilistic frequent itemset mining");
        List<Pair<Set<Set<String>>, Map<Set<String>, Double>>> result2 = wPFIApriori(DB, n, w, density, msup, t, alpha, true);
        printResult(result2);
    }

    // Method to print the itemsets found by the WPFI algorithm and their weighted support
    private static void printResult(List<Pair<Set<Set<String>>, Map<Set<String>, Double>>> result) {
        int i = 0;
        for (Pair<Set<Set<String>>, Map<Set<String>, Double>> pair : result) {

            if (i < result.size() - 1) { 
                // Adding a separator for readability between pairs
                System.out.println("------------------------------------------------");

                // Print the ordinal number of level
                System.out.println("Level " + (i + 1) + " itemsets:");
            
                // Print itemsets and their support (Map<Set<String>, Double>)
                for (Map.Entry<Set<String>, Double> entry : pair.getValue().entrySet()) {
                    System.out.println("Itemset (Set): " + entry.getKey() + ", Support (Double): " + entry.getValue());
                }
                
                i++;
            }
        }
    }
}

