package org.compx556;

import java.io.*;
import java.util.Random;

public class RectanglePacker {

    public static void main(String[] args) throws IOException {

        BoxList initialList = new BoxList(400);
        if (args.length != 1) {
            System.err.println("Usage: java RectanglePacker <filepath>");
            System.exit(0);
        }

        String filepath = args[0];
        File file  = new File(filepath);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);

        String line = br.readLine();
        boolean isBoxData = false;
        int size = 0;

        Random rand = new Random();

        while (line != null) {
            String[] data = line.split(",");
            if (isBoxData){
                int numBox = Integer.parseInt(data[0]);
                int xLocation = rand.nextInt(); // Placeholder
                int width = Integer.parseInt(data[1]);
                int height = Integer.parseInt(data[2]);
                Box inputBox = new Box(width,height,xLocation);
                initialList.add(inputBox);

                if(numBox == size){
                    isBoxData = false;
                }
            }
            else{
                if(line.contains("name:")){
                    String name = data[2];
                    System.out.println(name);
                }
                if(line.contains("width")){
                    isBoxData = true;
                }
                if(line.contains("size")){
                    size = Integer.parseInt(data[2]);
                }
            }
            line = br.readLine();

        }

        System.out.println(initialList.toString());
    }

}
