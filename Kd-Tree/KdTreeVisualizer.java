/*************************************************************************
 *  Ejecución:    java KdTreeVisualizer
 *  Dependencies: StdDraw.java Point2D.java KdTree.java
 *************************************************************************/

import java.io.*;
public class KdTreeVisualizer {

    public static void main(String[] args) {

        In in = new In(new File("input.txt"));

        StdDraw.show(0);

        // Inicializa la estrucrtura de datos con el punto de entrada estandar
        PointSET point = new PointSET();
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
            point.insert(p);
            kdtree.draw();
            StdDraw.show(50);
        }

    }
}
