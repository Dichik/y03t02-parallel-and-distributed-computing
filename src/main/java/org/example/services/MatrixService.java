package org.example.services;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.example.strategies.CalculationStrategy;
import org.example.tasks.MatrixTask;

import java.util.stream.IntStream;

public class MatrixService {

    private static final Logger logger = LogManager.getLogger(MatrixService.class);

    public MatrixService() {
    }

    public double[][] processMatrices(double[][] A, double[][] B, CalculationStrategy strategy) {
        int n = A.length;
        int m = B[0].length;

        double[][] result = new double[n][m];

        MatrixTask.ConcurrencyContext context = new MatrixTask.ConcurrencyContext(result.length);
        Runnable task = new MatrixTask(context, A, B, result, strategy);
        Thread[] workers = new Thread[5];

        for (int i = 0; i < workers.length; ++i) {
            workers[i] = new Thread(task, "worker-" + i);
        }
        for (Thread worker : workers) {
            worker.start();
        }
        for (Thread worker : workers) {
            try {
                worker.join();
            } catch (InterruptedException ex) {
                logger.error("Couldn't join workers, see: " + ex);
            }
        }
        logger.info(strategy.getDescription() + " was processed successfully.");
        return result;
    }


    public double[][] multiplyByNumber(double[][] A, double[][] B) {
        double number = B[0][0];
        int n = A.length;
        int m = A[0].length;
        double[][] result = new double[n][m];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < m; ++j) {
                result[i][j] = A[i][j] * number;
            }
        }
        logger.info("Matrix multiplication with a number was processed successfully.");
        return result;
    }

    public double[][] min(double[][] A) {
        int n = A.length;
        double[][] result = new double[1][1];
        for (double[] doubles : A) {
            IntStream.range(0, n).forEach(j -> result[0][0] = Math.min(result[0][0], doubles[j]));
        }
        logger.info("Finding min value in Matrix was processed successfully.");
        return result;
    }


}
