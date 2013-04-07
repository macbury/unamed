package com.macbury.unamed.level;

public class Water extends PassableBlock {

  public Water(int x, int y) {
    super(x, y);
  }

  @Override
  public float getSpeed() {
    // TODO Auto-generated method stub
    return 0.6f;
  }

}
