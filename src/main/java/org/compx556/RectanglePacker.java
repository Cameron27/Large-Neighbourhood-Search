package org.compx556;

public class RectanglePacker {
    public static void main(String[] args) {
        Box a = new Box(5, 10, 0);
        Box b = new Box(10, 15, 10);
        BoxList c = new BoxList(400);
        c.add(a);
        c.add(b);

        System.out.println(c.toString());
    }
}
