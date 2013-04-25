package com.macbury.unamed.util;

import org.newdawn.slick.SlickException;


public class RaytraceUtil {
  private RaytraceCallback delegate;
  
  public RaytraceUtil(RaytraceCallback delegate) {
    this.setDelegate(delegate);
  }
  
  public RaytraceCallback getDelegate() {
    return delegate;
  }

  public void setDelegate(RaytraceCallback delegate) {
    this.delegate = delegate;
  }
  
  public void raytrace(int x0, int y0, int x1, int y1) throws SlickException {;
    int dx = Math.abs(x1 - x0);
    int dy = Math.abs(y1 - y0);
    int x = x0;
    int y = y0;
    int en = 1 + dx + dy;
    int n  = en;
    int x_inc = (x1 > x0) ? 1 : -1;
    int y_inc = (y1 > y0) ? 1 : -1;
    int error = dx - dy;
    dx *= 2;
    dy *= 2;
    
    for (; n > 0; --n) {
      int distance  = Math.max(0, en - n);
      delegate.onRayVisits(x, y, distance);
        
      if (error > 0) {
        x += x_inc;
        error -= dy;
      } else {
        y += y_inc;
        error += dx;
      }
    }
  }
}
