package org.example.strategies;

public class MultiplyOperation implements CalculationStrategy {
    @Override
    public void calculate(double[][] A, double[][] B, double[][] result, int row) {
        for (int j = 0; j < B[0].length; j++) {
            double sum = 0.0;
            double error = 0.0;
            for (int k = 0; k < A[0].length; k++) {
                double x = A[row][k] * B[k][j] - error;
                double tempSum = sum + x;
                error = (tempSum - sum) - x;
                sum = tempSum;
            }
            result[row][j] = sum;
        }
    }

    @Override
    public String getDescription() {
        return "matrix multiplication";
    }
}
