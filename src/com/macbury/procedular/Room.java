package com.macbury.procedular;

import java.io.Serializable;

import org.newdawn.slick.geom.Rectangle;

public class Room extends Rectangle implements Serializable {

  public Room(float x, float y, float width, float height) {
    super(x, y, width, height);
  }
  
  public boolean isHorizontal() {
    return this.getWidth() > this.getHeight();
  }
}
