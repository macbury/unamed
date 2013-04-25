package com.macbury.unamed.util;

import org.newdawn.slick.SlickException;

public interface RaytraceCallback {
  void onRayVisits(int x, int y, int distance) throws SlickException;
}
