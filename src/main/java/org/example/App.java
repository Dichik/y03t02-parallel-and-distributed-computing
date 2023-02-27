package org.example;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * Task:
 * Створення потоків за використанням anonymous Runnable,
 * (синхронізація потоків за допомогою Synchronized)
 * <p>
 * Notes:
 * 1. size of matrix and vector is not less than 100
 * 2. values: float numbers greater than 0
 * 3. згенеруйте початкові дані, при цьому забезпечте, щоб порядки елементів матриць та векторів були різними
 * 4. save initial values to file (check if file already exists
 * 5. take initial values from file
 * 6. save output to the file
 * 7. logging of parallel computations (in console)
 * 8. Kahan algorithm
 * 9. time computation
 * <p>
 * My task (9):
 * E = В * МС + D * min(MC);
 * MА = b * MD * (MC - MX) + MX * MC * b.
 */

public class App {

    /**
     * Vectors: B, D (1xn)
     * Scalars: b (1x1)
     * Matrices: MC, MD, MX (nxn)
     */

    private static final Random random = new Random();

    private static double[][] generate(int rows, int columns) {
        double[][] result = new double[rows][columns];
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < columns; ++j) {
                result[i][j] = random.nextFloat(100);
            }
        }
        return result;
    }

    private static double[][] multiply(double[][] A, double[][] B) {
        assert A.length != A[0].length;
        int n = A.length;
        double[][] result = new double[n][n];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                // todo something
            }
        }
        return result;
    }

    private static double[][] sum(double[][] A, double[][] B) {
        assert A.length != A[0].length;
        int n = A.length;
        double[][] result = new double[n][n];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                // todo something
            }
        }
        return result;
    }

    private static double[][] diff(double[][] A, double[][] B) {
        assert A.length != A[0].length;
        int n = A.length;
        double[][] result = new double[n][n];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                // todo something
            }
        }
        return result;
    }

    private static double[][] min(double[][] A) {
        int n = A.length;
        double[][] result = new double[1][1];
        for (double[] doubles : A) {
            IntStream.range(0, n).forEach(j -> result[0][0] = Math.min(result[0][0], doubles[j]));
        }
        return result;
    }

    public static void main(String[] args) {
        int n = 100;

        double[][] B = generate(1, n);
        double[][] D = generate(1, n);

        double[][] b = generate(1, 1);

        double[][] MC = generate(n, n);
        double[][] MD = generate(n, n);
        double[][] MX = generate(n, n);

        double[][] minValue = min(MC);
        double[][] E = sum(multiply(B, MC), multiply(D, minValue));
        double[][] MA = sum(
                multiply(
                        multiply(b, MD),
                        diff(MC, MX)
                ),
                multiply(
                        multiply(MX, MC),
                        b
                )
        );
        print("E=", E);
        print("MA=", MA);
    }

    private static void print(String message, double[][] result) {
        System.out.println(message);
        for (double[] doubles : result) {
            Arrays.stream(doubles).mapToObj(aDouble -> aDouble + " ").forEach(System.out::print);
            System.out.println();
        }
    }

}
