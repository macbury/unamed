package com.macbury.unamed.level;

public class Lava extends PassableBlock {

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
  
  
}
