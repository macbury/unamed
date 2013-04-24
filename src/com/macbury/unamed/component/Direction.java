package com.macbury.unamed.component;

public enum Direction {
  None, Left, Right, Top, Down;
  
  public static Direction random() {
    return values()[(int) (Math.random() * values().length)];
  }
}
