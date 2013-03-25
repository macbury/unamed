package com.macbury.unamed;


import java.awt.List;
import java.util.ArrayList;

import org.newdawn.slick.geom.Point;

public class BresenhamLine {

  public static ArrayList<Point> line(int x0, int y0, int x1, int y1) {
    ArrayList<Point> result = new ArrayList<Point>();
  
     boolean steep = Math.abs(y1 - y0) > Math.abs(x1 - x0);
     if (steep) {
       int t = x0;
       x0 = y0;
       y0 = t;
       
       t  = x1;
       x1 = y1;
       y1 = t;
     }
     if (x0 > x1) {
       int t = x0;
       x0 = x1;
       x1 = t;
       
       t  = y0;
       y0 = y1;
       y1 = t;
     }
  
     int deltax = x1 - x0;
     int deltay = Math.abs(y1 - y0);
     int error = 0;
     int ystep;
     int y = y0;
     if (y0 < y1) ystep = 1; else ystep = -1;
     for (int x = x0; x <= x1; x++) {
         if (steep) {
           result.add(new Point(y, x));
         }
         else result.add(new Point(x, y));
         error += deltay;
         if (2 * error >= deltax) {
             y += ystep;
             error -= deltax;
         }
     }
  
     return result;
  }
}
