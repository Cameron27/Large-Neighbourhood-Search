package org.compx556;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import org.compx556.function.DestructionFunctions;
import org.compx556.function.InitialisationFunctions;
import org.compx556.function.RepairFunctions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.zip.DataFormatException;

public class RectanglePacker {
    private BoxList initialState;

    public RectanglePacker(Config config) throws IOException, DataFormatException {
        try {
            initialState = parseDataFile(config.dataFile);
        } catch (IndexOutOfBoundsException e) {
            throw new DataFormatException("Format of the provided data file is incorrect.");
        }
    }

    public int solve() {
        return 0;
    }

    private static BoxList parseDataFile(File file) throws IOException {
        BoxList initialList = new BoxList(400);

        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);

        String line = br.readLine();
        boolean isBoxData = false;
        int size = 0;

        while (line != null) {
            String[] data = line.split(",");
            if (isBoxData) {
                int numBox = Integer.parseInt(data[0]);
                int xLocation = 0; // Placeholder
                int width = Integer.parseInt(data[1]);
                int height = Integer.parseInt(data[2]);
                Box inputBox = new Box(width, height, xLocation);
                initialList.add(inputBox);

                if (numBox == size) {
                    isBoxData = false;
                }
            } else {
                if (line.contains("name:")) {
                    String name = data[2];
                    System.out.println(name);
                }
                if (line.contains("width")) {
                    isBoxData = true;
                }
                if (line.contains("size")) {
                    size = Integer.parseInt(data[2]);
                }
            }
            line = br.readLine();

        }

        return initialList;
    }

    public static void main(String[] args) {
        Config config = new Config(InitialisationFunctions.random, DestructionFunctions.randomNRemove, RepairFunctions.randomLocationOptimumX);

        JCommander builder = JCommander.newBuilder()
                .addObject(config)
                .build();
        try {
            builder.parse(args);
        } catch (ParameterException e) {
            System.out.println(e.getMessage());
            e.usage();
            return;
        }
        if (config.help) {
            builder.usage();
            return;
        }

        RectanglePacker rectanglePacker = null;
        try {
            rectanglePacker = new RectanglePacker(config);
        } catch (IOException e) {
            System.err.println("Failed to load data from file.");
            return;
        } catch (DataFormatException e) {
            System.err.println("Data is formatted incorrectly.");
            return;
        }

        int bestHeight = rectanglePacker.solve();

        System.out.println("Best height: " + bestHeight);
    }
}
