package org.example.io;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class FileReaderService {

    private static final Logger logger = LogManager.getLogger(FileReaderService.class);
    private static final Random random = new Random();

    public FileReaderService() {

    }

    private static double[][] generate(int rows, int columns) {
        double[][] result = new double[rows][columns];
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < columns; ++j) {
                result[i][j] = 100 * random.nextDouble();
            }
        }
        return result;
    }

    public double[][] getValues(String filename, int n, int m) throws FileNotFoundException {
        String path = "./data/" + filename + ".txt";
        File file = new File(path);
        double[][] result;
        if (file.exists() && !file.isDirectory()) {
            result = readFile(file, n, m);
            logger.info("Read data from " + path + " successfully.");
        } else {
            result = generate(n, m);
            logger.info("Generated data for " + path + " successfully.");
        }
        return result;
    }

    private double[][] readFile(File file, int n, int m) throws FileNotFoundException {
        double[][] result = new double[n][m];
        Scanner scanner = new Scanner(file);
        int i = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] numbers = line.trim().split(" ");
            for (int j = 0; j < numbers.length; ++j) {
                result[i][j] = Double.parseDouble(numbers[j]);
            }
            i++;
        }
        return result;
    }

}
