package com.macbury.unamed.level;

public class Water extends LiquidBlock {

  public Water(int x, int y) {
    super(x, y);
  }

  @Override
  public float getSpeed() {
    // TODO Auto-generated method stub
    return 0.6f;
  }

  @Override
  public float divePower() {
    return 0.7f;
  }

}
