package org.example.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FileWriterService {

    public FileWriterService() {

    }

    public void save(String filename, double[][] values) throws IOException {
        String path = "./data/" + filename + ".txt";
        File file = new File(path);
        if (!file.exists() || file.isDirectory()) {
            saveToFile(file, values);
        }
    }

    private void saveToFile(File file, double[][] values) throws IOException {
        file.getParentFile().mkdirs();
        file.createNewFile();
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        for (double[] value : values) {
            String sb = IntStream.range(0, values[0].length)
                    .mapToObj(j -> value[j] + " ")
                    .collect(Collectors.joining());
            writer.write(sb + "\n");
        }
        writer.close();
    }

}
