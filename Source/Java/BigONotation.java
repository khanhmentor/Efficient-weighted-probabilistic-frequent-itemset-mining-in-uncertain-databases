import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JFrame;

public class BigONotation {

    /**
     * Draws a line chart to visualize Big O Notations.
     *
     * @param title The title of the chart.
     * @param dataset The dataset containing Big O Notations.
     */

    private static void drawBigOChart(String title, DefaultCategoryDataset dataset) {
        // Create a line chart
        JFreeChart lineChart = ChartFactory.createLineChart(
                title,
                "Dataset Name", "Running Time (ms)",
                dataset, PlotOrientation.VERTICAL,
                true, true, false);

        // Create a frame to display the chart
        ChartPanel chartPanel = new ChartPanel(lineChart);
        JFrame frame = new JFrame();
        frame.setContentPane(chartPanel);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    /**
     * Creates a dataset for the Big O chart based on theoretical complexities.
     *
     * @return A DefaultCategoryDataset object for charting.
     */

    private static DefaultCategoryDataset createBigODataset() {
        DefaultCategoryDataset bigODataset = new DefaultCategoryDataset();
    
        String[] datasetList = DataLoader.getFileName(); // Retrieve dataset file names
    
        // Iterate over each dataset and measure running times
        for (String dataset : datasetList) {
            System.out.println("Running on " + dataset);
            // Load database
            Pair<List<Set<String>>, Integer> data = DataLoader.load("../Datasets/" + dataset);
            int n = data.getValue(); // Total number of transactions
            List<Set<String>> DB = data.getKey(); // Dataset
    
            int totalItems = 0; // Total number of items across all transactions
            Set<String> uniqueItems = new HashSet<>(); // Set to store unique items
    
            // Calculating the total number of items and unique items
            for (Set<String> transaction : DB) {
                totalItems += transaction.size();
                uniqueItems.addAll(transaction);
            }
    
            int m = totalItems / n; // Average number of items per transaction
            int u = uniqueItems.size(); // Number of unique items
    
            double complexity = MathCalculator.pow(2, u) * u * n * m; // O(2^u * u * nm)
            if (Double.isInfinite(complexity) ) {
                complexity = 1e308;
            }
    
            // Add only one value for 'Complexity'
            bigODataset.addValue(complexity, "Complexity", dataset);
        }
    
        return bigODataset;
    }    

    /**
     * Visualizes the Big O Notation in a chart.
     */

    public static void visualize() {
        DefaultCategoryDataset dataset = createBigODataset();
        drawBigOChart("Theoretical Big O Notation Analysis", dataset);
    }
}
