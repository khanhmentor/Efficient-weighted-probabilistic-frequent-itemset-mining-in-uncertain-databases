import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class GaussianProbability {

    /**
     * Adds uncertainties to each item in the database using a Gaussian distribution.
     *
     * @param DB The original database of transactions.
     * @param mean The mean value for the Gaussian distribution.
     * @param variance The variance value for the Gaussian distribution.
     * @return A modified database where each item in the transactions has an associated probability.
     */
    public static List<List<Pair<String, Double>>> addUncertainties(List<Set<String>> DB, double mean, double variance) {
        double stdDev = MathCalculator.sqrt(variance); // Calculate the standard deviation
        double lowerBound = MathCalculator.max(mean - stdDev, 0); // Calculate the lower bound ensuring it's not less than 0
        double upperBound = MathCalculator.min(mean + stdDev, 1); // Calculate the upper bound ensuring it's not more than 1

        List<List<Pair<String, Double>>> modifiedDB = new ArrayList<>();
        Random random = new Random();

        // Iterate over each transaction in the database
        for (Set<String> transaction : DB) {
            List<Pair<String, Double>> modifiedTransaction = new ArrayList<>();
            // Iterate over each item in the transaction
            for (String item : transaction) {
                // Generate a probability for the item within the bounds
                double probability = lowerBound + (upperBound - lowerBound) * random.nextDouble();
                modifiedTransaction.add(new Pair<>(item, probability)); // Add the item and its probability to the modified transaction
            }
            modifiedDB.add(modifiedTransaction); // Add the modified transaction to the modified database
        }

        return modifiedDB; // Return the modified database
    }

    /**
     * Prints a subset (or all) of the transactions in the uncertain database.
     *
     * @param DB The database to print.
     */
    public static void printDB(List<List<Pair<String, Double>>> DB) {
        System.out.println("Uncertain database:");
        String expression = "";
        // Limit the output to the first 5 transactions for brevity
        if (DB.size() > 5) {
            DB = DB.subList(0, 5);
            expression = ". . ."; // Indicate that there are more transactions
        }    
        
        int i = 0;
        // Iterate over each transaction
        for (List<Pair<String, Double>> transaction : DB) {
            System.out.println("Transaction " + (i + 1) + ":");
            System.out.print("{");
            int j = 0;
            // Iterate over each item in the transaction
            for (Pair<String, Double> itemWithProbability : transaction) {
                // Print the item and its probability
                System.out.print("(" + itemWithProbability.getKey() + ", " + String.format("%.3f", itemWithProbability.getValue()) + ")");
                if (j < transaction.size() - 1)
                    System.out.print(", ");
                
                j++;
            }
            
            System.out.print("}");
            System.out.println();
            
            i++;
        }

        System.out.println(expression);
    }
}