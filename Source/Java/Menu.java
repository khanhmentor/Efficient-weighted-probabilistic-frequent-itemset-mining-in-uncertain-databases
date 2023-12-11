import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Menu {

    /**
     * Initializes and displays the menu for user interaction.
     */
    public static void initialize() {
        Scanner scanner = new Scanner(System.in);
        int mainChoice, datasetChoice;
        String filePath = "../Datasets/";

        while (true) { // Loop to keep showing the menu until the user chooses to exit
            // Display the main menu options
            System.out.println("Menu:");
            System.out.println("1. Give an example");
            System.out.println("2. Test on an optional dataset");
            System.out.println("3. Analyze the running time of all datasets");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            mainChoice = scanner.nextInt(); // Read the user's main choice

            switch (mainChoice) {
                case 1:
                    WPFI.Example(); // Execute the example method
                    break;
                case 2:
                    // Display dataset choices
                    System.out.println("Select dataset:");
                    System.out.println("1. Connect");
                    System.out.println("2. Accidents");
                    System.out.println("3. USCensus");
                    System.out.println("4. T40I10D100K");
                    System.out.println("5. Generate a dataset then experiment on it");
                    System.out.print("Enter dataset choice: ");
                    datasetChoice = scanner.nextInt(); // Read the dataset choice

                    // Set file path based on dataset choice
                    switch (datasetChoice) {
                        case 1:
                            filePath = "../Datasets/Connect.txt";
                            break;
                        case 2:
                            filePath = "../Datasets/Accidents.txt";
                            break;
                        case 3:
                            filePath = "../Datasets/USCensus.txt";
                            break;
                        case 4:
                            filePath = "../Datasets/T40I10D100K.txt";
                            break;
                        case 5:
                            filePath = "../Datasets/GeneratedDB.txt";
                            DataGenerator.writeToFile(filePath); // Generate a dataset
                            break;
                        default:
                            System.out.println("Invalid dataset choice");
                            continue; // Continue to the beginning of the loop for a new choice
                    }

                    System.out.println("File path set to: " + filePath);
                    // Load the selected database
                    Pair<List<Set<String>>, Integer> DB = DataLoader.load(filePath);
                    DataLoader.printDB(DB.getKey()); // Print a part of the database

                    // Convert the database to an uncertain database with uncertainties
                    List<List<Pair<String, Double>>> uncertainDB = GaussianProbability.addUncertainties(DB.getKey(), 0.5, 0.125);

                    // Define weights for the items in the database
                    Map<String, Double> w = WeightAssigner.define(uncertainDB);

                    // Test the uncertain database with specified parameters
                    WPFI.Test(uncertainDB, w, DB.getValue(), 0.33, 0.2 * DB.getValue(), 0.6, 0.6);
                    break;
                case 3:
                    System.out.println("Analyzing the theoretical running time of all datasets");
                    BigONotation.visualize(); // Analyze the theoretical running time of all datasets
                    System.out.println("Analyzing the actual running time of all datasets");
                    RunningTimeAnalyser.analyze(); // Analyze the actual running time of all datasets
                    break;
                case 0:
                    System.out.println("Exiting the program.");
                    scanner.close(); // Close the scanner before exiting
                    return; // Exit the program
                default:
                    System.out.println("Invalid choice"); // Handle invalid choices
            }
        }
    }
}