package org.example;

import org.example.strategies.DiffOperation;
import org.example.strategies.SumOperation;
import org.example.tasks.MatrixTask;
import org.example.strategies.CalculationStrategy;
import org.example.strategies.MultiplyOperation;

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

        // TODO read/write to files

        long start = System.currentTimeMillis();

        int n = 100;

        double[][] B = generate(1, n);
        double[][] D = generate(1, n);

        double[][] b = generate(1, 1);

        double[][] MC = generate(n, n);
        double[][] MD = generate(n, n);
        double[][] MX = generate(n, n);

        // TODO create different tasks and put them to thread

        // FIXME where we can use Synchronized, can't we???

        double[][] minValue = min(MC);

        CalculationStrategy multiplication = new MultiplyOperation();
        CalculationStrategy sum = new SumOperation();
        CalculationStrategy difference = new DiffOperation();

        double[][] E = processMatrices(
                processMatrices(B, MC, multiplication),
                multiplyByNumber(D, minValue),
                sum
        );
        double[][] MA = processMatrices(
                processMatrices(
                        multiplyByNumber(MD, b),
                        processMatrices(MC, MX, difference),
                        multiplication
                ),
                multiplyByNumber(
                        processMatrices(MX, MC, multiplication),
                        b
                ),
                sum
        );

        long end = System.currentTimeMillis();

        print("E=", E);
        print("MA=", MA);
        System.out.println("Time: " + (end - start) + " ms");
    }

    private static void print(String message, double[][] result) {
        System.out.println(message);
        System.out.println("n=" + result.length + " m=" + result[0].length);
        for (double[] doubles : result) {
            Arrays.stream(doubles).mapToObj(aDouble -> aDouble + " ").forEach(System.out::print);
            System.out.println();
        }
    }

    // FIXME improve with Runnable
    private static double[][] processMatrices(double[][] A, double[][] B, CalculationStrategy strategy) {
        int n = A.length;
        int m = B[0].length;

        double[][] result = new double[n][m];

        MatrixTask.ConcurrencyContext context = new MatrixTask.ConcurrencyContext(result.length);
        Runnable task = new MatrixTask(context, A, B, result, strategy);
        Thread[] workers = new Thread[5];

        for (int i = 0; i < workers.length; ++ i) {
            workers[i] = new Thread(task, "worker-" + i);
        }
        for (Thread worker : workers) {
            worker.start();
        }
        for (Thread worker : workers) {
            try {
                worker.join();
            } catch (InterruptedException ex) {

            }
        }

        return result;
    }

}
