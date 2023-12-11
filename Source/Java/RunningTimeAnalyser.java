import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.JFrame;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RunningTimeAnalyser {

    /**
     * Draws a line chart to visualize running times.
     *
     * @param title The title of the chart.
     * @param dataset The dataset containing running times.
     */
    private static void drawRunningTimeChart(String title, DefaultCategoryDataset dataset) {
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
     * Creates a dataset for the chart from the running time data.
     *
     * @param datasetRunningTimes The running times of different models on various datasets.
     * @return A DefaultCategoryDataset object for charting.
     */
    private static DefaultCategoryDataset createDataset(List<Pair<String, List<Pair<String, Double>>>> datasetRunningTimes) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Populate the dataset
        for (Pair<String, List<Pair<String, Double>>> datasetInfo : datasetRunningTimes) {
            String datasetName = datasetInfo.getKey();
            List<Pair<String, Double>> runningTimes = datasetInfo.getValue();

            for (Pair<String, Double> modelRunningTime : runningTimes) {
                dataset.addValue(modelRunningTime.getValue(), modelRunningTime.getKey(), datasetName);
            }
        }
        return dataset;
    }

    /**
     * Conducts the running time analysis.
     */
    public static void analyze() {
        String[] datasetList = DataLoader.getFileName(); // Retrieve dataset file names
        List<Pair<String, List<Pair<String, Double>>>> datasetRunningTimes = new ArrayList<>();

        // Iterate over each dataset and measure running times
        for (String dataset : datasetList) {
            System.out.println("Running on " + dataset);
            // Load database, convert to uncertain database, define weight, and measure running time
            Pair<List<Set<String>>, Integer> DB = DataLoader.load("../Datasets/" + dataset);
            List<List<Pair<String, Double>>> uncertainDB = GaussianProbability.addUncertainties(DB.getKey(), 0.5, 0.125);
            Map<String, Double> w = WeightAssigner.define(uncertainDB);
            List<Pair<String, Double>> runningTime = TimeMeasurement.measure(uncertainDB, w, DB.getValue(), 0.33, 0.2 * DB.getValue(), 0.6, 0.6);

            datasetRunningTimes.add(new Pair<>(dataset, runningTime));
        }

        // Print running time results in the console
        System.out.println("Result:");
        for (Pair<String, List<Pair<String, Double>>> datasetRunningTime : datasetRunningTimes) {
            System.out.println("Dataset: " + datasetRunningTime.getKey());
            List<Pair<String, Double>> runningTimes = datasetRunningTime.getValue();
            for (Pair<String, Double> modelRunningTime : runningTimes) {
                System.out.println("  Model: " + modelRunningTime.getKey() + ", Running Time: " + modelRunningTime.getValue() + " ms");
            }
        }

        // Create dataset and draw chart for visual analysis
        DefaultCategoryDataset dataset = createDataset(datasetRunningTimes);
        drawRunningTimeChart("Running Time Analysis", dataset);
    }
}