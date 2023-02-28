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

    // FIXME improve with Runnable
    private static double[][] multiplyMatrices(double[][] A, double[][] B) {
        int n = A.length;
        int m = B[0].length;
        int common = B.length;

        double[][] result = new double[n][m];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < m; ++j) {
                double count = 0.0;
                for (int k = 0; k < common; ++k) {
                    count += (A[i][k] * B[k][j]);
                }
                result[i][j] = count;
            }
        }

        System.out.println("MM: n=" + result.length + " m=" + result[0].length);
        return result;
    }

    // TODO improve with Runnable
    private static double[][] sum(double[][] A, double[][] B) {
        int n = A.length;
        int m = A[0].length;

        System.out.println("n=" + A.length + " m=" + A[0].length);
        System.out.println("n=" + B.length + " m=" + B[0].length);


        double[][] result = new double[n][m];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < m; ++j) {
                result[i][j] = A[i][j] + B[i][j];
            }
        }

        System.out.println("sum: n=" + result.length + " m=" + result[0].length);
        return result;
    }

    private static double[][] multiplyByNumber(double[][] A, double[][] B) {
        double number = B[0][0];
        int n = A.length;
        int m = A[0].length;
        double[][] result = new double[n][m];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < m; ++j) {
                result[i][j] = A[i][j] * number;
            }
        }
        System.out.println("MbN: n=" + result.length + " m=" + result[0].length);
        return result;
    }

    private static double[][] diff(double[][] A, double[][] B) {
        int n = A.length;
        int m = A[0].length;
        double[][] result = new double[n][m];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < m; ++j) {
                result[i][j] = A[i][j] - B[i][j];
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
        double[][] E = sum(multiplyMatrices(B, MC), multiplyByNumber(D, minValue));
        double[][] MA = sum(
                multiplyMatrices(
                        multiplyByNumber(MD, b),
                        diff(MC, MX)
                ),
                multiplyByNumber(
                        multiplyMatrices(MX, MC),
                        b
                )
        );
        print("E=", E);
        print("MA=", MA);
    }

    private static void print(String message, double[][] result) {
        System.out.println(message);
        System.out.println("n=" + result.length + " m=" + result[0].length);
        for (double[] doubles : result) {
            Arrays.stream(doubles).mapToObj(aDouble -> aDouble + " ").forEach(System.out::print);
            System.out.println();
        }
    }

}
