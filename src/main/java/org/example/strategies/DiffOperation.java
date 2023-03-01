package org.example.strategies;

public class DiffOperation implements CalculationStrategy {
    @Override
    public void calculate(double[][] A, double[][] B, double[][] result, int row) {
        int m = A[0].length;
        for (int i = 0; i < m; ++i) {
            result[row][i] = A[row][i] - B[row][i];
        }
    }

    @Override
    public String getDescription() {
        return "finding difference between cells";
    }
}
