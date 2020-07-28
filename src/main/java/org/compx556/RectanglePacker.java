package org.compx556;

public class RectanglePacker {
    public static void main(String[] args) {
        Box a = new Box(5,10,0);
        Box b = new Box(10,15,10);
        BoxList c = new BoxList();
        c.addBox(a);
        c.addBox(b);
    }

    public int evaluator(BoxList solution){
        return solution.calculateHeight();
    }
}
