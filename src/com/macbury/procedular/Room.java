package com.macbury.procedular;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.util.Log;

import com.macbury.unamed.Core;

public class Room extends Rectangle {
  private DungeonBSPNode node;
  
  public Room(float x, float y, float width, float height) {
    super(x, y, width, height);
    Core.log(this.getClass(),"Creating room: " + this.toString());
  }
  
  public boolean isHorizontal() {
    return this.getWidth() > this.getHeight();
  }

  public DungeonBSPNode getNode() {
    return node;
  }

  public void setNode(DungeonBSPNode node) {
    this.node = node;
  }
}
