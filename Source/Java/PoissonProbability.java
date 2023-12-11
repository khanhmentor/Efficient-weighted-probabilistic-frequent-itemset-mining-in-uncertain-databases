public class PoissonProbability {

    /**
     * Calculates the probability mass function (pmf) for the Poisson distribution.
     *
     * @param lambda The average number of events (rate parameter).
     * @param x The number of occurrences for which the probability is calculated.
     * @return The probability of observing exactly x events.
     */
    private static double poissonPmf(double lambda, int x) {
        // The Poisson pmf formula: (e^(-lambda) * lambda^x) / x!
        return MathCalculator.pow(lambda, x) * ExpNegativeLambda.cal(lambda) / Factorial.cal(x);
    }

    /**
     * Calculates the cumulative distribution function (cdf) for the Poisson distribution.
     *
     * @param lambda The average number of events (rate parameter).
     * @param k The upper bound of occurrences for cumulative probability.
     * @return The probability of observing up to k events.
     */
    public static double poissonCdf(double lambda, double k) {
        double cdfValue = 0.0;
        // Summing up the probabilities for all occurrences from 0 to k
        for (int i = 0; i <= k; i++) {
            cdfValue += poissonPmf(lambda, i);
        }
        return cdfValue; // The cumulative probability
    }
}