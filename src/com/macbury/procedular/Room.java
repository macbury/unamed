package com.macbury.procedular;

import java.io.Serializable;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.util.Log;

public class Room extends Rectangle implements Serializable {

  public Room(float x, float y, float width, float height) {
    super(x, y, width, height);
    Log.info("Creating room: " + this.toString());
  }
  
  public boolean isHorizontal() {
    return this.getWidth() > this.getHeight();
  }
}
