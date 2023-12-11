public class ExpNegativeLambda {

    /**
     * Calculates the value of e^(-lambda) using a series expansion.
     *
     * @param lambda The value of lambda in e^(-lambda).
     * @return The calculated value of e^(-lambda).
     */
    public static double cal(double lambda) {
        int n = 20;  // Number of terms in the series expansion for accuracy
        double eNegLambda = 0.0;

        // Loop to calculate each term in the series and add to the sum
        for (int i = 0; i < n; i++) {
            // Calculate the ith term in the series: (-lambda)^i / i!
            eNegLambda += MathCalculator.pow(-lambda, i) / Factorial.cal(i);
        }

        return eNegLambda;  // Return the sum of the series
    }
}