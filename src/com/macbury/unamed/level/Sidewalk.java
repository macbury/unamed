package com.macbury.unamed.level;

public class Sidewalk extends PassableBlock {
  public Sidewalk(int x, int y) {
    super(x,y);
  }

  @Override
  public float getSpeed() {
    return 1.0f;
  }
}
