import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class DataGenerator {

    /**
     * Generates a random string of a specified length.
     * 
     * @param random The Random object to use for generating random numbers.
     * @param length The length of the string to generate.
     * @return A random string.
     */
    private static String generateRandomString(Random random, int length) {
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(999) + 1; // Generate a random number between 1 and 999
            result.append(number); // Append the number to the result string
        }
        return result.toString();
    }

    /**
     * Generates a database of transactions.
     * 
     * @param numberOfTransactions The number of transactions to generate.
     * @param maxItemsPerTransaction The maximum number of items per transaction.
     * @return A Pair containing the generated database and its size.
     */
    private static Pair<List<Set<String>>, Integer> generateDB(int numberOfTransactions, int maxItemsPerTransaction) {
        List<Set<String>> DB = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < numberOfTransactions; i++) {
            Set<String> transaction = new HashSet<>();
            // Ensure at least 33 items, up to 942 items per transaction
            int itemsCount = random.nextInt(maxItemsPerTransaction - 32) + 33;

            for (int j = 0; j < itemsCount; j++) {
                // Generate random strings of length 1
                transaction.add(generateRandomString(random, 1));
            }

            DB.add(transaction); // Add the transaction to the database
        }

        return new Pair<>(DB, DB.size()); // Return the database and its size
    }

    /**
     * Writes the generated database to a file.
     * 
     * @param fileName The name of the file to write to.
     */
    public static void writeToFile(String fileName) {
        // Generate 10000 transactions with 33 to 942 items each
        List<Set<String>> DB = generateDB(10000, 942).getKey(); 
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Set<String> transaction : DB) {
                for (String item : transaction) {
                    writer.write(item + " "); // Write each item followed by a space
                }
                writer.newLine(); // Start a new line after each transaction
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle possible IOExceptions
        }
    }
}