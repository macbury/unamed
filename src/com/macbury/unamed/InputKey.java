package com.macbury.unamed;

public class InputKey {
  
  private short time;
  private int key;

  public InputKey(int key, short time) {
    this.setKey(key);
    this.time = time;
  }

  public int getKey() {
    return key;
  }

  public void setKey(int key) {
    this.key = key;
  }
  
  public boolean expired(int delta) {
    time -= delta;
    return (time <= 0);
  }
}
