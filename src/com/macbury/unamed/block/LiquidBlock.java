package com.macbury.unamed.block;

public abstract class LiquidBlock extends PassableBlock {

  public LiquidBlock(int x, int y) {
    super(x, y);
  }
  
  public abstract float divePower();
}
