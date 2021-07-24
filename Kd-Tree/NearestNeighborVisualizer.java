/*************************************************************************
 *  Ejecución:    java NearestNeighborVisualizer input.txt
 *  Dependencias: PointSET.java KdTree.java Point2D.java In.java StdDraw.java
 *************************************************************************/

import java.io.*;

public class NearestNeighborVisualizer {

    public static void main(String[] args) {
        
        //Variables para determinar el tiempo de ejecución
        double TInicioFB, TFinFB, tiempoFB;
        double TInicioKD, TFinKD, tiempoKD;

        //Tomamos la hora en que inicio
                 
        
        //entrada de datos
        In in = new In(new File("input100.txt"));

        StdDraw.show(0);

        // inicializando las dos estructuras de datos con un punto standard de entrada
        PointSET brute = new PointSET();
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
            brute.insert(p);
        }
        //while (true) {
            //TInicioFB = System.currentTimeMillis();  


            // ubicación del mouse (x, y)
            //double x = 0.4;
            //double y = 0.8;

            double x = 0.35;
            double y = 0.7;

            Point2D query = new Point2D(x, y);
        

            // graficando todos los puntos
            StdDraw.clear();
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius(.01);
            brute.draw();
            kdtree.draw();
            StdDraw.setPenColor(StdDraw.GREEN);
            query.draw();

            // graficando de color rojo el vecino más cercano (usando algoritmo fuerza bruta)
            TInicioFB = System.currentTimeMillis(); 
            StdDraw.setPenRadius(.03);
            StdDraw.setPenColor(StdDraw.RED);
            brute.nearest(query).draw();
            StdDraw.setPenRadius(.02);
            TFinFB = System.currentTimeMillis(); 
            tiempoFB = TFinFB - TInicioFB;

            // graficando de color azul el vecino más cercano (usando algoritmo kd-tree)
            TInicioKD = System.currentTimeMillis(); 
            StdDraw.setPenColor(StdDraw.BLUE);
            kdtree.closest(query).draw();
            StdDraw.show(0);
            StdDraw.show(40);
            TFinKD = System.currentTimeMillis(); 
            tiempoKD = TFinKD - TInicioKD;

            System.out.printf("%8.6f %8.6f\n", x, y);

            
            System.out.printf("Tiempo de ejecución FB en milisegundos: %8.6f -->\n", tiempoFB);
            System.out.printf("Tiempo de ejecución KD en milisegundos: %8.6f -->", tiempoKD);

        //}
       
        
    }
}