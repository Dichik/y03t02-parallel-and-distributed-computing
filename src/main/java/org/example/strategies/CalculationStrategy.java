package org.example.strategies;

public interface CalculationStrategy {

    void calculate(double[][] A, double[][] B, double[][] result, int row);

    String getDescription();

}
