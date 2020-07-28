package org.compx556;

public class Box {
    int width;
    int height;
    int xLocation;

    public Box(int width,int height,int xLocation){
        this.width = width;
        this.height = height;
        this.xLocation = xLocation;
    }

    public void Rotate(){
        int oldWidth = this.width;
        this.width = this.height;
        this.height = oldWidth;
    }

    public int getWidth(){
        return this.width;
    }

    public int getHeight(){
        return this.height;
    }

    public int getXLocation(){
        return this.xLocation;
    }
 }