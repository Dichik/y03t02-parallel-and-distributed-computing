package org.example;

import org.example.io.FileReaderService;
import org.example.io.FileWriterService;
import org.example.services.MatrixService;
import org.example.strategies.CalculationStrategy;
import org.example.strategies.DiffOperation;
import org.example.strategies.MultiplyOperation;
import org.example.strategies.SumOperation;

import java.io.IOException;
import java.util.Arrays;

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

    public static void main(String[] args) throws IOException {
        int n = 100;

        MatrixService matrixService = new MatrixService();
        FileReaderService fileReaderService = new FileReaderService();

        double[][] B = fileReaderService.getValues("B", 1, n);
        double[][] D = fileReaderService.getValues("D", 1, n);

        double[][] b = fileReaderService.getValues("b_", 1, 1);

        double[][] MC = fileReaderService.getValues("MC", n, n);
        double[][] MD = fileReaderService.getValues("MD", n, n);
        double[][] MX = fileReaderService.getValues("MX", n, n);

        double[][] minValue = matrixService.min(MC);

        CalculationStrategy multiplication = new MultiplyOperation();
        CalculationStrategy sum = new SumOperation();
        CalculationStrategy difference = new DiffOperation();

        long start = System.currentTimeMillis();

        double[][] E = matrixService.processMatrices(
                matrixService.processMatrices(B, MC, multiplication),
                matrixService.multiplyByNumber(D, minValue),
                sum
        );
        double[][] MA = matrixService.processMatrices(
                matrixService.processMatrices(
                        matrixService.multiplyByNumber(MD, b),
                        matrixService.processMatrices(MC, MX, difference),
                        multiplication
                ),
                matrixService.multiplyByNumber(
                        matrixService.processMatrices(MX, MC, multiplication),
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

}
