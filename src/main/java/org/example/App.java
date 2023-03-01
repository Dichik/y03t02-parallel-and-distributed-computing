package org.example;

import org.example.io.FileReaderService;
import org.example.io.FileWriterService;
import org.example.strategies.DiffOperation;
import org.example.strategies.SumOperation;
import org.example.tasks.MatrixTask;
import org.example.strategies.CalculationStrategy;
import org.example.strategies.MultiplyOperation;

import java.io.*;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;
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

    public static void main(String[] args) throws IOException {
        int n = 100;

        FileReaderService fileReaderService = new FileReaderService();

        double[][] B = fileReaderService.getValues("B", 1, n);
        double[][] D = fileReaderService.getValues("D", 1, n);

        double[][] b = fileReaderService.getValues("b_", 1, 1);

        double[][] MC = fileReaderService.getValues("MC", n, n);
        double[][] MD = fileReaderService.getValues("MD", n, n);
        double[][] MX = fileReaderService.getValues("MX", n, n);

        double[][] minValue = min(MC);

        CalculationStrategy multiplication = new MultiplyOperation();
        CalculationStrategy sum = new SumOperation();
        CalculationStrategy difference = new DiffOperation();

        long start = System.currentTimeMillis();

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

        FileWriterService fileWriterService = new FileWriterService();
        fileWriterService.save("B", B);
        fileWriterService.save("D", D);
        fileWriterService.save("b_", b);
        fileWriterService.save("MC", MC);
        fileWriterService.save("MD", MD);
        fileWriterService.save("MX", MX);
    }

    private static void print(String message, double[][] result) {
        System.out.println(message);
        System.out.println("n=" + result.length + " m=" + result[0].length);
        for (double[] doubles : result) {
            Arrays.stream(doubles).mapToObj(aDouble -> aDouble + " ").forEach(System.out::print);
            System.out.println();
        }
    }

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
