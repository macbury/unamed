package com.macbury.unamed.level;

public class Block {
  public static int gid = 0;
  int id = 0;
  
  public boolean solid   = false;
  public boolean visited = false;
  
  public Block() {
    this.id = Block.gid++;
  }
}
