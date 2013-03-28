package com.macbury.unamed.level;

public class Bedrock extends Block {
  private static final short INDESTRUCTIBLE = -1;

  public Bedrock(int x, int y) {
    super(x, y);
    this.hardness = INDESTRUCTIBLE;
    this.solid    = true;
  }

}
