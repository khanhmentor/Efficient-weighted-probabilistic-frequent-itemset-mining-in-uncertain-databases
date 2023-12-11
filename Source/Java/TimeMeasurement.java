import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TimeMeasurement {

    /**
     * Measures the running time of the WPFI.wPFIApriori method under different conditions.
     *
     * @param DB The database of transactions.
     * @param w The weights associated with each item.
     * @param n The total number of transactions.
     * @param density The density threshold.
     * @param msup The minimum support threshold.
     * @param t The threshold for determining whether a subset is frequent.
     * @param alpha Additional parameter used in WPFI.wPFIApriori.
     * @return A list of pairs containing the condition and its running time in milliseconds.
     */
    public static List<Pair<String, Double>> measure(List<List<Pair<String, Double>>> DB, Map<String, Double> w, int n, double density, double msup, double t, double alpha) {
        List<Boolean> conditionList = List.of(false, true); // Conditions to test

        List<Pair<String, Double>> runningTimeList = new ArrayList<>();
        for (Boolean condition : conditionList) {
            long startTime = System.nanoTime(); // Record start time
            // Execute the WPFI.wPFIApriori method with the given condition
            WPFI.wPFIApriori(DB, n, w, density, msup, t, alpha, condition);
            long endTime = System.nanoTime(); // Record end time
            // Calculate the duration in milliseconds
            double durationInMilliseconds = (endTime - startTime) / 1_000_000.0;
            // Add the condition and its running time to the list
            runningTimeList.add(new Pair<>(condition ? "aWPFI" : "eWPFI", durationInMilliseconds));
        }

        return runningTimeList;
    }
}