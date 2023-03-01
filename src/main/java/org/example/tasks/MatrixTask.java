package org.example.tasks;

import org.example.strategies.CalculationStrategy;

public class MatrixTask implements Runnable {

    private final CalculationStrategy strategy;

    private final double[][] A;
    private final double[][] B;
    private final double[][] result;

    private final ConcurrencyContext context;

    public MatrixTask(ConcurrencyContext context, double[][] A, double[][] B, double[][] result, CalculationStrategy strategy) {
        if (context == null) {
            throw new IllegalArgumentException("Context can't be null.");
        }
        this.A = A;
        this.B = B;
        this.result = result;
        this.context = context;
        this.strategy = strategy;
    }

    @Override
    public void run() {
        while (true) {
            int row;
            synchronized (context) {
                if (context.isFullyProcessed()) {
                    break;
                }
                row = context.nextRowNumber();
            }
//            System.out.println(Thread.currentThread().getName() + " is going to process row " + row);
            this.strategy.calculate(A, B, result, row);
        }
    }

    public static class ConcurrencyContext {
        private final int numberOfRows;
        private int currentNumber;

        public ConcurrencyContext(int numberOfRows) {
            this.numberOfRows = numberOfRows;
            currentNumber = 0;
        }

        public synchronized int nextRowNumber() {
            if (isFullyProcessed()) {
                throw new IllegalStateException("Already fully processed");
            }
            return this.currentNumber++;
        }

        public synchronized boolean isFullyProcessed() {
            return this.currentNumber == this.numberOfRows;
        }

    }

}

