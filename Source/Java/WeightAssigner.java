import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.HashMap;

public class WeightAssigner {

    /**
     * Assigns random weights to each unique item in the database.
     *
     * @param DB The database of transactions, each transaction being a list of item-probability pairs.
     * @return A map of items to their assigned weights.
     */
    public static Map<String, Double> define(List<List<Pair<String, Double>>> DB) {
        Set<String> items = new HashSet<>();
        // Collect all unique items from the database
        for (List<Pair<String, Double>> transaction : DB) {
            for (Pair<String, Double> itemWithProbability : transaction) {
                items.add(itemWithProbability.getKey());
            }
        }

        Random random = new Random();
        Map<String, Double> w = new HashMap<>();
        // Assign a random weight between 0.0 and 1.0 to each item
        for (String item : items) {
            w.put(item, 1.0 * random.nextDouble());
        }

        return w; // Return the map of items with their weights
    }

    /**
     * Prints a subset (or all) of the weights for items.
     *
     * @param w The map of item weights.
     */
    public static void printW(Map<String, Double> w) {
        System.out.println("w = {");
        // Print up to the first 5 weights
        w.entrySet().stream().limit(5).forEach(entry -> 
            System.out.println(entry.getKey() + ": " + String.format("%.2f", entry.getValue()))
        );
        if (w.size() > 5)
            System.out.println(". . ."); // Indicate more items exist if the list is longer than 5
        System.out.println("}");
    }
}