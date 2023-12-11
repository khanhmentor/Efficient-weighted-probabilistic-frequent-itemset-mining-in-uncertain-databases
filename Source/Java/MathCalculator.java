public class MathCalculator {

    /**
     * Calculates the square root of a number using the iterative method with a specified tolerance.
     *
     * @param number The number to calculate the square root of.
     * @param tolerance The tolerance for the approximation.
     * @return The approximate square root of the number.
     * @throws IllegalArgumentException If the number is negative.
     */
    public static double sqrt(double number) {
        if (number < 0) {
            throw new IllegalArgumentException("Cannot compute the square root of a negative number.");
        }

        if (number == 0 || number == 1) {
            return number;
        }

        double guess = number / 2.0, tolerance = 1e-10;
        while (abs(guess * guess - number) > tolerance) {
            guess = (guess + number / guess) / 2.0;
        }

        return guess;
    }

    /**
     * Returns the smaller of two double values.
     *
     * @param a The first value.
     * @param b The second value.
     * @return The smaller of a and b.
     */
    public static double min(double a, double b) {
        if (a < b) {
            return a;
        } else {
            return b;
        }
    }
    
    /**
     * Returns the larger of two double values.
     *
     * @param a The first value.
     * @param b The second value.
     * @return The larger of a and b.
     */
    public static double max(double a, double b) {
        if (a > b) {
            return a;
        } else {
            return b;
        }
    }

    /**
     * Returns the absolute value of a double value.
     *
     * @param a The value.
     * @return The absolute value of a.
     */
    public static double abs(double a) {
        if (a < 0) {
            return -a;
        } else {
            return a;
        }
    }

    /**
     * Calculates the value of a number raised to a power.
     *
     * @param base The base number.
     * @param exponent The exponent.
     * @return The result of raising base to the power of exponent.
     */
    public static double pow(double base, int exponent) {
        if (exponent == 0) {
            return 1; // Any number to the power of 0 is 1
        } else if (exponent < 0) {
            return 1 / pow(base, -exponent); // Handle negative exponents
        } else {
            double result = 1;
            for (int i = 1; i <= exponent; i++) {
                result *= base;
            }
            return result;
        }
    }    
}