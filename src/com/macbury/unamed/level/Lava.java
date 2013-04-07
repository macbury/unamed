package com.macbury.unamed.level;

public class Lava extends LiquidBlock {

  public Lava(int x, int y) {
    super(x, y);
  }

  @Override
  public boolean isVisible() {
    return true;
  }

  @Override
  public int getLightPower() {
    return 0;
  }

  @Override
  public float getSpeed() {
    return 0.5f;
  }

  @Override
  public float divePower() {
    return 0.7f;
  }
  
  
}
