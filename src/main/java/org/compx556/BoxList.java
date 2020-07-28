package org.compx556;

import java.util.ArrayList;
import java.util.List;

public class BoxList {
    List<Box> boxSchema = new ArrayList<Box>();
    public void addBox(Box b){
        boxSchema.add(b);
    }

    public int calculateHeight(){
        int Height = 0;
        for (Box b : boxSchema) {
            Height = Height + b.height;
        }
        return Height;
    }
}