package org.example.strategies;

public class MultiplyOperation implements CalculationStrategy {
    @Override
    public void calculate(double[][] A, double[][] B, double[][] result, int row) {
        for (int j = 0; j < B[0].length; j++) {
            for (int k = 0; k < A[0].length; k++) {
                result[row][j] += A[row][k] * B[k][j];
            }
        }
    }

    @Override
    public String getDescription() {
        return "matrix multiplication";
    }
}
