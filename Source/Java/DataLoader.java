import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DataLoader {

    /**
     * Loads transactions from a file into a database.
     * 
     * @param filePath The path of the file to load the transactions from.
     * @return A Pair containing the database (List of Sets of Strings) and its size.
     */
    public static Pair<List<Set<String>>, Integer> load(String filePath) {
        List<Set<String>> DB = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int counter = 0;
            while ((line = reader.readLine()) != null && counter < 10000) {
                Set<String> transaction = new HashSet<>();
                for (String item : line.trim().split("\\s+")) {
                    transaction.add(item);  // Add each item to the transaction
                }
                DB.add(transaction);  // Add the transaction to the database
                counter++;
            }
        } catch (IOException e) {
            e.printStackTrace();  // Handle potential IOException
        }
        return new Pair<>(DB, DB.size());  // Return the database and its size
    }

    /**
     * Retrieves the names of text files from the "../Datasets" directory.
     * 
     * @return An array of file names.
     */
    public static String[] getFileName() {
        File directory = new File("../Datasets");  // Specify the directory

        // Filename filter to accept only .txt files
        FilenameFilter filter = (dir, name) -> name.endsWith(".txt");

        // List all the text files in the directory
        String[] files = directory.list(filter);
        return files;
    }

    /**
     * Prints a subset of the database.
     * 
     * @param DB The database to be printed.
     */
    public static void printDB(List<Set<String>> DB) {
        System.out.println("Deterministic database:");
        // Print either the first 5 transactions or the entire database if it's smaller
        if (DB.size() > 5) {
            System.out.println(DB.subList(0, 5));
        } else {
            System.out.println(DB);
        }
    }
}