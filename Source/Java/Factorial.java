public class Factorial {

    /**
     * Calculates the factorial of a given number using an iterative approach.
     * 
     * This method computes the factorial of a number 'x' iteratively. The factorial
     * of a number 'n' is defined as the product of all positive integers less than or
     * equal to 'n'. For example, factorial(5) is 5*4*3*2*1 = 120.
     * 
     * @param x The number for which the factorial is to be calculated. It should be
     *          a non-negative integer, as the factorial is not defined for negative numbers.
     * @return The factorial of 'x' as an integer.
     */
    
    public static int cal(int x) {
        // Check if the input is negative; factorial is not defined for negative numbers.
        if (x < 0) {
            // Throw an exception to indicate incorrect usage of the method.
            throw new IllegalArgumentException("Factorial is not defined for negative numbers.");
        }

        // Initialize result to 1; factorial of 0 is 1 and it's the starting point for calculation.
        int result = 1;

        // Iterate from 1 to x (inclusive) to calculate the factorial.
        for (int i = 1; i <= x; i++) {
            // Multiply result with the current number i and store it back in result.
            result *= i;
        }

        // Return the calculated factorial.
        return result;
    }
}