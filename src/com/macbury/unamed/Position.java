package com.macbury.unamed;

import java.util.ArrayList;

import org.newdawn.slick.geom.Point;

public class Position {
  private float x;
  private float y;
  
  private Integer tileX;
  private Integer tileY;
  
  public Position(int x, int y) {
    this.setX(x);
    this.setY(y);
  }
  
  public Position(float x, float y) {
    this.setX(x);
    this.setY(y);
  }
  
  public float getX() {
    return x;
  }
  
  public void setX(float x) {
    if (this.x != x) {
      tileX = null;
    }
    this.x = x;
  }
  
  public float getY() {
    return y;
  }
  public void setY(float y) {
    if (this.y != y) {
      tileY = null;
    }
    this.y = y;
  }
  
  public int getTileX() {
    if (tileX == null) {
      tileX = new Integer(Math.round(this.x / Core.TILE_SIZE));
    }
    return tileX;
  }
  
  public void setTileX(int tileX) {
    if (tileX != this.tileX) {
      this.x = tileX * Core.TILE_SIZE;
    }
    this.tileX = tileX;
  }
  
  public int getTileY() {
    if (tileY == null) {
      tileY = new Integer(Math.round(this.y / Core.TILE_SIZE));
    }
    return tileY;
  }
  
  public void setTileY(int tileY) {
    if (tileY != this.tileY) {
      this.y = tileY * Core.TILE_SIZE;
    }
    this.tileY = tileY;
  }
  
  public ArrayList<Point> tiledLineTo(Position end) {
    return BresenhamLine.line(this.getTileX(), this.getTileY(), end.getTileX(), end.getTileY());
  }
}
